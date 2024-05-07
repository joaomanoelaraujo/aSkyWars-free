package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types;

import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEventHandler;
import me.joaomanoel.d4rkk.dev.skywars.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.perk.PerkLevel;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.*;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public abstract class Perk extends Cosmetic implements SWEventHandler {
  
  protected static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics/perks", "perks");
  protected List<PerkLevel> levels;
  private final String name;
  private final String icon;
  
  public Perk(long id, String key, String permission, String name, String icon, List<PerkLevel> levels) {
    super(id, CosmeticType.PERK, 0.0, permission);
    this.name = name;
    this.icon = icon;
    this.levels = levels;
    this.rarity = this.getRarity(key);
  }
  
  public static void setupPerks() {
    checkIfAbsent("preparations");
    checkIfAbsent("insanity");
    checkIfAbsent("archery_mastery");
    checkIfAbsent("sorcerer");
    checkIfAbsent("pyromaniac");
    checkIfAbsent("fall_resistant");
    checkIfAbsent("end_master");
    checkIfAbsent("cool_in_combat");
    checkIfAbsent("headshot");
    checkIfAbsent("revenge");
    checkIfAbsent("occultist");
    checkIfAbsent("rocket");
    checkIfAbsent("necromancer");
    checkIfAbsent("luck_clover");
    checkIfAbsent("armored");
    checkIfAbsent("extra_life");


    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Preparativos(1, "preparations");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Insanidade(1, "insanity");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.MaestriaComArcos(1, "archery_mastery");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Headshot(1, "headshot");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Necromante(1, "necromancer");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Feiticeiro(1, "sorcerer");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Piromaniaco(1, "pyromaniac");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.ResistenteAQuedas(1, "fall_resistant");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.MestreDoFim(1, "end_master");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.FrioNoCombate(1, "cool_in_combat");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Vinganca(1, "revenge");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Ocultista(1, "occultist");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.TrevoDaSorte(1, "luck_clover");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.Blindado(1, "armored");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk.VidaExtra(1, "extra_life");

// If the ability is enabled, register it.
    if (CONFIG.getBoolean("rocket.enabled")) {
      new Foguete(1, "rocket");
    }
  }
  
  private static void checkIfAbsent(String key) {
    if (CONFIG.contains(key)) {
      return;
    }
    
    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource("perks.yml"), StandardCharsets.UTF_8));
    for (String dataKey : config.getConfigurationSection(key).getKeys(false)) {
      CONFIG.set(key + "." + dataKey, config.get(key + "." + dataKey));
    }
  }
  
  protected EnumRarity getRarity(String key) {
    if (!CONFIG.contains(key + ".rarity")) {
      CONFIG.set(key + ".rarity", getAbsentProperty("perks", key + ".rarity"));
    }
    
    return EnumRarity.fromName(CONFIG.getString(key + ".rarity"));
  }
  
  protected void register() {
    SWEvent.registerHandler(this);
  }
  
  protected void setupLevels(String key) {
    ConfigurationSection section = CONFIG.getSection(key);
    for (String level : section.getConfigurationSection("levels").getKeys(false)) {
      if (!section.contains("levels." + level + ".cash")) {
        CONFIG.set(key + ".levels." + level + ".cash", getAbsentProperty("perks", key + ".levels." + level + ".cash"));
      }
      
      PerkLevel perkLevel =
          new PerkLevel(section.getDouble("levels." + level + ".coins"), section.getInt("levels." + level + ".cash"), section.getString("levels." + level + ".description"),
              new HashMap<>());
      for (String property : section.getConfigurationSection("levels." + level).getKeys(false)) {
        if (!property.equals("coins") && !property.equals("cash") && !property.equals("description")) {
          perkLevel.getValues().put(property, section.get("levels." + level + "." + property));
        }
      }
      
      this.levels.add(perkLevel);
    }
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public double getCoins() {
    return this.getFirstLevel().getCoins();
  }
  
  @Override
  public long getCash() {
    return this.getFirstLevel().getCash();
  }
  
  public PerkLevel getFirstLevel() {
    return this.levels.get(0);
  }
  
  public PerkLevel getCurrentLevel(Profile profile) {
    return this.levels.get((int) (profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).getLevel(this) - 1));
  }
  
  public List<PerkLevel> getLevels() {
    return this.levels;
  }
  
  @Override
  public ItemStack getIcon(Profile profile) {
    return this.getIcon(profile, true);
  }
  
  public ItemStack getIcon(Profile profile, boolean select) {
    return this.getIcon(profile, true, select);
  }
  
  public ItemStack getIcon(Profile profile, boolean useDesc, boolean select) {
    double coins = profile.getCoins("aCoreSkyWars");
    long cash = profile.getStats("aCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelectedPerk(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    int currentLevel = (int) profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).getLevel(this);
    PerkLevel perkLevel = this.levels.get(currentLevel - 1);
    String levelName = " " + (currentLevel > 3 ? currentLevel == 4 ? "IV" : "V" : StringUtils.repeat("I", currentLevel));
    String color = has ?
        Language.cosmetics$color$unlocked :
        (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = "";
    if (useDesc) {
      desc = (has && canBuy ?
          (select ? "\n \n" + (isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) : Language.cosmetics$kit$icon$has_desc$start) :
          select ?
              "" :
              canBuy ?
                  Language.cosmetics$kit$icon$buy_desc$start.replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ?
                      Language.cosmetics$icon$buy_desc$click_to_buy :
                      Language.cosmetics$icon$buy_desc$enough) :
                  Language.cosmetics$kit$icon$perm_desc$start
                      .replace("{perm_desc_status}", role == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{role}", role.getName())))
          .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins()))
          .replace("{cash}", StringUtils.formatNumber(this.getCash()));
    }
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon.replace("{description}", perkLevel.getDescription()) + desc + " : name>" + color + this.name + levelName);
    if (select && isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
}
