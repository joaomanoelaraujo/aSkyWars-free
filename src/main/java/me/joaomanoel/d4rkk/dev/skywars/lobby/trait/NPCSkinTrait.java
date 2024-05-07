package me.joaomanoel.d4rkk.dev.skywars.lobby.trait;

import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.skin.Skin;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.skin.SkinnableEntity;
import me.joaomanoel.d4rkk.dev.libraries.npclib.trait.NPCTrait;

public class NPCSkinTrait extends NPCTrait {
  
  private final Skin skin;
  
  public NPCSkinTrait(NPC npc, String value, String signature) {
    super(npc);
    this.skin = Skin.fromData(value, signature);
  }
  
  @Override
  public void onSpawn() {
    this.skin.apply((SkinnableEntity) this.getNPC().getEntity());
  }
}
