package me.joaomanoel.d4rkk.dev.skywars.menus.lobby;

import me.joaomanoel.d4rkk.dev.libraries.menu.PlayerMenu;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import me.joaomanoel.d4rkk.dev.skywars.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.Cosmetic;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.*;
import me.joaomanoel.d4rkk.dev.skywars.cosmetics.types.kits.NormalKit;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.MenuCosmetics;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.kits.MenuKits;
import me.joaomanoel.d4rkk.dev.skywars.menus.cosmetics.perks.MenuPerks;
import me.joaomanoel.d4rkk.dev.skywars.utils.sign.Sign;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MenuShopCosmetics extends PlayerMenu {

  public MenuShopCosmetics(Profile profile) {
    super(profile.getPlayer(), "My Cosmetics", 6);


    long max;
    long owned;
    long percentage;
    String color = "§a";


   StringBuilder sb5 = new StringBuilder();
    Optional<ProjectileEffect> selectedCosmetic5 = Cosmetic.listByType(ProjectileEffect.class)
            .stream()
            .map(f -> profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(f.getType(), ProjectileEffect.class))
            .filter(Objects::nonNull)
            .findFirst();
    selectedCosmetic5.ifPresent(cosmetic -> sb5.append("\n").append(" &a").append(cosmetic.getName()));

      StringBuilder sb4 = new StringBuilder();
      Optional<Cage> selectedCosmetic4 = Cosmetic.listByType(Cage.class)
              .stream()
              .map(f -> profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(f.getType(), Cage.class))
              .filter(Objects::nonNull)
              .findFirst();
      selectedCosmetic4.ifPresent(cosmetic -> sb4.append("\n").append(" &a").append(cosmetic.getName()));

      StringBuilder sb3 = new StringBuilder();
      Optional<WinAnimation> selectedCosmetic3 = Cosmetic.listByType(WinAnimation.class)
              .stream()
              .map(f -> profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(f.getType(), WinAnimation.class))
              .filter(Objects::nonNull)
              .findFirst();
      selectedCosmetic3.ifPresent(cosmetic -> sb3.append("\n").append(" &a").append(cosmetic.getName()));

      StringBuilder sb2 = new StringBuilder();
      Optional<KillEffect> selectedCosmetic2 = Cosmetic.listByType(KillEffect.class)
              .stream()
              .map(f -> profile.getAbstractContainer("aCoreSkyWars", "selected", SelectedContainer.class).getSelected(f.getType(), KillEffect.class))
              .filter(Objects::nonNull)
              .findFirst();
      selectedCosmetic2.ifPresent(cosmetic -> sb2.append("\n").append(" &a").append(cosmetic.getName()));


      List<ProjectileEffect> projectileeffects = Cosmetic.listByType(ProjectileEffect.class);
    max = projectileeffects.size();
    owned = projectileeffects.stream().filter(kprojectileEffect -> kprojectileEffect.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    projectileeffects.clear();
    this.setItem(10, BukkitUtils.deserializeItemStack(
            "ARROW : 1 : name>&aProjectile Trails : desc>&7Change your projectile particle trail\n§7effect. \n\n&7Unlocked: " + color + owned + "/" + max + " &8(" + percentage + "%)\n§7Currently Selected: " +  (sb5.toString().isEmpty() ? "\n &aNone" : sb5.toString()) + " \n\n&eClick to view!"));

      List<Cage> cages = Cosmetic.listByType(Cage.class);
      max = cages.size();
      owned = cages.stream().filter(cage -> cage.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      cages.clear();
      this.setItem(12, BukkitUtils.deserializeItemStack(
              "95 : 1 : name>&aCages : desc>&7Change the color of your spawning\n§7cell.\n\n&7Unlocked: " + color + owned + "/" + max + " &8(" + percentage + "%)\n§7Currently Selected: " +  (sb4.toString().isEmpty() ? "\n &aNone" : sb4.toString()) + " \n\n&eClick to view!"));

      List<WinAnimation> animations = Cosmetic.listByType(WinAnimation.class);
      max = animations.size();
      owned = animations.stream().filter(animation -> animation.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      animations.clear();
      this.setItem(14, BukkitUtils.deserializeItemStack(
              "SKULL_ITEM:3 : 1 : name>&aVictory Dances : desc>&7Celebrate by gloating and showing\n&7of to other players whenever you\n§7win!\n \n&7Unlocked: " + color + owned + "/" + max + " &8(" + percentage + "%)\n§7Currently Selected: " + (sb3.toString().isEmpty() ? "\n &aNone" : sb3.toString()) + "\n \n&eClick to view! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q1NmY4ODc4YjkyMmJjN2E0MDE4YWNlNTdiNTMyMGI3MDQ2NWI1YWM2MDBmMTU1ODFiNjE2MWQyY2MwOGE3YiJ9fX0="));


      List<KillEffect> killeffects = Cosmetic.listByType(KillEffect.class);
      max = killeffects.size();
      owned = killeffects.stream().filter(killEffect -> killEffect.has(profile)).count();
      percentage = max == 0 ? 100 : (owned * 100) / max;
      killeffects.clear();
      this.setItem(16, BukkitUtils.deserializeItemStack(
              "BONE : 1 : name>&aKill Effects : desc>&7Pick an effect to trigger whenever\n§7you eliminate another player! \n\n&7Unlocked: " + color + owned + "/" + max + " &8(" + percentage + "%)\n§7Currently Selected: " +  (sb2.toString().isEmpty() ? "\n &aNone" : sb2.toString()) + " \n\n&eClick to view!"));


      this.setItem(50, BukkitUtils.deserializeItemStack(
              "345 : 1 : name>&aSearch : desc>&7Use this feature to easily find a\n§7specifc cosmetic item."));
      this.setItem(48, BukkitUtils.deserializeItemStack("ARROW : 1 : name>§aGo Back : desc>§7To SkyWars Shop"));
      this.setItem(49, BukkitUtils.deserializeItemStack("EMERALD : 1 : name>§7Total Coins: §6" + profile.getCoins("aCoreSkyWars") + " : desc>§6" + Language.skywars$shop));


    this.register(Main.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          if (item != null && item.getType() != Material.AIR) {
           if (evt.getSlot() == 50) {
                EnumSound.CLICK.play(this.player,0.5F,2.0F);
                Sign.openSign(player, new String[]{"","^^^^^^","Enter you","search query"});
            } else if (evt.getSlot() == 16) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuCosmetics<>(profile, "Kill Effects", KillEffect.class);
            } else if (evt.getSlot() == 12) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuCosmetics<>(profile, "Cages", Cage.class);
            } else if (evt.getSlot() == 14) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuCosmetics<>(profile, "Victory Dances", WinAnimation.class);
            } else if (evt.getSlot() == 10) {
                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                new MenuCosmetics<>(profile, "Projectile Trails", ProjectileEffect.class);
            } else if (evt.getSlot() == 48) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShop(profile);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
