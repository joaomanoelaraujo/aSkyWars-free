package me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perksinsane;


import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.perk.PerkInsaneLevel;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.PerkInsane;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perksinsane.level.MenuBuyPerkLevel;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuPerkUpgradeInsane<T extends PerkInsane> extends PlayerMenu {
  
  private static final int[] SLOTS = {11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24, 25};
  private String name;
  private T cosmetic;
  private Class<T> cosmeticClass;
  private Map<ItemStack, Integer> intLevels = new HashMap<>();
  private Map<ItemStack, PerkInsaneLevel> perkLevels = new HashMap<>();
  public MenuPerkUpgradeInsane(Profile profile, String name, T cosmetic, Class<T> cosmeticClass) {
    super(profile.getPlayer(), "Habilidade " + cosmetic.getName(), 5);
    this.name = name;
    this.cosmetic = cosmetic;
    this.cosmeticClass = cosmeticClass;
    
    this.setItem(10, cosmetic.getIcon(profile, false, false));
    
    double coins = profile.getCoins("aCoreSkyWars");
    long cash = profile.getStats("aCoreProfile", "cash");
    long currentLevel = profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).getLevel(cosmetic);
    int slot = 0;
    for (PerkInsaneLevel perkLevel : cosmetic.getLevels()) {
      int level = slot + 1;
      
      String id = level <= currentLevel ? "13" : (level - 1) == currentLevel ? "4" : "14";
      String color = id.equals("13") ? "&a" : id.equals("4") ? "&e" : "&c";
      String levelName = " " + (level > 3 ? level == 4 ? "IV" : "V" : StringUtils.repeat("I", level));
      String desc = id.equals("13") ?
          "\n \n&aYou already have this upgrade." :
          id.equals("4") ?
              Language.cosmetics$perk$icon$buy_desc$start.replace("{rarity}", cosmetic.getRarity().getName()).replace("{cash}", StringUtils.formatNumber(perkLevel.getCash()))
                  .replace("{coins}", StringUtils.formatNumber(perkLevel.getCoins())).replace("{buy_desc_status}",
                  ((coins >= perkLevel.getCoins() || (CashManager.CASH && cash >= perkLevel.getCash())) ?
                      Language.cosmetics$icon$buy_desc$click_to_buy :
                      Language.cosmetics$icon$buy_desc$enough)) :
              "\n \n&cVocê não pode comprar este upgrade.";
      ItemStack item =
          BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:" + id + " : 1 : name>" + color + cosmetic.getName() + levelName + " : desc>&8" + perkLevel.getDescription() + desc);
      this.setItem(SLOTS[slot++], item);
      if ((level - 1) == currentLevel) {
        this.intLevels.put(item, level);
        this.perkLevels.put(item, perkLevel);
      }
    }
    
    this.setItem(40, BukkitUtils.deserializeItemStack("ARROW : 1 : name>&cGo Back : desc>&7To Perks " + this.name + "."));
    this.setItem(41, BukkitUtils.deserializeItemStack("INK_SACK:" +
        (!cosmetic.isSelectedPerkInsane(profile) ? "8" : "10") + " : 1 : name>" + (cosmetic.isSelectedPerkInsane(profile) ?
        "&aSelected : desc>&eClick to unselect!" : "&eClick to select!")));
    
    this.register(Main.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuPerksInsane<>(profile, this.name, this.cosmeticClass);
            } else if (evt.getSlot() == 41) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              if (cosmetic.isSelectedPerkInsane(profile)) {
                profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).
                    setSelected(cosmetic.getType(), 0,
                        profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).
                            getIndex(cosmetic));
              } else {
                profile.getAbstractContainer("aCoreSkyWars", "selected",
                    SelectedContainer.class).setSelected(cosmetic.getType(),
                    cosmetic.getId(), cosmetic.getAvailableSlotInsane(profile));
              }
              new MenuPerkUpgradeInsane<>(profile, name, this.cosmetic, cosmeticClass);
            } else {
              PerkInsaneLevel perkLevel = this.perkLevels.get(item);
              if (perkLevel != null) {
                long level = this.intLevels.get(item);
                if (profile.getCoins("aCoreSkyWars") < perkLevel.getCoins() && (CashManager.CASH && profile.getStats("aCoreProfile", "cash") < perkLevel.getCash())) {
                  EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                  return;
                }
                
                if (!CashManager.CASH || cosmetic.getCash() == 0) {
                  new MenuBuyPerkLevel<>(profile, this.name, this.cosmetic, perkLevel, level, this.cosmeticClass);
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
    this.name = null;
    this.cosmetic = null;
    this.cosmeticClass = null;
    this.intLevels.clear();
    this.intLevels = null;
    this.perkLevels.clear();
    this.perkLevels = null;
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
