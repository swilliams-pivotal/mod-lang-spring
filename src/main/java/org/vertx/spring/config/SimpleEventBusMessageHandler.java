/*
 * Copyright 2013 the original author or authors.
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
package org.vertx.spring.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.spring.EventBusMessageHandler;

/**
 * @author swilliams
 *
 */
public class SimpleEventBusMessageHandler<T> implements InitializingBean, SmartLifecycle, Handler<Message<T>> {

  private final EventBus eventBus;

  private final EventBusMessageHandler<T> listener;

  private volatile boolean running = false;

  private boolean autoStartup = true;

  private int phase = 1;

  /**
   * @param eventBus 
   * @param value
   * @param listener
   */
  SimpleEventBusMessageHandler(EventBus eventBus, EventBusMessageHandler<T> listener) {
    this.eventBus = eventBus;
    this.listener = listener;
  }

  @Override
  public void handle(Message<T> event) {
    if (event.replyAddress != null) {
      T reply = listener.replyOnEvent(event.body);
      event.reply(reply);
    }
    else {
      listener.onEvent(event.body);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.eventBus == null) {
      throw new IllegalStateException("eventBus is not set!");
    }
  }

  @Override
  public void start() {
    eventBus.registerHandler(listener.getAddress(), this, new AsyncResultHandler<Void>() {
      @Override
      public void handle(AsyncResult<Void> event) {
        SimpleEventBusMessageHandler.this.running = true;
      }
    });
  }

  @Override
  public void stop() {
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    return running;
  }

  @Override
  public int getPhase() {
    return phase;
  }

  @Override
  public boolean isAutoStartup() {
    return autoStartup;
  }

  @Override
  public void stop(final Runnable callback) {
    eventBus.unregisterHandler(listener.getAddress(), this, new AsyncResultHandler<Void>() {
      @Override
      public void handle(AsyncResult<Void> event) {
        callback.run();
      }
    });
  }

}
