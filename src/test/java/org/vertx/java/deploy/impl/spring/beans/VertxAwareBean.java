package org.vertx.java.deploy.impl.spring.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.deploy.impl.spring.VertxAware;

public class VertxAwareBean implements InitializingBean, DisposableBean, VertxAware {

  private Vertx vertx;

  private String handlerId;

  @Override
  public void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }

  public Vertx getVertx() {
    return vertx;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.handlerId = vertx.eventBus().registerHandler("vertx.test.echo0", new Handler<Message<String>>() {
      @Override
      public void handle(Message<String> event) {
        event.reply(event.body);
      }
    });
  }

  @Override
  public void destroy() throws Exception {
    vertx.eventBus().unregisterHandler(handlerId);
  }

}
