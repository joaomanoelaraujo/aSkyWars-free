package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.kit;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.KitInsane;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class InsaneKitConfig {

  private long kitId;
  private boolean autoEquipArmor;
  private Map<Integer, Integer> slots;

  public InsaneKitConfig(KitInsane kit) {
    this.kitId = kit.getId();
    this.autoEquipArmor = true;
    this.slots = new HashMap<>();
    int index = 1;
    for (Integer slot : kit.getSlots()) {
      this.slots.put(index++, slot);
    }
  }

  public InsaneKitConfig(long kitId, JSONObject object) {
    this.kitId = kitId;
    this.autoEquipArmor = (boolean) object.get("a");
    this.slots = new HashMap<>();
    for (Object e : ((JSONObject) object.get("s")).entrySet()) {
      Map.Entry entry = (Map.Entry) e;
      this.slots.put(Integer.parseInt(entry.getKey().toString()), Integer.parseInt(entry.getValue().toString()));
    }
  }


  public static int convertConfigSlot(int slot) {
    if (slot >= 0 && slot <= 8) {
      return slot + 36;
    }
    
    return slot - 9;
  }
  
  public static int convertInventorySlot(int slot) {
    if (slot >= 0 && slot <= 26) {
      return slot + 9;
    }
    
    return slot - 36;
  }
  
  public static boolean isArmor(ItemStack item) {
    return item.getType().name().contains("_HELMET") || item.getType().name().contains("_CHESTPLATE") || item.getType().name().contains("_LEGGINGS") || item.getType().name()
        .contains("_BOOTS");
  }
  
  public void gc() {
    this.kitId = 0;
    this.autoEquipArmor = false;
    this.slots.clear();
    this.slots = null;
  }
  
  public boolean toggleAutoEquipArmor() {
    this.autoEquipArmor = !this.autoEquipArmor;
    return this.isAutoEquipArmor();
  }
  
  public void setSlot(int index, int slot) {
    this.slots.put(index, slot);
  }
  
  public int getSlot(int index) {
    return this.slots.getOrDefault(index, -1);
  }
  
  public long getKitId() {
    return this.kitId;
  }
  
  public boolean isAutoEquipArmor() {
    return this.autoEquipArmor;
  }
  
  public JSONObject toJsonObject() {
    JSONObject object = new JSONObject();
    object.put("a", this.autoEquipArmor);
    JSONObject slots = new JSONObject();
    this.slots.forEach((key, value) -> slots.put(String.valueOf(key), value));
    object.put("s", slots);
    return object;
  }
}
