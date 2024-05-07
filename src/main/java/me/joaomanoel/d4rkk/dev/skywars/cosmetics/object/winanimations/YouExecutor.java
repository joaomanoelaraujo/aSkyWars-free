package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import com.mojang.authlib.GameProfile;
import me.joaomanoel.d4rkk.dev.libraries.npclib.NPCLibrary;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import me.joaomanoel.d4rkk.dev.skywars.lobby.trait.NPCSkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

import static me.joaomanoel.d4rkk.dev.utils.BukkitUtils.GET_PROFILE;

public class YouExecutor extends AbstractExecutor implements Listener {
  
  protected List<NPC> NPC_LIST;
  protected boolean sneak;
  
  public YouExecutor(Player player) {
    super(player);
    this.NPC_LIST = new ArrayList<>();
  }

  @Override
  public void cancel() {
    NPC_LIST.forEach(NPC::destroy);
    NPC_LIST.clear();
    NPC_LIST = null;
    HandlerList.unregisterAll(this);
  }
  
  @Override
  public void tick() {
    Location randomLocation = this.player.getLocation().clone().add(Math.floor(Math.random() * 5.0D),
        0, Math.floor(Math.random() * 5.0D));
    randomLocation.setY(this.player.getWorld().getHighestBlockYAt(this.player.getLocation()));
    NPC entity = NPCLibrary.createNPC(EntityType.PLAYER, "§7" + this.player.getName());
    ((GameProfile) GET_PROFILE.invoke(player)).getProperties().values().stream().filter(property -> property.getName().equals("textures")).findFirst()
        .ifPresent(skin -> entity.addTrait(new NPCSkinTrait(entity, skin.getValue(), skin.getSignature())));
    entity.spawn(randomLocation);
    NPC_LIST.add(entity);
    NPC_LIST.stream().map(m -> ((Player) m.getEntity())).forEach(a -> a.setSneaking(sneak));
    sneak = !sneak;
  }
}
