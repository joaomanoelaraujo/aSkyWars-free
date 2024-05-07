package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Cage;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.entity.Player;

public class CageCommand extends SubCommand {

  public CageCommand() {
    super("cage", "cage [create/delete] [name]", "Create/Delete cage.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length <= 1) {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
      return;
    }

    String action = args[0];
    String name = StringUtils.join(args, 1, "");
    if (action.equalsIgnoreCase("create")) {
      if (Cosmetic.listByType(Cage.class).stream().anyMatch(c -> c.getName().equals(name))) {
        player.sendMessage("§cA cage with this name already exists.");
        return;
      }

      Cage.createCage(player, name);
      player.sendMessage("§aThe cage has been created based on the blocks around you.");
    } else if (action.equalsIgnoreCase("delete")) {
      Cage cage = Cosmetic.listByType(Cage.class).stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
      if (cage == null) {
        player.sendMessage("§cNo cage found with this name.");
        return;
      }

      Cage.removeCage(cage);
      player.sendMessage(
              " \n§cYou have removed the cage with ID " + cage.getId() + ".\n \n§6§lWARNING: §7It is a fact that if another cage uses this ID and someone has this cage, they will have it automatically.\n ");
    } else {
      player.sendMessage("§cUsage: /sw " + this.getUsage());
    }
  }
}
