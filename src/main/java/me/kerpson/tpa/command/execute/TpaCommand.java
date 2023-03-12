package me.kerpson.tpa.command.execute;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import eu.okaeri.injector.annotation.Inject;
import me.kerpson.tpa.cache.audience.AudienceManager;
import me.kerpson.tpa.cache.tpa.TpaManager;
import me.kerpson.tpa.config.MessageConfiguration;
import me.kerpson.tpa.util.Placeholders;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

@Route(name = "tpa")
public class TpaCommand {

  private final TpaManager tpaManager;
  private final AudienceManager audienceManager;
  private final MessageConfiguration messageConfiguration;

  @Inject
  public TpaCommand(TpaManager tpaManager, AudienceManager audienceManager, MessageConfiguration messageConfiguration) {
    this.tpaManager = tpaManager;
    this.audienceManager = audienceManager;
    this.messageConfiguration = messageConfiguration;
  }

  @Execute(required = 1)
  public void defaultCommand(Player player, @Arg Player target) {
    Audience playerAudience = this.audienceManager.audience(player);
    if (this.tpaManager.hasRequest(target, player)) {
      playerAudience.sendMessage(this.messageConfiguration.getAlreadyTeleported());
      return;
    }

    this.tpaManager.putRequest(player, target);
    playerAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", target.getName())
            .applyToComponent(this.messageConfiguration.getTeleportRequestSend())
    );

    Audience targetAudience = this.audienceManager.audience(target);
    targetAudience.sendMessage(
        Placeholders.create()
            .with("{PLAYER}", player.getName())
            .applyToComponent(this.messageConfiguration.getTeleportRequestReceive())
    );
  }
}
