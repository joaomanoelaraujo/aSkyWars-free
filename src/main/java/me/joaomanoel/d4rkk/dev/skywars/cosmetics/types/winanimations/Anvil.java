package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations.AnvilExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Anvil extends WinAnimation {
  
  public Anvil(ConfigurationSection section) {
    super(section.getLong("id"), "cake", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new AnvilExecutor(player);
  }
}
