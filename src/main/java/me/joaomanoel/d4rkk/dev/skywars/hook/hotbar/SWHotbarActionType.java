package me.joaomanoel.d4rkk.dev.skywars.hook.hotbar;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.HotbarActionType;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.PerkInsane;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.kits.InsaneKit;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.kits.NormalKit;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
import me.joaomanoel.d4rkk.dev.skywars.menus.MenuLobbies;
import me.joaomanoel.d4rkk.dev.skywars.menus.MenuPlay;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perksinsane.MenuSelectPerkInsane;
import me.joaomanoel.d4rkk.dev.skywars.menus.lobby.MenuShop;
import me.joaomanoel.d4rkk.dev.skywars.menus.MenuSpectator;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kits.MenuSelectKit;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perks.MenuSelectPerk;

public class SWHotbarActionType extends HotbarActionType {
  
  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("loja")) {
      new MenuShop(profile);
    } else if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    } else if (action.equalsIgnoreCase("kits")) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game.getMode() == SkyWarsMode.INSANE || game.getMode() == SkyWarsMode.INSANEDOUBLES){
        new me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kitsinsane.MenuSelectKit<>(profile, InsaneKit.class);
      } else {
        new MenuSelectKit<>(profile, NormalKit.class);
      }
    } else if (action.equalsIgnoreCase("habilidades")) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game.getMode().equals(SkyWarsMode.INSANE) || game.getMode().equals(SkyWarsMode.INSANEDOUBLES)){
        new MenuSelectPerkInsane<>(profile, PerkInsane.class);
      } else {
        new MenuSelectPerk<>(profile, Perk.class);
      }
    } else if (action.equalsIgnoreCase("espectar")) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game != null) {
        new MenuSpectator(profile.getPlayer(), game);
      }
    } else if (action.equalsIgnoreCase("jogar")) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game != null) {
        new MenuPlay(profile, game.getMode());
      }
    } else if (action.equalsIgnoreCase("sair")) {
      AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
      if (game != null) {
        game.leave(profile, null);
      }
    }
  }
}
