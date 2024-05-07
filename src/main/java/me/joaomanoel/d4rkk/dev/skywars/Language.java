package me.joaomanoel.d4rkk.dev.skywars;

import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.config.KWriter;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings("rawtypes")
public class Language {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("language");
  public static long scoreboards$scroller$every_tick = 1;
  public static String skywars$shop = "https://store.example.net";


  public static List<String> scoreboards$scroller$titles = Arrays
      .asList("§6§lSKY WARS", "§6§lS§6§lKY WARS", "§f§lS§6§lK§6§lY WARS", "§f§lSK§6§lY §6§lWARS",
          "§f§lSKY §6§lW§6§lARS", "§f§lSKY W§6§lA§6§lRS", "§f§lSKY WA§6§lR§6§lS",
          "§f§lSKY WAR§6§lS", "§f§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§f§lSKY WARS", "§f§lSKY WARS",
          "§f§lSKY WARS", "§f§lSKY WARS", "§f§lSKY WARS", "§f§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§f§lSKY WARS", "§f§lSKY WARS", "§f§lSKY WARS", "§f§lSKY WARS", "§f§lSKY WARS",
          "§f§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS",
          "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS", "§6§lSKY WARS");
  public static String scoreboards$time$waiting = "Waiting...";
  public static String scoreboards$time$starting = "Starting in §a{time}s";
  public static String scoreboards$ranking$empty = "§7Empty §f[0]";
  public static String scoreboards$ranking$format = "{name} §f[{kills}]";
  public static List<String> scoreboards$lobby = Arrays
      .asList(" §7{date} §8LW1",
              "",
              " Your Level: {level}",
              "",
              " Solo Kills: §a{solo_kills}",
              " Solo Wins: §a{solo_wins}",
              " Doubles Kills: §a{doubles_kills}",
              " Doubles Wins: §a{doubles_wins}",
              "",
              " Coins: §6%aCore_SkyWars_coins%",
              " Souls: §b{souls}§7/{max_souls}",
              " Tokens: §20",
              "",
              " §ewww.example.net");
  public static List<String> scoreboards$waiting =
      Arrays
          .asList(" §7{date}",
                  "",
                  " Players: §a{players}/{max_players}",
                  "",
                  " {time}",
                  "",
                  " Map: §a{map}",
                  " Mode: {mode}",
                  "",
                  " §ewww.example.net");
  public static List<String> scoreboards$ingame$solo = Arrays
      .asList(" §7{date}",
              "",
              " Next Event:",
              " §a{next_event}",
              "",
              " Players left: §a{players}",
              "",
              " Kills: §a{kills}",
              "",
              " Map: §a{map}",
              " Mode: {mode}",
              "",
              " §ewww.example.net");
  public static List<String> scoreboards$ingame$dupla = Arrays
      .asList(" §7{date}",
              "",
              " Next Event:",
              " §a{next_event}",
              "",
              " Players left: §a{players}",
              " Teams left: §a{teams}",
              "",
              " Kills: §a{kills}",
              "",
              " Map: §a{map}",
              " Mode: §a{mode}",
              "",
              " §7www.example.net");
  public static List<String> lobby$npc$well$hologram = Arrays.asList("§bSoul Well", "§e§lRIGHT CLICK");

  public static String cosmetics$color$locked = "§c";
  public static String cosmetics$color$canbuy = "§e";
  public static String cosmetics$color$unlocked = "§a";
  public static String cosmetics$color$selected = "§a";
  public static String cosmetics$icon$perm_desc$common = "§cYou don't have permission.";
  public static String cosmetics$icon$perm_desc$role = "§7Exclusive for {role} §7or higher.";
  public static String cosmetics$icon$buy_desc$enough = "§cYou don't have enough balance.";
  public static String cosmetics$icon$buy_desc$click_to_buy = "§eClick to buy!";
  public static String cosmetics$icon$has_desc$select = "§aUNLOCKED!\n§eClick to select!";
  public static String cosmetics$icon$has_desc$selected = "§aUNLOCKED!\n§eClick to deselect!";

