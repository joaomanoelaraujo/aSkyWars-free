package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perk;

import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.api.event.game.SWGameStartEvent;
import me.joaomanoel.d4rkk.dev.skywars.api.event.player.SWPlayerDeathEvent;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Foguete extends Perk {
  
  protected static final DecimalFormat df = new DecimalFormat("###.#");
  protected static final Map<String, Long> DELAY_CACHE = new HashMap<>();
  protected final int index;
  protected final List<Player> REMOVE_DAMAGE = new ArrayList<>();
  public Foguete(int index, String key) {
    super(13, key, CONFIG.getString(key + ".permission"), CONFIG.getString(key + ".name"), CONFIG.getString(key + ".icon"), new ArrayList<>());
    this.index = index;
    this.setupLevels(key);
    this.register();
    
    Bukkit.getPluginManager().registerEvents(new Listener() {
      
      @EventHandler(priority = EventPriority.MONITOR)
      public void onEntityDamage(EntityDamageEvent evt) {
        if (!(evt.getEntity() instanceof Player)) {
          return;
        }
        
        Player player = (Player) evt.getEntity();
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
          if (evt.getCause() == EntityDamageEvent.DamageCause.FALL && REMOVE_DAMAGE.contains(player)) {
            REMOVE_DAMAGE.remove(player);
            evt.setCancelled(true);
          }
        }
      }
      
      @EventHandler(priority = EventPriority.MONITOR)
      public void onPlayerInteract(PlayerInteractEvent evt) {
        Profile profile = Profile.getProfile(evt.getPlayer().getName());
        if (profile == null || evt.getAction() == Action.PHYSICAL) {
          return;
        }
        Player player = evt.getPlayer();
        AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
        ItemStack stack = evt.getItem();
        if (game != null && !game.isSpectator(player) && game.getMode().getCosmeticIndex() == getIndex() & isSelectedPerk(profile) && has(profile) && canBuy(player) && stack != null && stack.getItemMeta() != null && stack.getItemMeta().getDisplayName() != null && stack.getItemMeta().getDisplayName().equals("§6Foguete")) {
          long start = DELAY_CACHE.containsKey(player.getName()) ? DELAY_CACHE.get(player.getName()) : 0;
          if (start > System.currentTimeMillis()) {
            double time = (start - System.currentTimeMillis()) / 1000.0;
            if (time > 0.1) {
              evt.setCancelled(true);
              String timeString = df.format(time).replace(",", ".");
              if (timeString.endsWith("0")) {
                timeString = timeString.substring(0, timeString.lastIndexOf(".") < 0 ? 1 : timeString.lastIndexOf("."));
              }

              NMS.sendActionBar(player, "§cWait {time}s to use the rocket again.".replace("{time}", timeString));
              return;
            }
          }
          
          REMOVE_DAMAGE.add(player);
          DELAY_CACHE.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(getCurrentLevel(profile).getValue("delay", int.class, 0)));
          Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
          FireworkMeta meta = firework.getFireworkMeta();
          meta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255))).with(FireworkEffect.Type.values()[ThreadLocalRandom.current().nextInt(FireworkEffect.Type.values().length)]).build());
          meta.setPower(1);
          firework.setFireworkMeta(meta);
          firework.setPassenger(player);
          evt.setCancelled(true);
        }
      }
    }, Main.getInstance());
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
        if (has(profile) && canBuy(player) && this.isSelectedPerk(profile)) {
          player.getInventory().addItem(BukkitUtils.deserializeItemStack("FIREWORK : 1 : name>&6Firework"));
        }
      });
    } else if (evt2 instanceof SWPlayerDeathEvent) {
      SWPlayerDeathEvent evt = (SWPlayerDeathEvent) evt2;
      if (has(evt.getProfile()) && canBuy(evt.getProfile().getPlayer())) {
        DELAY_CACHE.remove(evt.getProfile().getPlayer().getName());
      }
    }
  }
  
  @Override
  public List<Class<?>> getEventTypes() {
    return Arrays.asList(SWGameStartEvent.class, SWPlayerDeathEvent.class);
  }
}
