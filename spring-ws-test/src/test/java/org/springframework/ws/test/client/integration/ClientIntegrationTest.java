/*
 * Copyright 2005-2010 the original author or authors.
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

package org.springframework.ws.test.client.integration;

import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

/**
 * Integration test for client-side WebService testing. In different package so we can't use the package-protected
 * classes.
 *
 * @author Arjen Poutsma
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("integration-test.xml")
public class ClientIntegrationTest {

    @Autowired
    private CustomerClient client;

    private MockWebServiceServer mockServer;

    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(client);
    }

    @Test
    public void basic() throws Exception {
        Source expectedRequestPayload = new StringSource(
                "<customerCountRequest xmlns='http://springframework.org/spring-ws'>" +
                        "<customerName>John Doe</customerName>" + "</customerCountRequest>");
        Source responsePayload = new StringSource(
                "<customerCountResponse xmlns='http://springframework.org/spring-ws'>" +
                        "<customerCount>10</customerCount>" + "</customerCountResponse>");

        mockServer.expect(payload(expectedRequestPayload)).andRespond(withPayload(responsePayload));

        int result = client.getCustomerCount();
        assertEquals(10, result);

        mockServer.verify();
    }

}
