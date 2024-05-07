package me.joaomanoel.d4rkk.dev.skywars.container;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.CosmeticType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class CosmeticsContainer extends AbstractContainer {
  
  public CosmeticsContainer(DataContainer dataContainer) {
    super(dataContainer);
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    if (!cosmetics.containsKey("KILL_EFFECT")) {
      for (CosmeticType type : CosmeticType.values()) {
        cosmetics.put(type.name(), type == CosmeticType.INSANEKIT || type == CosmeticType.PERKINSANE || type == CosmeticType.KIT || type == CosmeticType.PERK ? new JSONObject() : new JSONArray());
      }
    }
    
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public void addCosmetic(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    Object object = cosmetics.get(cosmetic.getType().name());
    if (object instanceof JSONArray) {
      ((JSONArray) object).add(cosmetic.getId());
    } else {
      ((JSONObject) object).put(String.valueOf(cosmetic.getId()), 1L);
    }
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public void setLevel(Cosmetic cosmetic, long level) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    JSONObject object = (JSONObject) cosmetics.get(cosmetic.getType().name());
    object.put(String.valueOf(cosmetic.getId()), level);
    this.dataContainer.set(cosmetics.toString());
    object.clear();
    cosmetics.clear();
  }
  
  public boolean hasCosmetic(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    boolean has;
    Object object = cosmetics.get(cosmetic.getType().name());
    if (object instanceof JSONArray) {
      has = ((JSONArray) object).contains(cosmetic.getId());
    } else {
      has = ((JSONObject) object).containsKey(String.valueOf(cosmetic.getId()));
    }
    cosmetics.clear();
    return has;
  }
  
  public long getLevel(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    JSONObject object = (JSONObject) cosmetics.get(cosmetic.getType().name());
    long level = (long) object.getOrDefault(String.valueOf(cosmetic.getId()), 1L);
    object.clear();
    cosmetics.clear();
    return level;
  }
}
