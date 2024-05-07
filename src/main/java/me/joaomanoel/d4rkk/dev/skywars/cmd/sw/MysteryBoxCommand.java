package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.hook.mysteryboxes.MysteryBoxesHook;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MysteryBoxCommand extends SubCommand {

  public MysteryBoxCommand() {
    super("mb", "mb sync", "Sync cosmetics with kMysterBoxes.", false);
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length == 0) {
      sender.sendMessage("§cUsage: /mb " + this.getUsage());
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("sync")) {
      sender.sendMessage("§aSyncing cosmetics...");
      MysteryBoxesHook.sync(sender);
    } else if ((sender instanceof Player) && action.equalsIgnoreCase("give")) {
      if (args.length <= 2) {
        return;
      }

      Profile profile = Profile.getProfile(args[1]);
      if (profile == null) {
        return;
      }

      Cosmetic cosmetic = Cosmetic.findById(args[2]);
      if (cosmetic.has(profile)) {
        double coins = cosmetic.getCoins();
        if (coins <= 0) {
          coins = 100;
        }
        coins /= 10;
        if (coins < 100) {
          coins += 100;
        }
        profile.addStats("kMysteryBox", 50, "mystery_frags");
        profile.addCoins("aCoreSkyWars", coins);
        profile.getPlayer().sendMessage("§aYou received §6" + coins + " Coins §afor already having " + cosmetic.getRarity().getTagged() + " " + cosmetic.getName() + "§a!");
      } else {
        cosmetic.give(profile);
        profile.getPlayer().sendMessage("§aYou received " + cosmetic.getRarity().getTagged() + " " + cosmetic.getName() + " §athrough a Magic Capsule!");
      }
    } else {
      sender.sendMessage("§cUsage: /sw " + this.getUsage());
    }
  }
}
