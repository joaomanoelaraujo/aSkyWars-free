package me.joaomanoel.d4rkk.dev.skywars.game.object;

import org.bukkit.Material;

public class SkyWarsBlock {
  
  private final Material material;
  private final byte data;
  
  public SkyWarsBlock(Material material, byte data) {
    this.material = material;
    this.data = data;
  }
  
  public Material getMaterial() {
    return this.material;
  }
  
  public byte getData() {
    return this.data;
  }
  
  @Override
  public String toString() {
    return "SkyWarsBlock{material=" + material + ", data=" + data + "}";
  }
}
