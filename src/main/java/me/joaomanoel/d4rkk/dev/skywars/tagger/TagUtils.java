package me.joaomanoel.d4rkk.dev.skywars.tagger;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import me.joaomanoel.d4rkk.dev.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

@SuppressWarnings("deprecation")
public class TagUtils {
  
  private static final Map<String, FakeTeam> TEAMS = new HashMap<>();
  private static final Map<String, FakeTeam> CACHED_FAKE_TEAMS = new HashMap<>();
  
  // - API
  
  public static void setTag(Player player) {
    Role role = Role.getPlayerRole(player);
    String suffix = "";
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      DataContainer container = profile.getDataContainer("aCoreProfile", "clan");
      if (container != null) {
        suffix = " " + container.getAsString().replace(" ", "");
        if (suffix.contains("§8")) {
          suffix = "";
        }
      }
    }
    
    setTag(player.getName(), role.getPrefix(), suffix, role.getId());
  }
  
  public static void setTag(String player, String prefix, String suffix, int sortPriority) {
    addPlayerToTeam(player, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
  }
  
  public static void sendTeams(Player player) {
    for (FakeTeam fakeTeam : TEAMS.values()) {
      new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers()).send(player);
    }
  }
  
  public static void reset() {
    for (FakeTeam fakeTeam : TEAMS.values()) {
      removePlayerFromTeamPackets(fakeTeam, fakeTeam.getMembers());
      removeTeamPackets(fakeTeam);
    }
    
    CACHED_FAKE_TEAMS.clear();
    TEAMS.clear();
  }
  
  // - Utility
  
  public static FakeTeam reset(String player) {
    return reset(player, decache(player));
  }
  
  private static FakeTeam decache(String player) {
    return CACHED_FAKE_TEAMS.remove(player);
  }
  
  public static FakeTeam getFakeTeam(String player) {
    return CACHED_FAKE_TEAMS.get(player);
  }
  
  private static void cache(String player, FakeTeam fakeTeam) {
    CACHED_FAKE_TEAMS.put(player, fakeTeam);
  }
  
  private static FakeTeam reset(String player, FakeTeam fakeTeam) {
    if (fakeTeam != null && fakeTeam.getMembers().remove(player)) {
      boolean delete;
      Player removing = Bukkit.getPlayerExact(player);
      if (removing != null) {
        delete = removePlayerFromTeamPackets(fakeTeam, removing.getName());
      } else {
        OfflinePlayer toRemoveOffline = Bukkit.getOfflinePlayer(player);
        delete = removePlayerFromTeamPackets(fakeTeam, toRemoveOffline.getName());
      }
      
      if (delete) {
        removeTeamPackets(fakeTeam);
        TEAMS.remove(fakeTeam.getName());
      }
    }
    
    return fakeTeam;
  }
  
  private static void addPlayerToTeam(String player, String prefix, String suffix, int sortPriority) {
    reset(player);
    FakeTeam joining = getTeam(prefix, suffix);
    if (joining != null) {
      joining.addMember(player);
    } else {
      joining = new FakeTeam(prefix, suffix, getNameFromInput(sortPriority));
      joining.addMember(player);
      TEAMS.put(joining.getName(), joining);
      addTeamPackets(joining);
    }
    
    Player adding = Bukkit.getPlayerExact(player);
    if (adding != null) {
      addPlayerToTeamPackets(joining, adding.getName());
      cache(adding.getName(), joining);
    } else {
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
      addPlayerToTeamPackets(joining, offlinePlayer.getName());
      cache(offlinePlayer.getName(), joining);
    }
  }
  
  private static FakeTeam getTeam(String prefix, String suffix) {
    for (FakeTeam team : TEAMS.values()) {
      if (team.isSimilar(prefix, suffix)) {
        return team;
      }
    }
    
    return null;
  }
  
  private static String getNameFromInput(int input) {
    if (input < 0) {
      return "";
    }
    
    return String.valueOf((char) ((input) + 65));
  }
  
  // -- Packets
  
  private static void removeTeamPackets(FakeTeam fakeTeam) {
    new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 1, new ArrayList<>()).send();
  }
  
  private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, String... players) {
    return removePlayerFromTeamPackets(fakeTeam, Arrays.asList(players));
  }
  
  private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, List<String> players) {
    new Wrapper(fakeTeam.getName(), 4, players).send();
    fakeTeam.getMembers().removeAll(players);
    return fakeTeam.getMembers().isEmpty();
  }
  
  private static void addTeamPackets(FakeTeam fakeTeam) {
    new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers()).send();
  }
  
  private static void addPlayerToTeamPackets(FakeTeam fakeTeam, String player) {
    new Wrapper(fakeTeam.getName(), 3, Collections.singletonList(player)).send();
  }
}
