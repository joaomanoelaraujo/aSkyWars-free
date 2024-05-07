package me.joaomanoel.d4rkk.dev.skywars.game;

import me.joaomanoel.d4rkk.dev.game.GameTeam;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Cage;

public class SkyWarsTeam extends GameTeam {
  
  private final int index;

  
  public SkyWarsTeam(AbstractSkyWars game, String location, int size) {
    super(game, location, size);
    this.index = game.listTeams().size();
  }
  
  @Override
  public void reset() {
    super.reset();
  }
  
  public void startGame() {
    this.breakCage();

  }
  
  
  public void buildCage(Cage cage) {
    if (cage == null || this.getTeamSize() > 1) {
      Cage.applyCage(this.getLocation().clone().add(0, -1, 0), this.getTeamSize() > 1);
      return;
    }
    
    cage.apply(this.getLocation().clone().add(0, -1, 0));
  }
  
  public void breakCage() {
    Cage.destroyCage(this.getLocation().clone().add(0, -1, 0));
  }
}
