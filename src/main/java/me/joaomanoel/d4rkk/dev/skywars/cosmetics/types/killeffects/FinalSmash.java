package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.killeffects;

import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.KillEffect;
import me.joaomanoel.d4rkk.dev.skywars.nms.NMS;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import me.joaomanoel.d4rkk.dev.utils.particles.ParticleEffect;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class FinalSmash extends KillEffect {
  
  public FinalSmash(ConfigurationSection section) {
    super(section.getLong("id"), EnumRarity.fromName(section.getString("rarity")), section.getDouble("coins"), section.getInt("cash"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public void execute(Player viewer, Location location) {
    if (viewer == null) {
      WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
      EntityArmorStand entityArmorStand = new EntityArmorStand(world);
      entityArmorStand.setLocation(location.getX(), location.getY(), location.getZ(),
          MathHelper.d((entityArmorStand.pitch * 256F) / 360F),
          MathHelper.d((entityArmorStand.yaw * 256F) / 360F));
      entityArmorStand.setInvisible(false);
      ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
      SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
      skull.setItemMeta(skullMeta);
      ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
      ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
      ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
      net.minecraft.server.v1_8_R3.ItemStack nmsSkull = CraftItemStack.asNMSCopy(skull);
      net.minecraft.server.v1_8_R3.ItemStack nmsChest = CraftItemStack.asNMSCopy(chest);
      net.minecraft.server.v1_8_R3.ItemStack nmsLeg = CraftItemStack.asNMSCopy(leg);
      net.minecraft.server.v1_8_R3.ItemStack nmsBoots = CraftItemStack.asNMSCopy(boots);
      PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
      PacketPlayOutEntityEquipment packetEquipSkull = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 4,
          nmsSkull);
      PacketPlayOutEntityEquipment packetEquipChest = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 3,
          nmsChest);
      PacketPlayOutEntityEquipment packetEquipLeg = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 2,
          nmsLeg);
      PacketPlayOutEntityEquipment packetEquipBoots = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 1,
          nmsBoots);
      for (Player all : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipSkull);
        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipChest);
        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipLeg);
        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetEquipBoots);
      }
      BukkitRunnable runnable = new BukkitRunnable() {
        int i = 60;
        double addX = 0.5d;
        double addZ = 0.5d;
        Location lastPos = new Location(location.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
        
        @Override
        public void run() {
          if (i == 60 || i == 20) {
            double randomDouble1 = Math.random();
            if (randomDouble1 < 0.5d) {
              addX = 0.5d;
            }
            if (randomDouble1 >= 0.5d) {
              addX = -0.5d;
            }
            double randomDouble2 = Math.random();
            if (randomDouble2 < 0.5d) {
              addZ = 0.5d;
            }
            if (randomDouble2 >= 0.5d) {
              addZ = -0.5d;
            }
          }
          if (i > 0) {
            entityArmorStand.locY += 0.2;
            entityArmorStand.locX += addX;
            entityArmorStand.locZ += addZ;
            Location pos = new Location(location.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
            if (pos.getBlock().getType() == Material.AIR) {
              PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation(
                  entityArmorStand,
                  (byte) MathHelper.floor(((entityArmorStand.yaw += 10) * 256.0F) / 360.0F));
              PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(entityArmorStand);
              for (Player all : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);
                ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityHeadRotation);
              }
              ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, pos, 256f);
              lastPos = pos;
              i--;
            } else {
              i = 0;
            }
            if (i == 0) {
              PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
              for (Player all : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
              }
              org.bukkit.entity.Firework firework = lastPos.getWorld().spawn(lastPos, org.bukkit.entity.Firework.class);
              FireworkMeta meta = firework.getFireworkMeta();
              meta.setPower(3);
              meta.addEffect(FireworkEffect.builder()
                  .with(FireworkEffect.Type.BALL)
                  .withColor(BukkitUtils.COLORS.get(ThreadLocalRandom.current().nextInt(BukkitUtils.COLORS.size())).get(null), BukkitUtils.COLORS.get(ThreadLocalRandom.current().nextInt(BukkitUtils.COLORS.size())).get(null))
                  .withFade(BukkitUtils.COLORS.get(ThreadLocalRandom.current().nextInt(BukkitUtils.COLORS.size())).get(null))
                  .build());
              firework.setFireworkMeta(meta);
              Bukkit.getScheduler().runTaskLater(Main.getInstance(), firework::detonate, 1L);
              cancel();
            }
          }
        }
        
      };
      runnable.runTaskTimer(Main.getInstance(), 1, 1);
    } else {
      WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
      EntityArmorStand entityArmorStand = new EntityArmorStand(world);
      entityArmorStand.setLocation(location.getX(), location.getY(), location.getZ(),
          MathHelper.d((entityArmorStand.pitch * 256F) / 360F),
          MathHelper.d((entityArmorStand.yaw * 256F) / 360F));
      entityArmorStand.setInvisible(false);
      ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
      SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
      skullMeta.setOwner(viewer.getName());
      skull.setItemMeta(skullMeta);
      ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
      ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
      ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
      net.minecraft.server.v1_8_R3.ItemStack nmsSkull = CraftItemStack.asNMSCopy(skull);
      net.minecraft.server.v1_8_R3.ItemStack nmsChest = CraftItemStack.asNMSCopy(chest);
      net.minecraft.server.v1_8_R3.ItemStack nmsLeg = CraftItemStack.asNMSCopy(leg);
      net.minecraft.server.v1_8_R3.ItemStack nmsBoots = CraftItemStack.asNMSCopy(boots);
      PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
      PacketPlayOutEntityEquipment packetEquipSkull = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 4,
          nmsSkull);
      PacketPlayOutEntityEquipment packetEquipChest = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 3,
          nmsChest);
      PacketPlayOutEntityEquipment packetEquipLeg = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 2,
          nmsLeg);
      PacketPlayOutEntityEquipment packetEquipBoots = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 1,
          nmsBoots);
      ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packet);
      ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetEquipSkull);
      ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetEquipChest);
      ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetEquipLeg);
      ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetEquipBoots);
      BukkitRunnable runnable = new BukkitRunnable() {
        int i = 60;
        double addX = 0.5d;
        double addZ = 0.5d;
        Location lastPos = new Location(location.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
        
        @Override
        public void run() {
          if (i == 60 || i == 20) {
            double randomDouble1 = Math.random();
            if (randomDouble1 < 0.5d) {
              addX = 0.5d;
            }
            if (randomDouble1 >= 0.5d) {
              addX = -0.5d;
            }
            double randomDouble2 = Math.random();
            if (randomDouble2 < 0.5d) {
              addZ = 0.5d;
            }
            if (randomDouble2 >= 0.5d) {
              addZ = -0.5d;
            }
          }
          if (i > 0) {
            entityArmorStand.locY += 0.2;
            entityArmorStand.locX += addX;
            entityArmorStand.locZ += addZ;
            Location pos = new Location(location.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
            if (pos.getBlock().getType() == Material.AIR) {
              PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation(
                  entityArmorStand,
                  (byte) MathHelper.floor(((entityArmorStand.yaw += 10) * 256.0F) / 360.0F));
              PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(entityArmorStand);
              ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);
              ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetPlayOutEntityHeadRotation);
              ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, pos, viewer);
              lastPos = pos;
              i--;
            } else {
              i = 0;
            }
            
            if (i == 0) {
              PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
              ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
              
              org.bukkit.entity.Firework firework = NMS.createAttachedFirework(viewer, lastPos);
              FireworkMeta meta = firework.getFireworkMeta();
              meta.setPower(3);
              meta.addEffect(FireworkEffect.builder()
                  .with(FireworkEffect.Type.BALL)
                  .withColor(BukkitUtils.COLORS.get(ThreadLocalRandom.current().nextInt(BukkitUtils.COLORS.size())).get(null), BukkitUtils.COLORS.get(ThreadLocalRandom.current().nextInt(BukkitUtils.COLORS.size())).get(null))
                  .withFade(BukkitUtils.COLORS.get(ThreadLocalRandom.current().nextInt(BukkitUtils.COLORS.size())).get(null))
                  .build());
              firework.setFireworkMeta(meta);
              Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), firework::detonate, 1L);
              cancel();
            }
          }
        }
        
      };
      runnable.runTaskTimer(Main.getInstance(), 1, 1);
    }
  }
  
}
