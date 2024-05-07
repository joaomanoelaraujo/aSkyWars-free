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

public class MenuShop extends PlayerMenu {
  
  public MenuShop(Profile profile) {
    super(profile.getPlayer(), "SkyWars Shop", 4);
    SkyWarsLevel next = SkyWarsLevel.listLevels().stream()
            .filter(a -> a.getLevel() == SkyWarsLevel.getPlayerLevel(profile).getLevel() + 1)
            .findFirst().orElse(null);

    this.setItem(10, BukkitUtils.deserializeItemStack("381 : 1 : name>§aKits and Perks : desc>§7Change the way you play by picking\n§7kits and perks!\n\n§7Win kits and perks in the Soul Well or\n§7buy them directly using §6coins§7. \n\n§eClick to browse!"));
    this.setItem(12, BukkitUtils.deserializeItemStack("120 : 1 : name>§bSoul Well : desc>§7Use §bSouls §7to roll the well and win kits & perks!\n\n§7Earn §bSouls §7by killing players in\n§7SkyWars. This category contains §bSoul\n§bHarvesting §7and §bSoul Upgrading§7.\n\n§7Souls: §b\n\n§eClick to browse!"));
    this.setItem(14, BukkitUtils.deserializeItemStack("341 : 1 : name>§aMy Cosmetics : desc>§7Browse and equip all the available\n§7in-game SkyWars cosmetics. \n\n§eClick to browse!"));
    this.setItem(16, BukkitUtils.deserializeItemStack("399 : 1 : name>§dSkyWars Level Progression : desc>§7View information about your SkyWars\n§7Level progression, select your\n§7Prestige Icon, and view level rewards.\n\n§7Progress: §b" + profile.getStats("aCoreSkyWars", "experience") + "§7/§a" + next.getExperience() + "\n§f" + next.getLevel() + " §8[" +  progressBar(profile.getStats("aCoreSkyWars", "experience"), next.getExperience()) + " §8] §6" + next.getNextLevel().getLevel() + "\n\n§eClick to view!"));

    this.setItem(31, BukkitUtils.deserializeItemStack("BARRIER : 1 : name>§cClose"));
    this.setItem(35, BukkitUtils.deserializeItemStack("292 : 1 : name>§cGrim Reaper : desc>§7Speak with the Grim Reaper.\n\n§7This category contains §bAngel's\n§bDescent§7, the §5Angel of Death§7, §6Head\n§5Collection§7, and §bAngel's Brewery§7.\n\n§eClick to browse!"));


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
            if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShopKits(profile);
            } else if (evt.getSlot() == 12) {
              EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 2.0F);
              if (player.isOp()){
                player.sendMessage("§cTo acquire more benefits from the plugin, visit our discord!");

              }
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProgress(profile);
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShopCosmetics(profile);
            } else if (evt.getSlot() == 31) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
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
