package me.kerpson.tpa.cache.audience;

import me.kerpson.tpa.TpaPlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;

public class AudienceManager {

  private BukkitAudiences audiences;

  public AudienceManager(TpaPlugin plugin) {
    this.audiences = BukkitAudiences.create(plugin);
  }

  public Audience audience(Player player) {
    return audiences.player(player);
  }

  public void close() {
    if (this.audiences == null) {
      return;
    }

    this.audiences.close();
    this.audiences = null;
  }

}
