package me.joaomanoel.d4rkk.dev.skywars.menus.lobby;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.*;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.kits.InsaneKit;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.kits.NormalKit;

import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perks.MenuPerks;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perksinsane.MenuPerksInsane;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuShopKits extends PlayerMenu {

  public MenuShopKits(Profile profile) {
    super(profile.getPlayer(), "Kits & Perks".replace("&", "§8&"), 6);

    this.setItem(11, BukkitUtils.deserializeItemStack("272 : 1 : name>§aNormal Kits : desc>§7Selection of unique kits for Normal\n§7games.\n\n§eClick to browse!"));
    this.setItem(13, BukkitUtils.deserializeItemStack("IRON_SWORD : 1 : name>§aInsane Kits : desc>§7Selection of unique kits for Insane\n§7games.\n\n§eClick to browse!"));
    this.setItem(15, BukkitUtils.deserializeItemStack("DIAMOND_SWORD : 1 : name>§aMega Kits : desc>§7Selection of unique kits for Mega\n§7games.\n\n§cComing..."));

    this.setItem(20, BukkitUtils.deserializeItemStack("379 : 1 : name>§aNormal Perks : desc>§7Selection of unique perks for Normal\n§7games. \n\n§eClick to browse!"));
    this.setItem(22, BukkitUtils.deserializeItemStack("380 : 1 : name>§aInsane Perks : desc>§7Selection of unique perks for Insane\n§7games. \n\n§eClick to browse!"));
    this.setItem(24, BukkitUtils.deserializeItemStack("116 : 1 : name>§aMega Perks : desc>§7Selection of unique perks for Mega\n§7games. \n\n§cComing..."));

    this.setItem(38, BukkitUtils.deserializeItemStack("397:1 : 1 : name>§aMythical Kits : desc>§7Browse and unlock different Mythical\n§7Kits.\n\n§7Mythical kits require heads to unlock\n§7and have unique abilities. Your skin\n§7will change in-game to match the\n§7mythical kit you have selected!\n\n§7Mythical kits can be used in SOLO and\n§7Doubles.\n\n§cComing..."));
    this.setItem(42, BukkitUtils.deserializeItemStack("120 : 1 : name>§bSoul Well : desc>§7Use §bSouls §7to roll the well and win kits & perks!\n\n§7Earn §bSouls §7by killing players in\n§7SkyWars. This category contains §bSoul\n§bHarvesting §7and §bSoul Upgrading§7.\n\n§7Souls: §b\n\n§eClick to browse!"));

    this.setItem(48, BukkitUtils.deserializeItemStack("ARROW : 1 : name>§aGo Back : desc>§7To SkyWars Shop"));
    this.setItem(49, BukkitUtils.deserializeItemStack("EMERALD : 1 : name>§7Total Coins: §6" + profile.getCoins("aCoreSkyWars") + " : desc>§6" + Language.skywars$shop));

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
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kits.MenuKits<>(profile, "Normal", NormalKit.class);
            } else if (evt.getSlot() == 13) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kitsinsane.MenuKits<>(profile, "Insane", InsaneKit.class);
            } else if (evt.getSlot() == 42){
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
              if (player.isOp()){
                player.sendMessage("§cHello! pay for ");
              }
            } else if (evt.getSlot() == 15){
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 20){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuPerks<>(profile, "Normal", Perk.class);
            } else if (evt.getSlot() == 24){
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 22){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuPerksInsane<>(profile, "Insane", PerkInsane.class);
            } else if (evt.getSlot() == 48){
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShop(profile);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
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
