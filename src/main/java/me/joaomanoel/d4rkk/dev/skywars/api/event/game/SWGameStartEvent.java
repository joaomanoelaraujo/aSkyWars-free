package me.joaomanoel.d4rkk.dev.skywars.api.event.game;

import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.game.GameTeam;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;

public class SWGameStartEvent extends SWEvent {
  
  private final Game<? extends GameTeam> game;
  
  public SWGameStartEvent(Game<? extends GameTeam> game) {
    this.game = game;
  }
  
  public Game<? extends GameTeam> getGame() {
    return this.game;
  }
}
