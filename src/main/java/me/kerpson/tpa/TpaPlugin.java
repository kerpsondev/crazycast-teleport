package me.kerpson.tpa;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.serdes.standard.StandardSerdes;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import java.io.File;
import me.kerpson.tpa.cache.audience.AudienceManager;
import me.kerpson.tpa.cache.teleport.TeleportInjector;
import me.kerpson.tpa.cache.tpa.TpaManager;
import me.kerpson.tpa.command.CommandFactory;
import me.kerpson.tpa.command.execute.ReloadConfigurationCommand;
import me.kerpson.tpa.command.execute.TpaCommand;
import me.kerpson.tpa.command.execute.TpacceptCommand;
import me.kerpson.tpa.command.execute.TpdenyCommand;
import me.kerpson.tpa.config.MessageConfiguration;
import me.kerpson.tpa.config.PluginConfiguration;
import me.kerpson.tpa.config.transformer.ComponentTransformer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(
    name = "crazycast-tpa-plugin",
    version = "1.0"
)
@Author("kerpson")
@ApiVersion(Target.v1_13)
@Website("www.crazycast.eu")
public class TpaPlugin extends JavaPlugin {

  private final File configFile = new File(this.getDataFolder(), "config.yml");
  private final File messageFile = new File(this.getDataFolder(), "messages.yml");

  private AudienceManager audienceManager;

  @Override
  public void onEnable() {
    CrazyCastLogger crazyCastLogger = new CrazyCastLogger(this);
    this.audienceManager = new AudienceManager(this);

    MessageConfiguration messageConfiguration;
    PluginConfiguration pluginConfiguration;

    try {
      pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
        it.withBindFile(this.configFile);
        it.withConfigurer(new YamlBukkitConfigurer(), new StandardSerdes());
        it.withSerdesPack(registry -> registry.register(new ComponentTransformer()));
        it.withRemoveOrphans(true);
        it.saveDefaults();
        it.load(true);
      });

      messageConfiguration = ConfigManager.create(MessageConfiguration.class, (it) -> {
        it.withBindFile(this.messageFile);
        it.withConfigurer(new YamlBukkitConfigurer(), new StandardSerdes());
        it.withSerdesPack(registry -> registry.register(new ComponentTransformer()));
        it.withRemoveOrphans(true);
        it.saveDefaults();
        it.load(true);
      });
    } catch (OkaeriException exception) {
      crazyCastLogger.error("Error while initialize configuration!", exception);
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    TpaManager tpaManager = new TpaManager();
    TeleportInjector teleportInjector = new TeleportInjector(this, pluginConfiguration);

    Injector injector = OkaeriInjector.create()
        .registerInjectable(this)
        .registerInjectable(tpaManager)
        .registerInjectable(teleportInjector)
        .registerInjectable(this.audienceManager)
        .registerInjectable(pluginConfiguration)
        .registerInjectable(messageConfiguration);

    new CommandFactory(
        this.getServer(),
        this.audienceManager,
        messageConfiguration
    ).registerCommand(
        injector.registerInjectable(ReloadConfigurationCommand.class),
        injector.createInstance(TpacceptCommand.class),
        injector.createInstance(TpaCommand.class),
        injector.createInstance(TpdenyCommand.class)
    ).build();
  }

  @Override
  public void onDisable() {
    this.audienceManager.close();
  }
}
