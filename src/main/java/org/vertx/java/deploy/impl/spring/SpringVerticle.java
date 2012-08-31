package org.vertx.java.deploy.impl.spring;

import org.vertx.java.deploy.Verticle;
import org.vertx.java.spring.support.VertxApplicationContext;

/**
 * @author swilliams
 *
 */
public class SpringVerticle extends Verticle {

  private final ClassLoader loader;

  private final String springConfig;

  private VertxApplicationContext context;

  public SpringVerticle(ClassLoader loader, String springConfig) {
    this.loader = loader;
    this.springConfig = springConfig;
  }

  @Override
  public void start() throws Exception {

    this.context = new VertxApplicationContext(loader, vertx, container);
    context.createContext(springConfig);
  }

  @Override
  public void stop() throws Exception {

    if (context != null) {
      context.stop();
    }

    super.stop();
  }

}
