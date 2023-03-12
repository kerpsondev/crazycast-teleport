package me.kerpson.tpa.command.argument;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

@ArgumentName("player")
public class PlayerArgument implements OneArgument<Player> {

  private final Server server;

  public PlayerArgument(Server server) {
    this.server = server;
  }

  @Override
  public Result<Player, ?> parse(LiteInvocation liteInvocation, String s) {
    return Option.of(this.server.getPlayer(s)).toResult("&8[&&8]");
  }

  @Override
  public List<Suggestion> suggest(LiteInvocation invocation) {
    return this.server.getOnlinePlayers()
        .stream()
        .map(Player::getName)
        .map(Suggestion::of)
        .collect(Collectors.toList());
  }
}
