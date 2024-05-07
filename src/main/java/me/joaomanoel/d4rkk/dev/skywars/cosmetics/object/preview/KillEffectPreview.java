package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview;


import me.joaomanoel.d4rkk.dev.game.FakeGame;
import me.joaomanoel.d4rkk.dev.libraries.npclib.NPCLibrary;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.event.NPCDeathEvent;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.event.NPCNavigationEndEvent;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.libraries.npclib.trait.NPCTrait;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractPreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.KillEffect;
import me.joaomanoel.d4rkk.dev.skywars.lobby.trait.NPCSkinTrait;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.MenuCosmetics;
import me.joaomanoel.d4rkk.dev.skywars.nms.NMS;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPCAnimation.SWING_ARM;

public class KillEffectPreview extends AbstractPreview<KillEffect> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[3];

  static {
    createLocations();
  }

  private NPC opponent;
  private NPC attacker;
  private Entity cart;
  private Location oldLocation;
  
  public KillEffectPreview(Profile profile, KillEffect cosmetic) {
    super(profile, cosmetic);
    
    this.opponent = NPCLibrary.createNPC(EntityType.PLAYER, "§cOponente");
    this.opponent.data().set(NPC.ATTACHED_PLAYER, profile.getName());
    this.opponent.addTrait(new NPCSkinTrait(this.opponent,
        "eyJ0aW1lc3RhbXAiOjE1ODY5MzIzODcyNTQsInByb2ZpbGVJZCI6ImJjNzAxYzk1NTViNzQ5YmJhNDdkZWEzZTlmZDgwMDFkIiwicHJvZmlsZU5hbWUiOiIweDQ1MiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk5NDc3OWM2ODBlMzhiNzU3ZGUyMTZkYmQ0OTVlZjljYmFmYzg3MjllYTlhYjAyNTNkMTNlZDU5N2Y1MTQ0MiJ9fX0=",
        "ppEIxcszLINYQw4BeE0jftmrBpvKgt5MUGRm21QYJBil60QWagrMZlovFv/0+sHwJJ7eJHLNjhvthY+i41vNljVY3/O21DbTXygyGkHkhAAyihCBroy5IxV87vXsylZNGZ5gfd7t3OLxpyOI6TUizWT10XzsrmhYo3hCZETkdVhIll+T6ENZxCHB/8Sl1mUhWko4hnf8pgmoS0rsixXM9WbccGFO00pNYYZ2Jai133QE0slMkbyCf/7t4Q2ATzjXGx+8avSu3LpHxSff2tUDWH54qanQ5x6eey9Rc846V24TUiy3kiKWmTchGiegZ6kAj3sO1wO/ohvQySzbeAsnz94rGdwzjpUF3pWgH5mJ6vHEstsH1hoYUBKwFAxaKhp2iI7CzgHOO+BMVbIF1Fm66OuDJ3+4am2mvCGRsGG0ufPxHM6O3TZHpkw3rUGWbx13KQLSFWvLzZjzl/EIcO8Kt6XTKgl8qciCG9nFM97EkCjpNHMoedKnphwFVu/K1O3hGz6QxI4/8PdsYZnLOlvPlG1nHDzlkDGjDtkLXTgWKHVTNFD7R10jXNbWoJyt712D2c7otceOOpu1s70JRbHMKKxMZtSMOt3MQMuCw6LJJEdbtyC4d8D6mE8lM1HZmMa3tcb7cBFHryy1eEwMStJh7A2O3GP6SPssgCI2TTtoLEs="));
    this.opponent.spawn(LOCATIONS[0].clone().add(LOCATIONS[0].getDirection().normalize().multiply(-1)));
    
    this.attacker = NPCLibrary.createNPC(EntityType.PLAYER, player.getName());
    this.attacker.data().set(NPC.ATTACHED_PLAYER, profile.getName());
    this.attacker.data().set(NPC.COPY_PLAYER_SKIN, true);
    this.attacker.data().set(NPC.GRAVITY, true);
    this.attacker.addTrait(new NPCTrait(this.attacker) {
      @Override
      public void onSpawn() {
        ((Player) this.getNPC().getEntity()).setItemInHand(BukkitUtils.deserializeItemStack("DIAMOND_SWORD : 1"));
      }
    });
    this.attacker.spawn(LOCATIONS[1]);
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    profile.setHotbar(null);
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    if (Main.kCosmetics) {
      //CosmeticsAPI.disable(player);
    }
    
    this.cart = NMS.createAttachedCart(this.player.getName(), LOCATIONS[2]);
    this.player.teleport(LOCATIONS[2]);
    
    this.runTaskLater(Main.getInstance(), 3L);
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  public static void createLocations() {
    if (CONFIG.contains("killeffect")) {
      for (int index = 0; index < 3; index++) {
        String value = CONFIG.getString("killeffect." + (index + 1));
        if (value != null) {
          LOCATIONS[index] = BukkitUtils.deserializeLocation(value);
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.stop();
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onNPCDeathEvent(NPCDeathEvent evt) {
    if (evt.getNPC().equals(this.opponent)) {
      this.opponent = null;
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onNPCNavigationEnd(NPCNavigationEndEvent evt) {
    if (evt.getNPC().equals(this.attacker)) {
      this.opponent.data().set(NPC.PROTECTED_KEY, false);
      this.attacker.playAnimation(SWING_ARM);
      Location currentLocation = this.opponent.getCurrentLocation();
      ((Player) this.opponent.getEntity()).damage(20.0, this.attacker.getEntity());
      this.cosmetic.execute(this.player, currentLocation);
      ((Player) this.attacker.getEntity()).setItemInHand(null);
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
        this.returnToMenu();
        this.stop();
      }, 60L);
    }
  }
  
  public void stop() {
    this.oldLocation = null;
    if (this.cart != null) {
      this.cart.remove();
      this.cart = null;
    }
    if (this.opponent != null) {
      this.opponent.destroy();
      this.opponent = null;
    }
    if (this.attacker != null) {
      this.attacker.destroy();
      this.attacker = null;
      HandlerList.unregisterAll(this);
    }
  }
  
  @Override
  public void run() {
    NMS.sendFakeSpectator(this.player, this.cart);
    this.attacker.setWalkingTo(LOCATIONS[0]);
  }
  
  @Override
  public void returnToMenu() {
    Profile profile = Profile.getProfile(this.player.getName());
    if (profile != null) {
      NMS.sendFakeSpectator(this.player, null);
      this.player.setAllowFlight(this.player.hasPermission("aCore.fly"));
      profile.setGame(null);
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      if (Main.kCosmetics) {
        //CosmeticsAPI.enable(player);
      }
      profile.refreshPlayers();
      this.player.teleport(this.oldLocation);
      new MenuCosmetics<>(profile, "Kill Effects", KillEffect.class);
    }
  }
}
