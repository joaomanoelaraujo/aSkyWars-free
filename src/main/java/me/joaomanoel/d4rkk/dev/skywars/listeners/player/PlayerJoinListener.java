package me.joaomanoel.d4rkk.dev.skywars.listeners.player;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.ArenaRollbackerTask;
import me.joaomanoel.d4rkk.dev.skywars.hook.SWCoreHook;
import me.joaomanoel.d4rkk.dev.skywars.hook.mysteryboxes.MysteryBoxesHook;
import me.joaomanoel.d4rkk.dev.skywars.tagger.TagUtils;
import me.joaomanoel.d4rkk.dev.skywars.utils.updater.aUpdater;
import me.joaomanoel.d4rkk.dev.titles.TitleManager;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent evt) {
    evt.setJoinMessage(null);
    
    Player player = evt.getPlayer();
    TagUtils.sendTeams(player);
    
    Profile profile = Profile.getProfile(player.getName());
    SWCoreHook.reloadScoreboard(profile);
    profile.setHotbar(Hotbar.getHotbarById("lobby"));
    profile.refresh();
    
    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
      TagUtils.setTag(evt.getPlayer());
      
      if (Role.getPlayerRole(player).isBroadcast()) {
        String broadcast = Language.lobby$broadcast
            .replace("{player}", Role.getPrefixed(player.getName()));
        Profile.listProfiles().forEach(pf -> {
          if (!pf.playingGame()) {
            Player players = pf.getPlayer();
            if (players != null) {
              players.sendMessage(broadcast);
            }
          }
        });
      }
    }, 5);

    Bukkit.getScheduler().runTaskLaterAsynchronously(Core.getInstance(), () -> {
      TitleManager.joinLobby(profile);
    }, 10);
    
    NMS.sendTitle(player, "", "", 0, 1, 0);
    if (Language.lobby$tab$enabled) {
      NMS.sendTabHeaderFooter(player, Language.lobby$tab$header, Language.lobby$tab$footer);
    }
    if (player.hasPermission("askywars.cmd.skywars")) {
      sendUpdateNotification(player);
    }
  }


  private void sendUpdateNotification(Player player) {
    new BukkitRunnable() {
      @Override
      public void run() {
        if (aUpdater.UPDATER != null && aUpdater.UPDATER.canDownload) {
          TextComponent component = new TextComponent("");
          for (BaseComponent components : TextComponent
                  .fromLegacyText(" \n §6§laSkyWars Update Available\n \n §7There's an update available for aSkyWars. Click ")) {
            component.addExtra(components);
          }
          TextComponent click = new TextComponent("here");
          click.setColor(ChatColor.GREEN);
          click.setBold(true);
          click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/update"));
          click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent
                  .fromLegacyText("§7Click here to update aSkyWars.")));
          component.addExtra(click);
          for (BaseComponent components : TextComponent.fromLegacyText("§7.\n ")) {
            component.addExtra(components);
          }

          player.spigot().sendMessage(component);
        }
      }
    }.runTaskLater(Core.getInstance(), 20L); // Delay of 1 second after joining
  }
}
