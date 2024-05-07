package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class ColoredSheepExecutor extends AbstractExecutor implements Listener {
  
  private final DyeColor[] dyes = new DyeColor[]{DyeColor.WHITE, DyeColor.ORANGE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.PINK, DyeColor.GRAY, DyeColor.SILVER, DyeColor.CYAN, DyeColor.PURPLE, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK};
  private final List<Sheep> entitestodelete = new ArrayList<>();
  
  public ColoredSheepExecutor(Player player) {
    super(player);
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @Override
  public void tick() {
    Sheep sheep = this.player.getWorld().spawn(player.getLocation().clone().add(Math.floor(Math.random() * 3.0D), 5, Math.floor(Math.random() * 3.0D)), Sheep.class);
    sheep.setColor(dyes[(int) Math.floor(Math.random() * 16.0D)]);
    entitestodelete.add(sheep);
  }
  
  @Override
  public void cancel() {
    entitestodelete.forEach(Entity::remove);
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamage(EntityDamageEvent evt) {
    if (evt.getEntity() instanceof Sheep) {
      evt.setCancelled(true);
    }
  }
  
}
