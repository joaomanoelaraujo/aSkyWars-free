package me.joaomanoel.d4rkk.dev.skywars.cmd.sw;

import me.joaomanoel.d4rkk.dev.booster.Booster.BoosterType;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.cmd.SubCommand;
import org.bukkit.command.CommandSender;

public class GiveCommand extends SubCommand {

  public GiveCommand() {
    super("give", "give [player] [booster/coins]", "Give multipliers and coins.", false);
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      sender.sendMessage(" \n§eHelp - Give\n \n§6/sw give [player] coins [amount]\n§6/sw give [player] booster [private/network] [multiplier] [hours]\n ");
      return;
    }

    Profile target = Profile.getProfile(args[0]);
    if (target == null) {
      sender.sendMessage("§cUser not found.");
      return;
    }

    String action = args[1];
    if (action.equalsIgnoreCase("booster")) {
      if (args.length < 5) {
        sender.sendMessage("§cUsage: /sw give [player] booster [private/network] [multiplier] [hours]");
        return;
      }

      try {
        BoosterType type = BoosterType.valueOf(args[2].toUpperCase());
        try {
          double multiplier = Double.parseDouble(args[3]);
          long hours = Long.parseLong(args[4]);
          if (multiplier < 1.0D || hours < 1) {
            throw new Exception();
          }

          target.getBoostersContainer().addBooster(type, multiplier, hours);
          sender.sendMessage("§aMultiplier added.");
        } catch (Exception ex) {
          sender.sendMessage("§cPlease use valid numbers.");
        }
      } catch (Exception ex) {
        sender.sendMessage("§cUsage: /sw give [player] booster [private/network] [multiplier] [hours]");
      }
    } else if (action.equalsIgnoreCase("coins")) {
      if (args.length < 3) {
        sender.sendMessage("§cUsage: /sw give [player] coins [amount]");
        return;
      }

      try {
        double coins = Double.parseDouble(args[2]);
        if (coins < 1.0D) {
          throw new Exception();
        }

        target.addCoins("aCoreSkyWars", coins);
        sender.sendMessage("§aCoins added.");
      } catch (Exception ex) {
        sender.sendMessage("§cPlease use valid numbers.");
      }
    } else {
      sender.sendMessage(" \n§eHelp - Give\n \n§6/sw give [player] coins [amount]\n§6/sw give [player] booster [private/network] [multiplier] [hours]\n ");
    }
  }
}
