package me.kerpson.tpa.cache.teleport;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import me.kerpson.tpa.TpaPlugin;
import me.kerpson.tpa.config.PluginConfiguration;
import me.kerpson.tpa.config.PluginConfiguration.TitleTeleportConfiguration;
import me.kerpson.tpa.util.LocationUtil;
import me.kerpson.tpa.util.Placeholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class TeleportInjector {

  private final TpaPlugin plugin;
  private final Map<UUID, BukkitTask> teleportMap;
  private final PluginConfiguration pluginConfiguration;

  public TeleportInjector(TpaPlugin plugin, PluginConfiguration pluginConfiguration) {
    this.plugin = plugin;
    this.teleportMap = new HashMap<>();
    this.pluginConfiguration = pluginConfiguration;
  }

  public void injectTeleport(Player player, Audience audience, Location target) {
    TitleTeleportConfiguration titleTeleportConfiguration = this.pluginConfiguration.getTitleTeleportConfiguration();
    UUID uuid = player.getUniqueId();
    int seconds = this.pluginConfiguration.getPermissionTeleportTime()
        .entrySet()
        .stream()
        .filter(entry -> player.hasPermission(entry.getKey()))
        .mapToInt(Entry::getValue)
        .min()
        .orElse(this.pluginConfiguration.getDefaultTeleportTime());

    if (this.teleportMap.containsKey(uuid)) {
      this.remove(uuid);
    }

    if (seconds == 0) {
      Title title = Title.title(
          titleTeleportConfiguration.getTeleportSuccessTitle(),
          titleTeleportConfiguration.getTeleportSuccessSubTitle(),
          Title.Times.times(
              Duration.ofMillis(250L),
              Duration.ofMillis(500L),
              Duration.ofMillis(250L))
      );

      audience.showTitle(title);
      player.teleport(target);
      this.remove(uuid);

      return;
    }

    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (seconds * 20), 2));

    Location startTeleportLocation = player.getLocation();
    long requiredTime = System.currentTimeMillis() + (seconds * 1000L);
    this.teleportMap.put(uuid, Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
      if (!player.isOnline()) {
        this.remove(uuid);
        return;
      }

      Location currentLocation = player.getLocation();
      if (!LocationUtil.equals(currentLocation, startTeleportLocation)) {
        player.removePotionEffect(PotionEffectType.CONFUSION);
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        Title title = Title.title(
            titleTeleportConfiguration.getTeleportFailedTitle(),
            titleTeleportConfiguration.getTeleportFailedSubTitle()
        );

        audience.showTitle(title);
        this.remove(uuid);

        return;
      }

      if (requiredTime > System.currentTimeMillis()) {
        Placeholders placeholders = Placeholders.create()
            .with("{TIME}", (requiredTime - System.currentTimeMillis() < 1000L ? (requiredTime - System.currentTimeMillis() + "ms") : ((int) (requiredTime - System.currentTimeMillis()) / 1000L)));
        Title title = Title.title(
            placeholders.applyToComponent(titleTeleportConfiguration.getTeleportCountdownTitle()),
            placeholders.applyToComponent(titleTeleportConfiguration.getTeleportCountdownSubTitle()),
            Title.Times.times(
                Duration.ofMillis(250),
                Duration.ofMillis(500),
                Duration.ofMillis(250)
            )
        );

        audience.showTitle(title);
        return;
      }

      Title title = Title.title(
          titleTeleportConfiguration.getTeleportSuccessTitle(),
          titleTeleportConfiguration.getTeleportSuccessSubTitle()
      );

      audience.showTitle(title);
      player.teleport(target);
      this.remove(uuid);

    }, 0L, 20L));
  }

  private void remove(UUID uuid) {
    BukkitTask task = this.teleportMap.remove(uuid);
    if (task == null) {
      return;
    }

    task.cancel();
  }
}
