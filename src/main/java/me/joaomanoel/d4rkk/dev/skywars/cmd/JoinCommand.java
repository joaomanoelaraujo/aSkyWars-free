package me.joaomanoel.d4rkk.dev.skywars.cmd;


import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends Commands {
  
  public JoinCommand() {
    super("entrar");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem executar este comando.");
      return;
    }
    
    Player player = (Player) sender;
    Profile profile = Profile.getProfile(player.getName());
    if (!player.hasPermission("kskywars.cmd.join")) {
      player.sendMessage("§cVocê não possui permissão para utilizar esse comando.");
      return;
    }
    
    if (args.length == 0) {
      player.sendMessage("§cUtilize /entrar [nome]");
      return;
    }
    
    AbstractSkyWars game = AbstractSkyWars.getByWorldName(args[0]);
    if (game == null) {
      player.sendMessage("§cSala não encontrada.");
      return;
    } else if (game.getState() == GameState.EMJOGO) {
      player.sendMessage("§cA partida já está em andamento.");
      return;
    }
    
    player.sendMessage(Language.lobby$npc$play$connect);
    game.join(profile);
  }
}
