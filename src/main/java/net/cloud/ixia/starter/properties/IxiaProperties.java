package net.cloud.ixia.starter.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class IxiaProperties {
  @Value("${public_key}")
  private String pubKey;

  public String getPubKey() {
    return pubKey;
  }
}
