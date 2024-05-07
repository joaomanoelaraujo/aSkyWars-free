package me.joaomanoel.d4rkk.dev.skywars.listeners.player;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InteractItemFrameEvent extends Event implements Cancellable {
   private final Player clicker;
   private final ItemFrame clicked;
   private final boolean placeMode;
   private static final HandlerList handlers = new HandlerList();
   private boolean c = false;

   public boolean getPlaceMode() {
      return this.placeMode;
   }

   public InteractItemFrameEvent(Player clicker, ItemFrame clicked, boolean placeMode) {
      this.clicked = clicked;
      this.clicker = clicker;
      this.placeMode = placeMode;
   }

   public Player getClicker() {
      return this.clicker;
   }

   public ItemFrame getClicked() {
      return this.clicked;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public boolean isCancelled() {
      return this.c;
   }

   public void setCancelled(boolean bool) {
      this.c = bool;
   }
}
