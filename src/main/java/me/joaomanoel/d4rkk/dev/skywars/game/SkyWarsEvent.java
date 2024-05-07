package me.joaomanoel.d4rkk.dev.skywars.game;

import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.events.AnnounceEvent;
import me.joaomanoel.d4rkk.dev.skywars.game.events.EndEvent;
import me.joaomanoel.d4rkk.dev.skywars.game.events.RefillEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class SkyWarsEvent {
  
  public static final Map<Integer, SkyWarsEvent> SOLO = new LinkedHashMap<>(), DUPLA = new LinkedHashMap<>(), RANKED = new LinkedHashMap<>();
  public static KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("EVENTS");
  private static SkyWarsEvent END_EVENT, REFILL_EVENT, ANNOUNCE_EVENT;

  public static void setupEvents() {
    END_EVENT = new EndEvent();
    REFILL_EVENT = new RefillEvent();
    ANNOUNCE_EVENT = new AnnounceEvent();
    for (String evt : Language.options$events$solo$timings) {
      Object[] event = parseEvent(evt);
      if (event == null) {
        LOGGER.log(Level.WARNING, "O evento solo \"" + evt + "\" nao e valido");
        continue;
      }
      
      SOLO.put((int) event[0], (SkyWarsEvent) event[1]);
    }
    
    for (String evt : Language.options$events$ranked$timings) {
      Object[] event = parseEvent(evt);
      if (event == null) {
        LOGGER.log(Level.WARNING, "O evento normal \"" + evt + "\" nao e valido");
        continue;
      }
      
      RANKED.put((int) event[0], (SkyWarsEvent) event[1]);
    }
    
    for (String evt : Language.options$events$dupla$timings) {
      Object[] event = parseEvent(evt);
      if (event == null) {
        LOGGER.log(Level.WARNING, "O evento dupla \"" + evt + "\" nao e valido");
        continue;
      }
      
      DUPLA.put((int) event[0], (SkyWarsEvent) event[1]);
    }
  }

  private static Object[] parseEvent(String evt) {
    String[] splitter = evt.split(":");
    if (splitter.length <= 1) {
      return null;
    }
    
    int time = 0;
    try {
      if (splitter[1].startsWith("-")) {
        throw new Exception();
      }
      time = Integer.parseInt(splitter[1]);
    } catch (Exception ex) {
      return null;
    }
    
    String eventName = splitter[0];
    if (eventName.equalsIgnoreCase("fim")) {
      return new Object[]{time, END_EVENT};
    } else if (eventName.equalsIgnoreCase("refill")) {
      return new Object[]{time, REFILL_EVENT};
    } else if (eventName.equalsIgnoreCase("anuncio")) {
      return new Object[]{time, ANNOUNCE_EVENT};
    }
    
    return null;
  }
  
  public abstract void execute(AbstractSkyWars game);
  
  public abstract String getName();
}
