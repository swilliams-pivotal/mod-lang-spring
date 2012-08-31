package org.vertx.java.spring.support;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
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
public class VertxApplicationContext implements ApplicationContext {

  private static final String VERTX_BEAN_NAME = "org.vertx.java.core.Vertx";

  private static final String VERTICLE_CONTAINER_BEAN_NAME = "org.vertx.java.core.VerticleContainer";

  private final Set<ApplicationListener<?>> listeners = new HashSet<>();

  private final GenericApplicationContext parent;

  private final Vertx vertx;

  private GenericApplicationContext context;

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
    factory.registerSingleton(VERTX_BEAN_NAME, vertx);
    factory.registerAlias(VERTX_BEAN_NAME, "vertx");
    factory.registerSingleton(VERTICLE_CONTAINER_BEAN_NAME, container);
    factory.registerAlias(VERTICLE_CONTAINER_BEAN_NAME, "verticle-container");

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

  public void stop() {
    if (context != null) {
      context.close();
    }
    parent.stop();
  }

  public ClassLoader getClassLoader() {
    return context.getClassLoader();
  }

  public Resource getResource(String location) {
    return context.getResource(location);
  }

  public Resource[] getResources(String locationPattern) throws IOException {
    return context.getResources(locationPattern);
  }

  public final ConfigurableListableBeanFactory getBeanFactory() {
    return context.getBeanFactory();
  }

  public final DefaultListableBeanFactory getDefaultListableBeanFactory() {
    return context.getDefaultListableBeanFactory();
  }

  public String getDisplayName() {
    return context.getDisplayName();
  }

  public ApplicationContext getParent() {
    return context.getParent();
  }

  public ConfigurableEnvironment getEnvironment() {
    return context.getEnvironment();
  }

  public BeanDefinition getBeanDefinition(String beanName)
      throws NoSuchBeanDefinitionException {
    return context.getBeanDefinition(beanName);
  }

  public boolean isBeanNameInUse(String beanName) {
    return context.isBeanNameInUse(beanName);
  }

  public AutowireCapableBeanFactory getAutowireCapableBeanFactory()
      throws IllegalStateException {
    return context.getAutowireCapableBeanFactory();
  }

  public boolean isAlias(String beanName) {
    return context.isAlias(beanName);
  }

  public long getStartupDate() {
    return context.getStartupDate();
  }

  public void publishEvent(ApplicationEvent event) {
    context.publishEvent(event);
  }

  public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
    return context.getBeanFactoryPostProcessors();
  }

  public Collection<ApplicationListener<?>> getApplicationListeners() {
    return context.getApplicationListeners();
  }

  public boolean isActive() {
    return context.isActive();
  }

  public Object getBean(String name) throws BeansException {
    return context.getBean(name);
  }

  public <T> T getBean(String name, Class<T> requiredType)
      throws BeansException {
    return context.getBean(name, requiredType);
  }

  public <T> T getBean(Class<T> requiredType) throws BeansException {
    return context.getBean(requiredType);
  }

  public Object getBean(String name, Object... args) throws BeansException {
    return context.getBean(name, args);
  }

  public boolean containsBean(String name) {
    return context.containsBean(name);
  }

  public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
    return context.isSingleton(name);
  }

  public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
    return context.isPrototype(name);
  }

  public boolean isTypeMatch(String name, Class<?> targetType)
      throws NoSuchBeanDefinitionException {
    return context.isTypeMatch(name, targetType);
  }

  public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
    return context.getType(name);
  }

  public String[] getAliases(String name) {
    return context.getAliases(name);
  }

  public boolean containsBeanDefinition(String beanName) {
    return context.containsBeanDefinition(beanName);
  }

  public int getBeanDefinitionCount() {
    return context.getBeanDefinitionCount();
  }

  public String[] getBeanDefinitionNames() {
    return context.getBeanDefinitionNames();
  }

  public String[] getBeanNamesForType(Class<?> type) {
    return context.getBeanNamesForType(type);
  }

  public String[] getBeanNamesForType(Class<?> type,
      boolean includeNonSingletons, boolean allowEagerInit) {
    return context.getBeanNamesForType(type, includeNonSingletons,
        allowEagerInit);
  }

  public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
    return context.getBeansOfType(type);
  }

  public <T> Map<String, T> getBeansOfType(Class<T> type,
      boolean includeNonSingletons, boolean allowEagerInit)
      throws BeansException {
    return context.getBeansOfType(type, includeNonSingletons, allowEagerInit);
  }

  public Map<String, Object> getBeansWithAnnotation(
      Class<? extends Annotation> annotationType) throws BeansException {
    return context.getBeansWithAnnotation(annotationType);
  }

  public <A extends Annotation> A findAnnotationOnBean(String beanName,
      Class<A> annotationType) {
    return context.findAnnotationOnBean(beanName, annotationType);
  }

  public BeanFactory getParentBeanFactory() {
    return context.getParentBeanFactory();
  }

  public boolean containsLocalBean(String name) {
    return context.containsLocalBean(name);
  }

  public String getMessage(String code, Object[] args, String defaultMessage,
      Locale locale) {
    return context.getMessage(code, args, defaultMessage, locale);
  }

  public String getMessage(String code, Object[] args, Locale locale)
      throws NoSuchMessageException {
    return context.getMessage(code, args, locale);
  }

  public String getMessage(MessageSourceResolvable resolvable, Locale locale)
      throws NoSuchMessageException {
    return context.getMessage(resolvable, locale);
  }

  public boolean isRunning() {
    return context.isRunning();
  }

  @Override
  public String getId() {
    return parent.getId();
  }

}
