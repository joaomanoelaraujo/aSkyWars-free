package me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kitsinsane.level;


import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.kit.KitInsaneLevel;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Kit;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.KitInsane;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kitsinsane.MenuKitUpgrade;
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

public class MenuBuyKitLevel<T extends KitInsane> extends PlayerMenu {
  
  private String name;
  private T cosmetic;
  private KitInsaneLevel KitInsaneLevel;
  private long level;
  public MenuBuyKitLevel(Profile profile, String name, T cosmetic, KitInsaneLevel KitInsaneLevel, long level) {
    super(profile.getPlayer(), "Confirm", 3);
    this.name = name;
    this.cosmetic = cosmetic;
    this.KitInsaneLevel = KitInsaneLevel;
    this.level = level;

    this.setItem(11, BukkitUtils.deserializeItemStack(
            "159:13 : 1 : name>&aConfirm : desc>&7Unlock \"" + cosmetic.getName() + "\"\n&7Cost: &2" + StringUtils.formatNumber(KitInsaneLevel.getCoins()) + "&7."));

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
              if (profile.getCoins("aCoreSkyWars") < this.KitInsaneLevel.getCoins()) {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                new MenuKitUpgrade<>(profile, this.name, this.cosmetic);
                return;
              }
              
              EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              profile.removeCoins("aCoreSkyWars", this.KitInsaneLevel.getCoins());
              profile.getAbstractContainer("aCoreSkyWars", "cosmetics", CosmeticsContainer.class).setLevel(this.cosmetic, this.level);
              this.player.sendMessage("§aVocê comprou '" + this.KitInsaneLevel.getName() + "'");
              new MenuKitUpgrade<>(profile, this.name, this.cosmetic);
            } else if (evt.getSlot() == 15) {
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
              new MenuKitUpgrade<>(profile, this.name, this.cosmetic);
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
    this.KitInsaneLevel = null;
    this.level = 0;
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
