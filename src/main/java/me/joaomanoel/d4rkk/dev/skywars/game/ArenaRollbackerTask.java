package me.joaomanoel.d4rkk.dev.skywars.game;

import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.interfaces.LoadCallback;
import me.joaomanoel.d4rkk.dev.skywars.game.object.SkyWarsBlock;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.CubeID;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars.QUEUE;

public class ArenaRollbackerTask extends BukkitRunnable {
  
  private boolean locked;
  private AbstractSkyWars rollbacking;
  private CubeID.CubeIterator iterator;

  public static void scan(AbstractSkyWars game, KConfig config, LoadCallback callback) {
    new BukkitRunnable() {
      
      final List<String> blocks = new ArrayList<>();
      final Iterator<Block> iterator = game.getCubeId().iterator();
      
      @Override
      public void run() {
        int count = 0;
        while (this.iterator.hasNext() && count < 50000) {
          Block block = this.iterator.next();
          if (block.getType() != Material.AIR) {
            String location = BukkitUtils.serializeLocation(block.getLocation());
            game.getBlocks().put(location, new SkyWarsBlock(block.getType(), block.getData()));
            blocks.add(location + " : " + block.getType().name() + ", " + block.getData());
          }
          
          count++;
        }
        
        if (!this.iterator.hasNext()) {
          cancel();
          config.set("dataBlocks", blocks);
          game.setState(GameState.AGUARDANDO);
          if (callback != null) {
            callback.finish();
          }
        }
      }
    }.runTaskTimer(Main.getInstance(), 0, 1);
  }
  
  @Override
  public void run() {
    if (this.locked) {
      return;
    }
    
    int count = 0;
    if (this.rollbacking != null) {
      if (Language.options$regen$world_reload) {
        this.locked = true;
        this.rollbacking.getConfig().reload(() -> {
          this.rollbacking.setState(GameState.AGUARDANDO);
          this.rollbacking.setTimer(Language.options$start$waiting + 1);
          this.rollbacking.getTask().reset();
          this.rollbacking = null;
          this.iterator = null;
          this.locked = false;
        });
      } else {
        while (this.iterator.hasNext() && count < Language.options$regen$block_regen$per_tick) {
          this.rollbacking.resetBlock(this.iterator.next());
          count++;
        }
        
        if (!this.iterator.hasNext()) {
          this.rollbacking.getWorld().getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
          this.rollbacking.setState(GameState.AGUARDANDO);
          this.rollbacking.setTimer(Language.options$start$waiting + 1);
          this.rollbacking.getTask().reset();
          this.rollbacking = null;
          this.iterator = null;
        }
      }
    } else {
      if (!QUEUE.isEmpty()) {
        this.rollbacking = QUEUE.get(0);
        this.iterator = this.rollbacking.getCubeId().iterator();
        QUEUE.remove(0);
      }
    }
  }
}
