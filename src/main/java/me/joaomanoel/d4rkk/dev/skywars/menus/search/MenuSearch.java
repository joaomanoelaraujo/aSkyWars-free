package me.joaomanoel.d4rkk.dev.skywars.menus.search;


import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.libraries.menu.PagedPlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.*;
import me.joaomanoel.d4rkk.dev.skywars.menus.lobby.MenuShop;
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

public class MenuSearch<T extends Cosmetic> extends PagedPlayerMenu {


    private Class<T> cosmeticClass;
    private Map<ItemStack, Cosmetic> cosmetics = new HashMap<>();
    private String name;

    public MenuSearch(Profile profile, String name) {
        super(profile.getPlayer(), "Search: " + (name.equals("") ? "All" : name), 5);
        this.previousPage = (this.rows * 9) - 9;
        this.nextPage = (this.rows * 9) - 1;
        this.name = name;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);


        this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : name>&cGo Back"), (this.rows * 9) - 5);

        List<ItemStack> items = new ArrayList<>();
        List<Cosmetic> cosmetics3 = new ArrayList<>();
        cosmetics3.addAll(Cosmetic.listByType(WinAnimation.class));
        cosmetics3.addAll(Cosmetic.listByType(ProjectileEffect.class));
        cosmetics3.addAll(Cosmetic.listByType(Kit.class));
        cosmetics3.addAll(Cosmetic.listByType(KitInsane.class));
        cosmetics3.addAll(Cosmetic.listByType(Perk.class));
        cosmetics3.addAll(Cosmetic.listByType(KillEffect.class));
        cosmetics3.addAll(Cosmetic.listByType(Cage.class));
        for (Cosmetic cosmetic : cosmetics3) {
            if (cosmetic.getName().contains(name)) {
                ItemStack icon = cosmetic.getIcon(profile);
                items.add(icon);
                this.cosmetics.put(icon, cosmetic);
            }
        }

        if (items.isEmpty()) {
            this.removeSlotsWith(BukkitUtils.deserializeItemStack("BARRIER : 1 : name>&cNothing found..."),22);
        }

        this.setItems(items);
        cosmetics3.clear();
        items.clear();

        this.register(Main.getInstance());
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
                            new MenuShop(profile);
                        } else {
                            Cosmetic cosmetic = this.cosmetics.get(item);
                            if (cosmetic != null) {
                                if (!cosmetic.has(profile)) {
                                    if (!cosmetic.canBuy(this.player) || (profile.getCoins("aCoreSkyWars") < cosmetic.getCoins() && (CashManager.CASH && profile
                                            .getStats("aCoreProfile", "cash") < cosmetic.getCash()))) {
                                        EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                                        return;
                                    }

                                    EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                    if (!CashManager.CASH || cosmetic.getCash() == 0) {
                                        new MenuBuySearch<>(profile, this.name.replace("SkyWars - ", ""), cosmetic);
                                    } else {
                                        new MenuBuyCashSearch<>(profile, this.name.replace("SkyWars - ", ""), cosmetic);
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
