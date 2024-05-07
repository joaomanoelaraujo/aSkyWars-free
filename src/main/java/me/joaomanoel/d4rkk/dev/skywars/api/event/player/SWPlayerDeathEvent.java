package me.joaomanoel.d4rkk.dev.skywars.api.event.player;


import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.game.GameTeam;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.event.Cancellable;

public class SWPlayerDeathEvent extends SWEvent implements Cancellable {
  
  private boolean isCancelled;
  private final Game<? extends GameTeam> game;
  private final Profile profile;
  private final Profile killer;
  
  public SWPlayerDeathEvent(AbstractSkyWars game, Profile profile, Profile killer) {
    this.game = game;
    this.profile = profile;
    this.killer = killer;
  }
  
  public Game<? extends GameTeam> getGame() {
    return this.game;
  }
  
  public Profile getProfile() {
    return this.profile;
  }
  
  public Profile getKiller() {
    return this.killer;
  }
  
  public boolean hasKiller() {
    return this.killer != null;
  }
  
  @Override
  public boolean isCancelled() {
    return this.isCancelled;
  }
  
  @Override
  public void setCancelled(boolean isCancelled) {
    //this.isCancelled = isCancelled;
  }
}
