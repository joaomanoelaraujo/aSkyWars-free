package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.lobby.Leaderboard;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends SubCommand {

  public LeaderboardCommand() {
    super("leaderboard", "leaderboard", "Add/remove Leaderboards.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eHelp\n \n§6/sw leaderboard add [id] [type] §f- §7Add Leaderboard.\n§6/sw leaderboard remove [id] §f- §7Remove Leaderboard.\n ");
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("add")) {
      if (args.length <= 2) {
        player.sendMessage("§cUsage: /sw leaderboard add [id] [wins/kills]");
        return;
      }

      String id = args[1];
      if (Leaderboard.getById(id) != null) {
        player.sendMessage("§cThere is already a Leaderboard using \"" + id + "\" as ID.");
        return;
      }

      String type = args[2];
      if (!type.equalsIgnoreCase("wins") && !type.equalsIgnoreCase("kills")) {
        player.sendMessage("§cUsage: /sw leaderboard add [id] [winskills]");
        return;
      }

      Location location = player.getLocation().clone();
      location.setX(location.getBlock().getLocation().getX() + 0.5);
      location.setZ(location.getBlock().getLocation().getZ() + 0.5);
      Leaderboard.add(location, id, type);
      player.sendMessage("§aLeaderboard added successfully.");
    } else if (action.equalsIgnoreCase("remove")) {
      if (args.length <= 1) {
        player.sendMessage("§cUsage: /sw leaderboard remove [id]");
        return;
      }

      String id = args[1];
      Leaderboard board = Leaderboard.getById(id);
      if (board == null) {
        player.sendMessage("§cThere is no Leaderboard using \"" + id + "\" as ID.");
        return;
      }

      Leaderboard.remove(board);
      player.sendMessage("§cLeaderboard removed successfully.");
    } else {
      player.sendMessage(" \n§eHelp\n \n§6/sw leaderboard add [id] [type] §f- §7Add Leaderboard.\n§6/sw leaderboard remove [id] §f- §7Remove Leaderboard.\n ");
    }
  }
}
