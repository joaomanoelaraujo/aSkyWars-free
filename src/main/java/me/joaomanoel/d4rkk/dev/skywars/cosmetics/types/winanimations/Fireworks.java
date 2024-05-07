package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.winanimations;


import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations.FireworksExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Fireworks extends WinAnimation {
  
  public Fireworks(ConfigurationSection section) {
    super(0, "fireworks", 0.0, "", section.getString("name"), section.getString("icon"));
  }
  
  @Override
  public boolean has(Profile profile) {
    return true;
  }
  
  public AbstractExecutor execute(Player player) {
    return new FireworksExecutor(player);
  }
}
