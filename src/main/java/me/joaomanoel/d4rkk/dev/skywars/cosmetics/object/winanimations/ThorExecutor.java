package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ThorExecutor extends AbstractExecutor {
  
  public ThorExecutor(Player player) {
    super(player);
  }
  
  @Override
  public void tick() {
    this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.LIGHTNING);
    this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.LIGHTNING);
    this.player.getLocation().getWorld().strikeLightning(this.player.getLocation());
    this.player.getLocation().getWorld().strikeLightningEffect(this.player.getLocation());
  }
}