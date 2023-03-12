package me.kerpson.tpa.command.execute;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import eu.okaeri.injector.annotation.Inject;
import java.util.Set;
import me.kerpson.tpa.cache.audience.AudienceManager;
import me.kerpson.tpa.cache.teleport.TeleportInjector;
import me.kerpson.tpa.cache.tpa.TpaManager;
import me.kerpson.tpa.config.MessageConfiguration;
import me.kerpson.tpa.util.Placeholders;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import panda.utilities.text.Joiner;

@Route(name = "tpaccept")
public class TpacceptCommand {

  private final TpaManager tpaManager;
  private final AudienceManager audienceManager;
  private final TeleportInjector teleportInjector;
  private final MessageConfiguration messageConfiguration;

  @Inject
  public TpacceptCommand(
      TpaManager tpaManager,
      AudienceManager audienceManager,
      TeleportInjector teleportInjector,
      MessageConfiguration messageConfiguration
  ) {
    this.tpaManager = tpaManager;
    this.audienceManager = audienceManager;
    this.teleportInjector = teleportInjector;
    this.messageConfiguration = messageConfiguration;
  }

  @Execute(required = 0)
  @Route(name = "all", aliases = "*")
  public void acceptAll(Player player) {
    Audience playerAudience = this.audienceManager.audience(player);
    Set<Player> requesters = this.tpaManager.getAllRequests(player);
    if (requesters.isEmpty()) {
      playerAudience.sendMessage(this.messageConfiguration.getTeleportRequestNotFound());
      return;
    }

    for (Player requester : requesters) {
      Audience requesterAudience = this.audienceManager.audience(requester);
      this.tpaManager.removeRequest(requester);
      this.teleportInjector.injectTeleport(requester, requesterAudience, player.getLocation());
      requesterAudience.sendMessage(
          Placeholders.create()
              .with("{PLAYER}", player.getName())
              .applyToComponent(this.messageConfiguration.getTeleportAcceptForRequester())
      );
    }

    playerAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", Joiner.on(", ").join(requesters, Player::getName))
            .applyToComponent(this.messageConfiguration.getTeleportAcceptedForTarget())
    );
  }

  @Execute(required = 1)
  public void defaultCommand(Player player, @Arg Player requester) {
    Audience playerAudience = this.audienceManager.audience(player);
    if (!this.tpaManager.hasRequest(requester, player)) {
      playerAudience.sendMessage(this.messageConfiguration.getTeleportRequestNotFound());;
      return;
    }

    Audience requesterAudience = this.audienceManager.audience(requester);
    this.tpaManager.removeRequest(requester);
    this.teleportInjector.injectTeleport(requester, requesterAudience, player.getLocation());

    playerAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", requester.getName())
            .applyToComponent(this.messageConfiguration.getTeleportAcceptedForTarget())
    );

    requesterAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", player.getName())
            .applyToComponent(this.messageConfiguration.getTeleportAcceptForRequester())
    );
  }
}
