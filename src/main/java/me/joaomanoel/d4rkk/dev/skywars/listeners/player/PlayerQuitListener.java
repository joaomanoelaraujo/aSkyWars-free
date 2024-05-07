package me.joaomanoel.d4rkk.dev.skywars.listeners.player;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.BuildCommand;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.ChestCommand;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.CreateCommand;
import me.joaomanoel.d4rkk.dev.skywars.tagger.TagUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    evt.setQuitMessage(null);
    BuildCommand.remove(evt.getPlayer());
    TagUtils.reset(evt.getPlayer().getName());
    Profile.getProfile(evt.getPlayer().getName()).saveSync();
    CreateCommand.CREATING.remove(evt.getPlayer());
    ChestCommand.CHEST.remove(evt.getPlayer());
  }
}
