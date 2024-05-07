package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.preview;

import me.joaomanoel.d4rkk.dev.game.FakeGame;
import me.joaomanoel.d4rkk.dev.libraries.npclib.NPCLibrary;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractPreview;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.ProjectileEffect;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.MenuCosmetics;
import me.joaomanoel.d4rkk.dev.skywars.nms.NMS;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class ProjectileEffectPreview extends AbstractPreview<ProjectileEffect> implements Listener {
  
  private static final Location[] LOCATIONS = new Location[3];

  static {
    createLocations();
  }

  private Location oldLocation;
  private Entity cart;
  private NPC npc;
  
  public ProjectileEffectPreview(Profile profile, ProjectileEffect cosmetic) {
    super(profile, cosmetic);
    
    this.npc = NPCLibrary.createNPC(EntityType.PLAYER, player.getName());
    this.npc.data().set(NPC.ATTACHED_PLAYER, profile.getName());
    this.npc.data().set(NPC.COPY_PLAYER_SKIN, true);
    this.npc.data().set(NPC.GRAVITY, true);
    this.npc.spawn(LOCATIONS[0].clone().add(LOCATIONS[0].getDirection().normalize().multiply(-1)));
    
    this.oldLocation = this.player.getLocation();
    profile.setGame(FakeGame.FAKE_GAME);
    profile.setHotbar(null);
    for (Player players : Bukkit.getOnlinePlayers()) {
      players.hidePlayer(player);
    }
    
    if (Main.kCosmetics) {
      //CosmeticsAPI.disable(player);
    }
    
    Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(),
        () -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityEquipment(((CraftPlayer) npc.getEntity()).getHandle().getId(), 0, new ItemStack(Item.getById(261)))),
        12);
    Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
      DataWatcher watcher = new DataWatcher(((CraftPlayer) npc.getEntity()).getHandle());
      watcher.a(0, (byte) 16);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(((CraftPlayer) npc.getEntity()).getHandle().getId(), watcher, true));
      Vector playerDirection = player.getLocation().add(0, 1, 0).getDirection();
      EntityArrow arrow = ((CraftArrow) ((Player) npc.getEntity()).launchProjectile(Arrow.class, playerDirection)).getHandle();
      arrow.fromPlayer = 2;
    }, 25);
    
    this.cart = NMS.createAttachedCart(this.player.getName(), LOCATIONS[1]);
    this.player.teleport(LOCATIONS[1]);
    
    this.runTaskLater(Main.getInstance(), 3L);
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  public static void createLocations() {
    if (CONFIG.contains("projectileeffect")) {
      for (int index = 0; index < 2; index++) {
        String value = CONFIG.getString("projectileeffect." + (index + 1));
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
  
  public void stop() {
    this.oldLocation = null;
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }
    if (this.cart != null) {
      this.cart.remove();
      this.cart = null;
    }
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void run() {
    NMS.sendFakeSpectator(this.player, this.cart);
    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      this.returnToMenu();
      this.stop();
    }, 90L);
  }
  
  @Override
  public void returnToMenu() {
    Profile profile = Profile.getProfile(this.player.getName());
    if (profile != null) {
      NMS.sendFakeSpectator(this.player, null);
      this.player.setAllowFlight(this.player.hasPermission("aCore.fly"));
      profile.setGame(null);
      profile.setHotbar(Hotbar.getHotbarById("lobby"));
      profile.refreshPlayers();
      this.player.teleport(this.oldLocation);
      
      if (Main.kCosmetics) {
       // CosmeticsAPI.enable(player);
      }
      
      new MenuCosmetics<>(profile, "Trails Effect", ProjectileEffect.class);
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onProjectileLaunch(ProjectileLaunchEvent evt) {
    if (!(evt.getEntity().getShooter() instanceof Player)) {
      return;
    }
    NPC npc = NPCLibrary.getNPC(((Entity) evt.getEntity().getShooter()));
    if (npc == null || !npc.equals(this.npc)) {
      return;
    }
    this.cosmetic.preview(this.player, this.npc.getCurrentLocation(), evt.getEntity());
  }
}

