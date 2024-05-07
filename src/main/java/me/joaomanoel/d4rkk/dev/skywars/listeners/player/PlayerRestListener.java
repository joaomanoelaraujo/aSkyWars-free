package me.joaomanoel.d4rkk.dev.skywars.listeners.player;

import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.BuildCommand;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerRestListener implements Listener {
  
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile != null) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game == null) {
        evt.setCancelled(true);
      } else {
        evt.setCancelled(game.getState() != GameState.EMJOGO || game.isSpectator(evt.getPlayer()));
      }
    } else {
      evt.setCancelled(evt.getItemDrop().getType().equals(Material.GOLD_PLATE));
    }
  }
  
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile != null) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game == null) {
        evt.setCancelled(true);
      } else {
        evt.setCancelled(game.getState() != GameState.EMJOGO || game.isSpectator(evt.getPlayer()));
      }
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile != null) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game == null) {
        evt.setCancelled(!BuildCommand.hasBuilder(evt.getPlayer()));
      } else {
        if (evt.getBlock().getType() == Material.SLIME_BLOCK) {
          evt.setCancelled(true);
          return;
        }
        evt.setCancelled(game.getState() != GameState.EMJOGO || game.isSpectator(evt.getPlayer()) || !game.getCubeId().contains(evt.getBlock().getLocation()));
      }
    }
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile != null) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game == null) {
        evt.setCancelled(!BuildCommand.hasBuilder(evt.getPlayer()));
      } else {
        evt.setCancelled(game.getState() != GameState.EMJOGO || game.isSpectator(evt.getPlayer()) || !game.getCubeId().contains(evt.getBlock().getLocation()));
      }
    }
  }
}
