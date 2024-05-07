package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.kit;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitInsaneLevel {

  private final String name;
  private final double coins;
  private final long cash;
  private final List<ItemStack> items;
  private final String desc;

  public KitInsaneLevel(String name, double coins, long cash, List<ItemStack> items, String desc) {
    this.name = name;
    this.coins = coins;
    this.cash = cash;
    this.items = items;
    this.desc = desc;
  }
  
  public String getName() {
    return this.name;
  }
  
  public double getCoins() {
    return this.coins;
  }
  
  public long getCash() {
    return this.cash;
  }
  
  public List<ItemStack> getItems() {
    return this.items;
  }
  
  public String getDesc() {
    return this.desc;
  }
}
