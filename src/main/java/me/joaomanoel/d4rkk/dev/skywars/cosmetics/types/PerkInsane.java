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
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.perk.PerkInsaneLevel;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane.Blindado;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane.TrevoDaSorte;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane.VidaExtra;
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

public abstract class PerkInsane extends Cosmetic implements SWEventHandler {

  protected static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics/perks", "perksinsane");
  protected List<PerkInsaneLevel> levels;
  private final String name;
  private final String icon;

  public PerkInsane(long id, String key, String permission, String name, String icon, List<PerkInsaneLevel> levels) {
    super(id, CosmeticType.PERKINSANE, 0.0, permission);
    this.name = name;
    this.icon = icon;
    this.levels = levels;
    this.rarity = this.getRarity(key);
  }
  
  public static void setupPerksInsane() {
    checkIfAbsent("luck_clover");
    checkIfAbsent("armored");
    checkIfAbsent("extra_life");

    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane.TrevoDaSorte(1, "luck_clover");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane.Blindado(1, "armored");
    new me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane.VidaExtra(1, "extra_life");

  }
  
  private static void checkIfAbsent(String key) {
    if (CONFIG.contains(key)) {
      return;
    }
    
    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource("perksinsane.yml"), StandardCharsets.UTF_8));
    for (String dataKey : config.getConfigurationSection(key).getKeys(false)) {
      CONFIG.set(key + "." + dataKey, config.get(key + "." + dataKey));
    }
  }
  
  protected EnumRarity getRarity(String key) {
    if (!CONFIG.contains(key + ".rarity")) {
      CONFIG.set(key + ".rarity", getAbsentProperty("perksinsane", key + ".rarity"));
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
        CONFIG.set(key + ".levels." + level + ".cash", getAbsentProperty("perksinsane", key + ".levels." + level + ".cash"));
      }
      
      PerkInsaneLevel perkLevel =
          new PerkInsaneLevel(section.getDouble("levels." + level + ".coins"), section.getInt("levels." + level + ".cash"), section.getString("levels." + level + ".description"),
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
  
  public PerkInsaneLevel getFirstLevel() {
    return this.levels.get(0);
  }
  
  public PerkInsaneLevel getCurrentLevel(Profile profile) {
    return this.levels.get((int) (profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).getLevel(this) - 1));
  }
  
  public List<PerkInsaneLevel> getLevels() {
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
    boolean isSelected = this.isSelectedPerkInsane(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    int currentLevel = (int) profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).getLevel(this);
    PerkInsaneLevel perkLevel = this.levels.get(currentLevel - 1);
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
