package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsChest;
import me.joaomanoel.d4rkk.dev.skywars.menus.MenuChestEdit;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class ChestCommand extends SubCommand {

  public static final Map<Player, Object[]> CHEST = new HashMap<>();

  public ChestCommand() {
    super("chest", "chest [name] [create/edit/change]", "Create/Edit/Change chest type(s).", true);
  }

  public static void handleClick(Profile profile, String display, PlayerInteractEvent evt) {
    Player player = profile.getPlayer();
    AbstractSkyWars game = AbstractSkyWars.getByWorldName(player.getWorld().getName());

    switch (display) {
      case "§aWand": {
        evt.setCancelled(true);
        SkyWarsChest chest = null;
        if (evt.getClickedBlock() != null) {
          chest = game.getConfig().getChest(evt.getClickedBlock());
        }

        if (evt.getAction().name().contains("CLICK_BLOCK")) {
          if (chest == null) {
            player.sendMessage("§cThis block is not considered a chest in this arena.");
            return;
          }

          if (evt.getAction().name().contains("LEFT")) {
            game.getConfig().changeChestType(chest, (SkyWarsChest.ChestType) CHEST.get(player)[1]);
            player.sendMessage(" \n§aYou have changed the chest type to: §f" + chest.getChestType() + "\n ");
          } else {
            player.sendMessage(" \n§aChest type: §f" + chest.getChestType() + "\n ");
          }
        } else if (evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
          if (chest == null) {
            player.sendMessage("");
          }
        } else {
          player.sendMessage("§cClick on a block.");
        }
        break;
      }
      case "§cExit": {
        evt.setCancelled(true);
        if (BuildCommand.hasBuilder(player)) {
          player.performCommand("sw build");
        }
        profile.setHotbar(Hotbar.getHotbarById("lobby"));
        profile.refresh();
        break;
      }
    }
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length <= 1) {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    String action = args[1];
    if (action.equalsIgnoreCase("create")) {
      SkyWarsChest.ChestType chestType = SkyWarsChest.ChestType.createChestType(args[0]);
      if (chestType == null) {
        player.sendMessage("§cA chest type with this name already exists.");
        return;
      }

      player.sendMessage("§aChest type created.");
      player.performCommand("sw chest " + chestType.getName() + " edit");
    } else if (action.equalsIgnoreCase("edit")) {
      SkyWarsChest.ChestType chestType = SkyWarsChest.ChestType.getByName(args[0]);
      if (chestType == null) {
        player.sendMessage("§cChest type not found.");
        return;
      }

      if (!BuildCommand.hasBuilder(player)) {
        player.performCommand("sw build");
      }
      new MenuChestEdit(player, chestType);
    } else if (action.equalsIgnoreCase("change")) {
      SkyWarsChest.ChestType chestType = SkyWarsChest.ChestType.getByName(args[0]);
      if (chestType == null) {
        player.sendMessage("§cChest type not found.");
        return;
      }

      if (AbstractSkyWars.getByWorldName(player.getWorld().getName()) == null) {
        player.sendMessage("§cThere is no room in this world.");
        return;
      }

      Object[] array = new Object[2];
      array[0] = player.getWorld();
      array[1] = chestType;
      CHEST.put(player, array);

      player.getInventory().clear();
      player.getInventory().setArmorContents(null);

      player.getInventory().setItem(0, BukkitUtils.deserializeItemStack(
              "BLAZE_ROD : 1 : name>&aWand : desc>&7Left-click to\n&7change the chest type.\n \n&7Right-click to\n&7view the chest type."));
      player.getInventory().setItem(1, BukkitUtils.deserializeItemStack("STAINED_CLAY:14 : 1 : name>&cExit"));

      player.updateInventory();
      if (!BuildCommand.hasBuilder(player)) {
        player.performCommand("sw build");
      }

      Profile.getProfile(player.getName()).setHotbar(null);
      player.sendMessage("§aUse the wand to change and view chest types.");
    } else {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
    }
  }
}
