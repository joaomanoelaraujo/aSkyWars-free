package me.joaomanoel.d4rkk.dev.skywars.cmd;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand extends Commands {

  public SpectateCommand() {
    super("spectate");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        if (!player.hasPermission("kskywars.cmd.spectate")) {
          player.sendMessage("§cYou do not have permission to use this command.");
          return;
        }

        if (args.length == 0) {
          player.sendMessage("§cUsage: /spectate [player]");
          return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || (profile = Profile.getProfile(target.getName())) == null) {
          player.sendMessage("§cPlayer not found.");
          return;
        }

        if (!profile.playingGame()) {
          player.sendMessage("§cThe player is not in a game.");
          return;
        }

        player.sendMessage(Language.lobby$npc$play$connect);
        profile.getGame(AbstractSkyWars.class).spectate(player, target);
      }
    }
  }
}
