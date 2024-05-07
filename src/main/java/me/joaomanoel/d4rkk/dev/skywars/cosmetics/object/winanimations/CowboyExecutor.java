package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CowboyExecutor extends AbstractExecutor {
  
  private Horse horse;
  
  public CowboyExecutor(Player player) {
    super(player);
    this.horse = player.getWorld().spawn(player.getLocation(), Horse.class);
    this.horse.setTamed(true);
    this.horse.setOwner(player);
    this.horse.setAdult();
    this.horse.setColor(Horse.Color.CREAMY);
    this.horse.setStyle(Horse.Style.WHITEFIELD);
    this.horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
    this.horse.setPassenger(player);
  }
  
  @Override
  public void cancel() {
    this.horse.remove();
    this.horse = null;
  }
}
