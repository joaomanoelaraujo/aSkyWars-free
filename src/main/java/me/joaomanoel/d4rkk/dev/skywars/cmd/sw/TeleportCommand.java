package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportCommand extends SubCommand {

  public TeleportCommand() {
    super("tp", "tp [world]", "Teleport to a world.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    World world = Bukkit.getWorld(args[0]);
    if (world != null) {
      player.teleport(new Location(world, 0.5, world.getHighestBlockYAt(0, 0), 0.5));
      player.sendMessage("§aTeleported successfully.");
    } else {
      player.sendMessage("§cWorld not found.");
    }
  }
}
