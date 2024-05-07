package me.joaomanoel.d4rkk.dev.skywars.game.events;

import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.SkyWarsEvent;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;

public class AnnounceEvent extends SkyWarsEvent {
  
  @Override
  public void execute(AbstractSkyWars game) {
    int minutes = game.getTimeUntilEvent() / 60;
    
    game.listPlayers(false).forEach(player -> {
      EnumSound.CLICK.play(player, 0.5F, 2.0F);
      NMS.sendTitle(player, Language.ingame$titles$end$header,
          Language.ingame$titles$end$footer.replace("{time}", StringUtils.formatNumber(minutes)).replace("{s}", minutes > 1 ? "s" : ""), 20, 60, 20);
    });
  }
  
  @Override
  public String getName() {
    return "";
  }
}