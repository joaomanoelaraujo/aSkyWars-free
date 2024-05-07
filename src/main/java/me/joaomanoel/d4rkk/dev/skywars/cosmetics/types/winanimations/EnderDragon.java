package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations.EnderDragonExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class EnderDragon extends WinAnimation {
  
  public EnderDragon(ConfigurationSection section) {
    super(section.getLong("id"), "ender_dragon", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new EnderDragonExecutor(player);
  }
}
