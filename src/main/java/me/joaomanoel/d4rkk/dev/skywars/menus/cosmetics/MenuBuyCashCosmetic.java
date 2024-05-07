package me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics;

import me.joaomanoel.d4rkk.dev.cash.CashException;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
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

public class MenuBuyCashCosmetic<T extends Cosmetic> extends PlayerMenu {
  
  private String name;
  private T cosmetic;
  private Class<? extends Cosmetic> cosmeticClass;
  public MenuBuyCashCosmetic(Profile profile, String name, T cosmetic, Class<? extends Cosmetic> cosmeticClass) {
    super(profile.getPlayer(), "Confirm", 3);
    this.name = name;
    this.cosmetic = cosmetic;
    this.cosmeticClass = cosmeticClass;

    this.setItem(11, BukkitUtils.deserializeItemStack(
            "GOLD_INGOT : 1 : name>&aConfirm : desc>&7Unlock \"" + cosmetic.getName() + "\"\n&7Cost: &6" + StringUtils.formatNumber(cosmetic.getCoins()) + "&7."));

    this.setItem(13, BukkitUtils.deserializeItemStack(
            "159:13 : 1 : name>&aConfirm : desc>&7Unlock \"" + cosmetic.getName() + "\"\n&7Cost: &2" + StringUtils.formatNumber(cosmetic.getCash()) + "&7."));

    this.setItem(15, BukkitUtils.deserializeItemStack("159:14 : 1 : name>&cCancel"));

    
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
            if (evt.getSlot() == 11) {
              if (profile.getCoins("aCoreSkyWars") < this.cosmetic.getCoins()) {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                this.player.sendMessage("§cYou don't have enough Coins to complete this transaction.");
                return;
              }

              EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              profile.removeCoins("aCoreSkyWars", this.cosmetic.getCoins());
              this.cosmetic.give(profile);
              this.player.sendMessage("§aYou bought '" + this.cosmetic.getName() + "'");
              new MenuCosmetics<>(profile, this.name, this.cosmeticClass);
            } else if (evt.getSlot() == 13) {
              if (profile.getStats("aCoreProfile", "cash") < this.cosmetic.getCash()) {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                this.player.sendMessage("§cYou don't have enough Cash to complete this transaction.");
                return;
              }

              try {
                CashManager.removeCash(profile, this.cosmetic.getCash());
                this.cosmetic.give(profile);
                this.player.sendMessage("§aYou bought '" + this.cosmetic.getName() + "'");
                EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              } catch (CashException ignore) {
              }
              new MenuCosmetics<>(profile, this.name, this.cosmeticClass);
            } else if (evt.getSlot() == 15) {
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
              new MenuCosmetics<>(profile, this.name, this.cosmeticClass);
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