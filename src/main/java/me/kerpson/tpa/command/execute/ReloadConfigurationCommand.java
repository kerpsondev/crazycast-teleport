package me.kerpson.tpa.command.execute;

import dev.rollczi.litecommands.command.async.Async;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import eu.okaeri.injector.annotation.Inject;
import me.kerpson.tpa.cache.audience.AudienceManager;
import me.kerpson.tpa.config.MessageConfiguration;
import me.kerpson.tpa.config.PluginConfiguration;
import me.kerpson.tpa.util.ChatUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Route(name = "tpareload")
public class ReloadConfigurationCommand {

  private final AudienceManager audienceManager;
  private final PluginConfiguration pluginConfiguration;
  private final MessageConfiguration messageConfiguration;

  @Inject
  public ReloadConfigurationCommand(
      AudienceManager audienceManager,
      PluginConfiguration pluginConfiguration,
      MessageConfiguration messageConfiguration
  ) {
    this.audienceManager = audienceManager;
    this.pluginConfiguration = pluginConfiguration;
    this.messageConfiguration = messageConfiguration;
  }

  @Async
  @Execute
  @Permission("crazycast.command.reload")
  public void reloadCommand(Player player) {
    this.pluginConfiguration.load();
    this.messageConfiguration.load();

    Audience audience = this.audienceManager.audience(player);
    audience.sendMessage(this.messageConfiguration.getConfigurationReloadCommand());
  }
}
