package me.joaomanoel.d4rkk.dev.skywars.game.object;

import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.SkyWarsTeam;
import me.joaomanoel.d4rkk.dev.skywars.game.enums.SkyWarsMode;
import me.joaomanoel.d4rkk.dev.utils.CubeID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.joaomanoel.d4rkk.dev.skywars.utils.VoidChunkGenerator.VOID_CHUNK_GENERATOR;

public class SkyWarsConfig {
  
  private AbstractSkyWars game;
  private KConfig config;
  
  private String yaml;
  private World world;
  private String name;
  private SkyWarsMode mode;
  private List<SkyWarsTeam> teams;
  private final List<SkyWarsChest> chests;
  private CubeID cubeId;
  private final int minPlayers;
  
  public SkyWarsConfig(AbstractSkyWars game) {
    this.game = game;
    this.yaml = game.getGameName();
    this.config = Main.getInstance().getConfig("arenas", this.yaml);
    this.name = this.config.getString("name");
    this.mode = SkyWarsMode.fromName(this.config.getString("mode"));
    this.minPlayers = config.getInt("minPlayers");
    this.cubeId = new CubeID(config.getString("cubeId"));
    this.teams = new ArrayList<>();
    this.chests = this.config.getStringList("chests").stream().map(str -> new SkyWarsChest(game, str)).collect(Collectors.toList());
    this.reload(null);
  }
  
  public void setupSpawns() {
    this.config.getStringList("spawns").forEach(spawn -> this.teams.add(new SkyWarsTeam(this.game, spawn, this.mode.getSize())));
  }
  
  public void destroy() {
    if ((this.world = Bukkit.getWorld(this.yaml)) != null) {
      Bukkit.unloadWorld(this.world, false);
    }
    
    Main.getInstance().getFileUtils().deleteFile(new File(this.yaml));
    this.game = null;
    this.yaml = null;
    this.name = null;
    this.mode = null;
    this.teams.clear();
    this.teams = null;
    this.cubeId = null;
    this.world = null;
    this.config = null;
  }
  
  public void reload(final Runnable async) {
    File file = new File("plugins/aSkyWars/mundos/" + this.yaml);
    if (Bukkit.getWorld(file.getName()) != null) {
      Bukkit.unloadWorld(file.getName(), false);
    }
    
    Runnable delete = () -> {
      Main.getInstance().getFileUtils().deleteFile(new File(file.getName()));
      Main.getInstance().getFileUtils().copyFiles(file, new File(file.getName()));
      
      Runnable newWorld = () -> {
        WorldCreator wc = WorldCreator.name(file.getName());
        wc.generator(VOID_CHUNK_GENERATOR);
        wc.generateStructures(false);
        this.world = wc.createWorld();
        this.world.setTime(0L);
        this.world.setStorm(false);
        this.world.setThundering(false);
        this.world.setAutoSave(false);
        this.world.setAnimalSpawnLimit(0);
        this.world.setWaterAnimalSpawnLimit(0);
        this.world.setKeepSpawnInMemory(false);
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("mobGriefing", "false");
        this.world.getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
        if (async != null) {
          async.run();
        }
      };
      
      if (async == null) {
        newWorld.run();
        return;
      }
      
      Bukkit.getScheduler().runTask(Main.getInstance(), newWorld);
    };
    
    if (async == null) {
      delete.run();
      return;
    }
    
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), delete);
  }
  

  
  public void changeChestType(SkyWarsChest chest, SkyWarsChest.ChestType chestType) {
    List<String> chests = this.config.getStringList("chests");
    chests.remove(chest.toString());
    chest.setChestType(chestType);
    chests.add(chest.toString());
    this.config.set("chests", chests);
  }
  
  public World getWorld() {
    return this.world;
  }
  
  public KConfig getConfig() {
    return this.config;
  }
  
  public String getMapName() {
    return this.name;
  }
  
  public SkyWarsMode getMode() {
    return this.mode;
  }
  
  public List<SkyWarsTeam> listTeams() {
    return this.teams;
  }
  
  public List<SkyWarsChest> listChests() {
    return this.chests;
  }
  

  public SkyWarsChest getChest(Block block) {
    return this.listChests().stream().filter(chest -> chest.getLocation().getBlock().equals(block)).findFirst().orElse(null);
  }
  
  public CubeID getCubeId() {
    return this.cubeId;
  }
  
  public int getMinPlayers() {
    return minPlayers;
  }
}
