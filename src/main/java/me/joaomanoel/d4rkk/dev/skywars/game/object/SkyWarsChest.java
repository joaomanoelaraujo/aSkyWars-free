package me.joaomanoel.d4rkk.dev.skywars.game.object;

import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.libraries.holograms.HologramLibrary;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.Hologram;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class SkyWarsChest {
  
  private final String serialized;
  private String chestType;
  private final AbstractSkyWars game;
  private Hologram hologram = null;
  
  public SkyWarsChest(AbstractSkyWars game, String serialized) {
    this.serialized = serialized;
    this.game = game;
    this.chestType = serialized.split("; ")[6];
  }
  
  
  public void update() {
    if (this.hologram != null && game.getState() == GameState.EMJOGO) {
      Block block = this.getLocation().getBlock();
      if (!(block.getState() instanceof Chest)) {
        this.destroy();
        return;
      }
      
      NMS.playChestAction(this.getLocation(), true);
      if (game.getEvent().contains(Language.options$events$refill) && !game.getEvent().contains("de")) {
        this.hologram.updateLine(1, "§e{time}".replace("{time}", game.getEvent().split(" ")[1]));
      } else {
        this.destroy();
      }
    } else {
      this.destroy();
    }
  }
  
  public void createHologram() {
    if (this.hologram == null) {
      this.hologram = HologramLibrary.createHologram(this.getLocation().add(0.0, -0.5, 0.0));
      this.hologram.withLine("§e{time}".replace("{time}", game.getEvent().split(" ")[1]));
    }
  }
  
  public void destroy() {
    if (this.hologram != null) {
      NMS.playChestAction(this.getLocation(), false);
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
  }
  
  public void fill() {
    this.fill(false);
  }
  
  public void refill() {
    this.fill(true);
  }
  
  private void fill(boolean refill) {
    ChestType chestType = ChestType.getByName(this.chestType);
    if (chestType == null) {
      chestType = ChestType.getFirstType();
    }
    
    if (chestType != null) {
      chestType.apply(this.getLocation(), refill);
    }
  }
  
  public Location getLocation() {
    return BukkitUtils.deserializeLocation(this.serialized);
  }
  
  public String getChestType() {
    return this.chestType;
  }
  
  public void setChestType(ChestType chestType) {
    this.chestType = chestType.getName();
  }
  
  @Override
  public String toString() {
    return BukkitUtils.serializeLocation(this.getLocation()) + "; " + this.chestType;
  }
  
  public static class ChestType {
    
    public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("CHESTS");
    private static final KConfig CONFIG = Main.getInstance().getConfig("chests");
    private static final Map<String, ChestType> TYPES = new HashMap<>();
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final List<Integer> SLOTS = new ArrayList<>();
    
    static {
      for (int slot = 0; slot < 27; slot++) {
        SLOTS.add(slot);
      }
    }
    
    private final String name;
    private String refillChest;
    private boolean randomSlots;
    private final List<ChestType.ChestItem> items;
    
    public ChestType(String name, String refillChest, boolean randomSlots, List<ChestType.ChestItem> items) {
      this.name = name;
      this.refillChest = refillChest;
      this.randomSlots = randomSlots;
      this.items = items;
    }
    
    public static ChestType createChestType(String name) {
      if (TYPES.containsKey(name)) {
        return null;
      }
      
      CONFIG.set(name + ".refillChest", name);
      CONFIG.set(name + ".randomSlots", true);
      CONFIG.set(name + ".items", new ArrayList<>());
      ChestType chestType = new ChestType(name, "", true, new ArrayList<>());
      TYPES.put(name, chestType);
      return chestType;
    }
    
    public static void setupChestTypes() {
      for (String name : CONFIG.getKeys(false)) {
        List<ChestType.ChestItem> items = new ArrayList<>();
        for (String item : CONFIG.getStringList(name + ".items")) {
          try {
            items.add(new ChestType.ChestItem(BukkitUtils.deserializeItemStack(item.split("; ")[1]), Integer.parseInt(item.split("; ")[0])));
          } catch (Exception ex) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> LOGGER.log(Level.WARNING, "O item \"" + item + "\" do bau \"" + name + "\" esta invalido"));
          }
        }
        
        TYPES.put(name, new ChestType(name, CONFIG.getString(name + ".refillChest"), CONFIG.getBoolean(name + ".randomSlots"), items));
      }
    }
    
    public static ChestType getFirstType() {
      return listTypes().stream().findFirst().orElse(null);
    }
    
    public static ChestType getByName(String name) {
      return TYPES.get(name);
    }

    public static Collection<ChestType> listTypes() {
      return TYPES.values();
    }

    public boolean toggleRandomSlots() {
      this.randomSlots = !this.randomSlots;
      CONFIG.set(this.name + ".randomSlots", this.randomSlots);
      return this.randomSlots;
    }

    public void setRefill(ChestType chestType) {
      this.refillChest = chestType.getName();
      CONFIG.set(this.name + ".refillChest", chestType.getName());
    }

    public void save() {
      List<String> serialized = new ArrayList<>();
      for (ChestType.ChestItem item : this.listItems()) {
        serialized.add(item.percentage + "; " + BukkitUtils.serializeItemStack(item.item).replace("\n", "\\n"));
      }
      
      CONFIG.set(this.name + ".items", serialized);
    }
    
    public void apply(Location location, boolean refill) {
      Block block = location.getBlock();
      if (block != null && block.getState() instanceof Chest) {
        Chest chest = (Chest) block.getState();
        
        Inventory inventory = chest.getInventory();
        inventory.clear();
        int index = 0;
        Collections.shuffle(SLOTS);
        ChestType type = refill ? this.getRefillChest() : this;
        for (ChestType.ChestItem item : type.listItems()) {
          if (index >= 27) {
            break;
          }
          
          ItemStack apply = item.get();
          if (apply != null) {
            if (type.isRandomSlots()) {
              inventory.setItem(SLOTS.get(index++), apply);
            } else {
              inventory.addItem(apply);
            }
          }
        }
      }
    }
    
    public String getName() {
      return this.name;
    }
    
    public boolean isRandomSlots() {
      return this.randomSlots;
    }
    
    public ChestType getRefillChest() {
      return getByName(this.refillChest) == null ? this : getByName(this.refillChest);
    }
    
    public ChestType.ChestItem getItem(int index) {
      return index < this.items.size() ? this.items.get(index) : null;
    }
    
    public List<ChestType.ChestItem> listItems() {
      return this.items;
    }
    
    public static class ChestItem {
      
      private final ItemStack item;
      private int percentage;
      
      public ChestItem(ItemStack item, int percentage) {
        this.item = item;
        this.percentage = percentage;
      }
      
      public void modifyPercentage(int diff) {
        this.percentage += diff;
        if (this.percentage > 100) {
          this.percentage = 100;
        }
        if (this.percentage < 0) {
          this.percentage = 0;
        }
      }
      
      public ItemStack get() {
        if (RANDOM.nextInt(100) + 1 <= percentage) {
          return item;
        }
        
        return null;
      }
      
      public ItemStack getItem() {
        return this.item;
      }
      
      public int getPercentage() {
        return this.percentage;
      }
    }
  }
}
