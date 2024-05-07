package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview.CagePreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview.KillEffectPreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview.ProjectileEffectPreview;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PreviewCommand extends SubCommand {

  public PreviewCommand() {
    super("preview", "preview", "Set preview locations.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(
              " \n§eHelp - Preview\n \n§6/sw preview cage [cage/spectator] §f- §7Set cage preview locations.\n§6/sw preview killeffect [opponent/ally/spectator] §f- §7Set kill effect preview locations.\n ");
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("cage")) {
      if (args.length < 2) {
        player.sendMessage("§cUsage: /sw preview cage [cage/spectator]");
        return;
      }

      String type = args[1];
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      if (type.equalsIgnoreCase("cage")) {
        CagePreview.CONFIG.set("cage.1", BukkitUtils.serializeLocation(location));
        CagePreview.createLocations();
        player.sendMessage("§aCage location set!");
      } else if (type.equalsIgnoreCase("spectator")) {
        CagePreview.CONFIG.set("cage.2", BukkitUtils.serializeLocation(location));
        CagePreview.createLocations();
        player.sendMessage("§aSpectator location set!");
      } else {
        player.sendMessage("§cUsage: /sw preview cage [cage/spectator]");
      }
    } else if (action.equalsIgnoreCase("killeffect")) {
      if (args.length < 2) {
        player.sendMessage("§cUsage: /sw preview killeffect [opponent/ally/spectator]");
        return;
      }

      String type = args[1];
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      if (type.equalsIgnoreCase("opponent")) {
        KillEffectPreview.CONFIG.set("killeffect.1", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aOpponent location set!");
      } else if (type.equalsIgnoreCase("ally")) {
        KillEffectPreview.CONFIG.set("killeffect.2", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aAlly location set!");
      } else if (type.equalsIgnoreCase("spectator")) {
        KillEffectPreview.CONFIG.set("killeffect.3", BukkitUtils.serializeLocation(location));
        KillEffectPreview.createLocations();
        player.sendMessage("§aSpectator location set!");
      } else {
        player.sendMessage("§cUsage: /sw preview killeffect [opponent/ally/spectator]");
      }
    } else {
      player.sendMessage(
              " \n§eHelp - Preview\n \n§6/sw preview cage [cage/spectator] §f- §7Set cage preview locations.\n§6/sw preview killeffect [opponent/ally/spectator] §f- §7Set kill effect preview locations.\n ");
    }
  }
}
