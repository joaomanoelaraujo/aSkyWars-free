package me.joaomanoel.d4rkk.dev.skywars.lobby;

import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.servers.ServerItem;
import me.joaomanoel.d4rkk.dev.servers.ServerPing;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lobby {
  
  public static final KConfig CONFIG = Main.getInstance().getConfig("lobbies");
  public static final List<String> bonoro = new ArrayList<>();
  private static final List<Lobby> LOBBIES = new ArrayList<>();
  private final int slot;
  private final ServerPing serverPing;
  private final int maxPlayers;
  private final String icon;
  private final String serverName;
  
  public Lobby(int slot, String icon, int maxPlayers, String ip, String serverName) {
    this.slot = slot;
    this.icon = icon;
    this.serverPing = new ServerPing(new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1])));
    this.maxPlayers = maxPlayers;
    this.serverName = serverName;
  }
  
  public static void setupLobbies() {
    for (String key : CONFIG.getSection("items").getKeys(false)) {
      String servername = CONFIG.getString("items." + key + ".servername");
      
      LOBBIES.add(
          new Lobby(CONFIG.getInt("items." + key + ".slot"), CONFIG.getString("items." + key + ".icon"), CONFIG.getInt("items." + key + ".max-players"), servername.split(" ; ")[0],
              servername.split(" ; ")[1]));
    }
    
    List<Lobby> query = new ArrayList<>();
    for (Lobby lobby : LOBBIES) {
      if (!ServerItem.alreadyQuerying(lobby.getServerName())) {
        query.add(lobby);
      }
    }
    
    if (!query.isEmpty()) {
      new BukkitRunnable() {
        @Override
        public void run() {
          query.forEach(Lobby::fetch);
        }
      }.runTaskTimerAsynchronously(Main.getInstance(), 0, 40);
    }
  }
  
  public static Collection<Lobby> listLobbies() {
    return LOBBIES;
  }
  
  public void fetch() {
    this.serverPing.fetch();
    ServerItem.SERVER_COUNT.put(this.serverName, this.serverPing.getOnline());
  }
  
  public int getSlot() {
    return this.slot;
  }

  public String getIcon() {
    return this.icon;
  }

  public int getPlayers() {
    return this.serverName.equals(Main.currentServerName) ? Bukkit.getOnlinePlayers().size() : ServerItem.getServerCount(this.serverName);
  }
  
  public int getMaxPlayers() {
    return this.maxPlayers;
  }
  
  public String getServerName() {
    return this.serverName;
  }
}
