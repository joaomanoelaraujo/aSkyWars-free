package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.entity.Player;

public class StartCommand extends SubCommand {

  public StartCommand() {
    super("start", "start", "Start the game.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game != null) {
        if (game.getState() == GameState.AGUARDANDO) {
          game.start();
          player.sendMessage("§aYou started the game!");
        } else {
          player.sendMessage("§cThe game is already in progress.");
        }
      }
    }
  }
}
