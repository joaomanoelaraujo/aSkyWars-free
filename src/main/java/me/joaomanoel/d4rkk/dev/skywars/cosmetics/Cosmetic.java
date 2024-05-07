package me.joaomanoel.d4rkk.dev.skywars.cosmetics;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.*;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class Cosmetic {
  
  private static final List<Cosmetic> COSMETICS = new ArrayList<>();
  protected long cash;
  protected EnumRarity rarity;
  private final long id;
  private final double coins;
  private final String permission;
  private final CosmeticType type;
  
  public Cosmetic(long id, CosmeticType type, double coins, String permission) {
    this.id = id;
    this.coins = coins;
    this.permission = permission;
    this.type = type;
    COSMETICS.add(this);
  }
  
  public static void setupCosmetics() {
    Perk.setupPerks();
    PerkInsane.setupPerksInsane();
    KillEffect.setupEffects();
    ProjectileEffect.setupProjectileEffects();
    KitInsane.setupKits();
    Kit.setupKits();
    Cage.setupCages();
    WinAnimation.setupAnimations();
  }
  
  public static void removeCosmetic(Cosmetic cosmetic) {
    COSMETICS.remove(cosmetic);
  }
  
  public static <T extends Cosmetic> T findById(Class<T> cosmeticClass, long id) {
    return COSMETICS.stream()
        .filter(cosmetic -> (cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass)) && cosmetic.getId() == id)
        .map(cosmetic -> (T) cosmetic).findFirst().orElse(null);
  }
  
  public static Cosmetic findById(String lootChestID) {
    return COSMETICS.stream().filter(cosmetic -> cosmetic.getLootChestsID().equals(lootChestID)).findFirst().orElse(null);
  }
  
  public static List<Cosmetic> listCosmetics() {
    return COSMETICS;
  }
  
  public static <T extends Cosmetic> List<T> listByType(Class<T> cosmeticClass) {
    return COSMETICS.stream().filter(cosmetic -> cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass))
        .sorted(Comparator.comparingLong(Cosmetic::getId)).map(cosmetic -> (T) cosmetic).collect(Collectors.toList());
  }
  
  protected static Object getAbsentProperty(String file, String property) {
    InputStream stream = Main.getInstance().getResource(file + ".yml");
    if (stream == null) {
      return null;
    }
    
    return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8)).get(property);
  }
  
  public void give(Profile profile) {
    profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).addCosmetic(this);
  }
  
  public boolean has(Profile profile) {
    return profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).hasCosmetic(this);
  }
  
  public boolean isSelected(Profile profile) {
    return profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).isSelected(this);
  }
  
  public boolean isSelectedPerk(Profile profile) {
    int checkIndex = profile.getPlayer().hasPermission("role.mvpplus") ? 6 : profile.getPlayer().hasPermission("role.mvp") ? 5
        : profile.getPlayer().hasPermission("role.vip") ? 4 : 3;
    boolean isSelected = false;
    for (int i = 1; i < checkIndex + 1; i++) {
      if (isSelected) {
        continue;
      }
      Cosmetic cosmetic =
          profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(CosmeticType.PERK, Perk.class, i);
      if (cosmetic == null) {
        continue;
      }
      if (cosmetic == this) {
        isSelected = true;
      }
    }
    return isSelected;
  }

  public boolean isSelectedPerkInsane(Profile profile) {
    int checkIndex = profile.getPlayer().hasPermission("role.mvpplus") ? 6 : profile.getPlayer().hasPermission("role.mvp") ? 5
            : profile.getPlayer().hasPermission("role.vip") ? 4 : 3;
    boolean isSelected = false;
    for (int i = 1; i < checkIndex + 1; i++) {
      if (isSelected) {
        continue;
      }
      Cosmetic cosmetic =
              profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(CosmeticType.PERKINSANE, PerkInsane.class, i);
      if (cosmetic == null) {
        continue;
      }
      if (cosmetic == this) {
        isSelected = true;
      }
    }
    return isSelected;
  }
  public int getAvailableSlotInsane(Profile profile) {
    int checkIndex = profile.getPlayer().hasPermission("role.mvpplus") ? 6 : profile.getPlayer().hasPermission("role.mvp") ? 5
            : profile.getPlayer().hasPermission("role.vip") ? 4 : 3;
    int isSelected = 1;
    for (int i = 1; i < checkIndex + 1; i++) {
      Cosmetic cosmetic =
              profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(CosmeticType.PERKINSANE, PerkInsane.class, i);
      if (cosmetic != null) {
        i++;
      } else {
        isSelected = i;
      }
    }
    return isSelected;
  }
  public int getAvailableSlot(Profile profile) {
    int checkIndex = profile.getPlayer().hasPermission("role.mvpplus") ? 6 : profile.getPlayer().hasPermission("role.mvp") ? 5
        : profile.getPlayer().hasPermission("role.vip") ? 4 : 3;
    int isSelected = 1;
    for (int i = 1; i < checkIndex + 1; i++) {
      Cosmetic cosmetic =
          profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(type, Perk.class, i);
      if (cosmetic != null) {
        i++;
      } else {
        isSelected = i;
      }
    }
    return isSelected;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getLootChestsID() {
    return "sw" + this.type.ordinal() + "-" + this.id;
  }
  
  public long getIndex() {
    return 1;
  }
  
  public EnumRarity getRarity() {
    return this.rarity;
  }
  
  public double getCoins() {
    return this.coins;
  }
  
  public long getCash() {
    return this.cash;
  }
  
  public String getPermission() {
    return this.permission;
  }
  
  public CosmeticType getType() {
    return this.type;
  }
  
  public boolean canBuy(Player player) {
    return this.permission.isEmpty() || player.hasPermission(this.permission);
  }
  
  public abstract String getName();
  
  public abstract ItemStack getIcon(Profile profile);
}
