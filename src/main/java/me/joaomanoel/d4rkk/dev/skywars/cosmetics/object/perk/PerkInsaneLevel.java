package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.perk;


import java.util.Map;

@SuppressWarnings("unchecked")
public class PerkInsaneLevel {

  private final double coins;
  private final long cash;
  private final String description;
  private final Map<String, Object> values;

  public PerkInsaneLevel(double coins, long cash, String description, Map<String, Object> values) {
    this.coins = coins;
    this.cash = cash;
    this.description = description;
    this.values = values;
  }
  
  public double getCoins() {
    return this.coins;
  }
  
  public long getCash() {
    return this.cash;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public <T> T getValue(String key, Class<T> type, T def) {
    return (T) this.values.getOrDefault(key, def);
  }
  
  public Map<String, Object> getValues() {
    return this.values;
  }
}
