package me.joaomanoel.d4rkk.dev.skywars.lobby;

import me.joaomanoel.d4rkk.dev.libraries.holograms.HologramLibrary;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.Hologram;
import me.joaomanoel.d4rkk.dev.libraries.npclib.NPCLibrary;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrimReaperNPC {

  private static final KConfig CONFIG = Main.getInstance().getConfig("npcs");
  private static final List<GrimReaperNPC> NPCS = new ArrayList<>();

  private String id;
  private Location location;
  private me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC npc;
  private Hologram hologram;
  private ArmorStand armorStand;
  private ArmorStand click;
  private NPC giant;


  public GrimReaperNPC(Location location, String id) {
    this.location = location;
    this.id = id;
    if (!this.location.getChunk().isLoaded()) {
      this.location.getChunk().load(true);
    }
    
    this.spawn();
  }
  
  public static void setupNPCs() {
    if (!CONFIG.contains("grim")) {
      CONFIG.set("grim", new ArrayList<>());
    }
    for (String serialized : CONFIG.getStringList("grim")) {
      if (serialized.split("; ").length > 6) {
        String id = serialized.split("; ")[6];
        
        NPCS.add(new GrimReaperNPC(BukkitUtils.deserializeLocation(serialized), id));
      }
    }
  }
  
  public static void add(String id, Location location) {
    NPCS.add(new GrimReaperNPC(location, id));
    List<String> list = CONFIG.getStringList("grim");
    list.add(BukkitUtils.serializeLocation(location) + "; " + id);
    CONFIG.set("grim", list);
  }
  
  public static void remove(GrimReaperNPC npc) {
    NPCS.remove(npc);
    List<String> list = CONFIG.getStringList("grim");
    list.remove(BukkitUtils.serializeLocation(npc.getLocation()) + "; " + npc.getId());
    CONFIG.set("grim", list);
    
    npc.destroy();
  }
  
  public static GrimReaperNPC getById(String id) {
    return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
  }
  
  public static Collection<GrimReaperNPC> listNPCs() {
    return NPCS;
  }

  public Location getFinalLocation(Location loc){
    Location location = loc.clone().add(getLeftHeadDirection(loc).multiply(2));
    Location location1 = location.add(getBehind(location).multiply(-3.5));
    Location locationFinal = location1.subtract(0,9.7,0.5);
    locationFinal.setYaw(loc.clone().getYaw());
    Location finalBlock = locationFinal.getBlock().getLocation();
    finalBlock.add(0.5,0.2,1.0);
    return finalBlock;
  }

  public static Vector getBehind(Location location) {
    Vector direction = location.getDirection().normalize();
    return new Vector(direction.getX(), 0.0, direction.getZ()).normalize();
  }
  public static Vector getLeftHeadDirection(Location location) {
    Vector direction = location.getDirection().normalize();
    return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
  }


  public void spawn() {
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }

    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
    this.hologram = HologramLibrary.createHologram(this.location.clone().add(0, 0.5, 0));
    for (int index = Language.lobby$npc$stats$hologram.size(); index > 0; index--) {
      this.hologram.withLine(Language.lobby$npc$stats$hologram.get(index - 1));
    }
    this.npc = NPCLibrary.createNPC(EntityType.SKELETON, "§8[NPC] ");
    this.npc.data().set("play-npc", "GRIM");
    this.npc.data().set(NPC.HIDE_BY_TEAMS_KEY, true);

  }

  public void destroy() {
    this.id = null;

    this.location = null;

    if (this.giant != null) {
      this.giant = null;
    }
    if (this.armorStand != null) {
      this.armorStand.remove();
      this.armorStand = null;
    }
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }
    if (this.click != null) {
      this.click.remove();
      this.click = null;
    }
    HologramLibrary.removeHologram(this.hologram);
    this.hologram = null;
  }
  
  public String getId() {
    return id;
  }

  
  public Location getLocation() {
    return this.location;
  }
}
