package me.kerpson.tpa.util;

import com.google.common.base.Joiner;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class ChatUtil {

  private ChatUtil() {}

  private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand()
      .toBuilder()
      .hexColors()
      .character('&')
      .hexCharacter('#')
      .useUnusualXRepeatedCharacterHexFormat()
      .build();

  private final static MiniMessage MINI_MESSAGE = MiniMessage.builder()
      .postProcessor(new LegacyColorProcessor())
      .build();

  public static Component component(String text) {
    return MINI_MESSAGE.deserialize(text);
  }

  public static Component legacyComponent(String text) {
    return SERIALIZER.deserialize(text);
  }

  public static String component(Component component) {
    return MINI_MESSAGE.serialize(component);
  }

  public static String legacyComponent(Component component) {
    return SERIALIZER.serialize(component);
  }

  public static List<Component> component(List<String> list) {
    return list.stream()
        .map(ChatUtil::component)
        .collect(Collectors.toList());
  }

  public static List<Component> component(String... text) {
    return Arrays.stream(text)
        .map(ChatUtil::component)
        .collect(Collectors.toList());
  }
}
