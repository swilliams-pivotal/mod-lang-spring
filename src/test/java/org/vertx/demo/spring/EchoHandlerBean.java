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
package org.vertx.demo.spring;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * @author swilliams
 *
 */
public class EchoHandlerBean extends AbstractHandlerBean<JsonObject> {

  @Autowired
  public EchoHandlerBean(Vertx vert) {
    super(vert.eventBus());
  }

  @PostConstruct
  public void afterPropertiesSet() {
    setAddress("test.handler.bean");
  }

  @Override
  public void handle(Message<JsonObject> event) {
    event.reply(event.body);
  }

}
