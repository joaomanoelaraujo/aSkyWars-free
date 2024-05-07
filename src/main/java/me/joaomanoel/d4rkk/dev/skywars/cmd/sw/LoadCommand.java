package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.logging.Level;

import static me.joaomanoel.d4rkk.dev.skywars.utils.VoidChunkGenerator.VOID_CHUNK_GENERATOR;

public class LoadCommand extends SubCommand {

  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("LOAD_WORLD");

  public LoadCommand() {
    super("load", "load [world]", "Load a world.", false);
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length == 0) {
      sender.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    if (Bukkit.getWorld(args[0]) != null) {
      sender.sendMessage("§cWorld already exists.");
      return;
    }

    File map = new File(args[0]);
    if (!map.exists() || !map.isDirectory()) {
      sender.sendMessage("§cWorld folder not found.");
      return;
    }

    try {
      sender.sendMessage("§aLoading...");
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
        WorldCreator wc = WorldCreator.name(map.getName());
        wc.generateStructures(false);
        wc.generator(VOID_CHUNK_GENERATOR);
        World world = wc.createWorld();
        world.setTime(0L);
        world.setStorm(false);
        world.setThundering(false);
        world.setAutoSave(false);
        world.setAnimalSpawnLimit(0);
        world.setWaterAnimalSpawnLimit(0);
        world.setKeepSpawnInMemory(false);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("mobGriefing", "false");
        sender.sendMessage("§aWorld loaded successfully.");
      });
    } catch (Exception ex) {
      LOGGER.log(Level.WARNING, "Cannot load world \"" + args[0] + "\"", ex);
      sender.sendMessage("§cFailed to load world.");
    }
  }
}
