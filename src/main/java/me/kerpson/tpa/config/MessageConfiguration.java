package me.kerpson.tpa.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
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
public class MessageConfiguration extends OkaeriConfig {

  @Comment({
      "PL: Wiadomość o braku uprawnień",
      "EN: No permission message"
  })
  @CustomKey("no-permission-message")
  private Component noPermissionMessage = ChatUtil.component("&cYou don't have permission &8(&6{PERMISSION}&8)");

  @Comment({
      "PL: Niepoprawne użycie komendy",
      "EN: Incorrect use of command",}
  )
  @CustomKey("incorrect-usage")
  private Component incorrectUsage = ChatUtil.component("&8[&d&lCC&8] &7Correct usage: &f{USAGE}");

  @Comment({
      "PL: Niepoprawne użycie dla schematu komend",
      "EN: Incorrect usage for command scheme"}
  )
  @CustomKey("incorrect-scheme-usage")
  private Component incorrectSchemeUsage = ChatUtil.component("&8[&d&lCC&8] &7Correct usages:");

  @Comment({
      "PL: Wygląd użycia komendy dla schematu",
      "EN: Appearance of command usage for the scheme"}
  )
  @CustomKey("scheme-usage")
  private Component schemeUsage = ChatUtil.component(" &8- &f{USAGE}");

  @Comment({
      "PL: Wiadomość gdy już teleportujesz się już do danego użytkownika",
      "EN: Message once you have already teleported to the user in question"}
  )
  @CustomKey("already-teleported")
  private Component alreadyTeleported = ChatUtil.component("&cYou are already teleporting to this player!");

  @Comment({
      "PL: Wiadomość gdy już teleportujesz się już do danego użytkownika",
      "EN: Message when you send a teleportation request"}
  )
  @CustomKey("teleport-request-send")
  private Component teleportRequestSend = ChatUtil.component("&8[&d&lCC&8] &7You sent a teleportation request to a player &d{PLAYER}");

  @Comment({
      "PL: Wiadomość gdy osoba wysłała do Ciebie prośbę o teleportacje",
      "EN: Message when a person has sent you a teleportation request"}
  )
  @CustomKey("teleport-request-receive")
  private Component teleportRequestReceive = ChatUtil.component("&8[&d&lCC&8] &7Player &d{PLAYER} &7wants to teleport to you!");

  @Comment({
      "PL: Wiadomość gdy ta osoba się do Ciebie nie teleportowała",
      "EN: Message when that person has not teleported to you"}
  )
  @CustomKey("teleport-request-not-found")
  private Component teleportRequestNotFound = ChatUtil.component("&8[&d&lCC&8] &7This player did not teleport to you");

  @Comment({
      "PL: Wiadomość gdy gracz zaakceptuje prosbę o teleportacje",
      "EN: Message when player accepts teleportation request"}
  )
  @CustomKey("teleport-accept-for-requester")
  private Component teleportAcceptForRequester = ChatUtil.component("&8[&d&lCC&8] &7Player &d{PLAYER} &7has accepted a request from you");

  @Comment({
      "PL: Wiadomość gdy zaakceptujesz prosbę o teleportacje",
      "EN: Message when you accept a teleportation request"}
  )
  @CustomKey("teleport-accept-for-target")
  private Component teleportAcceptedForTarget = ChatUtil.component("&8[&d&lCC&8] &7You have accepted a teleportation request from the player &d{PLAYER}");

  @Comment({
      "PL: Wiadomość gdy gracz odrzuci prosbę o teleportacje",
      "EN: Message when player denied teleportation request"}
  )
  @CustomKey("teleport-deny-for-requester")
  private Component teleportDenyForRequester = ChatUtil.component("&8[&d&lCC&8] &7Player &d{PLAYER} &7denied your request for teleportation");

  @Comment({
      "PL: Wiadomość gdy odrzucisz prosbę o teleportacje",
      "EN: Message when you denied a teleportation request"}
  )
  @CustomKey("teleport-deny-for-target")
  private Component teleportDenyForTarget = ChatUtil.component("&8[&d&lCC&8] &7You denied a teleportation request from the &d{PLAYER}");

  @Comment({
      "PL: Wiadomość po przeładowaniu konfiguracji",
      "EN: Message after configuration reload"}
  )
  @CustomKey("configuration-reload-command")
  private Component configurationReloadCommand = ChatUtil.component("&aThe configuration has been reloaded!");
}
