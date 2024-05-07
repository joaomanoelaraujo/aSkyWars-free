package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.CubeID;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCommand extends SubCommand {

  public static final Map<Player, Object[]> CREATING = new HashMap<>();

  public CreateCommand() {
    super("create", "create [insane/insanedoubles/normal/normaldoubles] [name]", "Create a room.", true);
  }

  public static void handleClick(Profile profile, String display, PlayerInteractEvent evt) {
    Player player = profile.getPlayer();

    switch (display) {
      case "§aCubeID of Arena": {
        evt.setCancelled(true);
        if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
          CREATING.get(player)[3] = evt.getClickedBlock().getLocation();
          player.sendMessage("§aArena Border 1 set.");
        } else if (evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
          CREATING.get(player)[4] = evt.getClickedBlock().getLocation();
          player.sendMessage("§aArena Border 2 set.");
        } else {
          player.sendMessage("§cClick on a block.");
        }
        break;
      }
      case "§aConfirm": {
        evt.setCancelled(true);
        if (CREATING.get(player)[3] == null) {
          player.sendMessage("§cArena Border 1 not set.");
          return;
        }

        if (CREATING.get(player)[4] == null) {
          player.sendMessage("§cArena Border 2 not set.");
          return;
        }

        Object[] array = CREATING.get(player);
        World world = player.getWorld();
        KConfig config = Main.getInstance().getConfig("arenas", world.getName());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.updateInventory();
        CREATING.remove(player);
        player.sendMessage("§aCreating room...");

        CubeID cube = new CubeID((Location) array[3], (Location) array[4]);
        List<String> spawns = new ArrayList<>(), chests = new ArrayList<>();
        for (Block block : cube) {
          if (block.getType() == Material.BEACON) {
            block.setType(Material.AIR);
            spawns.add(BukkitUtils.serializeLocation(block.getLocation().clone().add(0.5, 0, 0.5)));
          } else if (block.getType() == Material.CHEST) {
            chests.add(BukkitUtils.serializeLocation(block.getLocation().clone().add(0.5, 0, 0.5)) + "; solo");
          }
        }

        config.set("name", array[1]);
        config.set("mode", array[2]);
        config.set("minPlayers", Math.max(spawns.size(), 4) / 2);
        config.set("cubeId", cube.toString());
        config.set("spawns", spawns);
        config.set("chests", chests);
        config.set("balloons", new ArrayList<>());
        world.save();

        player.sendMessage("§aBacking up the map...");
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
          Main.getInstance().getFileUtils().copyFiles(new File(world.getName()), new File("plugins/aSkyWars/worlds/" + world.getName()), "playerdata", "stats", "uid.dat");

          profile.setHotbar(Hotbar.getHotbarById("lobby"));
          profile.refresh();
          AbstractSkyWars.load(config.getFile(), () -> player.sendMessage("§aRoom created successfully."));
        }, 60);
        break;
      }
    }
  }

  @Override
  public void perform(Player player, String[] args) {
    if (AbstractSkyWars.getByWorldName(player.getWorld().getName()) != null) {
      player.sendMessage("§cThere is already a room in this world.");
      return;
    }

    if (args.length <= 1) {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    SkyWarsMode mode = SkyWarsMode.fromName(args[0]);
    if (mode == null) {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    String name = StringUtils.join(args, 1, " ");
    Object[] array = new Object[5];
    array[0] = player.getWorld();
    array[1] = name;
    array[2] = mode.name();
    CREATING.put(player, array);

    player.getInventory().clear();
    player.getInventory().setArmorContents(null);

    player.getInventory().setItem(0, BukkitUtils.deserializeItemStack("BLAZE_ROD : 1 : name>&aCubeID of Arena"));
    player.getInventory().setItem(1, BukkitUtils.deserializeItemStack("STAINED_CLAY:13 : 1 : name>&aConfirm"));

    player.updateInventory();

    Profile.getProfile(player.getName()).setHotbar(null);
  }
}
