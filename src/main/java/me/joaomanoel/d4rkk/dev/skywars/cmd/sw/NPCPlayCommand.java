package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
import me.joaomanoel.d4rkk.dev.skywars.lobby.PlayNPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCPlayCommand extends SubCommand {

  public NPCPlayCommand() {
    super("npcplay", "npcplay", "Add/remove Play NPCs.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eHelp - NPC Play\n \n§6/sw npcplay add [id] [insane/insanedoubles] §f- §7Add NPC.\n§6/sw npcplay remove [id] §f- §7Remove NPC.\n ");
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("add")) {
      if (args.length <= 2) {
        player.sendMessage("§cUsage: /sw npcplay add [id] [insane/insanedoubles]");
        return;
      }

      String id = args[1];
      if (PlayNPC.getById(id) != null) {
        player.sendMessage("§cA Play NPC with \"" + id + "\" ID already exists.");
        return;
      }

      SkyWarsMode mode = SkyWarsMode.fromName(args[2]);
      if (mode == null) {
        player.sendMessage("§cUsage: /sw npcplay add [id] [insane/insanedoubles]");
        return;
      }

      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      PlayNPC.add(id, location, mode);
      player.sendMessage("§aNPC Play added successfully.");
    } else if (action.equalsIgnoreCase("remove")) {
      if (args.length <= 1) {
        player.sendMessage("§cUsage: /sw npcplay remove [id]");
        return;
      }

      String id = args[1];
      PlayNPC npc = PlayNPC.getById(id);
      if (npc == null) {
        player.sendMessage("§cNo Play NPC found with \"" + id + "\" ID.");
        return;
      }

      PlayNPC.remove(npc);
      player.sendMessage("§cPlay NPC removed successfully.");
    } else {
      player.sendMessage(" \n§eHelp - NPC Play\n \n§6/sw npcplay add [id] [mode] §f- §7Add NPC.\n§6/sw npcplay remove [id] §f- §7Remove NPC.\n ");
    }
  }
}
