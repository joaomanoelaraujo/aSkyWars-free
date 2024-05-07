package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class TntExecutor extends AbstractExecutor {
  
  public TntExecutor(Player player) {
    super(player);
  }
  
  @Override
  public void tick() {
    this.player.getWorld().spawn(player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 5, Math.floor(Math.random() * 3.0D)), TNTPrimed.class);
  }
}
