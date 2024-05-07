package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types;


import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.logging.Level;

@SuppressWarnings({"unchecked", "deprecated"})
public class Cage extends Cosmetic {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("CAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics", "cages");
  private String name;
  private String icon;
  private JSONArray blocks;
  private final boolean isFireworks;
  
  public Cage(long id, EnumRarity rarity, double coins, long cash, String permission, String name, String icon, JSONArray blocks, boolean isFireworks) {
    super(id, CosmeticType.CAGE, coins, permission);
    this.name = name;
    this.icon = icon;
    this.blocks = blocks;
    this.rarity = rarity;
    this.cash = cash;
    
    this.isFireworks = isFireworks;
  }
  
  public static void setupCages() {
    for (String key : CONFIG.getKeys(false)) {
      long id = CONFIG.getInt(key + ".id");
      double coins = CONFIG.getDouble(key + ".coins");
      if (!CONFIG.contains(key + ".cash")) {
        CONFIG.set(key + ".cash", getAbsentProperty("cages", key + ".cash"));
      }
      long cash = CONFIG.getInt(key + ".cash", 0);
      String permission = CONFIG.getString(key + ".permission");
      String name = CONFIG.getString(key + ".name");
      String icon = CONFIG.getString(key + ".icon");
      boolean isFireworks = CONFIG.getBoolean(key + ".fireworks");
      if (!CONFIG.contains(key + ".rarity")) {
        CONFIG.set(key + ".rarity", getAbsentProperty("cages", key + ".rarity"));
      }
      JSONArray blocks = null;
      try {
        blocks = (JSONArray) new JSONParser().parse(CONFIG.getString(key + ".blocks"));
      } catch (ParseException ex) {
        LOGGER.log(Level.WARNING, "Cage \"" + key + "\" invalida: ", ex);
        continue;
      }
      
      new Cage(id, EnumRarity.fromName(CONFIG.getString(key + ".rarity")), coins, cash, permission, name, icon, blocks, isFireworks);
    }
  }
  
  public static void createCage(Player player, String name) {
    long id = 1;
    while (Cosmetic.findById(Cage.class, id) != null) {
      id++;
    }
    
    JSONArray blocks = new JSONArray();
    Location location = player.getLocation().getBlock().getLocation().add(0, -1, 0);
    runCage((x, y, z) -> {
      Block block = location.clone().add(x, y, z).getBlock();
      if (block.getType() != Material.AIR) {
        blocks.add(x + "; " + y + "; " + z + "; " + block.getType().name() + "; " + block.getData());
      }
    }, 5, 4, 2, 1);
    
    String key = name.replace(" ", "_");
    CONFIG.set(key + ".id", id);
    CONFIG.set(key + ".coins", 10000.0);
    CONFIG.set(key + ".cash", 50);
    CONFIG.set(key + ".rarity", "COMMON");
    CONFIG.set(key + ".name", name);
    CONFIG.set(key + ".permission", "");
    CONFIG.set(key + ".icon", "GLASS : 1");
    CONFIG.set(key + ".blocks", blocks.toString());
    new Cage(id, EnumRarity.COMMON, 10000.0, 50, "", name, "GLASS : 1", blocks, false);
  }
  
  public static void removeCage(Cage cage) {
    for (String key : CONFIG.getKeys(false)) {
      if (CONFIG.getInt(key + ".id") == cage.getId()) {
        CONFIG.set(key, null);
      }
    }
    
    cage.destroy();
    Cosmetic.removeCosmetic(cage);
  }
  
  public static void applyCage(Location location, boolean big) {
    runCage((x, y, z) -> location.clone().add(x, y, z).getBlock().setType(Material.GLASS), 4, big ? 2 : 1);
  }

  public static void destroyCage(Location location) {
    runCage((x, y, z) -> {
      if (location.clone().add(x, y, z).getBlock().getType() == Material.DISPENSER) {
        BlockState state = location.clone().add(x, y, z).getBlock().getState();
        state.getBlock().getLocation().add(0, 1, 0).getBlock().setType(Material.AIR);
        InventoryHolder iv = (InventoryHolder) state;
        state.setType(Material.AIR);
        iv.getInventory().clear();
        state.update(true);
      }
      location.clone().add(x, y, z).getBlock().setType(Material.AIR);
    }, 5, 4, 2, 1);
  }
  
