package me.joaomanoel.d4rkk.dev.skywars.cosmetics;


public enum CosmeticType {
  KIT("Normal Kit"),
  INSANEKIT("Insane Kit"),
  PERK("Normal Perk"),
  PERKINSANE("Insane Perk"),
  KILL_EFFECT("Kill Effects"),
  PROJECTILE_EFFECT("Projectile Effects"),
  CAGE("Cages"),
  WIN_ANIMATION("Victory Celebrations");
  
  private final String[] names;
  
  CosmeticType(String... names) {
    this.names = names;
  }
  
  public String getName(long index) {
    return this.names[(int) (index - 1)];
  }
}
