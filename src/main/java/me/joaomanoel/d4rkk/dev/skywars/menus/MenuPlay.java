package me.joaomanoel.d4rkk.dev.skywars.menus;

import me.joaomanoel.d4rkk.dev.libraries.menu.UpdatablePlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
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

public class MenuPlay extends UpdatablePlayerMenu {
  private final SkyWarsMode mode;
    public MenuPlay(Profile profile, SkyWarsMode mode) {
    super(profile.getPlayer(), "Play SkyWars ",  4);

        this.update();
        this.mode = mode;
    this.register(Main.getInstance(), 20);
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
          if (this.mode == SkyWarsMode.INSANE) {
            if (item != null && item.getType() != Material.AIR) {
              if (evt.getSlot() == 11) {
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.NORMAL);
                if (game != null) {
                  this.player.sendMessage(Language.lobby$npc$play$connect);
                  game.join(profile);
                }
              } else if (evt.getSlot() == 14) {
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.INSANE);
                if (game != null) {

                  this.player.sendMessage(Language.lobby$npc$play$connect);
                  game.join(profile);
                }
              } else if (evt.getSlot() == 12) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuMapSelector(profile, SkyWarsMode.NORMAL);
              } else if (evt.getSlot() == 15) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuMapSelector(profile, SkyWarsMode.INSANE);
              } else if (evt.getSlot() == 31) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                player.closeInventory();
              }
            }
          } else if (this.mode == SkyWarsMode.INSANEDOUBLES) {
            if (item != null && item.getType() != Material.AIR) {
              if (evt.getSlot() == 11) {
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.NORMALDOUBLES);
                if (game != null) {
                  this.player.sendMessage(Language.lobby$npc$play$connect);
                  game.join(profile);
                }
              } else if (evt.getSlot() == 14) {
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.INSANEDOUBLES);
                if (game != null) {
                  this.player.sendMessage(Language.lobby$npc$play$connect);
                  game.join(profile);
                }
              } else if (evt.getSlot() == 12) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuMapSelector(profile, SkyWarsMode.NORMALDOUBLES);
              } else if (evt.getSlot() == 15) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuMapSelector(profile, SkyWarsMode.INSANEDOUBLES);
              } else if (evt.getSlot() == 31) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                player.closeInventory();
              }
            }
        } else if (this.mode == SkyWarsMode.NORMALDOUBLES) {
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 11) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.NORMALDOUBLES);
              this.player.sendMessage(Language.lobby$npc$play$connect);
              game.join(profile);
            } else if (evt.getSlot() == 14) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.INSANEDOUBLES);
              this.player.sendMessage(Language.lobby$npc$play$connect);
              game.join(profile);
            } else if (evt.getSlot() == 12) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuMapSelector(profile, SkyWarsMode.NORMALDOUBLES);
            } else if (evt.getSlot() == 15) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuMapSelector(profile, SkyWarsMode.INSANEDOUBLES);
            } else if (evt.getSlot() == 31) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              player.closeInventory();
            }
          }
        } else {
            if (item != null && item.getType() != Material.AIR) {

              if (evt.getSlot() == 11) {
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.NORMAL);
                this.player.sendMessage(Language.lobby$npc$play$connect);
                game.join(profile);
              } else if (evt.getSlot() == 14) {
                EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                AbstractSkyWars game = AbstractSkyWars.findRandom(SkyWarsMode.INSANE);
                this.player.sendMessage(Language.lobby$npc$play$connect);
                game.join(profile);
              } else if (evt.getSlot() == 12) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuMapSelector(profile, SkyWarsMode.NORMAL);
              } else if (evt.getSlot() == 15) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuMapSelector(profile, SkyWarsMode.INSANE);
              } else if (evt.getSlot() == 31) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                player.closeInventory();
              }
            }
          }
        }
      }
    }
  }
  
  @Override
  public void update() {

      if (this.mode == SkyWarsMode.INSANEDOUBLES){
        int playingID = AbstractSkyWars.getPlaying(SkyWarsMode.INSANEDOUBLES) + AbstractSkyWars.getWaiting(SkyWarsMode.INSANEDOUBLES);
        int playingND = AbstractSkyWars.getPlaying(SkyWarsMode.NORMALDOUBLES) + AbstractSkyWars.getWaiting(SkyWarsMode.NORMALDOUBLES);

        this.setItem(11, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : name>&aSkyWars (Doubles Normal) : desc>&7Play Double Normal SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingND) + " currently playing\n&eClick to play!"));
        this.setItem(12, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Doubles Normal) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));

        this.setItem(14, BukkitUtils.deserializeItemStack("381 : 1 : name>&aSkyWars (Doubles Insane) : desc>&7Play Double Insane SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingID) + " currently playing\n&eClick to play!"));
        this.setItem(15, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Doubles Insane) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));
      } else if (this.mode == SkyWarsMode.INSANE){
        int playingI = AbstractSkyWars.getPlaying(SkyWarsMode.INSANE) + AbstractSkyWars.getWaiting(SkyWarsMode.INSANE);
        int playingN = AbstractSkyWars.getPlaying(SkyWarsMode.NORMAL) + AbstractSkyWars.getWaiting(SkyWarsMode.NORMAL);

        this.setItem(11, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : name>&aSkyWars (Solo Normal) : desc>&7Play Solo Normal SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingN) + " currently playing\n&eClick to play!"));
        this.setItem(12, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Solo Normal) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));

        this.setItem(14, BukkitUtils.deserializeItemStack("381 : 1 : name>&aSkyWars (Solo Insane) : desc>&7Play Solo Insane SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingI) + " currently playing\n&eClick to play!"));
        this.setItem(15, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Solo Insane) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));
      } else if (this.mode == SkyWarsMode.NORMAL){
        int playingI = AbstractSkyWars.getPlaying(SkyWarsMode.INSANE) + AbstractSkyWars.getWaiting(SkyWarsMode.INSANE);
        int playingN = AbstractSkyWars.getPlaying(SkyWarsMode.NORMAL) + AbstractSkyWars.getWaiting(SkyWarsMode.NORMAL);

        this.setItem(11, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : name>&aSkyWars (Solo Normal) : desc>&7Play Solo Normal SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingN) + " currently playing\n&eClick to play!"));
        this.setItem(12, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Solo Normal) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));

        this.setItem(14, BukkitUtils.deserializeItemStack("381 : 1 : name>&aSkyWars (Solo Insane) : desc>&7Play Solo Insane SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingI) + " currently playing\n&eClick to play!"));
        this.setItem(15, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Solo Insane) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));
      } else {
        int playingID = AbstractSkyWars.getPlaying(SkyWarsMode.INSANEDOUBLES) + AbstractSkyWars.getWaiting(SkyWarsMode.INSANEDOUBLES);
        int playingND = AbstractSkyWars.getPlaying(SkyWarsMode.NORMALDOUBLES) + AbstractSkyWars.getWaiting(SkyWarsMode.NORMALDOUBLES);

        this.setItem(11, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : name>&aSkyWars (Doubles Normal) : desc>&7Play Double Normal SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingND) + " currently playing\n&eClick to play!"));
        this.setItem(12, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Doubles Normal) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));

        this.setItem(14, BukkitUtils.deserializeItemStack("381 : 1 : name>&aSkyWars (Doubles Insane) : desc>&7Play Double Insane SkyWars!\n\n&7&7" + StringUtils.formatNumber(playingID) + " currently playing\n&eClick to play!"));
        this.setItem(15, BukkitUtils.deserializeItemStack("SIGN : 1 : name>&aMap Selector (Doubles Insane) : desc>&7Pick which map you want to play from\n§7a list of available games. \n\n§eClick to browse!"));
      }

    this.setItem(31, BukkitUtils.deserializeItemStack("BARRIER : 1 : name>§cClose"));
  }
  
  public void cancel() {
    super.cancel();
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
