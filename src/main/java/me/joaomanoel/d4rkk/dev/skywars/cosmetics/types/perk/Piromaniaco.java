package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Piromaniaco extends Perk {
  
  private final int index;
  
  public Piromaniaco(int index, String key) {
    super(5, key, CONFIG.getString(key + ".permission"), CONFIG.getString(key + ".name"), CONFIG.getString(key + ".icon"), new ArrayList<>());
    this.index = index;
    this.setupLevels(key);
    Bukkit.getPluginManager().registerEvents(new Listener() {
      @EventHandler(priority = EventPriority.MONITOR)
      public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
        if (evt.isCancelled()) {
          return;
        }
        
        if (evt.getEntity() instanceof Player && evt.getDamager() instanceof Arrow) {
          Player damaged = (Player) evt.getEntity();
          Arrow projectile = (Arrow) evt.getDamager();
          
          if (projectile.getShooter() instanceof Player) {
            Player player = (Player) projectile.getShooter();
            
            Profile profile = Profile.getProfile(player.getName());
            if (profile != null) {
              AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
              if (game != null && !game.isSpectator(player) && game.getMode().getCosmeticIndex() == getIndex() && isSelectedPerk(profile) && has(profile) && canBuy(player)) {
                int percentage = getCurrentLevel(profile).getValue("percentage", int.class, 0);
                if (ThreadLocalRandom.current().nextInt(100) < percentage) {
                  damaged.setFireTicks(100);
                }
              }
            }
          }
        }
      }
    }, Main.getInstance());
  }
  
  @Override
  public long getIndex() {
    return this.index;
  }
  
  @Override
  public void handleEvent(SWEvent evt2) {
  }
  
  @Override
  public List<Class<?>> getEventTypes() {
    return null;
  }
}
