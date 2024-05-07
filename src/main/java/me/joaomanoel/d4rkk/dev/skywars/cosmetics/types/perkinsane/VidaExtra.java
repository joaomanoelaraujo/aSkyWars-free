package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.api.event.game.SWGameStartEvent;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.PerkInsane;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VidaExtra extends PerkInsane {
  
  protected int index;
  
  public VidaExtra(int index, String key) {
    super(100, key, CONFIG.getString(key + ".permission"), CONFIG.getString(key + ".name"), CONFIG.getString(key + ".icon"), new ArrayList<>());
    this.index = index;
    this.setupLevels(key);
    this.register();
  }
  
  @Override
  public long getIndex() {
    return this.index;
  }
  
  @Override
  public void handleEvent(SWEvent evt2) {
    if (evt2 instanceof SWGameStartEvent) {
      SWGameStartEvent evt = (SWGameStartEvent) evt2;
      AbstractSkyWars game = (AbstractSkyWars) evt.getGame();
      game.listPlayers().forEach(player -> {
        Profile profile = Profile.getProfile(player.getName());
        if (has(profile) && isSelectedPerkInsane(profile) && canBuy(player)) {
          player.setMaxHealth(20.0D + Double.parseDouble(String.valueOf(getCurrentLevel(profile)
              .getValue("extra_health", int.class, 1))) * 2);
          player.setHealth(player.getMaxHealth());
        }
      });
    }
  }
  
  @Override
  public List<Class<?>> getEventTypes() {
    return Collections.singletonList(SWGameStartEvent.class);
  }
}
