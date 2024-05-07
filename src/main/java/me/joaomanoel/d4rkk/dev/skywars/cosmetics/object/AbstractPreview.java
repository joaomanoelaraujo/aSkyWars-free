package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractPreview<T extends Cosmetic> extends BukkitRunnable {
  
  public static final KConfig CONFIG = Main.getInstance().getConfig("previewLocations");
  protected Player player;
  protected T cosmetic;
  
  public AbstractPreview(Profile profile, T cosmetic) {
    this.player = profile.getPlayer();
    this.cosmetic = cosmetic;
  }
  
  public static boolean canDoKillEffect() {
    return CONFIG.getSection("killeffect") != null && CONFIG.getSection("killeffect").getKeys(false).size() > 2;
  }
  
  public static boolean canDoProjectileEffect() {
    return CONFIG.getSection("projectileeffect") != null && CONFIG.getSection("projectileeffect").getKeys(false).size() > 1;
  }
  
  public static boolean canDoCage() {
    return CONFIG.getSection("cage") != null && CONFIG.getSection("cage").getKeys(false).size() > 1;
  }
  
  public abstract void returnToMenu();
}
