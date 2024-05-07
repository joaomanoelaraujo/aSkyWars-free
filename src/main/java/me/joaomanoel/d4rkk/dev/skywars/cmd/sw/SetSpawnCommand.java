package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends SubCommand {

  public SetSpawnCommand() {
    super("setspawn", "setspawn", "Set server spawn.", true);
  }

  @Override
  public void perform(Player player, String[] args) {
    Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
    location.setYaw(player.getLocation().getYaw());
    location.setPitch(player.getLocation().getPitch());
    Main.getInstance().getConfig().set("spawn", BukkitUtils.serializeLocation(location));
    Main.getInstance().saveConfig();
    player.sendMessage("§aSpawn set.");
  }
}
