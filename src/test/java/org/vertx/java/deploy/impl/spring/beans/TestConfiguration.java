package org.vertx.java.deploy.impl.spring.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestConfiguration {

  @Bean
  public VertxAwareBean vertxAwareBean() {
    return new VertxAwareBean();
  }

  @Bean
  public VertxAutowiredBean vertxAutowiredBean() {
    return new VertxAutowiredBean();
  }

}
