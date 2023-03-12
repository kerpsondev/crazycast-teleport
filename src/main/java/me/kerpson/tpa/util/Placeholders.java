package me.kerpson.tpa.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

public class Placeholders {

  private final Map<String, Object> placeholders = new WeakHashMap<>();

  public static Placeholders create() {
    return new Placeholders();
  }

  public Placeholders with(String field, Object value) {
    this.placeholders.put(field, value);
    return this;
  }

  public Placeholders with(String field, Supplier<Object> value) {
    this.placeholders.put(field, value.get());
    return this;
  }

  public Component applyToComponent(Component component) {
    for (Entry<String, Object> entry : this.placeholders.entrySet()) {
      String field = entry.getKey();
      Object value = entry.getValue();
      component = component.replaceText(
          TextReplacementConfig.builder()
            .matchLiteral(field)
            .replacement(value.toString())
            .build()
      );
    }

    return component;
  }

  public List<Component> applyToList(List<Component> list) {
    return list.stream()
        .map(this::applyToComponent)
        .collect(Collectors.toList());
  }
}
