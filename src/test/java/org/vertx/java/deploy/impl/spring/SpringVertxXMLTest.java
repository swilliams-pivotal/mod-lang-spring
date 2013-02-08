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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.test.TestVerticle;
import org.vertx.java.test.VertxTestBase;
import org.vertx.java.test.junit.VertxJUnit4ClassRunner;


//@RunWith(VertxJUnit4ClassRunner.class)
@TestVerticle(main="spring:testConfig.xml")
public class SpringVertxXMLTest {

  @Before
  public void setup() {
//    lightSleep(1000L); // Still needs a delay for some reason
  }

  @Test
  public void testVertxAutowiredBean() throws Exception {
//    testMessageEcho("vertx.test.echo0", "What's the time Mr Wolfe?");
  }

  @Test
  public void testVertxAwareBean() throws Exception {
//    testMessageEcho("vertx.test.echo1", "I'm Winston Wolfe. I solve problems.");
  }
}
