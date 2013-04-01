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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.vertx.spring.EventBusMessageHandler;


/**
 * @author swilliams
 *
 */
public class EventBusMessageHandlerProxy implements EventBusMessageHandler<Object> {

  private final String address;

  private final Object bean;

  private final Method method;

  /**
   * @param annotation
   * @param bean
   * @param method
   */
  public EventBusMessageHandlerProxy(String address, Object bean, Method method) {
    this.address = address;
    this.bean = bean;
    this.method = method;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public void onEvent(Object payload) {
    try {
      method.invoke(bean, payload);
    } catch (IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object replyOnEvent(Object payload) {
    try {
      return method.invoke(bean, payload);
    } catch (IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
