package me.kerpson.tpa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyCastLogger {

  private final Logger logger;

  public CrazyCastLogger(JavaPlugin plugin) {
    this.logger = plugin.getLogger();
  }

  public void info(String message) {
    this.logger.info(message);
  }

  public void warn(String message) {
    this.logger.warning(message);
  }

  public void error(String message) {
    this.logger.severe(message);
  }

  public void error(String message, Throwable cause) {
    this.error("");
    this.error(message);
    this.error("");

    StringWriter error = new StringWriter();
    cause.printStackTrace(new PrintWriter(error));

    Arrays.stream(error.toString().split("\n")).forEach(this::error);
  }
}
