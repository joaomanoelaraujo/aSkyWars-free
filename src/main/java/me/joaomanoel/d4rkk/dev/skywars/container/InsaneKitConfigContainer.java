package me.joaomanoel.d4rkk.dev.skywars.container;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.kit.InsaneKitConfig;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.KitInsane;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class InsaneKitConfigContainer extends AbstractContainer {

  private Map<Long, List<InsaneKitConfig>> configs = new HashMap<>();

  public InsaneKitConfigContainer(DataContainer dataContainer) {
    super(dataContainer);
    if (dataContainer.get() == null) {
      dataContainer.set("{}");
    }
    JSONObject configMap = this.dataContainer.getAsJsonObject();
    for (String index : new String[]{"1"}) {
      if (!configMap.containsKey(index)) {
        configMap.put("1", new JSONObject());
      } else {
        List<InsaneKitConfig> configs = this.configs.computeIfAbsent(Long.parseLong(index), k -> new ArrayList<>());
        
        JSONObject cfgs = (JSONObject) configMap.get(index);
        for (Object e : cfgs.entrySet()) {
          Map.Entry entry = (Map.Entry) e;
          configs.add(new InsaneKitConfig(Long.parseLong(entry.getKey().toString()), ((JSONObject) entry.getValue())));
        }
      }
    }
    this.dataContainer.set(configMap.toString());
    configMap.clear();
  }
  
  @Override
  public void gc() {
    super.gc();
    this.configs.values().forEach(cfgs -> cfgs.forEach(InsaneKitConfig::gc));
    this.configs.clear();
    this.configs = null;
  }

  public void saveConfig(KitInsane kit, InsaneKitConfig config) {
    JSONObject configMap = this.dataContainer.getAsJsonObject();
    ((JSONObject) configMap.get(String.valueOf(kit.getIndex()))).put(String.valueOf(kit.getIndex()), config.toJsonObject());
    this.dataContainer.set(configMap.toString());
    configMap.clear();
  }

  public InsaneKitConfig getOrLoadConfig(KitInsane kit) {
    List<InsaneKitConfig> configs = this.configs.computeIfAbsent(kit.getIndex(), k -> new ArrayList<>());

    InsaneKitConfig config = configs.stream().filter(cfg -> cfg.getKitId() == kit.getId()).findFirst().orElse(null);
    if (config == null) {
      config = new InsaneKitConfig(kit);
      this.saveConfig(kit, config);
      configs.add(config);
    }

    return config;
  }
}