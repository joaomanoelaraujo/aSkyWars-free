package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AnvilExecutor extends AbstractExecutor {
  
  public AnvilExecutor(Player player) {
    super(player);
  }
  
  @Override
  public void tick() {
    Location randomLocation =
        this.player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 5, Math.floor(Math.random() * 3.0D));
    randomLocation.getWorld().spawnFallingBlock(randomLocation, Material.ANVIL, (byte) 0);
    
    randomLocation =
        this.player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 5, Math.floor(Math.random() * 3.0D));
    randomLocation.getWorld().spawnFallingBlock(randomLocation, Material.ANVIL, (byte) 0);
    randomLocation =
        this.player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 5, Math.floor(Math.random() * 3.0D));
    randomLocation.getWorld().spawnFallingBlock(randomLocation, Material.ANVIL, (byte) 0);
  }
}
