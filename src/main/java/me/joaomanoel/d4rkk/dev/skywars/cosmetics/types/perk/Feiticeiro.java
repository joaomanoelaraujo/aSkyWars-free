package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.api.event.player.SWPlayerDeathEvent;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Feiticeiro extends Perk {
  
  private final int index;
  
  public Feiticeiro(int index, String key) {
    super(4, key, CONFIG.getString(key + ".permission"), CONFIG.getString(key + ".name"), CONFIG.getString(key + ".icon"), new ArrayList<>());
    this.index = index;
    this.setupLevels(key);
    this.register();
  }
  
  @Override
  public long getIndex() {
    return this.index;
  }
  
  public void handleEvent(SWEvent evt2) {
    if (evt2 instanceof SWPlayerDeathEvent) {
      SWPlayerDeathEvent evt = (SWPlayerDeathEvent) evt2;
      if (evt.hasKiller()) {
        AbstractSkyWars game = (AbstractSkyWars) evt.getGame();
        Profile profile = evt.getKiller();
        
        Player player = profile.getPlayer();
        if (!game.isSpectator(player) && game.getMode().getCosmeticIndex() == this.getIndex() && this.isSelectedPerk(profile) && this.has(profile) && this.canBuy(player)) {
          player.setLevel(player.getLevel() + this.getCurrentLevel(profile).getValue("level", int.class, 0));
        }
      }
    }
  }
  
  @Override
  public List<Class<?>> getEventTypes() {
    return Collections.singletonList(SWPlayerDeathEvent.class);
  }
}
