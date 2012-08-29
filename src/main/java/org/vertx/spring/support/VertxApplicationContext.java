package org.vertx.spring.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.vertx.java.core.Vertx;
import org.vertx.java.deploy.Container;

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
 * @author pidster
 *
 */
public class VertxApplicationContext {

  private final Set<ApplicationListener<?>> listeners = new HashSet<>();

  private final GenericApplicationContext parent;

  private final Vertx vertx;

  /**
   * @param loader
   * @param vertx
   * @param container 
   */
  public VertxApplicationContext(final ClassLoader loader, final Vertx vertx, final Container container) {
    this.vertx = vertx;

    this.parent = new GenericApplicationContext();
    parent.setClassLoader(loader);

    ConfigurableListableBeanFactory factory = parent.getBeanFactory();
    factory.registerSingleton("vertx", vertx);
    factory.registerSingleton("vertx-container", container);

    parent.refresh();
    parent.start();
    parent.registerShutdownHook();
  }

  /**
   * @param listener
   */
  public void addApplicationListener(final ApplicationListener<?> listener) {
    listeners.add(listener);
  }

  /**
   * @param springConfig
   */
  public void createContext(final String springConfig) {

    final GenericApplicationContext context;

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

    for (ApplicationListener<?> listener : listeners) {
      context.addApplicationListener(listener);
    }

    ConfigurableListableBeanFactory factory = context.getBeanFactory();
    BeanPostProcessor vertxSupportProcessor = new VertxAwareBeanPostProcessor(vertx);
    factory.addBeanPostProcessor(vertxSupportProcessor);

    context.refresh();
    context.start();
  }

  public void close() {
    if (parent != null) {
      parent.close();
    }
  }

}
