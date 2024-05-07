package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.lobby.DeliveryNPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCDeliveryCommand extends SubCommand {

  public NPCDeliveryCommand() {
    super("npcentregas", "npcentregas", "Add/remove Delivery NPCs.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eHelp\n \n§6/sw npcentregas add [id] §f- §7Add NPC.\n§6/sw npcentregas remove [id] §f- §7Remove NPC.\n ");
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("add")) {
      if (args.length <= 1) {
        player.sendMessage("§cUsage: /sw npcentregas add [id]");
        return;
      }

      String id = args[1];
      if (DeliveryNPC.getById(id) != null) {
        player.sendMessage("§cA Delivery NPC with \"" + id + "\" ID already exists.");
        return;
      }

      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      DeliveryNPC.add(id, location);
      player.sendMessage("§aDelivery NPC added successfully.");
    } else if (action.equalsIgnoreCase("remove")) {
      if (args.length <= 1) {
        player.sendMessage("§cUsage: /sw npcentregas remove [id]");
        return;
      }

      String id = args[1];
      DeliveryNPC npc = DeliveryNPC.getById(id);
      if (npc == null) {
        player.sendMessage("§cNo Delivery NPC found with \"" + id + "\" ID.");
        return;
      }

      DeliveryNPC.remove(npc);
      player.sendMessage("§cDelivery NPC removed successfully.");
    } else {
      player.sendMessage(" \n§eHelp\n \n§6/sw npcentregas add [id] §f- §7Add NPC.\n§6/sw npcentregas remove [id] §f- §7Remove NPC.\n ");
    }
  }
}
