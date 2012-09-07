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
package org.vertx.java.deploy.impl.spring.beans;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.vertx.java.deploy.Container;
import org.vertx.java.deploy.impl.spring.ContainerAware;

/**
 * @author swilliams
 *
 */
public class ContainerAwareBean implements InitializingBean, ContainerAware {

  private Container container;

  @Override
  public void setContainer(Container container) {
    this.container = container;
  }

  public Container getContainer() {
    return container;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(container, "Container must not be null!");
  }

}
