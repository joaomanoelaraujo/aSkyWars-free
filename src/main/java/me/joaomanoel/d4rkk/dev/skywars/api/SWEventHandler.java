package me.joaomanoel.d4rkk.dev.skywars.api;

import java.util.List;

public interface SWEventHandler {
  
  void handleEvent(SWEvent evt);
  
  List<Class<?>> getEventTypes();
}
