package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.PerkInsane;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TrevoDaSorte extends PerkInsane {
  
  private final int index;
  
  public TrevoDaSorte(int index, String key) {
    super(101, key, CONFIG.getString(key + ".permission"), CONFIG.getString(key + ".name"), CONFIG.getString(key + ".icon"), new ArrayList<>());
    this.index = index;
    this.setupLevels(key);
    Bukkit.getPluginManager().registerEvents(new Listener() {
      @EventHandler(priority = EventPriority.MONITOR)
      public void onBlockBreak(BlockBreakEvent evt) {
        Profile profile = Profile.getProfile(evt.getPlayer().getName());
        if (profile != null) {
          Player player = evt.getPlayer();
          AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
          if (game != null && !game.isSpectator(player) && game.getMode().getCosmeticIndex() == getIndex() && isSelectedPerkInsane(profile) && has(profile) && canBuy(player)) {
            if (ThreadLocalRandom.current().nextInt(100) < getCurrentLevel(profile).getValue("percentage", int.class, 0)) {
              player.sendMessage("§a[Lucky Clover] §fYou found a Golden Apple!");
              // dar a maçã ao jogador.
              player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            }
          }
        }
      }
    }, Main.getInstance());
  }
  
  @Override
  public long getIndex() {
    return this.index;
  }
  
  @Override
  public void handleEvent(SWEvent evt) {
  }
  
  @Override
  public List<Class<?>> getEventTypes() {
    return null;
  }
}