  public static String cosmetics$kit$icon$perm_desc$start = "\n \n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$kit$icon$buy_desc$start = "\n \n§7Rarity: {rarity}\n§7Cost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$kit$icon$has_desc$start = "\n \n§7Rarity: {rarity}\n \n§eAcquired!\n§eClick to customize or upgrade!";
  public static String cosmetics$perk$icon$buy_desc$start = "\n \n§7Rarity: {rarity}\n§7Cost: §2{coins}\n \n{buy_desc_status}";

  public static String cosmetics$sprays$icon$perm_desc$start = "§8Spray\n\n§7Select the {name} Spray. This\n§7change is cosmetic.\n\n§eRight-Click to preview!\n \n \n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$sprays$icon$buy_desc$start = "§8Spray\n\n§7Select the {name} Spray. This\n§7change is cosmetic.\n\n§eRight-Click to preview!\n \n \n§7Rarity: {rarity}\n§7Cost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$sprays$icon$has_desc$start = "§8Spray\n\n§7Select the {name} Spray. This\n§7change is cosmetic.\n\n§eRight-Click to preview!\n \n \n§7Rarity: {rarity}\n \n{has_desc_status}";

  public static String cosmetics$kill_effect$icon$perm_desc$start = "\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$kill_effect$icon$buy_desc$start =
      "\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n§fCost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$kill_effect$icon$has_desc$start = "\n \n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n{has_desc_status}";
  public static String cosmetics$projectile_effect$icon$perm_desc$start =
      "§8Projectile Trail\n\n§7Selects the {name} Projectile\n§7Trail.\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$projectile_effect$icon$buy_desc$start =
      "§8Projectile Trail\n\n§7Selects the {name} Projectile\n§7Trail.\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n§fCost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$projectile_effect$icon$has_desc$start =
      "§8Projectile Trail\n\n§7Selects the {name} Projectile\n§7Trail.\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$fall_effect$icon$perm_desc$start =
      "§7Quando você levar dano de queda\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$fall_effect$icon$buy_desc$start =
      "§7Quando você levar dano de queda\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$fall_effect$icon$has_desc$start =
      "§7Quando você levar dano de queda\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$teleport_effect$icon$perm_desc$start =
      "§7Quando você se teleportar\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$teleport_effect$icon$buy_desc$start =
      "§7Quando você se teleportar\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$teleport_effect$icon$has_desc$start =
      "§7Quando você se teleportar\n§7sairá partículas de {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$deathcry$icon$perm_desc$start =
      "§8Death Cry\n\n§7Plays the {name} Death Cry when you die\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$deathcry$icon$buy_desc$start =
      "§8Death Cry\n\n§7Plays the {name} Death Cry when you die\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n§7Cost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$deathcry$icon$has_desc$start =
      "§8Death Cry\n\n§7Plays the {name} Death Cry when you die\n\n§eRight-Click to preview!\n \n§7Rarity: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$deathmessage$icon$perm_desc$start =
      "§8Kill Message\n\n§7Select the {name} Kill Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$deathmessage$icon$buy_desc$start =
          "§8Kill Message\n\n§7Select the {name} Kill Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n§7Cost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$deathmessage$icon$has_desc$start =
          "§8Kill Message\n\n§7Select the {name} Kill Message for\n§7in-game chat messages!\n\n§eRight-Click to preview! \n\n§7Rarity: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$cage$icon$perm_desc$start =
              "§8Cage\n\n" +
                      "§7Select the {name} Cage.\n" +
              " \n§eRight-Click to preview!\n" +
              " \n§7Rarity {rarity}\n" +
              " \n{perm_desc_status}";
  public static String cosmetics$cage$icon$buy_desc$start =
          "§8Cage\n\n" +
                  "§7Select the {name} Cage.\n" +
                  " \n§eRight-Click to preview!\n" +
                  " \n§7Rarity {rarity}\n" +
              "§7Cost: §2{coins}\n" +
              " \n{buy_desc_status}";
  public static String cosmetics$cage$icon$has_desc$start =
          "§8Cage\n\n" +
                  "§7Select the {name} Cage.\n" +
                  " \n§eRight-Click to preview!\n" +
                  " \n§7Rarity {rarity}\n" +
              " \n{has_desc_status}";
  public static String cosmetics$balloon$icon$perm_desc$start =
          "§8Balloon\n\n" +
                  "§7Select the {name} Balloon. This\n§7change is cosmetic.\n" +
                  " \n§7Rarity {rarity}\n" +
                  " \n{perm_desc_status}";
  public static String cosmetics$balloon$icon$buy_desc$start =
          "§8Balloon\n\n" +
                  "§7Select the {name} Balloon. This\n§7change is cosmetic.\n" +
                  " \n§7Rarity {rarity}\n" +
              "§7Cost: §2{coins}\n" +
              " \n{buy_desc_status}";
  public static String cosmetics$balloon$icon$has_desc$start =
          "§8Balloon\n\n" +
                  "§7Select the {name} Balloon. This\n§7change is cosmetic.\n" +
                  " \n§7Rarity {rarity}\n" +
                  " \n{has_desc_status}";
  public static String cosmetics$win_animation$icon$perm_desc$start = "§8Victory Dance\n \n§7Rarity: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$win_animation$icon$buy_desc$start = "§8Victory Dance\n \n§7Rarity: {rarity}\n§7Cost: §2{coins}\n \n{buy_desc_status}";
  public static String cosmetics$win_animation$icon$has_desc$start = "§8Victory Dance\n \n§7Rarity: {rarity}\n \n{has_desc_status}";
  public static String chat$delay = "§cWait {time}s before speaking again.";
  public static String chat$color$default = "§7";
  public static String chat$color$custom = "§f";
  public static String chat$format$lobby = "{level} {player}{color}: {message}";
  public static String chat$format$spectator = "§8[Spectator] {player}{color}: {message}";
  public static int options$coins$wins = 50;
  public static int options$coins$kills = 5;
  public static int options$points$wins = 60;
  public static int options$points$kills = 20;
  public static int options$start$waiting = 15;
  public static int options$start$full = 10;
  @KWriter.YamlEntryInfo(annotation = "Existem dois tipos de Regeneração de Arena: WorldReload e BlockRegen.\nÉ recomendável que teste os dois e veja qual se sai melhor.")
  public static boolean options$regen$world_reload = true;
  @KWriter.YamlEntryInfo(annotation = "Quantos blocos serão regenerados por tick no BlockRegen")
  public static int options$regen$block_regen$per_tick = 20000;
  public static String options$events$refill = "Refill";
  public static String options$events$end = "End Game";
  @KWriter.YamlEntryInfo(annotation = "Se você não definir o evento FIM a partida não terá fim por tempo.\nEventos disponíveis:\nFIM:tempo_em_segundos\nANUNCIO(anuncio de minutos restante):tempo_em_segundos\nREFILL:tempo_em_segundos")
  public static List<String> options$events$solo$timings = Arrays
      .asList("REFILL:300", "REFILL:480", "ANUNCIO:540", "FIM:840");
  public static List<String> options$events$ranked$timings = Arrays
      .asList("REFILL:300", "REFILL:480", "ANUNCIO:540", "FIM:840");
  public static List<String> options$events$dupla$timings = Arrays
      .asList("REFILL:300", "REFILL:480", "ANUNCIO:540", "FIM:840");
  public static String lobby$achievement = " \n§aVocê completou o desafio §f{name}\n ";
  public static String lobby$broadcast = "{player} §6joined the lobby!";
  public static boolean lobby$tab$enabled = true;
  public static String lobby$tab$header = " \n§b§lREDE SLICK\n  §fredeslick.com\n ";
  public static String lobby$tab$footer =
      " \n \n§aForúm: §fredeslick.com/forum\n§aTwitter: §f@RedeSlick\n§aDiscord: §fredeslick.com/discord\n \n                                          §bAdquira VIP acessando: §floja.redeslick.com                                          \n ";
  //  public static long lobby$leaderboard$minutes = 30;
  public static String lobby$leaderboard$empty = "§7...";
  public static List<String> lobby$leaderboard$wins$hologram = Arrays
          .asList("{monthly_color}Monthly {total_color}Total",
                  "§6Click to change!",
                  "§e10. {name_10} §7- §e{stats_10}",
                  "§e9. {name_9} §7- §e{stats_9}",
                  "§e8. {name_8} §7- §e{stats_8}",
                  "§e7. {name_7} §7- §e{stats_7}",
                  "§e6. {name_6} §7- §e{stats_6}",
                  "§e5. {name_5} §7- §e{stats_5}",
                  "§e4. {name_4} §7- §e{stats_4}",
                  "§e3. {name_3} §7- §e{stats_3}",
                  "§e2. {name_2} §7- §e{stats_2}",
                  "§e1. {name_1} §7- §e{stats_1}",
              "",
              "§7All Modes",
              "§b§n§lMonthly Wins");
  public static List<String> lobby$leaderboard$kills$hologram = Arrays
      .asList("{monthly_color}Monthly {total_color}Total",
              "§6Click to change!",
              "§e10. {name_10} §7- §e{stats_10}",
              "§e9. {name_9} §7- §e{stats_9}",
              "§e8. {name_8} §7- §e{stats_8}",
              "§e7. {name_7} §7- §e{stats_7}",
              "§e6. {name_6} §7- §e{stats_6}",
              "§e5. {name_5} §7- §e{stats_5}",
              "§e4. {name_4} §7- §e{stats_4}",
              "§e3. {name_3} §7- §e{stats_3}",
              "§e2. {name_2} §7- §e{stats_2}",
              "§e1. {name_1} §7- §e{stats_1}",
              "",
              "§7All Modes",
              "§b§n§lMonthly Kills");

