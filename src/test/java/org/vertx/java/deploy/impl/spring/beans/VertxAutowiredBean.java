package org.vertx.java.deploy.impl.spring.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;

public class VertxAutowiredBean implements InitializingBean, DisposableBean {

  private Vertx vertx;

  private String handlerId;

  @Autowired
  public VertxAutowiredBean(Vertx vertx) {
    this.vertx = vertx;
  }

  public Vertx getVertx() {
    return vertx;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.handlerId = vertx.eventBus().registerHandler("vertx.test.echo1", new Handler<Message<String>>() {
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
