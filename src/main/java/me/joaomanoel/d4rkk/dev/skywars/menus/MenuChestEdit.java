package me.joaomanoel.d4rkk.dev.skywars.menus;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsChest;
import me.joaomanoel.d4rkk.dev.skywars.menus.chest.MenuChestItems;
import me.joaomanoel.d4rkk.dev.skywars.menus.chest.MenuChestRefill;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChestEdit extends PlayerMenu {
  
  private SkyWarsChest.ChestType chestType;
  private Map<ItemStack, Integer> itemIndex;
  public MenuChestEdit(Player player, SkyWarsChest.ChestType chestType) {
    super(player, "Baú " + chestType.getName(), 5);
    this.chestType = chestType;
    this.itemIndex = new HashMap<>();
    
    int slot = 0;
    for (SkyWarsChest.ChestType.ChestItem item : chestType.listItems()) {
      ItemStack clone = item.getItem().clone();
      ItemMeta meta = clone.getItemMeta();
      List<String> lore = Arrays
          .asList("§fChance de spawn: §7" + item.getPercentage() + "%", "", "§eAções:", " §8▪ §fBotão esquerdo: §a+1%", " §8▪ §fBotão direito: §c-1%", " §8▪ §fBotão esquerdo shift: §a+10%", " §8▪ §fBotão direito shift: §c-10%");
      meta.setLore(lore);
      clone.setItemMeta(meta);
      this.itemIndex.put(clone, slot);
      this.setItem(slot++, clone);
    }
    
    for (int glass = 27; glass < 36; glass++) {
      this.setItem(glass, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:14 : 1 : name>&8↑ Inventário : desc>&8↓ Ações"));
    }
    
    this.setItem(39, BukkitUtils.deserializeItemStack(
        "REDSTONE_COMPARATOR : 1 : name>&aAdicionar ou remover itens : desc>&7Você pode adicionar ou remover os itens\n&7do baú quando desejar através de uma\n&7outra GUI gerada automaticamente.\n \n&eClique para abrir!"));
    if (this.chestType.isRandomSlots()) {
      this.setItem(40, BukkitUtils.deserializeItemStack(
          "SKULL_ITEM:3 : 1 : name>&cSlots enfileirados : desc>&7Clique para ativar o enfileiramento\n&7dos items no baú. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFmODY4NzYxYTVjOTEzOWRhMWZlYzY3MjZlMTY1NTkyOTQyOWE0M2FiOTcyYzgxMThiYTI2YjkyNTVlN2MifX19"));
    } else {
      this.setItem(40, BukkitUtils.deserializeItemStack(
          "SKULL_ITEM:3 : 1 : name>&aSlots enfileirados : desc>&7Clique para desativar o enfileiramento\n&7dos items no baú. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzMxYjE2MTc3MGYwMjAyMzdiNGFhMWU5MjhjNTQ4MGM4YjM2Y2JmZTNhNmFhYjllMjJlNTU3YzFkOTEwIn19fQ=="));
    }
    this.setItem(41, BukkitUtils.deserializeItemStack(
        "SKULL_ITEM:3 : 1 : name>&aBaú de Reabastecimento : desc>&7Altere qual será o tipo de baú\n&7que será aplicado no evento de\n&7reabastecimento do skywars.\n \n&fReabastecimento atual: &7" + chestType
            .getRefillChest().getName() + "\n \n&eClique para alterar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZjZmZlMTQ0NWU3NTA1NDQ2YmE0YzA3NTc1NDA3MTIyYTgzNTEyOGE5OTJhNzc2NDZjMDMyNDUxYjk0YjgxYiJ9fX0="));
    
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
          if (evt.getSlot() >= 27) {
            if (evt.getSlot() == 39) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuChestItems(this.player, this.chestType);
            } else if (evt.getSlot() == 40) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              this.player.sendMessage(this.chestType.toggleRandomSlots() ? "§aO baú não terá mais enfileiramento." : "§aO baú agora terá seus itens enfileirados.");
              new MenuChestEdit(this.player, this.chestType);
            } else if (evt.getSlot() == 41) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuChestRefill(this.player, this.chestType);
            }
          } else if (evt.getSlot() < 27) {
            ItemStack item = evt.getCurrentItem();
            if (item != null && item.getType() != Material.AIR && this.itemIndex.containsKey(item)) {
              SkyWarsChest.ChestType.ChestItem chestItem = this.chestType.getItem(this.itemIndex.get(item));
              if (chestItem != null) {
                if (evt.getClick() == ClickType.LEFT || evt.getClick() == ClickType.SHIFT_LEFT) {
                  EnumSound.NOTE_PLING.play(this.player, 0.5F, 2.0F);
                  int percentage = evt.getClick() == ClickType.SHIFT_LEFT ? 10 : 1;
                  chestItem.modifyPercentage(percentage);
                  this.chestType.save();
                  new MenuChestEdit(this.player, this.chestType);
                } else if (evt.getClick() == ClickType.RIGHT || evt.getClick() == ClickType.SHIFT_RIGHT) {
                  EnumSound.NOTE_PLING.play(this.player, 0.5F, 2.0F);
                  int percentage = evt.getClick() == ClickType.SHIFT_RIGHT ? -10 : -1;
                  chestItem.modifyPercentage(percentage);
                  this.chestType.save();
                  new MenuChestEdit(this.player, this.chestType);
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
    this.chestType = null;
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
