package me.joaomanoel.d4rkk.dev.skywars.listeners.player;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class PlayerDeathListener implements Listener {
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent evt) {
    Player player = evt.getEntity();
    evt.setDeathMessage(null);
    
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      evt.setDroppedExp(0);
      player.setHealth(20.0);
      
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game == null) {
        evt.getDrops().clear();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), profile::refresh, 3);
      } else {
        List<Profile> hitters = profile.getLastHitters();
        Profile killer = hitters.size() > 0 ? hitters.get(0) : null;
        game.kill(profile, killer);
        for (Profile hitter : hitters) {
          if (!hitter.equals(killer) && hitter.playingGame() && hitter.getGame().equals(game) && !game.isSpectator(hitter.getPlayer())) {
            hitter.addStats("aCoreSkyWars", game.getMode().getStats() + "assists");
          }
        }
        hitters.clear();
      }
    }
  }
}
