package me.joaomanoel.d4rkk.dev.skywars.listeners.entity;

import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.game.GameTeam;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.enums.BloodAndGore;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.game.AbstractSkyWars;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EntityListener implements Listener {
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    if (evt.getEntity() instanceof Player) {
      Player player = (Player) evt.getEntity();
      
      AbstractSkyWars game;
      Profile profile = Profile.getProfile(player.getName());
      if (profile == null || (game = profile.getGame(AbstractSkyWars.class)) == null || game.getState() != GameState.EMJOGO || game.isSpectator(player)) {
        evt.setCancelled(true);
      } else {
        GameTeam team = game.getTeam(player);
        
        Player damager = null;
        Profile profile2;
        if (evt.getDamager() instanceof Player) {
          damager = (Player) evt.getDamager();
          profile2 = Profile.getProfile(damager.getName());
          if (profile2 == null || profile2.getGame() == null || !profile2.getGame().equals(game) || game.isSpectator(damager) || damager.equals(player) || (team != null && team
              .hasMember(damager))) {
            evt.setCancelled(true);
          } else {
            if (profile.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
              player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
            if (profile2.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
              damager.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
          }
        }
        
        if (evt.getDamager() instanceof Projectile) {
          Projectile proj = (Projectile) evt.getDamager();
          if (proj.getShooter() instanceof Player) {
            damager = (Player) proj.getShooter();
            profile2 = Profile.getProfile(damager.getName());
            if (profile2 == null || profile2.getGame() == null || !profile2.getGame().equals(game) || game.isSpectator(damager) || damager.equals(player) || (team != null && team
                .hasMember(damager))) {
              evt.setCancelled(true);
            } else {
              if (profile.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
                player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
              }
              if (profile2.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
                damager.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
              }
              
              if (proj instanceof Arrow) {
                Player finalDamager = damager;
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> finalDamager
                        .sendMessage(Language.ingame$messages$bow$hit.replace("{name}", Role.getColored(player.getName())).replace("{hp}", StringUtils.formatNumber(player.getHealth()))),
                    5L);
              }
            }
          }
        }
        
        if (!evt.isCancelled() && damager != null) {
          profile.setHit(damager.getName());
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityDamage(EntityDamageEvent evt) {
    if (evt.getEntity() instanceof Player) {
      Player player = (Player) evt.getEntity();
      
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
        if (game == null) {
          evt.setCancelled(true);
        } else {
          if (game.getState() != GameState.EMJOGO) {
            evt.setCancelled(true);
          } else if (game.isSpectator(player)) {
            evt.setCancelled(true);
          } else if (player.getNoDamageTicks() > 0 && evt.getCause() == DamageCause.FALL) {
            evt.setCancelled(true);
          }
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCreatureSpawn(CreatureSpawnEvent evt) {
    evt.setCancelled(evt.getSpawnReason() != SpawnReason.CUSTOM);
  }
  
  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent evt) {
    evt.setCancelled(true);
    if (evt.getEntity() instanceof Player) {
      Profile profile = Profile.getProfile(evt.getEntity().getName());
      if (profile != null) {
        AbstractSkyWars game = profile.getGame(AbstractSkyWars.class);
        if (game != null) {
          evt.setCancelled(game.getState() != GameState.EMJOGO || game.isSpectator((Player) evt.getEntity()));
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onFoodLevelChangeMonitor(FoodLevelChangeEvent evt) {
    if (!evt.isCancelled() && evt.getEntity() instanceof Player) {
      ((Player) evt.getEntity()).setSaturation(5.0f);
    }
  }
}