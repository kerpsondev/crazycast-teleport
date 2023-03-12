package me.kerpson.tpa.command.execute;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import eu.okaeri.injector.annotation.Inject;
import java.util.Set;
import me.kerpson.tpa.cache.audience.AudienceManager;
import me.kerpson.tpa.cache.tpa.TpaManager;
import me.kerpson.tpa.config.MessageConfiguration;
import me.kerpson.tpa.util.Placeholders;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import panda.utilities.text.Joiner;

@Route(name = "tpdeny")
public class TpdenyCommand {

  private final TpaManager tpaManager;
  private final AudienceManager audienceManager;
  private final MessageConfiguration messageConfiguration;

  @Inject
  public TpdenyCommand(TpaManager tpaManager, AudienceManager audienceManager, MessageConfiguration messageConfiguration) {
    this.tpaManager = tpaManager;
    this.audienceManager = audienceManager;
    this.messageConfiguration = messageConfiguration;
  }

  @Execute(required = 0)
  @Route(name = "all", aliases = "*")
  public void denyAll(Player player) {
    Audience playerAudience = this.audienceManager.audience(player);
    Set<Player> requesters = this.tpaManager.getAllRequests(player);
    if (requesters.isEmpty()) {
      playerAudience.sendMessage(this.messageConfiguration.getTeleportRequestNotFound());
      return;
    }

    for (Player requester : requesters) {
      Audience requesterAudience = this.audienceManager.audience(requester);
      this.tpaManager.removeRequest(requester);
      requesterAudience.sendMessage(
          Placeholders.create()
              .with("{PLAYER}", player.getName())
              .applyToComponent(this.messageConfiguration.getTeleportDenyForRequester())
      );
    }

    playerAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", Joiner.on(", ").join(requesters, Player::getName))
            .applyToComponent(this.messageConfiguration.getTeleportDenyForTarget())
    );
  }

  @Execute(required = 1)
  public void defaultCommand(Player player, @Arg Player requester) {
    Audience playerAudience = this.audienceManager.audience(player);
    if (!this.tpaManager.hasRequest(requester, player)) {
      playerAudience.sendMessage(this.messageConfiguration.getTeleportRequestNotFound());
      return;
    }

    Audience requesterAudience = this.audienceManager.audience(requester);
    this.tpaManager.removeRequest(requester);

    playerAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", requester.getName())
            .applyToComponent(this.messageConfiguration.getTeleportDenyForTarget())
    );

    requesterAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", player.getName())
            .applyToComponent(this.messageConfiguration.getTeleportDenyForRequester())
    );
  }
}
