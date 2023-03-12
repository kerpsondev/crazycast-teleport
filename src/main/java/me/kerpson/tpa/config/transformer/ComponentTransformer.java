package me.kerpson.tpa.config.transformer;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import me.kerpson.tpa.util.ChatUtil;
import net.kyori.adventure.text.Component;

public class ComponentTransformer extends BidirectionalTransformer<String, Component> {

  @Override
  public GenericsPair<String, Component> getPair() {
    return this.genericsPair(String.class, Component.class);
  }

  @Override
  public Component leftToRight(@NonNull String data, @NonNull SerdesContext serdesContext) {
    return ChatUtil.component(data);
  }

  @Override
  public String rightToLeft(@NonNull Component data, @NonNull SerdesContext serdesContext) {
    return ChatUtil.component(data);
  }
}