  public static String lobby$npc$play$connect = "§aConnecting...";
  public static String lobby$npc$play$menu$info$item = "PAPER : 1 : name>§aInformation : desc>{desc}";
  public static String lobby$npc$play$menu$info$desc_limit =
          "§fDaily Limit: §7{limit}\n \n§7Players with §aVIP §7or\n§7higher rank can choose maps without\n§7any limit.\n \n&7www.redeslick.com/store";
  public static String lobby$npc$play$menu$info$desc_not_limit = "§7You don't have a daily selection limit.";
  public static String lobby$npc$deliveries$deliveries = "§c{deliveries} Delivery{s}";
  public static List<String> lobby$npc$deliveries$hologram = Arrays
          .asList("{deliveries}", "§bDelivery", "§e§lRIGHT CLICK");
  public static List<String> lobby$npc$stats$hologram = Arrays
          .asList("§6Statistics", "Total Kills: §7%aCore_SkyWars_kills%", "Total Wins: §7%aCore_SkyWars_wins%", "§e§lRIGHT CLICK");

  public static List<String> lobby$npc$play$solo$hologram = Arrays
      .asList("§e§lCLICK TO PLAY",
              "§bSolo §7[Normal/Insane]",
              "§e§l{players} Players");
  public static List<String> lobby$npc$play$dupla$hologram = Arrays
      .asList("§e§lCLICK TO PLAY",
              "§bDoubles §7[Normal/Insane]",
              "§e§l{players} Players");
  public static List<String> lobby$npc$play$ranked$hologram = Arrays
      .asList("§bNormal",
              "§a{players} Jogadores");
  public static String lobby$npc$deliveries$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1ODM0NTc4OTkzMTksInByb2ZpbGVJZCI6IjIxMWNhN2E4ZWFkYzQ5ZTVhYjBhZjMzMTBlODY0M2NjIiwicHJvZmlsZU5hbWUiOiJNYXh0ZWVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85MWU0NTc3OTgzZjEzZGI2YTRiMWMwNzQ1MGUyNzQ2MTVkMDMyOGUyNmI0MGQ3ZDMyMjA3MjYwOWJmZGQ0YTA4IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=";
  public static String lobby$npc$deliveries$skin$signature =
      "SXnMF3f9x90fa+FdP2rLk/V6/zvMNuZ0sC4RQpPHF9JxdVWYRZm/+DhxkfjCHWKXV/4FSTN8LPPsxXd0XlYSElpi5OaT9/LGhITSK6BbeBfaYhLZnoD0cf9jG9nl9av38KipnkNXI+cO3wttB27J7KHznAmfrJd5bxdO/M0aGQYtwpckchYUBG6pDzaxN7tr4bFxDdxGit8Tx+aow/YtYSQn4VilBIy2y/c2a4PzWEpWyZQ94ypF5ZojvhaSPVl88Fbh+StdgfJUWNN3hNWt31P68KT4Jhx+SkT2LTuDj0jcYsiuxHP6AzZXtOtPPARqM0/xd53CUHCK+TEF5mkbJsG/PZYz/JRR1B1STk4D2cgbhunF87V4NLmCBtF5WDQYid11eO0OnROSUbFduCLj0uJ6QhNRRdhSh54oES7vTi0ja3DftTjdFhPovDAXQxCn+ROhTeSxjW5ZvP6MpmJERCSSihv/11VGIrVRfj2lo9MaxRogQE3tnyMNKWm71IRZQf806hwSgHp+5m2mhfnjYeGRZr44j21zqnSKudDHErPyEavLF83ojuMhNqTTO43ri3MVbMGix4TbIOgB2WDwqlcYLezENBIIkRsYO/Y1r5BWCA7DJ5IlpxIr9TCu39ppVmOGReDWA/Znyox5GP6JIM53kQoTOFBM3QWIQcmXll4=";
  public static String lobby$npc$play$solo$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1MjM1Njk3MjI0OTgsInByb2ZpbGVJZCI6IjdiM2QxNGQ2YzExZDRjODA5NTc1ZjI5ODczNGE0ZDFiIiwicHJvZmlsZU5hbWUiOiJUYWxvbkRldiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwMTdhYmQ5ZjExZTlkZTM4ODBkNGM0OTAxODUzNTdiOGY4ZmY1NGM3MzA2Mzg2ZTgyYWQ1NjdhNTMwMzMifX19";
  public static String lobby$npc$play$solo$skin$signature =
      "i7k5tYkZ0CJ1hnGrGELLVXjIi0hfVVtg+c4a/iXP4wOwvAPj6tQtExFWgGaZYnYhN6ldcjJKUw13a/TRwHi4er4OceOlxBgqSvc0zzT7U4iZsEUuCwv7r9t6a+3MELqSQe3/bbX6WP6pDA9TRSVWaCTGpBtZfAYyrszk+VTowMjKrDB7r/kzrhE+h2rSozVcv4fUMGOd4m8xbTPlcvBatZ9OcHfZEpuoTpECUq3tWH3GIJi+Uxz3rTVl5rKJdKLOeUVXLpiLSgQ0jybMy705WlB0NWFbWFkY0mEQU7yca6keopEsGaQ+36yEtcE4hKYhibqW2sFhne/wIZh5arwyXVv/04twL/dpdiBwg4nqGEO60i+tQoF9RVWeCmIwJizEn3+WO6H2QogfCy+W1vNO65/HoHlhVbC6Y6nkUUQ8r0jtqz/sBQVAEBhFDjOQcdFucyjnO4LXrZPajdzJtBhkottBZDQZQlbFoZxC47WpQ+sktc51SWT2f3BzMowRKg08R8xpZxMTf+bB5OldilMuDPggXF/wVQU4+N9OFo1qYNxRPtM/7DCP8dtS7pwfhJkRhnQOfBVu7/mkNX1EM3mlMRzhEiUmqXfhL3SSyzTzqdTB76JgrRF92zuW+ouUlnXHe4hWiaWvRQ1XHB4fc+HOQ6/1RMYb4NItJFte1tjcQQs=";
  public static String lobby$npc$play$dupla$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1MjM1Njk3MjI0OTgsInByb2ZpbGVJZCI6IjdiM2QxNGQ2YzExZDRjODA5NTc1ZjI5ODczNGE0ZDFiIiwicHJvZmlsZU5hbWUiOiJUYWxvbkRldiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwMTdhYmQ5ZjExZTlkZTM4ODBkNGM0OTAxODUzNTdiOGY4ZmY1NGM3MzA2Mzg2ZTgyYWQ1NjdhNTMwMzMifX19";
  public static String lobby$npc$play$dupla$skin$signature =
      "i7k5tYkZ0CJ1hnGrGELLVXjIi0hfVVtg+c4a/iXP4wOwvAPj6tQtExFWgGaZYnYhN6ldcjJKUw13a/TRwHi4er4OceOlxBgqSvc0zzT7U4iZsEUuCwv7r9t6a+3MELqSQe3/bbX6WP6pDA9TRSVWaCTGpBtZfAYyrszk+VTowMjKrDB7r/kzrhE+h2rSozVcv4fUMGOd4m8xbTPlcvBatZ9OcHfZEpuoTpECUq3tWH3GIJi+Uxz3rTVl5rKJdKLOeUVXLpiLSgQ0jybMy705WlB0NWFbWFkY0mEQU7yca6keopEsGaQ+36yEtcE4hKYhibqW2sFhne/wIZh5arwyXVv/04twL/dpdiBwg4nqGEO60i+tQoF9RVWeCmIwJizEn3+WO6H2QogfCy+W1vNO65/HoHlhVbC6Y6nkUUQ8r0jtqz/sBQVAEBhFDjOQcdFucyjnO4LXrZPajdzJtBhkottBZDQZQlbFoZxC47WpQ+sktc51SWT2f3BzMowRKg08R8xpZxMTf+bB5OldilMuDPggXF/wVQU4+N9OFo1qYNxRPtM/7DCP8dtS7pwfhJkRhnQOfBVu7/mkNX1EM3mlMRzhEiUmqXfhL3SSyzTzqdTB76JgrRF92zuW+ouUlnXHe4hWiaWvRQ1XHB4fc+HOQ6/1RMYb4NItJFte1tjcQQs=";
  public static String lobby$npc$play$ranked$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1MjM1Njk3MjI0OTgsInByb2ZpbGVJZCI6IjdiM2QxNGQ2YzExZDRjODA5NTc1ZjI5ODczNGE0ZDFiIiwicHJvZmlsZU5hbWUiOiJUYWxvbkRldiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwMTdhYmQ5ZjExZTlkZTM4ODBkNGM0OTAxODUzNTdiOGY4ZmY1NGM3MzA2Mzg2ZTgyYWQ1NjdhNTMwMzMifX19";
  public static String lobby$npc$play$ranked$skin$signature =
      "i7k5tYkZ0CJ1hnGrGELLVXjIi0hfVVtg+c4a/iXP4wOwvAPj6tQtExFWgGaZYnYhN6ldcjJKUw13a/TRwHi4er4OceOlxBgqSvc0zzT7U4iZsEUuCwv7r9t6a+3MELqSQe3/bbX6WP6pDA9TRSVWaCTGpBtZfAYyrszk+VTowMjKrDB7r/kzrhE+h2rSozVcv4fUMGOd4m8xbTPlcvBatZ9OcHfZEpuoTpECUq3tWH3GIJi+Uxz3rTVl5rKJdKLOeUVXLpiLSgQ0jybMy705WlB0NWFbWFkY0mEQU7yca6keopEsGaQ+36yEtcE4hKYhibqW2sFhne/wIZh5arwyXVv/04twL/dpdiBwg4nqGEO60i+tQoF9RVWeCmIwJizEn3+WO6H2QogfCy+W1vNO65/HoHlhVbC6Y6nkUUQ8r0jtqz/sBQVAEBhFDjOQcdFucyjnO4LXrZPajdzJtBhkottBZDQZQlbFoZxC47WpQ+sktc51SWT2f3BzMowRKg08R8xpZxMTf+bB5OldilMuDPggXF/wVQU4+N9OFo1qYNxRPtM/7DCP8dtS7pwfhJkRhnQOfBVu7/mkNX1EM3mlMRzhEiUmqXfhL3SSyzTzqdTB76JgrRF92zuW+ouUlnXHe4hWiaWvRQ1XHB4fc+HOQ6/1RMYb4NItJFte1tjcQQs=";
  public static String ingame$broadcast$join = "{player} §ejoined the game! §a[{players}/{max_players}]";
  public static String ingame$broadcast$leave = "{player} §cleft the game! §a[{players}/{max_players}]";
  public static String ingame$broadcast$starting = "§aThe game starts in §f{time} §asecond{s}.";
  public static String ingame$broadcast$suicide = "{name} §edied alone.";
  public static String ingame$broadcast$default_killed_message = "{name} §ewas killed by {killer}";
  public static String ingame$broadcast$double_kill = "§e. §e§lDOUBLE KILL";
  public static String ingame$broadcast$triple_kill = "§e. §b§lTRIPLE KILL";
  public static String ingame$broadcast$quadra_kill = "§e. §6§lQUADRA KILL";
  public static String ingame$broadcast$monster_kill = "§e. §c§lMONSTER KILL";
  public static String ingame$broadcast$end = " \n§aTime's up, there were no winners.\n ";
  public static String ingame$broadcast$win$solo = " \n{name} §awon the game!\n ";
  public static String ingame$broadcast$win$dupla = " \n{name} §awon the game!\n ";
  public static String ingame$actionbar$killed = "§a{alive} §aplayers remaining!";
  public static String ingame$actionbar$kitselected = "§eSelected Kit: §a{kit}";
  public static String ingame$titles$end$header = "";
  public static String ingame$titles$end$footer = "§a{time} minute{s} remaining";
  public static String ingame$titles$refill$header = "";
  public static String ingame$titles$refill$footer = "§aChests have been refilled";
  public static String ingame$titles$border$header = "§c§lWARNING";
  public static String ingame$titles$border$footer = "§aYou are leaving the border!";
  public static String ingame$titles$death$header = "§c§lYOU DIED";
  public static String ingame$titles$death$footer = "§7You are now a spectator";
  public static String ingame$titles$win$header = "§6§lVICTORY";
  public static String ingame$titles$win$footer = "§7You are the last one standing";
  public static String ingame$titles$lose$header = "§c§lGAME OVER";
  public static String ingame$titles$lose$footer = "§7You were not victorious this time";
  public static String ingame$messages$bow$hit = "{name} §awas hit and has §c{hp} §aHP left.";
  public static String ingame$messages$coins$base = " \n  §a{coins} coins earned in this game:\n {coins_win}{coins_kills}\n ";
  public static String ingame$messages$coins$win = "\n       §a+{coins} §ffor winning the game";
  public static String ingame$messages$coins$kills = "\n       §a+{coins} §ffor getting §c{kills} §fkills";
  public static String ingame$messages$points$base = "\n  \n §a{points} points earned in this game:\n {points_win}{points_kills}\n ";
  public static String ingame$messages$points$win = "\n       §a+{points} §ffor winning the game";
  public static String ingame$messages$points$kills = "\n       §a+{points} §ffor getting §c{kills} §fkills";


