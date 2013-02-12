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

import org.springframework.beans.factory.xml.ParserContext;

/**
 * @author swilliams
 *
 */
public class VertxConfigUtils {

  public static final String VERTX_AWARE_PROCESSOR_BEAN_NAME = "org.vertx.java.spring.internalVertxAwarePostProcessor";

  public static final String CONTAINER_AWARE_PROCESSOR_BEAN_NAME = "org.vertx.java.spring.internalContainerAwarePostProcessor";

  /**
   * @param parserContext
   * @param source
   */
  public static void registerAnnotationComponents(ParserContext parserContext,
      Object source) {
    // TODO Auto-generated method stub
  }

}
