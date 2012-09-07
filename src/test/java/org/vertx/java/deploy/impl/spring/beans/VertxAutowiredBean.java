/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.java.deploy.impl.spring.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;

/**
 * @author swilliams
 *
 */
public class VertxAutowiredBean implements InitializingBean, DisposableBean {

  private final Vertx vertx;

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
