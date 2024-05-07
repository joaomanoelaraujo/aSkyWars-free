package me.joaomanoel.d4rkk.dev.skywars.nms.entity;


import me.joaomanoel.d4rkk.dev.nms.v1_8_R3.entity.EntityNPCPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class EntityFirework extends EntityFireworks {
  
  private int ticks;
  private final Player owner;
  
  public EntityFirework(Player owner, Location location) {
    super(((CraftWorld) location.getWorld()).getHandle());
    this.owner = owner;
  }
  
  @Override
  public void t_() {
    this.P = this.locX;
    this.Q = this.locY;
    this.R = this.locZ;
    this.K();
    this.motX *= 1.15D;
    this.motZ *= 1.15D;
    this.motY += 0.04D;
    this.move(this.motX, this.motY, this.motZ);
    float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
    this.yaw = (float) (MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);
    
    for (this.pitch = (float) (MathHelper.b(this.motY, f) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
    }
    
    while (this.pitch - this.lastPitch >= 180.0F) {
      this.lastPitch += 360.0F;
    }
    
    while (this.yaw - this.lastYaw < -180.0F) {
      this.lastYaw -= 360.0F;
    }
    
    while (this.yaw - this.lastYaw >= 180.0F) {
      this.lastYaw += 360.0F;
    }
    
    this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
    this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
    if (this.ticks == 0) {
      this.makeSound("fireworks.launch", 3.0F, 1.0F);
    }
    
    ++this.ticks;
    if (!this.world.isClientSide && this.ticks > this.expectedLifespan) {
      if (!CraftEventFactory.callFireworkExplodeEvent(this).isCancelled()) {
        this.world.broadcastEntityEffect(this, (byte) 17);
      }
      
      this.die();
    }
  }
  
  @Override
  public void makeSound(String s, float f, float f1) {
    if (this.owner != null) {
      ((CraftPlayer) this.owner).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(s, locX, locY, locZ, f, f1));
    } else {
      super.makeSound(s, f, f1);
    }
  }
  
  public static class FireworkTrackEntry extends EntityTrackerEntry {
    
    private static Field U;
    
    static {
      try {
        U = EntityTrackerEntry.class.getDeclaredField("u");
        U.setAccessible(true);
      } catch (ReflectiveOperationException e) {
        e.printStackTrace();
      }
    }
    
    public FireworkTrackEntry(Entity entity, int i, int j, boolean flag) {
      super(entity, i, j, flag);
    }
    
    public FireworkTrackEntry(EntityTrackerEntry entry) {
      this(entry.tracker, entry.b, entry.c, getU(entry));
    }
    
    static boolean getU(EntityTrackerEntry entry) {
      try {
        return (boolean) U.get(entry);
      } catch (ReflectiveOperationException e) {
        e.printStackTrace();
      }
      
      return false;
    }
    
    @Override
    public void updatePlayer(EntityPlayer entityplayer) {
      if (entityplayer instanceof EntityNPCPlayer) {
        return;
      }
      
      if (entityplayer != tracker && c(entityplayer)) {
        if (!trackedPlayers.contains(entityplayer) && (entityplayer.u().getPlayerChunkMap().a(entityplayer, tracker.ae, tracker.ag) || tracker.attachedToPlayer)) {
          if (tracker instanceof EntityFirework) {
            EntityFirework entity = (EntityFirework) tracker;
            if (entity.owner != null && !entity.owner.equals(entityplayer.getBukkitEntity())) {
              return;
            }
          }
        }
      }
      
      super.updatePlayer(entityplayer);
    }
  }
}
