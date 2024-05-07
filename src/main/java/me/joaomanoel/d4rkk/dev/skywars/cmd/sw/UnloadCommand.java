package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class UnloadCommand extends SubCommand {

  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("UNLOAD_WORLD");

  public UnloadCommand() {
    super("unload", "unload [world]", "Unload a world.", false);
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length == 0) {
      sender.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    World world = Bukkit.getWorld(args[0]);
    if (world != null) {
      try {
        Bukkit.unloadWorld(world, true);
        sender.sendMessage("§aWorld unloaded successfully.");
      } catch (Exception ex) {
        LOGGER.log(Level.WARNING, "Cannot unload world \"" + world.getName() + "\"", ex);
        sender.sendMessage("§cError unloading world.");
      }
    } else {
      sender.sendMessage("§cWorld not found.");
    }
  }
}
