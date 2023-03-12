package me.kerpson.tpa.config;

import com.google.common.collect.ImmutableMap;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import me.kerpson.tpa.util.ChatUtil;
import net.kyori.adventure.text.Component;

@Header("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
@Header("~       CrazyCast.EU         ~")
@Header("~      TeleportPlugin        ~")
@Header("~        By: kerpson         ~")
@Header("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
@Names(modifier = NameModifier.TO_LOWER_CASE, strategy = NameStrategy.SNAKE_CASE)
@Getter
public class PluginConfiguration extends OkaeriConfig {

  @Comment({
      "PL: Podstawowy czas teleportacji podany w sekundach",
      "EN: Basic teleportation time given in seconds"
  })
  @CustomKey("default-teleport-time")
  private int defaultTeleportTime = 10;

  @Comment({
      "PL: Czas teleportacji dla danych uprawnień w sekundach",
      "EN: Teleportation time for given permissions in seconds"
  })
  @CustomKey("permission-teleport-time")
  private Map<String, Integer> permissionTeleportTime = ImmutableMap.of(
      "crazycast.teleport.vip", 10,
      "crazycast.teleport.admin", 3
  );

  @Comment({
      "PL: Konfiguracja title przy teleportacji",
      "EN: Title configuration when teleporting"
  })
  @CustomKey("title-teleport-configuration")
  private TitleTeleportConfiguration titleTeleportConfiguration = new TitleTeleportConfiguration();

  @Getter
  public static class TitleTeleportConfiguration extends OkaeriConfig {

    @Comment({
        "PL: Title dla udanej teleportacji",
        "EN: Title for a successful teleport"
    })
    @CustomKey("teleport-success-title")
    private Component teleportSuccessTitle = Component.empty();

    @CustomKey("teleport-success-sub-title")
    private Component teleportSuccessSubTitle = ChatUtil.component("&aTeleport success!");

    @Comment({
        "PL: Title dla nieudanej teleportacji",
        "EN: Title for failed teleportation"
    })
    @CustomKey("teleport-failed-title")
    private Component teleportFailedTitle = Component.empty();

    @CustomKey("teleport-failed-sub-title")
    private Component teleportFailedSubTitle = ChatUtil.component("&cTeleport failed!");

    @Comment({
        "PL: Title dla odliczania czasu teleportacji",
        "EN: Title for teleportation countdown"
    })
    @CustomKey("teleport-countdown-title")
    private Component teleportCountdownTitle = Component.empty();

    @CustomKey("teleport-countdown-sub-title")
    private Component teleportCountdownSubTitle = ChatUtil.component("&aThe teleportation will follow {TIME}");
  }
}
