package me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.perkinsane;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.api.SWEvent;
import me.joaomanoel.d4rkk.dev.skywars.api.event.player.SWPlayerDeathEvent;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.Perk;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.PerkInsane;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blindado extends PerkInsane {
  
  private final int index;
  
  public Blindado(int index, String key) {
    super(102, key, CONFIG.getString(key + ".permission"), CONFIG.getString(key + ".name"), CONFIG.getString(key + ".icon"), new ArrayList<>());
    this.index = index;
    this.setupLevels(key);
    this.register();
  }
  
  @Override
  public long getIndex() {
    return this.index;
  }
  
  public void handleEvent(SWEvent evt2) {
    if (evt2 instanceof SWPlayerDeathEvent) {
      SWPlayerDeathEvent evt = (SWPlayerDeathEvent) evt2;
      if (evt.hasKiller()) {
        AbstractSkyWars game = (AbstractSkyWars) evt.getGame();
        Profile profile = evt.getKiller();
        
        Player player = profile.getPlayer();
        if (!game.isSpectator(player) && game.getMode().getCosmeticIndex() == this.getIndex() && this.isSelectedPerkInsane(profile) && this.has(profile) && this.canBuy(player)) {
          if (game.getKills(player) % 2 == 0) {
            ItemStack helmet = player.getInventory().getHelmet(), chestplate = player.getInventory().getChestplate(), leggings = player.getInventory().getLeggings(), boots =
                player.getInventory().getBoots();
            
            int maxLevel = this.getCurrentLevel(profile).getValue("limit", int.class, 1);
            boolean changed = false;
            if (helmet != null && helmet.getType() != Material.AIR) {
              int level = helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
              if (level < maxLevel) {
                helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level + 1);
                player.getInventory().setHelmet(helmet);
                changed = true;
              }
            }
            
            if (chestplate != null && chestplate.getType() != Material.AIR) {
              int level = chestplate.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
              if (level < maxLevel) {
                chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level + 1);
                player.getInventory().setChestplate(chestplate);
                changed = true;
              }
            }
            
            if (leggings != null && leggings.getType() != Material.AIR) {
              int level = leggings.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
              if (level < maxLevel) {
                leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level + 1);
                player.getInventory().setLeggings(leggings);
                changed = true;
              }
            }
            
            if (boots != null && boots.getType() != Material.AIR) {
              int level = boots.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
              if (level < maxLevel) {
                boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level + 1);
                player.getInventory().setBoots(boots);
                changed = true;
              }
            }
            
            if (changed) {
              player.updateInventory();
            }
          }
        }
      }
    }
  }
  
  @Override
  public List<Class<?>> getEventTypes() {
    return Collections.singletonList(SWPlayerDeathEvent.class);
  }
}
