package me.joaomanoel.d4rkk.dev.skywars.game.events;

import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.SkyWarsEvent;

public class EndEvent extends SkyWarsEvent {
  
  @Override
  public void execute(AbstractSkyWars game) {
    game.stop(null);
  }
  
  @Override
  public String getName() {
    return Language.options$events$end;
  }
}
