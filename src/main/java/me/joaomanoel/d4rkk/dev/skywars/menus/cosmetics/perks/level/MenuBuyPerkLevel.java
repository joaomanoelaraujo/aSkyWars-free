package me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perks.level;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.perk.PerkLevel;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perks.MenuPerkUpgrade;
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

public class MenuBuyPerkLevel<T extends Perk> extends PlayerMenu {
  
  private String name;
  private T cosmetic;
  private PerkLevel perkLevel;
  private long level;
  private Class<T> cosmeticClass;
  public MenuBuyPerkLevel(Profile profile, String name, T cosmetic, PerkLevel perkLevel, long level, Class<T> cosmeticClass) {
    super(profile.getPlayer(), "Confirm", 3);
    this.name = name;
    this.cosmetic = cosmetic;
    this.perkLevel = perkLevel;
    this.level = level;
    this.cosmeticClass = cosmeticClass;
    
    String levelName = " " + (level > 3 ? level == 4 ? "IV" : "V" : StringUtils.repeat("I", (int) level));
    this.setItem(11, BukkitUtils.deserializeItemStack(
        "STAINED_GLASS_PANE:13 : 1 : name>&aConfirm : desc>&7Comprar \"" + cosmetic.getName() + levelName + "\"\n&7por &6" + StringUtils.formatNumber(perkLevel.getCoins()) + " Coins&7."));
    
    this.setItem(15, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:14 : 1 : name>&cCancelar : desc>&7Voltar para a Habilidade " + cosmetic.getName() + "."));
    
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
              if (profile.getCoins("aCoreSkyWars") < this.perkLevel.getCoins()) {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                new MenuPerkUpgrade<>(profile, this.name, this.cosmetic, this.cosmeticClass);
                return;
              }
              
              String levelName = " " + (level > 3 ? level == 4 ? "IV" : "V" : StringUtils.repeat("I", (int) level));
              EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              profile.removeCoins("aCoreSkyWars", this.perkLevel.getCoins());
              profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).setLevel(this.cosmetic, level);
              this.player.sendMessage("§aYou purchased '" + this.cosmetic.getName() + levelName + "'");
              new MenuPerkUpgrade<>(profile, this.name, this.cosmetic, this.cosmeticClass);
            } else if (evt.getSlot() == 15) {
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
              new MenuPerkUpgrade<>(profile, this.name, this.cosmetic, this.cosmeticClass);
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
    this.perkLevel = null;
    this.level = 0;
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
