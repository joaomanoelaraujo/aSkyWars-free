package me.joaomanoel.d4rkk.dev.skywars.nms.entity;

import me.joaomanoel.d4rkk.dev.nms.v1_8_R3.utils.NullBoundingBox;
import me.joaomanoel.d4rkk.dev.skywars.nms.NMS;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;

public class EntityCart extends EntitySkeleton {
  
  private final String owner;
  
  public EntityCart(String owner, Location location) {
    super(((CraftWorld) location.getWorld()).getHandle());
    this.owner = owner;
    setInvisible(true);
    
    NMS.clearPathfinderGoal(this);
    super.a(new NullBoundingBox());
  }
  
  @Override
  public void a(AxisAlignedBB axisalignedbb) {
  }
  
  public boolean isInvulnerable(DamageSource source) {
    return true;
  }
  
  public void setCustomName(String customName) {
  }
  
  public void setCustomNameVisible(boolean visible) {
  }
  
  @Override
  public void t_() {
    if (this.dead) {
      super.t_();
    }
    
    if (this.owner == null) {
      this.killEntity();
    }
  }
  
  @Override
  public void die() {
  }
  
  public void killEntity() {
    NMS.ATTACHED_CART.remove(this.getId());
    this.dead = true;
  }
  
  public void makeSound(String sound, float f1, float f2) {
  }
  
  @Override
  public CraftEntity getBukkitEntity() {
    if (bukkitEntity == null) {
      bukkitEntity = new CraftCart(this);
    }
    
    return super.getBukkitEntity();
  }
  
  static class CraftCart extends CraftSkeleton {
    
    public CraftCart(EntityCart entity) {
      super(entity.world.getServer(), entity);
    }
    
    public int getId() {
      return entity.getId();
    }
    
    @Override
    public void remove() {
      ((EntityCart) entity).killEntity();
    }
  }
}
