package me.joaomanoel.d4rkk.dev.skywars.nms;

import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.reflection.acessors.FieldAccessor;
import me.joaomanoel.d4rkk.dev.skywars.nms.entity.*;
import me.joaomanoel.d4rkk.dev.skywars.nms.interfaces.BalloonEntity;
import me.joaomanoel.d4rkk.dev.utils.Utils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NMS {
  
  private static final FieldAccessor<Set> SET_TRACKERS;
  private static final FieldAccessor<Map> CLASS_TO_ID, CLASS_TO_NAME;
  private static final FieldAccessor<List> PATHFINDERGOAL_B, PATHFINDERGOAL_C;
  public static Map<Integer, String> ATTACHED_CART = new HashMap<>();
  
  static {
    SET_TRACKERS = Accessors.getField(EntityTracker.class, "c", Set.class);
    CLASS_TO_ID = Accessors.getField(EntityTypes.class, "f", Map.class);
    CLASS_TO_NAME = Accessors.getField(EntityTypes.class, "d", Map.class);
    PATHFINDERGOAL_B = Accessors.getField(PathfinderGoalSelector.class, 0, List.class);
    PATHFINDERGOAL_C = Accessors.getField(PathfinderGoalSelector.class, 1, List.class);
    
    CLASS_TO_ID.get(null).put(EntityCart.class, 51);
    CLASS_TO_ID.get(null).put(EntityFirework.class, 22);
    CLASS_TO_ID.get(null).put(BalloonEntityBat.class, 65);
    CLASS_TO_ID.get(null).put(BalloonEntityLeash.class, 8);
    CLASS_TO_ID.get(null).put(BalloonEntityGiant.class, 53);
    CLASS_TO_ID.get(null).put(MountableEnderDragon.class, 63);
    CLASS_TO_NAME.get(null).put(EntityCart.class, "kCart");
    CLASS_TO_NAME.get(null).put(EntityFirework.class, "kFirework");
    CLASS_TO_NAME.get(null).put(BalloonEntityBat.class, "kBat");
    CLASS_TO_NAME.get(null).put(BalloonEntityLeash.class, "kLeash");
    CLASS_TO_NAME.get(null).put(BalloonEntityGiant.class, "kGiant");
    CLASS_TO_NAME.get(null).put(MountableEnderDragon.class, "kEnderDragon");
    
  }
  
  public static Entity createAttachedCart(String owner, Location location) {
    EntityCart cart = new EntityCart(owner, location);
    ATTACHED_CART.put(cart.getId(), owner);
    cart.setPosition(location.getX(), location.getY(), location.getZ());
    me.joaomanoel.d4rkk.dev.nms.NMS.look(cart.getBukkitEntity(), location.getYaw(), location.getPitch());
    cart.world.addEntity(cart, CreatureSpawnEvent.SpawnReason.CUSTOM);
    return cart.getBukkitEntity();
  }
  
  public static Firework createAttachedFirework(Player viewer, Location location) {
    EntityFirework firework = new EntityFirework(viewer, location.clone().add(0, -.5, 0));
    firework.setPosition(location.getX(), location.getY(), location.getZ());
    firework.world.addEntity(firework, CreatureSpawnEvent.SpawnReason.CUSTOM);
    WorldServer server = (WorldServer) firework.world;
    EntityTrackerEntry entry = server.getTracker().trackedEntities.get(firework.getId());
    if (viewer != null) {
      for (EntityPlayer ep : new ArrayList<>(entry.trackedPlayers)) {
        if (!ep.getBukkitEntity().equals(viewer)) {
          entry.clear(ep);
        }
      }
    }
    if (entry != null) {
      EntityFirework.FireworkTrackEntry replace = new EntityFirework.FireworkTrackEntry(entry);
      server.getTracker().trackedEntities.a(firework.getId(), replace);
      if (SET_TRACKERS != null) {
        Set<Object> set = (Set) SET_TRACKERS.get(server.getTracker());
        set.remove(entry);
        set.add(replace);
      }
    }
    return (Firework) firework.getBukkitEntity();
  }
  
  public static void sendFakeSpectator(Player player, Entity entity) {
    player.setGameMode(entity == null ? GameMode.ADVENTURE : GameMode.SPECTATOR);
    
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    if (entity != null) {
      PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, ep);
      FieldAccessor<Object> accessor = Accessors.getField(packet.getClass(), "b");
      List<PacketPlayOutPlayerInfo.PlayerInfoData> infoList = new ArrayList<>();
      infoList.add(packet.new PlayerInfoData(ep.getProfile(), ep.ping, WorldSettings.EnumGamemode.CREATIVE, ep.listName));
      accessor.set(packet, infoList);
      
      ep.playerConnection.sendPacket(packet);
    }
    
    ep.playerConnection.sendPacket(new PacketPlayOutCamera(entity == null ? ep : ((CraftEntity) entity).getHandle()));
  }
  
  public static BalloonEntity createBalloonLeash(Location location) {
    BalloonEntityLeash entity = new BalloonEntityLeash(location);
    if (entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return entity;
    }
    
    return null;
  }
  
  public static BalloonEntity createBalloonBat(Location location, BalloonEntity leash) {
    BalloonEntityBat entity = new BalloonEntityBat(location, (BalloonEntityLeash) leash);
    if (entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return entity;
    }
    
    return null;
  }
  
  public static BalloonEntity createBalloonGiant(Location location, List<String> frames) {
    BalloonEntityGiant entity = new BalloonEntityGiant(location, frames);
    if (entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
      return entity;
    }
    
    return null;
  }
  
  public static void look(Object entity, float yaw, float pitch) {
    if (entity instanceof Entity) {
      entity = ((CraftEntity) entity).getHandle();
    }
    
    yaw = Utils.clampYaw(yaw);
    net.minecraft.server.v1_8_R3.Entity handle = (net.minecraft.server.v1_8_R3.Entity) entity;
    handle.yaw = yaw;
    handle.pitch = pitch;
    if (handle instanceof EntityLiving) {
      ((EntityLiving) handle).aJ = yaw;
      if (handle instanceof EntityHuman) {
        ((EntityHuman) handle).aI = yaw;
      }
      ((EntityLiving) handle).aK = yaw;
    }
  }
  
  public static void clearPathfinderGoal(Object entity) {
    if (entity instanceof Entity) {
      entity = ((CraftEntity) entity).getHandle();
    }
    
    net.minecraft.server.v1_8_R3.Entity handle = (net.minecraft.server.v1_8_R3.Entity) entity;
    if (handle instanceof EntityInsentient) {
      EntityInsentient entityInsentient = (EntityInsentient) handle;
      PATHFINDERGOAL_B.get(entityInsentient.goalSelector).clear();
      PATHFINDERGOAL_C.get(entityInsentient.targetSelector).clear();
    }
  }
  
  public static void createMountableEnderDragon(Player player) {
    MountableEnderDragon enderDragon = new MountableEnderDragon(player);
    enderDragon.world.addEntity(enderDragon, CreatureSpawnEvent.SpawnReason.CUSTOM);
    enderDragon.getBukkitEntity().setPassenger(player);
  }
}
