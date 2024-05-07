package me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractPreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview.CagePreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview.KillEffectPreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview.ProjectileEffectPreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.*;
import me.joaomanoel.d4rkk.dev.skywars.menus.lobby.MenuShop;
import me.joaomanoel.d4rkk.dev.skywars.menus.lobby.MenuShopCosmetics;
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

import java.util.*;

public class MenuCosmetics<T extends Cosmetic> extends PagedPlayerMenu {

  private Class<T> cosmeticClass;
  private Map<ItemStack, T> cosmetics = new HashMap<>();
  public MenuCosmetics(Profile profile, String name, Class<T> cosmeticClass) {
    super(profile.getPlayer(), "Sky Wars - " + name, (Cosmetic.listByType(cosmeticClass).size() / 7) + 4);
    this.cosmeticClass = cosmeticClass;
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

    String desc = "§7To SkyWars Cosmetics.";
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : name>&cGo back : desc>" + desc), (this.rows * 9) - 5);

    List<ItemStack> items = new ArrayList<>();
    List<T> cosmetics = Cosmetic.listByType(cosmeticClass);
    for (T cosmetic : cosmetics) {
      ItemStack icon = cosmetic.getIcon(profile);
      items.add(icon);
      this.cosmetics.put(icon, cosmetic);
    }

    this.setItems(items);
    cosmetics.clear();
    items.clear();

    this.register(Core.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);

      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();

          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == this.previousPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openPrevious();
            } else if (evt.getSlot() == this.nextPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openNext();
            } else if (evt.getSlot() == (this.rows * 9) - 5) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShopCosmetics(profile);
            } else {
              T cosmetic = this.cosmetics.get(item);
              if (cosmetic != null) {
                if (evt.isRightClick()) {
                   if (cosmetic.getType() == CosmeticType.KILL_EFFECT) {
                    if (!AbstractPreview.canDoKillEffect()) {
                      if (player.hasPermission("kskywars.cmd.skywars")) {
                        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                        player.sendMessage("§cSet the preview locations using /sw preview killeffect");
                      }
                      return;
                    }

                    new KillEffectPreview(profile, (KillEffect) cosmetic);

                    player.closeInventory();
                    return;
                  } else if (cosmetic.getType() == CosmeticType.PROJECTILE_EFFECT) {
                    if (!AbstractPreview.canDoProjectileEffect()) {
                      if (player.hasPermission("kskywars.cmd.skywars")) {
                        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                        player.sendMessage("§cSet the preview locations using /sw preview projectileeffect");
                      }
                      return;
                    }

                    new ProjectileEffectPreview(profile, (ProjectileEffect) cosmetic);

                    player.closeInventory();
                    return;
                  } else if (cosmetic.getType() == CosmeticType.CAGE) {
                    if (!AbstractPreview.canDoCage()) {
                      if (player.hasPermission("kskywars.cmd.skywars")) {
                        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
                        player.sendMessage("§cSet the preview locations using /sw preview cage");
                      }
                      return;
                    }
                    new CagePreview(profile, (Cage) cosmetic);

                    player.closeInventory();
                    return;
                  }
                }

                if (!cosmetic.has(profile)) {
                  if (!cosmetic.canBuy(this.player) || (profile.getCoins("aCoreSkyWars") < cosmetic.getCoins() && (CashManager.CASH && profile
                          .getStats("aCoreProfile", "cash") < cosmetic.getCash()))) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    return;
                  }

                  EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                  if (!CashManager.CASH || cosmetic.getCash() == 0) {
                    new MenuBuyCosmetic<>(profile, this.name.replace("Sky Wars - ", ""), cosmetic, this.cosmeticClass);
                  } else {
                    new MenuBuyCashCosmetic<>(profile, this.name.replace("Sky Wars - ", ""), cosmetic, this.cosmeticClass);
                  }
                  return;
                }

                if (!cosmetic.canBuy(this.player)) {
                  EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                  this.player.sendMessage("§cYou do not have sufficient permission to continue.");
                  return;
                }

                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                if (cosmetic.isSelected(profile)) {
                  profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).setSelected(cosmetic.getType(), 0);
                } else {
                  profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).setSelected(cosmetic);
                }

                new MenuCosmetics<>(profile, this.name.replace("Sky Wars - ", ""), this.cosmeticClass);
              }
            }
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.cosmeticClass = null;
    this.cosmetics.clear();
    this.cosmetics = null;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}