  private static void runCage(CageRunnable cageRunnable, double height, double width) {
    runCage(cageRunnable, height, height, width, width);
  }
  
  private static void runCage(CageRunnable cageRunnable, double height, double heightIndex, double width, double widthInside) {
    for (double y = 0; y <= height; y++) {
      for (double x = -width; x <= width; x++) {
        for (double z = -width; z <= width; z++) {
          if (y > 0 && y < heightIndex) {
            if ((x > -widthInside && x < widthInside) && (z > -widthInside && z < widthInside)) {
              continue;
            }
          }
          
          cageRunnable.run(x, y, z);
        }
      }
    }
  }
  
  public void destroy() {
    this.name = null;
    this.icon = null;
    this.blocks.clear();
    this.blocks = null;
  }
  
  public void apply(Location location) {
    for (Object object : this.blocks) {
      if (object instanceof String) {
        String offset = (String) object;
        double offsetX = Double.parseDouble(offset.split("; ")[0]);
        double offsetY = Double.parseDouble(offset.split("; ")[1]);
        double offsetZ = Double.parseDouble(offset.split("; ")[2]);
        Material blockMaterial = Material.matchMaterial(offset.split("; ")[3]);
        byte data = Byte.parseByte(offset.split("; ")[4]);
        
        Block block = location.clone().add(offsetX, offsetY, offsetZ).getBlock();
        block.setType(blockMaterial);
        BlockState state = block.getState();
        if (isFireworks) {
          if (state.getType() == Material.DISPENSER) {
            Block plate = state.getLocation().add(0, 1, 0).getBlock();
            plate.setType(Material.GOLD_PLATE);
            InventoryHolder holder = ((InventoryHolder) block.getState());
            for (int i = 0; i <= 10; i++) {
              holder.getInventory().addItem(BukkitUtils.deserializeItemStack("FIREWORK : 64"));
            }
            plate.getState().update(true);
          }
        }
        state.getData().setData(data);
        state.update(true);
      }
    }
  }
  
  public void preview(Player viewer, Location location, boolean destroy) {
    for (Object object : this.blocks) {
      if (object instanceof String) {
        String offset = (String) object;
        double offsetX = Double.parseDouble(offset.split("; ")[0]);
        double offsetY = Double.parseDouble(offset.split("; ")[1]);
        double offsetZ = Double.parseDouble(offset.split("; ")[2]);
        Material blockMaterial = Material.matchMaterial(offset.split("; ")[3]);
        byte data = Byte.parseByte(offset.split("; ")[4]);
        Block block = location.clone().add(offsetX, offsetY, offsetZ).getBlock();
        BlockState state = block.getState();
        if (isFireworks) {
          if (blockMaterial == Material.DISPENSER) {
            if (destroy) {
              viewer.sendBlockChange(state.getLocation().clone().add(0, 1, 0), Material.AIR, (byte) 0);
            } else {
              viewer.sendBlockChange(state.getLocation().clone().add(0, 1, 0), Material.GOLD_PLATE, (byte) 0);
            }
          }
        }
        if (destroy) {
          viewer.sendBlockChange(location.clone().add(offsetX, offsetY, offsetZ), Material.AIR, (byte) 0);
        } else {
          viewer.sendBlockChange(location.clone().add(offsetX, offsetY, offsetZ), blockMaterial, data);
        }
      }
    }
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public ItemStack getIcon(Profile profile) {
    double coins = profile.getCoins("aCoreSkyWars");
    long cash = profile.getStats("aCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
        (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked) :
        (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$cage$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$cage$icon$buy_desc$start.replace("{buy_desc_status}",
                (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$cage$icon$perm_desc$start
                .replace("{perm_desc_status}", (role == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{role}", role.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins()))
        .replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + " : name>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
  
  private interface CageRunnable {
    void run(double x, double y, double z);
  }
}
