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

import org.vertx.java.core.Vertx;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;
import org.vertx.java.platform.VerticleFactory;


/**
 * @author swilliams
 * @since 2.0
 *
 */
public class SpringVerticleFactory implements VerticleFactory {

  private static final String PREFIX = "spring:";

  private Vertx vertx;

  private Container container;

  private ClassLoader loader;

  @Override
  public final void init(Vertx vertx, Container container, ClassLoader cl) {
    if (this.vertx != null || this.container != null || this.loader != null) {
      throw new IllegalStateException("The 'init(v,c,l)' method has already been called for this VerticleFactory");
    }
    this.vertx = vertx;
    this.container = container;
    this.loader = cl;
  }

  @Override
  public Verticle createVerticle(String main) throws Exception {
    return new SpringVerticle(loader, container, main.replaceFirst(PREFIX, ""));
  }

  @Override
  public void reportException(Logger logger, Throwable t) {
    logger.error("Exception in Spring verticle", t);
  }

  @Override
  public void close() {
    //
  }

}
