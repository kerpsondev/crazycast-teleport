package me.kerpson.tpa.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationUtil {

  private LocationUtil() {}

  public static boolean equals(Location location1, Location location2) {
    if (location1.getBlockX() != location2.getBlockX()) {
      return false;
    }

    if (location1.getBlockY() != location2.getBlockY()) {
      return false;
    }

    return location1.getBlockZ() == location2.getBlockZ();
  }
}
