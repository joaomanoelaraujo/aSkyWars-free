package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class CartExecutor extends AbstractExecutor {
  
  private Entity cart;
  
  public CartExecutor(Player player) {
    super(player);
    this.cart = player.getWorld().spawn(player.getLocation(), Minecart.class);
  }
  
  @Override
  public void tick() {
    this.cart.teleport(player.getLocation().add(0.5, 0, 0.5));
  }
  
  @Override
  public void cancel() {
    this.cart.remove();
    this.cart = null;
  }
}
