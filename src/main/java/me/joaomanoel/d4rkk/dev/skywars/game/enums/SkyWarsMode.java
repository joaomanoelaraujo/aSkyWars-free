package me.joaomanoel.d4rkk.dev.skywars.game.enums;

import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.skywars.game.interfaces.LoadCallback;
import me.joaomanoel.d4rkk.dev.skywars.game.types.NormalSkyWars;

public enum SkyWarsMode {
  NORMAL("Normal", "normal", 1, NormalSkyWars.class, 1),
  INSANE("Insane", "insane", 1, NormalSkyWars.class, 1),
  NORMALDOUBLES("NormalDoubles", "normal2v2", 2, NormalSkyWars.class, 1),
  INSANEDOUBLES("InsaneDoubles", "insane2v2", 2, NormalSkyWars.class, 1);
  
  private static final SkyWarsMode[] VALUES = values();
  private final int size;
  private final String stats;
  private final String name;
  private final Class<? extends AbstractSkyWars> gameClass;
  private final int cosmeticIndex;
  
  SkyWarsMode(String name, String stats, int size, Class<? extends AbstractSkyWars> gameClass, int cosmeticIndex) {
    this.name = name;
    this.stats = stats;
    this.size = size;
    this.gameClass = gameClass;
    this.cosmeticIndex = cosmeticIndex;
  }
  
  public static SkyWarsMode fromName(String name) {
    for (SkyWarsMode mode : VALUES) {
      if (name.equalsIgnoreCase(mode.name())) {
        return mode;
      }
    }
    
    return null;
  }
  
  public AbstractSkyWars buildGame(String name, LoadCallback callback) {
    return Accessors.getConstructor(this.gameClass, String.class, LoadCallback.class).newInstance(name, callback);
  }
  
  public int getSize() {
    return this.size;
  }
  
  public String getStats() {
    return this.stats;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getCosmeticIndex() {
    return cosmeticIndex;
  }
}
