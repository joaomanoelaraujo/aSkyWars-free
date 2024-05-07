package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations.ZombieExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Zombie extends WinAnimation {
  
  public Zombie(ConfigurationSection section) {
    super(section.getLong("id"), "zombie", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new ZombieExecutor(player);
  }
}
