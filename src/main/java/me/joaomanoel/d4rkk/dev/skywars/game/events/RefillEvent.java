package me.joaomanoel.d4rkk.dev.skywars.game.events;

import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.SkyWarsEvent;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsChest;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;

public class RefillEvent extends SkyWarsEvent {
  
  @Override
  public void execute(AbstractSkyWars game) {
    game.listChests().forEach(SkyWarsChest::refill);
    game.listPlayers(false).forEach(player -> {
      EnumSound.CHEST_OPEN.play(player, 0.5F, 1.0F);
      NMS.sendTitle(player, Language.ingame$titles$refill$header, Language.ingame$titles$refill$footer, 20, 60, 20);
    });
  }
  
  @Override
  public String getName() {
    return Language.options$events$refill;
  }
}
