package me.kerpson.tpa.cache.tpa;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpaManager {

  private final Cache<UUID, UUID> teleports = Caffeine.newBuilder()
      .expireAfterWrite(60, TimeUnit.SECONDS)
      .build();

  public void putRequest(Player requester, Player target) {
    this.teleports.put(requester.getUniqueId(), target.getUniqueId());
  }

  public boolean hasRequest(Player requester, Player target) {
    UUID uuidRequester = requester.getUniqueId();
    UUID uuidTarget = target.getUniqueId();
    Map<UUID, UUID> map = this.teleports.asMap();
    if (!map.containsKey(uuidRequester)) {
      return false;
    }

    return map.get(uuidRequester).equals(uuidTarget);
  }

  public Set<Player> getAllRequests(Player target) {
    return teleports.asMap().entrySet()
        .stream()
        .filter(entry -> {
          UUID uuidTarget = entry.getValue();
          return uuidTarget.equals(target.getUniqueId());
        })
        .map(Map.Entry::getKey)
        .map(Bukkit::getPlayer)
        .collect(Collectors.toSet());
  }

  public void removeRequest(Player request) {
    this.teleports.invalidate(request.getUniqueId());
  }
}
