/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.italiangrid.utils.test;

import junit.framework.Assert;

import org.eclipse.jetty.server.Server;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.junit.Test;

/**
 * Tests the server factory newserver methods
 * 
 * @author valerioventuri
 * 
 */
public class InstantiationTest {

  /**
   * Calling newServer with wrong certificate location.
   * 
   */
  @Test
  public void wrongCertificateLocation() {

    SSLOptions options = new SSLOptions();

    options.setCertificateFile("certs/voms_service.cert.pe");
    options.setKeyFile("certs/voms_service.key.pem");
    options.setTrustStoreDirectory("certs/ca");

    Server s = ServerFactory.newServer("localhost", 443, options);

    Assert.assertNull(s);

  }

  /**
   * Calling newServer with wrong key location.
   * 
   */
  @Test
  public void wrongKeyLocation() {

    SSLOptions options = new SSLOptions();

    options.setCertificateFile("certs/voms_service.cert.pem");
    options.setKeyFile("certs/voms_service.key.pe");
    options.setTrustStoreDirectory("certs/ca");

    Server s = ServerFactory.newServer("localhost", 443, options);

    Assert.assertNull(s);

  }

  /**
   * Calling newServer with certificate location pointing to a non certificate
   * file.
   * 
   */
  public void wrongCertificate() {

    SSLOptions options = new SSLOptions();

    options.setCertificateFile("certs/README");
    options.setKeyFile("certs/voms_service.key.pem");
    options.setTrustStoreDirectory("certs/ca");

    Server s = ServerFactory.newServer("localhost", 443, options);

    Assert.assertNull(s);

  }

}
