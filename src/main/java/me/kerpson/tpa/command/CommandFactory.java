package me.kerpson.tpa.command;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.schematic.Schematic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.kerpson.tpa.TpaPlugin;
import me.kerpson.tpa.cache.audience.AudienceManager;
import me.kerpson.tpa.command.argument.PlayerArgument;
import me.kerpson.tpa.config.MessageConfiguration;
import me.kerpson.tpa.util.ChatUtil;
import me.kerpson.tpa.util.Placeholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Joiner;

public class CommandFactory {

  private final Server server;
  private final AudienceManager audienceManager;
  private final MessageConfiguration messageConfiguration;
  private final LiteCommandsBuilder<CommandSender> liteCommands;

  public CommandFactory(Server server, AudienceManager audienceManager, MessageConfiguration messageConfiguration) {
    this.server = server;
    this.audienceManager = audienceManager;
    this.messageConfiguration = messageConfiguration;
    this.liteCommands = LiteBukkitFactory.builder(server, "crazycast-tpa-plugin");

    this.registerMessages();
    this.registerArguments();
    this.registerContextualBind();
  }

  private void registerMessages() {
    this.liteCommands.permissionHandler((commandSender, liteInvocation, requiredPermissions) -> {
      if (!(commandSender instanceof Player)) {
        return;
      }

      Player player = (Player) commandSender;
      Audience audience = this.audienceManager.audience(player);
      audience.sendMessage(
          Placeholders.create()
              .with("{PERMISSION}", Joiner.on(", ").join(requiredPermissions.getPermissions()))
              .applyToComponent(this.messageConfiguration.getNoPermissionMessage())
      );
    });

    this.liteCommands.invalidUsageHandler((commandSender, liteInvocation, schematic) -> {
      if (!(commandSender instanceof Player)) {
        return;
      }

      Player player = (Player) commandSender;
      Audience audience = this.audienceManager.audience(player);
      List<String> schematics = schematic.getSchematics();

      if (schematics.size() == 1) {
        audience.sendMessage(
            Placeholders.create()
                .with("{USAGE}", schematics.get(0))
                .applyToComponent(this.messageConfiguration.getIncorrectUsage())
        );

        return;
      }

      List<Component> components = new ArrayList<>(
          Collections.singletonList(this.messageConfiguration.getIncorrectSchemeUsage())
      );

      for (String usage : schematics) {
        components.add(Placeholders.create()
            .with("{USAGE}", usage)
            .applyToComponent(this.messageConfiguration.getSchemeUsage()));
      }

      components.forEach(audience::sendMessage);
    });
  }

  private void registerArguments() {
    this.liteCommands.argument(Player.class, new PlayerArgument(this.server));
  }

  private void registerContextualBind() {
    this.liteCommands.contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("Command is only for players!"));
  }

  public CommandFactory registerCommand(Object... classes) {
    this.liteCommands.commandInstance(classes);
    return this;
  }

  public CommandFactory build() {
    this.liteCommands.register();
    return this;
  }
}
