package me.joaomanoel.d4rkk.dev.skywars.listeners.player;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.event.NPCLeftClickEvent;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.event.NPCRightClickEvent;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.menus.MenuDeliveries;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.BuildCommand;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.ChestCommand;
import me.joaomanoel.d4rkk.dev.skywars.cmd.sw.CreateCommand;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsChest;
import me.joaomanoel.d4rkk.dev.skywars.menus.MenuPlay;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static me.joaomanoel.d4rkk.dev.skywars.cmd.sw.ChestCommand.CHEST;
import static me.joaomanoel.d4rkk.dev.skywars.cmd.sw.CreateCommand.CREATING;

public class PlayerInteractListener implements Listener {
  
  @EventHandler
  public void onNPCLeftClick(NPCLeftClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        new MenuPlay(profile, SkyWarsMode.fromName(npc.data().get("play-npc")));
      }
    }
  }



  @EventHandler
  public void onNPCRightClick(NPCRightClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        new MenuPlay(profile, SkyWarsMode.fromName(npc.data().get("play-npc")));
      } else if (npc.data().has("delivery-npc")) {
        new MenuDeliveries(profile);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      if (CREATING.containsKey(player) && CREATING.get(player)[0].equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          CreateCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      } else if (CHEST.containsKey(player) && CHEST.get(player)[0].equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          ChestCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      }
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game == null && !BuildCommand.hasBuilder(player)) {
        evt.setCancelled(true);
      } else if (game != null && (game.getState() != GameState.EMJOGO || game.isSpectator(player))) {
        if (!(game.getState() == GameState.AGUARDANDO)) {
          player.updateInventory();
          evt.setCancelled(true);
        } else if (game.getState() == GameState.AGUARDANDO && evt.getClickedBlock() != null && evt.getClickedBlock().getType() == Material.GOLD_PLATE) {
          evt.setCancelled(false);
          InventoryHolder ih = ((InventoryHolder) evt.getClickedBlock().getLocation().clone().subtract(0, 0.5, 0).getBlock().getState());
          // Limpar e adicionar os foguetes novamente.
          ih.getInventory().clear();
          for (int i = 0; i < 10; i++) {
            ih.getInventory().addItem(BukkitUtils.deserializeItemStack("FIREWORK : 64"));
          }
        } else if (game.getState() == GameState.AGUARDANDO && evt.getClickedBlock() != null && evt.getClickedBlock().getType() != Material.GOLD_PLATE) {
          evt.setCancelled(true);
        }
      } else if (game != null && game.getState() == GameState.EMJOGO && !game.isSpectator(player)) {
        if (evt.getClickedBlock() != null) {
          Block block = evt.getClickedBlock();
          if (evt.getAction().name().contains("RIGHT")) {
            if (block.getState() instanceof Chest) {
              SkyWarsChest chest = game.getConfig().getChest(block);
              if (chest != null && game.getNextEvent().getKey() != 0) {
                chest.createHologram();
              }
            }
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent evt) {
    if (evt.getTo().getBlockY() != evt.getFrom().getBlockY() && evt.getTo().getBlockY() < 0) {
      Player player = evt.getPlayer();
      Profile profile = Profile.getProfile(player.getName());
      
      if (profile != null) {
        AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
        if (game == null) {
          player.teleport(Core.getLobby());
        } else {
          if (game.getState() != GameState.EMJOGO || game.isSpectator(player)) {
            player.teleport(game.getCubeId().getCenterLocation());
          } else {
            ((CraftPlayer) player).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, (float) player.getMaxHealth());
          }
        }
      }
    }
  }




  @EventHandler
  public void paintingRotate (final PlayerInteractEntityEvent event) {
    final Entity clicked = event.getRightClicked();
    if (clicked instanceof ItemFrame) {
      event.setCancelled(true);
    }
  }


  @EventHandler(priority = EventPriority.MONITOR)
  public void teste(HangingBreakByEntityEvent evt) {
    if ((evt.getRemover() instanceof Player)) {
      Player p = (Player) evt.getRemover();
      if ((evt.getEntity().getType() == EntityType.ITEM_FRAME)) {
        if (!BuildCommand.hasBuilder(p)) {
          evt.setCancelled(true);
        }
      }
    }
  }
  @EventHandler(priority =  EventPriority.MONITOR)
  public void interactEntityEvent(PlayerInteractAtEntityEvent evt) {
    if (evt.getRightClicked().getType() == EntityType.ARMOR_STAND) {
      evt.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
    if ((e.getDamager() instanceof Player)) {
      Player p = (Player) e.getDamager();
      if ((e.getEntity().getType() == EntityType.ITEM_FRAME)) {
        if (!BuildCommand.hasBuilder(p)) {
          e.setCancelled(true);
        }
      } if ((e.getEntity().getType() == EntityType.ARMOR_STAND)) {
        e.setCancelled(true);
      }
    }
    if ((e.getDamager() instanceof Projectile) && (e.getEntity().getType() == EntityType.ITEM_FRAME)) {
      e.setCancelled(true);
    }
  }
}