  public static void setupLanguage() {
    boolean save = false;
    KWriter writer = Main.getInstance().getWriter(CONFIG.getFile(),
        "aSkyWars - Created by D4RKK \nConfiguration Version: " + Main.getInstance()
            .getDescription().getVersion());
    for (Field field : Language.class.getDeclaredFields()) {
      if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
        String nativeName = field.getName().replace("$", ".").replace("_", "-");
        
        try {
          Object value;
          KWriter.YamlEntryInfo entryInfo = field.getAnnotation(KWriter.YamlEntryInfo.class);
          
          if (CONFIG.contains(nativeName)) {
            value = CONFIG.get(nativeName);
            if (value instanceof String) {
              value = StringUtils.formatColors((String) value).replace("\\n", "\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            field.set(null, value);
            writer.set(nativeName, new KWriter.YamlEntry(
                new Object[]{entryInfo == null ? "" : entryInfo.annotation(),
                    CONFIG.get(nativeName)}));
          } else {
            value = field.get(null);
            if (value instanceof String) {
              value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            save = true;
            writer.set(nativeName, new KWriter.YamlEntry(
                new Object[]{entryInfo == null ? "" : entryInfo.annotation(), value}));
          }
        } catch (ReflectiveOperationException e) {
          LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
        }
      }
    }
    if (save) {
      writer.write();
      CONFIG.reload();
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
          () -> LOGGER.info("A config §6language.yml §afoi modificada ou criada."));
    }
  }
}