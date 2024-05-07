package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getPluginManager;

public class VictoryHeatExecutor extends AbstractExecutor implements Listener {
  
  
  public VictoryHeatExecutor(Player player) {
    super(player);
    
    try {
      getPluginManager().getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class)
          .invoke(getPluginManager(), this, Main.getInstance());
    } catch (Exception ignore) {
    }
  }
  
  @Override
  public void tick() {
    Location randomLocation = this.player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 0, Math.floor(Math.random() * 3.0D));
    for (int i = 0; i < 5; i++) {
      randomLocation.getBlock().setType(Material.FIRE);
      randomLocation = this.player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 0, Math.floor(Math.random() * 3.0D));
    }
  }
  
  @Override
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onEntityCombust(EntityCombustEvent evt) {
    if (evt.getEntity() instanceof Player) {
      if (evt.getEntity() == this.player) {
        evt.setCancelled(true);
      }
    }
  }
}
