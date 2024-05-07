package me.joaomanoel.d4rkk.dev.skywars.menus.chest;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsChest;
import me.joaomanoel.d4rkk.dev.skywars.menus.MenuChestEdit;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuChestRefill extends PlayerMenu {

  private SkyWarsChest.ChestType chestType;
  private Map<Integer, SkyWarsChest.ChestType> chestTypes;

  public MenuChestRefill(Player player, SkyWarsChest.ChestType chestType) {
    super(player, "Escolher reabastecimento", Math.min(Math.max(SkyWarsChest.ChestType.listTypes().size() / 9, 1), 6));
    this.chestType = chestType;
    this.chestTypes = new HashMap<>();

    int slot = 0;
    for (SkyWarsChest.ChestType chestType1 : SkyWarsChest.ChestType.listTypes()) {
      this.setItem(slot, BukkitUtils.deserializeItemStack(
              "SKULL_ITEM:3 : 1 : name>&fReabastecimento: &a" + chestType1.getName() + " : desc>&7Clique para alterar o tipo de\n&7baú utilizado como reabastecimento. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM3Y2FlNWM1MWViMTU1OGVhODI4ZjU4ZTBkZmY4ZTZiN2IwYjFhMTgzZDczN2VlY2Y3MTQ2NjE3NjEifX19"));
      this.chestTypes.put(slot++, chestType1);
    }

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
            SkyWarsChest.ChestType chestType = this.chestTypes.get(evt.getSlot());
            if (chestType != null) {
              EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              this.player.sendMessage("§aO reabastecimento do baú §f" + this.chestType.getName() + " §afoi alterado para §f" + chestType.getName());
              this.chestType.setRefill(chestType);
              new MenuChestEdit(this.player, this.chestType);
            }
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.chestType = null;
    this.chestTypes.clear();
    this.chestTypes = null;
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
