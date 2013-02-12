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

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;
import org.vertx.spring.config.ContainerAwareBeanPostProcessor;
import org.vertx.spring.config.VertxAwareBeanPostProcessor;

/**
 *
 * This class uses a parent context which serves a couple of purposes.
 *
 * The vertx singleton needs to be registered before the context XML
 * is loaded or annotated @Configuration class is registered.  Using a 
 * parent context permits this.
 *
 * Use of a parent context will also prevent BeanPostProcessors
 * interfering with the Vertx instance.
 *
 * I also like the analogy of the vertx instance being in a parent
 * context, as it's not instantiated in the context you really use.
 *
 * @author swilliams
 * @since 1.0
 *
 */
public class VertxApplicationContext {

  private static final String VERTX_BEAN_NAME = "org.vertx.java.core.Vertx";

  private static final String VERTICLE_CONTAINER_BEAN_NAME = "org.vertx.java.core.VerticleContainer";

  private final Set<ApplicationListener<?>> listeners = new HashSet<>();

  private final GenericApplicationContext parent;

  private final Vertx vertx;

  private GenericApplicationContext context;

  private final Container container;

  /**
   * @param loader
   * @param vertx
   * @param container 
   */
  public VertxApplicationContext(final ClassLoader loader, final Vertx vertx, final Container container) {
    this.vertx = vertx;
    this.container = container;

    this.parent = new GenericApplicationContext();
    parent.setClassLoader(loader);

    ConfigurableListableBeanFactory factory = parent.getBeanFactory();
    factory.registerSingleton(VERTX_BEAN_NAME, this.vertx);
    factory.registerAlias(VERTX_BEAN_NAME, "vertx");
    factory.registerSingleton(VERTICLE_CONTAINER_BEAN_NAME, this.container);
    factory.registerAlias(VERTICLE_CONTAINER_BEAN_NAME, "verticle-container");

    parent.refresh();
    parent.start();
    parent.registerShutdownHook();
  }

  /**
   * @param listener
   */
  public void addApplicationListener(final ApplicationListener<?> listener) {
    if (context == null) {
      listeners.add(listener);
    }
    else {
      context.addApplicationListener(listener);
    }
  }

  /**
   * @param springConfig
   */
  public void createContext(final String springConfig) {

    // Detect whether this app is configured with an XML or @Configuration.
    if (springConfig.endsWith(".xml")) {
      context = new GenericXmlApplicationContext();
      context.setParent(parent);

      ((GenericXmlApplicationContext) context).load(springConfig);
    }
    else {
      context = new AnnotationConfigApplicationContext();
      context.setParent(parent);

      try {
        Class<?> c = Class.forName(springConfig);
        ((AnnotationConfigApplicationContext) context).register(c);
      } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException(e);
      }
    }

    if (!listeners.isEmpty()) {
      for (ApplicationListener<?> listener : listeners) {
        context.addApplicationListener(listener);
      }
      listeners.clear();
    }

    ConfigurableListableBeanFactory factory = context.getBeanFactory();

    BeanPostProcessor vertxSupportProcessor = new VertxAwareBeanPostProcessor(vertx);
    factory.addBeanPostProcessor(vertxSupportProcessor);

    BeanPostProcessor containerSupportProcessor = new ContainerAwareBeanPostProcessor(container);
    factory.addBeanPostProcessor(containerSupportProcessor);
  }

  public void refresh() {
    context.refresh();
  }

  public void start() {
    context.start();
  }

  public void stop() {
    if (context != null) {
      context.close();
    }
    parent.stop();
  }

}
