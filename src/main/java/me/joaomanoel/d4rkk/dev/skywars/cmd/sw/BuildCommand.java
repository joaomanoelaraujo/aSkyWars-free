package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BuildCommand extends SubCommand {

  private static final List<String> BUILDERS = new ArrayList<>();

  public BuildCommand() {
    super("build", "build", "Enable/Disable builder mode.", true);
  }

  public static void remove(Player player) {
    BUILDERS.remove(player.getName());
  }

  public static boolean hasBuilder(Player player) {
    return BUILDERS.contains(player.getName());
  }

  @Override
  public void perform(Player player, String[] args) {
    if (hasBuilder(player)) {
      BUILDERS.remove(player.getName());
      player.setGameMode(GameMode.ADVENTURE);
      player.sendMessage("§cBuilder mode disabled.");
    } else {
      BUILDERS.add(player.getName());
      player.setGameMode(GameMode.CREATIVE);
      player.sendMessage("§aBuilder mode enabled.");
    }
  }
}
