package me.joaomanoel.d4rkk.dev.skywars.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {
  
  private final String name;
  private final String usage;
  private final String description;
  private final boolean onlyForPlayer;
  
  public SubCommand(String name, String usage, String description, boolean onlyForPlayer) {
    this.name = name;
    this.usage = usage;
    this.description = description;
    this.onlyForPlayer = onlyForPlayer;
  }
  
  public void perform(CommandSender sender, String[] args) {
  }
  
  public void perform(Player player, String[] args) {
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getUsage() {
    return this.usage;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public boolean onlyForPlayer() {
    return this.onlyForPlayer;
  }
}
