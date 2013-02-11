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
package org.vertx.java.spring.listener;

import org.vertx.java.spring.listener.VertxMessageListener;

/**
 * @author swilliams
 *
 */
public class EchoVertxMessageListener implements VertxMessageListener<Object> {

  /* (non-Javadoc)
   * @see org.vertx.spring.listener.VertxListener#onEvent(java.lang.Object)
   */
  @Override
  public void onEvent(Object payload) {
    // TODO Auto-generated method stub
  }

  /* (non-Javadoc)
   * @see org.vertx.spring.listener.VertxListener#replyOnEvent(java.lang.Object)
   */
  @Override
  public Object replyOnEvent(Object payload) {
    return payload;
  }

}
