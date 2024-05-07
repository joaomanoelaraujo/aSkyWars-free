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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChestItems extends PlayerMenu {

  private SkyWarsChest.ChestType chestType;
  private Map<Integer, Integer> chestItems;

  public MenuChestItems(Player player, SkyWarsChest.ChestType chestType) {
    super(player, "Customizar " + chestType.getName(), 5);
    this.chestType = chestType;
    this.chestItems = new HashMap<>();

    int slot = 0;
    for (SkyWarsChest.ChestType.ChestItem item : chestType.listItems()) {
      this.setItem(slot, item.getItem());
      this.chestItems.put(slot++, item.getPercentage());
    }

    for (int glass = 27; glass < 36; glass++) {
      this.setItem(glass, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:14 : 1 : name>&8↑ Inventário : desc>&8↓ Ações"));
    }

    this.setItem(40,
            BukkitUtils.deserializeItemStack("WATCH : 1 : name>&aSalvar e sair : desc>&7Salva as mudanças e volta para o\n&7menu de edição do baú.\n \n&eClique para voltar!"));

    this.register(Main.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          if (evt.getSlot() >= 27) {
            evt.setCancelled(true);
            if (evt.getSlot() == 40) {
              EnumSound.LEVEL_UP.play(this.player, 0.5F, 2.0F);
              List<SkyWarsChest.ChestType.ChestItem> chestItems = new ArrayList<>();
              for (int slot = 0; slot < 27; slot++) {
                ItemStack item = this.getItem(slot);
                if (item != null && item.getType() != Material.AIR) {
                  chestItems.add(new SkyWarsChest.ChestType.ChestItem(item, this.chestItems.getOrDefault(slot, 100)));
                }
              }
              this.chestType.listItems().clear();
              this.chestType.listItems().addAll(chestItems);
              this.chestType.save();
              new MenuChestEdit(this.player, this.chestType);
            }
          } else if (evt.getSlot() < 27) {
            evt.setCancelled(false);
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.chestType = null;
    this.chestItems.clear();
    this.chestItems = null;
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
