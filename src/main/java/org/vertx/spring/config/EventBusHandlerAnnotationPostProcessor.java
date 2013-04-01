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

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.vertx.spring.EventBusHandler;


/**
 * @author swilliams
 *
 */
public class EventBusHandlerAnnotationPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements AnnotatedMethodPostProcessor<EventBusHandler> {

  /*
   * (non-Javadoc)
   * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    Method[] methods = bean.getClass().getMethods();
    for (Method m : methods) {
      if (m.isAnnotationPresent(EventBusHandler.class)) {
        return postProcess(bean, beanName, m, m.getAnnotation(EventBusHandler.class));
      }
    }

    return super.postProcessBeforeInitialization(bean, beanName);
  }

  /* (non-Javadoc)
   * @see org.vertx.java.spring.config.AnnotatedMethodPostProcessor#postProcess(java.lang.Object, java.lang.String, java.lang.reflect.Method, java.lang.annotation.Annotation)
   */
  @Override
  public Object postProcess(final Object bean, final String beanName, final Method method, final EventBusHandler annotation) {
    return new EventBusMessageHandlerProxy(annotation.value(), bean, method);
  }

}
