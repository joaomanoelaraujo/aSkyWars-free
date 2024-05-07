package me.joaomanoel.d4rkk.dev.skywars;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.plugin.KPlugin;
import me.joaomanoel.d4rkk.dev.skywars.cmd.Commands;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsChest;
import me.joaomanoel.d4rkk.dev.skywars.hook.SWCoreHook;
import me.joaomanoel.d4rkk.dev.skywars.level.SkyWarsLevel;
import me.joaomanoel.d4rkk.dev.skywars.listeners.Listeners;
import me.joaomanoel.d4rkk.dev.skywars.lobby.*;
import me.joaomanoel.d4rkk.dev.skywars.tagger.TagUtils;
import me.joaomanoel.d4rkk.dev.skywars.utils.sign.Sign;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Bukkit;

public class Main extends KPlugin {

  public static boolean kMysteryBox, kCosmetics, kClans;
  public static String currentServerName;
  private static Main instance;
  private static boolean validInit;


  public static Main getInstance() {
    return instance;
  }

  @Override
  public void start() {
    instance = this;
  }

  @Override
  public void load() {
  }

  @Override
  public void enable() {
    if (MinecraftVersion.getCurrentVersion().getCompareId() != 183) {
      this.setEnabled(false);
      this.getLogger().warning("The plugin only works in version 1_8_R3 (Current: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }

    saveDefaultConfig();
    currentServerName = getConfig().getString("lobby");
    kMysteryBox = Bukkit.getPluginManager().getPlugin("kMysteryBox") != null;
    kCosmetics = Bukkit.getPluginManager().getPlugin("kCosmetics") != null;
    kClans = Bukkit.getPluginManager().getPlugin("kClans") != null;
    if (getConfig().getString("spawn") != null) {
      Core.setLobby(BukkitUtils.deserializeLocation(getConfig().getString("spawn")));
    }

          getLogger().info("This plugin is free! acess discord to premium skywars!");
          getLogger().warning("https://discord.gg/QU5GvqdYUQ");
          AbstractSkyWars.setupGames();
          Language.setupLanguage();
          SWCoreHook.setupHook();
          Listeners.setupListeners();

          Commands.setupCommands();

          Leaderboard.setupLeaderboards();

          Sign.registerTaskUpdaterSign();

          DeliveryNPC.setupNPCs();
          Cosmetic.setupCosmetics();
          PlayNPC.setupNPCs();
          SkyWarsLevel.setupLevels();

          SkyWarsChest.ChestType.setupChestTypes();
          Lobby.setupLobbies();


          validInit = true;

          getLogger().info("The plugin has been activated.");
  }

  @Override
  public void disable() {
    if (validInit) {
      DeliveryNPC.listNPCs().forEach(DeliveryNPC::destroy);
      PlayNPC.listNPCs().forEach(PlayNPC::destroy);
      Leaderboard.listLeaderboards().forEach(Leaderboard::destroy);
      TagUtils.reset();
    }
    this.getLogger().info("The plugin has been deactivated.");
  }
}