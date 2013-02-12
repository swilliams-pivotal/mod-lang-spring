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
package org.vertx.spring.config.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.vertx.spring.config.EventBusHandlerAnnotationPostProcessor;
import org.vertx.spring.config.EventBusMessageHandlerPostProcessor;

/**
 * @author swilliams
 *
 */
public class EnableVertxImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

  /* (non-Javadoc)
   * @see org.springframework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
   */
  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    RootBeanDefinition eventBusHandlerAnnotationPostProcessor = new RootBeanDefinition(EventBusHandlerAnnotationPostProcessor.class);
    eventBusHandlerAnnotationPostProcessor.setAutowireCandidate(true);
    eventBusHandlerAnnotationPostProcessor.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    eventBusHandlerAnnotationPostProcessor.setRole(BeanDefinition.ROLE_SUPPORT);
    eventBusHandlerAnnotationPostProcessor.setScope(BeanDefinition.SCOPE_SINGLETON);
    registry.registerBeanDefinition("eventBusHandlerAnnotationPostProcessor", eventBusHandlerAnnotationPostProcessor);

    RootBeanDefinition eventBusHandlerPostProcessor = new RootBeanDefinition(EventBusMessageHandlerPostProcessor.class);
    eventBusHandlerPostProcessor.setAutowireCandidate(true);
    eventBusHandlerPostProcessor.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    eventBusHandlerPostProcessor.setRole(BeanDefinition.ROLE_SUPPORT);
    eventBusHandlerPostProcessor.setScope(BeanDefinition.SCOPE_SINGLETON);
    registry.registerBeanDefinition("eventBusHandlerPostProcessor", eventBusHandlerPostProcessor);

    //
  }

}
