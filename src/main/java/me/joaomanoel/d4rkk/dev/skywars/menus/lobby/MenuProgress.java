package me.joaomanoel.d4rkk.dev.skywars.menus.lobby;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.level.SkyWarsLevel;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static me.joaomanoel.d4rkk.dev.skywars.level.SkyWarsLevel.progressBar;

public class MenuProgress extends PlayerMenu {


    private ItemStack createProgressItemENG(double experience2, double requiredExperience2, int slot) {
        double percentage = (experience2 / requiredExperience2) * 100;

        Profile profile = Profile.getProfile(player.getName());
        SkyWarsLevel next = SkyWarsLevel.listLevels().stream()
                .filter(a -> a.getLevel() == SkyWarsLevel.getPlayerLevel(profile).getLevel() + 1)
                .findFirst().orElse(null);

        String formattedPercentage = String.format("%.2f%%", percentage);
        String itemData;

        if (experience2 >= requiredExperience2 * (slot - 10) / 5) {
            itemData = "95:5 : 1 : name>&fLevel " + SkyWarsLevel.getPlayerLevel(profile).getLevel() + " : desc>\n§7Progress: §b" + profile.getStats("aCoreSkyWars", "experience") + "§7/§a" + next.getExperience() + "\n §8[" +  progressBar(profile.getStats("aCoreSkyWars", "experience"), next.getExperience()) + "§8]\n \n&7Percentage: &e" + formattedPercentage;
        } else {
            itemData = "95:14 : 1 : name>&6Level " + SkyWarsLevel.getPlayerLevel(profile).getLevel() + " : desc>\n§7Progress: §b" + profile.getStats("aCoreSkyWars", "experience") + "§7/§a" + next.getExperience() + "\n §8[" +  progressBar(profile.getStats("aCoreSkyWars", "experience"), next.getExperience()) + " §8]\n \n&7Percentage: &e" + formattedPercentage;
        }

        return BukkitUtils.deserializeItemStack(itemData);
    }

    public MenuProgress(Profile profile) {
        super(profile.getPlayer(),"SkyWars Level Progression", 5);

            SkyWarsLevel next2 = SkyWarsLevel.listLevels().stream()
                    .filter(a -> a.getLevel() == SkyWarsLevel.getPlayerLevel(profile).getLevel() + 1L)
                    .findFirst().orElse(null);

            double experience2 = profile.getStats("aCoreSkyWars", "experience");

            this.setItem(10, BukkitUtils.deserializeItemStack("265 : 1 : name>&fLevel " + profile.getStats("aCoreSkyWars", "level")));

            for (int slot = 11; slot <= 15; slot++) {
                this.setItem(slot, createProgressItemENG(experience2, next2.getExperience(), slot));
            }

            this.setItem(16, BukkitUtils.deserializeItemStack("266 : 1 : name>&6Level " + next2.getLevel() + " : desc>\n§7Progress: §b" + profile.getStats("aCoreSkyWars", "experience") + "§7/§a" + next2.getExperience() + "\n §8[" +  progressBar(profile.getStats("aCoreSkyWars", "experience"), next2.getExperience()) + " §8]\n\n&7Rewards:\n&e" + next2.getRewardsNames().replace("[", "").replace("]", "")));

            this.setItem(39, BukkitUtils.deserializeItemStack("ARROW : 1 : name>&aGo Back : desc>§7To SkyWars Shop"));
            this.setItem(40, BukkitUtils.deserializeItemStack("BARRIER : 1 : name>&cClose"));


        register(Main.getInstance());
        open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(getInventory())) {
            evt.setCancelled(true);

            if (evt.getWhoClicked().equals(this.player)) {
                Profile profile = Profile.getProfile(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(getInventory())) {
                    ItemStack item = evt.getCurrentItem();

                    if (item != null && item.getType() != Material.AIR) {
                        if (evt.getSlot() == 39) {
                            EnumSound.CLICK.play(this.player, 0.5F, 1.0F);
                                new MenuShop(profile);

                        } else if (evt.getSlot() == 40) {
                            EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
                            player.closeInventory();
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
            cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(getInventory())) {
            cancel();
        }
    }
}
