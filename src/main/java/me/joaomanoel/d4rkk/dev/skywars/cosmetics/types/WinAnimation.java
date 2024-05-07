package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types;

import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.winanimations.*;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class WinAnimation extends Cosmetic {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics", "winanimations");
  private final String name;
  private final String icon;
  
  public WinAnimation(long id, String key, double coins, String permission, String name, String icon) {
    super(id, CosmeticType.WIN_ANIMATION, coins, permission);
    this.name = name;
    this.icon = icon;
    if (id != 0) {
      this.rarity = this.getRarity(key);
      this.cash = this.getCash(key);
    } else {
      this.rarity = EnumRarity.COMMON;
    }
  }
  
  public static void setupAnimations() {
    checkIfAbsent("fireworks");
    checkIfAbsent("ender_dragon");
    checkIfAbsent("cowboy");
    checkIfAbsent("thor");
    checkIfAbsent("cart");
    checkIfAbsent("rainbow");
    checkIfAbsent("victoryheat");
    checkIfAbsent("you");
    checkIfAbsent("anvil");
    checkIfAbsent("cake");
    checkIfAbsent("wither");
    checkIfAbsent("zombie");
    checkIfAbsent("tnt");
    
    new Fireworks(CONFIG.getSection("fireworks"));
    new EnderDragon(CONFIG.getSection("ender_dragon"));
    new Cowboy(CONFIG.getSection("cowboy"));
    new Thor(CONFIG.getSection("thor"));
    new Cart(CONFIG.getSection("cart"));
    new ColoredSheep(CONFIG.getSection("rainbow"));
    new You(CONFIG.getSection("you"));
    new Anvil(CONFIG.getSection("anvil"));
    new VictoryHeat(CONFIG.getSection("victoryheat"));
    new Cake(CONFIG.getSection("cake"));
    new Wither(CONFIG.getSection("wither"));
    new Zombie(CONFIG.getSection("zombie"));
    new Tnt(CONFIG.getSection("tnt"));
  }
  
  private static void checkIfAbsent(String key) {
    if (CONFIG.contains(key)) {
      return;
    }
    
    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource("winanimations.yml"), StandardCharsets.UTF_8));
    for (String dataKey : config.getConfigurationSection(key).getKeys(false)) {
      CONFIG.set(key + "." + dataKey, config.get(key + "." + dataKey));
    }
  }
  
  protected long getCash(String key) {
    if (!CONFIG.contains(key + ".cash")) {
      CONFIG.set(key + ".cash", getAbsentProperty("winanimations", key + ".cash"));
    }
    
    return CONFIG.getInt(key + ".cash");
  }
  
  protected EnumRarity getRarity(String key) {
    if (!CONFIG.contains(key + ".rarity")) {
      CONFIG.set(key + ".rarity", getAbsentProperty("winanimations", key + ".rarity"));
    }
    
    return EnumRarity.fromName(CONFIG.getString(key + ".rarity"));
  }
  
  public abstract AbstractExecutor execute(Player player);
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public ItemStack getIcon(Profile profile) {
    double coins = profile.getCoins("aCoreSkyWars");
    long cash = profile.getStats("aCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ? (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked)
        : (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$win_animation$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$win_animation$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$win_animation$icon$perm_desc$start
                .replace("{perm_desc_status}", (role == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{role}", role.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon.replace("{player}", profile.getName()) + desc + " : name>" + (color + this.name));
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
}
