package me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kitsinsane;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.InsaneKitConfigContainer;
import me.joaomanoel.d4rkk.dev.skywars.container.KitConfigContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.kit.InsaneKitConfig;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.kit.KitConfig;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Kit;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.KitInsane;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuKitConfig<T extends KitInsane> extends PlayerMenu {
  
  private String name;
  private T cosmetic;
  private InsaneKitConfig config;
  private Map<ItemStack, Integer> itemIndex;
  public MenuKitConfig(Profile profile, String name, T cosmetic) {
    super(profile.getPlayer(), "Customizar " + cosmetic.getName(), 6);
    this.name = name;
    this.cosmetic = cosmetic;
    this.config = profile.getAbstractContainer("aCoreSkyWars", "kitconfig", InsaneKitConfigContainer.class).getOrLoadConfig(cosmetic);
    this.itemIndex = new HashMap<>();
    
    int index = 1;
    for (ItemStack item : cosmetic.getCurrentLevel(profile).getItems()) {
      int slot = config.getSlot(index);
      if (!this.config.isAutoEquipArmor() || !KitConfig.isArmor(item)) {
        this.setItem(slot == -1 ? (index - 1) : KitConfig.convertConfigSlot(slot), item);
      }
      this.itemIndex.put(item, index++);
    }
    
    for (int glass = 27; glass < 36; glass++) {
      this.setItem(glass, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:14 : 1 : name>&8↑ Inventário : desc>&8↓ Hotbar"));
    }
    
    this.setItem(48, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : name>&cVoltar : desc>&7Para o Kit " + cosmetic.getName() + "."));
    this.setItem(49, cosmetic.getIcon(profile, false, false));
    if (this.config.isAutoEquipArmor()) {
      this.setItem(50, BukkitUtils.deserializeItemStack(
          "DIAMOND_CHESTPLATE : 1 : enchant>LURE:1 : hide>all : name>&aAuto-Equipar Armadura : desc>&7Clique para colocar a armadura\n&7no seu inventário."));
    } else {
      this.setItem(50, BukkitUtils.deserializeItemStack("DIAMOND_CHESTPLATE : 1 : name>&cAuto-Equipar Armadura : desc>&7Clique para colocar a armadura automaticamente."));
    }
    
    this.register(Main.getInstance());
    this.open();
  }
  
  @EventHandler(priority = EventPriority.LOW)
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
            if (evt.getSlot() == 48) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuKitUpgrade<>(profile, this.name, this.cosmetic);
            } else if (evt.getSlot() == 50) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              this.player.sendMessage((this.config.toggleAutoEquipArmor() ?
                  "§aSua armadura será equipada automaticamente" :
                  "§aSua armadura não será mais equipada automaticamente") + " para o Kit " + this.cosmetic.getName() + "!");
              new MenuKitConfig<>(profile, this.name, this.cosmetic);
            } else if (this.itemIndex.containsKey(item)) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuKitConfigSlot<>(profile, this.name, this.cosmetic, this.itemIndex.get(item));
            }
          }
        }
      }
    }
  }
  
  private void save(Profile profile) {
    profile.getAbstractContainer("aCoreSkyWars", "kitconfig", InsaneKitConfigContainer.class).saveConfig(this.cosmetic, this.config);
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
    this.name = null;
    this.cosmetic = null;
    this.config = null;
    this.itemIndex.clear();
    this.itemIndex = null;
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
