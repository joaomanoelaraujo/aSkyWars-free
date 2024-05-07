package me.joaomanoel.d4rkk.dev.skywars.hook;

import com.comphenix.protocol.ProtocolLibrary;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.achievements.Achievement;
import me.joaomanoel.d4rkk.dev.achievements.types.BlockSumoAchievement;
import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.player.hotbar.HotbarAction;
import me.joaomanoel.d4rkk.dev.player.hotbar.HotbarActionType;
import me.joaomanoel.d4rkk.dev.player.hotbar.HotbarButton;
import me.joaomanoel.d4rkk.dev.player.scoreboard.KScoreboard;
import me.joaomanoel.d4rkk.dev.player.scoreboard.scroller.ScoreboardScroller;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Kit;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.SkyWarsTeam;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
import me.joaomanoel.d4rkk.dev.skywars.hook.hotbar.SWHotbarActionType;
import me.joaomanoel.d4rkk.dev.skywars.hook.protocollib.HologramAdapter;
import me.joaomanoel.d4rkk.dev.skywars.level.SkyWarsLevel;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;

public class SWCoreHook {
  
  public static void setupHook() {
    Core.minigame = "Sky Wars";
    
    setupHotbars();
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (profile.getScoreboard() != null) {
            profile.getScoreboard().scroll();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, Language.scoreboards$scroller$every_tick);
    
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (!profile.playingGame() && profile.getScoreboard() != null) {
            profile.update();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
    
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
  }
  
  public static void checkAchievements(Profile profile) {
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
      Achievement.listAchievements(BlockSumoAchievement.class).stream().filter(swa -> swa.canComplete(profile)).forEach(swa -> {
        swa.complete(profile);
        profile.getPlayer().sendMessage(Language.lobby$achievement.replace("{name}", swa.getName()));
      });
    });
  }
  public static void checkLevel(Profile profile) {
    SkyWarsLevel level = SkyWarsLevel.getPlayerLevel(profile);
    if (level != null) {
      level.tryUpgrade(profile);
    }

  }
  public static void reloadScoreboard(Profile profile) {
    if (!profile.playingGame()) {
      checkAchievements(profile);
      checkLevel(profile);
    }

    long is = profile.getStats("aCoreSkyWars", "insanekills");
    long ns = profile.getStats("aCoreSkyWars", "normalkills");

    long dis = profile.getStats("aCoreSkyWars", "insane2v2kills");
    long dns = profile.getStats("aCoreSkyWars", "normal2v2kills");
    double stt = profile.getStats("aCoreSkyWars", "souls");
    long gers = is + ns;
    long gerd = dis + dns;
    long wis1 = profile.getStats("aCoreSkyWars", "insanewins");
    long wns2 = profile.getStats("aCoreSkyWars", "normalwins");

    long wis = profile.getStats("aCoreSkyWars", "insane2v2wins");
    long wns = profile.getStats("aCoreSkyWars", "normal2v2wins");

    long wers = wis1 + wns2;
    long werd = wis + wns;
    Player player = profile.getPlayer();
    AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
    List<String> lines = game == null ?
        Language.scoreboards$lobby :
        game.getState() == GameState.AGUARDANDO ?
            Language.scoreboards$waiting :
            (game.getMode() == SkyWarsMode.INSANE || game.getMode() == SkyWarsMode.NORMAL ? Language.scoreboards$ingame$solo : Language.scoreboards$ingame$dupla);
    
    profile.setScoreboard(new KScoreboard() {
      @Override
      public void update() {
        for (int index = 0; index < Math.min(lines.size(), 15); index++) {
          String line = lines.get(index);
          if (game != null) {
            Kit kit = profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(CosmeticType.KIT, Kit.class, game.getMode().getCosmeticIndex());
            line =
                line.replace("{map}", game.getMapName())
                    .replace("{server}", game.getGameName())
                    .replace("{mode}", game.getMode() == SkyWarsMode.INSANE ? "§cInsane" : game.getMode() == SkyWarsMode.NORMAL ? "§9Normal" : game.getMode() == SkyWarsMode.INSANEDOUBLES ? "§cInsane Doubles" : "§9Normal Doubles" )
                    .replace("{next_event}", game.getEvent())
                    .replace("{players}", StringUtils.formatNumber(game.getOnline()))
                    .replace("{teams}", StringUtils.formatNumber(game.listTeams().stream().filter(SkyWarsTeam::isAlive).count()))
                    .replace("{max_players}", StringUtils.formatNumber(game.getMaxPlayers()))
                    .replace("{time}", game.getTimer() == 16 ? Language.scoreboards$time$waiting : Language.scoreboards$time$starting.replace("{time}", StringUtils.formatNumber(game.getTimer())))
                    .replace("{kills}", StringUtils.formatNumber(game.getKills(player)))
                    .replace("{ranking_1}", game.getTopKill(1))
                    .replace("{ranking_2}", game.getTopKill(2))
                    .replace("{date}", new SimpleDateFormat("dd/MM/yy").format(System.currentTimeMillis()))
                    .replace("{kit}", kit == null ? "Nenhum" : kit.getName())
                    .replace("{ranking_3}", game.getTopKill(3));
          } else {
            line = PlaceholderAPI.setPlaceholders(player, line);
            try{
              line = line.replace("{level}", StringUtils.getFirstColor(SkyWarsLevel.getPlayerLevel(profile).getTag()) + StringUtils.formatNumber(profile.getStats("aCoreSkyWars", "level")) + SkyWarsLevel.getPlayerLevel(profile).getSymbol());
              SkyWarsLevel next = SkyWarsLevel.listLevels().stream().filter((a) -> {
                return a.getLevel() == SkyWarsLevel.getPlayerLevel(profile).getLevel() + 1L;
              }).findFirst().orElse(null);
            } catch (Exception ignored){
            }
            line = line.replace("{date}", new SimpleDateFormat("dd/MM/yy").format(System.currentTimeMillis()));
            line = line.replace("{solo_kills}", StringUtils.formatNumber(gers));
            line = line.replace("{doubles_kills}", StringUtils.formatNumber(gerd));
            line = line.replace("{souls}", StringUtils.formatNumber(stt));
            line = line.replace("{max_souls}", StringUtils.formatNumber(250));
            line = line.replace("{solo_wins}", StringUtils.formatNumber(wers));
            line = line.replace("{doubles_wins}", StringUtils.formatNumber(werd));
          }
          
          this.add(15 - index, line);
        }
      }
    }.scroller(new ScoreboardScroller(Language.scoreboards$scroller$titles)).to(player).build());
    if (game != null && game.getState() != GameState.AGUARDANDO) {
      profile.getScoreboard().health().updateHealth();
    }
    profile.update();
    profile.getScoreboard().scroll();
  }
  
  private static void setupHotbars() {
    HotbarActionType.addActionType("skywars", new SWHotbarActionType());
    
    KConfig config = Main.getInstance().getConfig("hotbar");
    for (String id : new String[]{"lobby", "waiting", "spectator"}) {
      Hotbar hotbar = new Hotbar(id);
      
      ConfigurationSection hb = config.getSection(id);
      for (String button : hb.getKeys(false)) {
        try {
          hotbar.getButtons().add(new HotbarButton(hb.getInt(button + ".slot"), new HotbarAction(hb.getString(button + ".execute")), hb.getString(button + ".icon")));
        } catch (Exception ex) {
          Main.getInstance().getLogger().log(Level.WARNING, "Failed to load button \"" + button + "\" from the hotbar \"" + id + "\": ", ex);
        }
      }
      
      Hotbar.addHotbar(hotbar);
    }
  }
}
