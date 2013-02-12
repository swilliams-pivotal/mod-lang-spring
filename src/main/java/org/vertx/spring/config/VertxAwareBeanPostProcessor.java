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
package org.vertx.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.vertx.java.core.Vertx;
import org.vertx.spring.EventBusAware;
import org.vertx.spring.VertxAware;

/**
 * @author swilliams
 * @since 1.0
 * 
 */
public class VertxAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

  private final Vertx vertx;

  public VertxAwareBeanPostProcessor(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * Detects beans that implement VertxAware, EventBusAware
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    if (bean instanceof VertxAware) {
      VertxAware vertxAware = (VertxAware) bean;
      vertxAware.setVertx(vertx);
      return vertxAware;
    }

    if (bean instanceof EventBusAware) {
      EventBusAware eventBusAware = (EventBusAware) bean;
      eventBusAware.setEventBus(vertx.eventBus());
      return eventBusAware;
    }

    return bean;
  }

}
