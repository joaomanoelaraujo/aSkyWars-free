package me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.winanimations;

import me.joaomanoel.d4rkk.dev.skywars.cosmetics.object.AbstractExecutor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.concurrent.ThreadLocalRandom;

public class FireworksExecutor extends AbstractExecutor {
  
  public FireworksExecutor(Player player) {
    super(player);
  }
  
  @Override
  public void tick() {
    Firework firework = (Firework) this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.FIREWORK);
    FireworkMeta meta = firework.getFireworkMeta();
    meta.addEffect(FireworkEffect.builder()
        .withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255)))
        .with(FireworkEffect.Type.values()[ThreadLocalRandom.current().nextInt(FireworkEffect.Type.values().length)])
        .build());
    meta.setPower(1);
    firework.setFireworkMeta(meta);
  }
}