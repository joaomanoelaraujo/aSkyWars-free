package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getPluginManager;

public class ZombieExecutor extends AbstractExecutor implements Listener {
  
  private final ArrayList<Entity> zombies;
  
  public ZombieExecutor(Player player) {
    super(player);
    
    zombies = new ArrayList<>();
    try {
      getPluginManager().getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class)
          .invoke(getPluginManager(), this, Main.getInstance());
    } catch (Exception ignore) {
    }
  }
  
  @Override
  public void tick() {
    Location randomLocation = this.player.getLocation().clone().add(Math.floor(Math.random() * 5.0D), 0, Math.floor(Math.random() * 5.0D));
    Zombie entity = this.player.getWorld().spawn(randomLocation, Zombie.class);
    entity.setNoDamageTicks(Integer.MAX_VALUE);
    entity.setBaby(false);
    entity.setCustomName("§cVítima");
    entity.setVillager(false);
    zombies.add(entity);
  }
  
  @Override
  public void cancel() {
    zombies.forEach(Entity::remove);
    zombies.clear();
    HandlerList.unregisterAll(this);
  }
  
  /**
   * Fazer que com o {@link Zombie} não queime de dia.
   */
  @EventHandler
  public void onEntityCombust(EntityCombustEvent event) {
    if (event.getEntity() instanceof Zombie) {
      event.setCancelled(true);
    }
  }
  
  /**
   * Fazer com que {@link Zombie} nunca olhe pra ninguém.
   */
  @EventHandler
  public void onEntityTarget(EntityTargetEvent evt) {
    if (evt.getTarget() instanceof Player) {
      evt.setCancelled(true);
    }
  }
}
