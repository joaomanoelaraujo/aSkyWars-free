package me.joaomanoel.d4rkk.dev.skywars.level;

import me.joaomanoel.d4rkk.dev.deliveries.DeliveryReward;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SkyWarsLevel {
  private static final KConfig CONFIG = Main.getInstance().getConfig("levels");
  private static final List<SkyWarsLevel> LEVELS = new ArrayList<>();
  protected String tag;
  protected String name;
  protected String key;
  protected String symbol;
  protected String rewardsNames;
  protected long level;
  protected long experience;

  protected List<DeliveryReward> rewards;

  public SkyWarsLevel(String name, String symbol, String key, String tag, long level, long experience, List<String> rewards, List<String> rewardsNames) {
    this.name = StringUtils.formatColors(name);
    this.symbol = symbol;
    this.tag = StringUtils.formatColors(tag.replace("{symbol}", symbol).replace("{name}", name));
    this.level = level;
    this.key = key;
    this.rewardsNames = rewardsNames.toString();
    this.experience = experience;
    this.rewards = parseRewards(rewards);
  }

  public static SkyWarsLevel fromKey(String key) {
    return listLevels().stream().filter((k) -> k.getKey().equals(key)).findFirst().orElse(null);
  }
  private void giveRewards(Profile profile) {
    for (DeliveryReward reward : rewards) {
      reward.dispatch(profile);
    }
  }
  public static SkyWarsLevel fromPoints(long compare) {
    return listLevels().stream().filter((a) -> compare >= Long.parseLong(String.valueOf(a.getExp()))).findFirst().orElse(listLevels().get(listLevels().size() - 1));
  }

  public long getLevel() {
    return this.level;
  }

  public long getExperience() {
    return this.experience;
  }
  private List<DeliveryReward> parseRewards(List<String> rewards) {
    List<DeliveryReward> parsedRewards = new ArrayList<>();
    for (String reward : rewards) {
      parsedRewards.add(new DeliveryReward(reward));
    }
    return parsedRewards;
  }
  public void tryUpgrade(Profile profile) {
    long experience = profile.getStats("aCoreSkyWars", "experience");
    SkyWarsLevel next = listLevels().stream().filter((level) -> level.getLevel() == this.level).findFirst().orElse(null);
    String tag = StringUtils.getFirstColor(next.getTag()) + "[" + next.getLevel() + next.getSymbol() + "]";
    if (experience >= next.getExperience()) {
      profile.addStats("aCoreSkyWars", -next.getExperience(), "experience");
      profile.addStats("aCoreSkyWars", "level");
      profile.getPlayer().sendMessage("§6§lCONGRATULATIONS! §eYou have leveled up §6" + tag + "§7!");
      NMS.sendTitle(profile.getPlayer(), ChatColor.GOLD + "NEW LEVEL!", ChatColor.YELLOW + "§eYou have leveled up §6" + tag + "§7!");
      EnumSound.LEVEL_UP.play(profile.getPlayer(), 2.0F, 2.0F);
      giveRewards(profile);
    }
  }

  public static SkyWarsLevel getPlayerLevel(Profile profile) {
    long level = profile.getStats("aCoreSkyWars", "level");
    return listLevels().stream().filter((a) -> a.getLevel() == level).findFirst().orElse(null);
  }

  public SkyWarsLevel getNextLevel() {
    long nextLevel = this.level + 1;
    for (SkyWarsLevel level : listLevels()) {
      if (level.getLevel() == nextLevel) {
        return level;
      }
    }
    return null;
  }


  public static void setupLevels() {
    ConfigurationSection section = CONFIG.getSection("levels");
    section.getKeys(false).forEach((key) -> {
      String name = section.getString(key + ".name");
      String symbol = section.getString(key + ".symbol");
      String tag = section.getString(key + ".tag");
      long level = section.getLong(key + ".level");
      long experience = section.getLong(key + ".exp");
      List<String> rewards = section.getStringList(key + ".rewards");
      List<String> rewardsname = section.getStringList(key + ".rewardsname");
      SkyWarsLevel level1 = new SkyWarsLevel(name, symbol, key, tag, level, experience, rewards, rewardsname);
      LEVELS.add(level1);
    });
    LEVELS.sort((l1, l2) -> Long.compare(l2.getExp(), l1.getExp()));
  }

  public static List<SkyWarsLevel> listLevels() {
    return LEVELS;
  }

  public List<DeliveryReward> getRewards() {
    return this.rewards;
  }

  public String getRewardsNames(){
    return String.join("\n", this.rewardsNames);
  }
  public long getExp() {
    return this.experience;
  }

  public String getKey() {
    return this.key;
  }

  public String getTag() {
    return this.tag;
  }
  public static String progressBar(double youExp,double nextExp) {
    StringBuilder progressBar = new StringBuilder();
    double percentage = youExp >= nextExp ? 80.0 : ((youExp * 80.0) / nextExp);

    boolean higher = false, hasColor = false;
    for (double d = 8.0; d <= 80.0; d += 8.0) {
      if (!higher && percentage >= d) {
        progressBar.append("§b");
        higher = true;
        hasColor = true;
      } else if ((higher || !hasColor) && percentage < d) {
        higher = false;
        hasColor = true;
        progressBar.append("§7");
      }

      progressBar.append("■");
    }

    return progressBar.toString();
  }

  public static String formatXP(long xp) {
    if (xp >= 10000) { // Verifica se o XP tem 5 ou mais dígitos
      return (xp / 1000) + "K";
    } else if (xp >= 1000) { // Verifica se o XP tem 4 dígitos (entre 1000 e 9999)
      return (xp / 1000) + "k";
    } else {
      return String.valueOf(xp);
    }
  }
  public String getSymbol() {
    return this.symbol;
  }

  public String getName() {
    return this.name;
  }

}
