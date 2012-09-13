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
package org.vertx.java.deploy.impl.spring;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.vertx.java.deploy.Verticle;

/**
 * @author swilliams
 * @since 1.0
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

    ClassLoader tccl = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(loader);

    this.context = new VertxApplicationContext(loader, vertx, container);
    context.createContext(springConfig);

    Set<ApplicationListener<?>> listeners = new HashSet<>();
    // TODO discover configured listeners

    for (ApplicationListener<?> listener : listeners) {
      context.addApplicationListener(listener);
    }

    context.refresh();
    context.start();

    Thread.currentThread().setContextClassLoader(tccl);
  }

  @Override
  public void stop() throws Exception {

    if (context != null) {
      context.stop();
    }

    super.stop();
  }

}
