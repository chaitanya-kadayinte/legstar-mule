/*******************************************************************************
 * Copyright (c) 2009 LegSem.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     LegSem - initial API and implementation
 ******************************************************************************/
package org.mule.transport.legstar.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.api.endpoint.EndpointURI;
import org.mule.api.transport.Connector;
import org.mule.endpoint.MuleEndpointURI;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.mule.transport.service.TransportFactory;

/**
 * Test the LegstarHttpConnectorFactory class.
 *
 */
public class LegstarHttpConnectorFactoryTestCase extends
        AbstractMuleContextTestCase {

    /** Legstar listening port. */
    private static final int LEGSTAR_PORT = 8083;

    /**
     * Check that connector can be created.
     * @throws Exception if fails
     */
    @Test
    public void testCreateFromFactory() throws Exception {
        EndpointURI url = new MuleEndpointURI(getEndpointURI(), muleContext);
        TransportFactory transportFactory = new TransportFactory(muleContext);
        Connector connector = transportFactory.createConnector(url);
        assertNotNull(connector);
        assertTrue(connector instanceof LegstarHttpConnector);
        assertEquals("legstar", connector.getProtocol());
    }

    /**
     * @return a legstar scheme URI.
     */
    public String getEndpointURI() {
        return "legstar://localhost:" + Integer.toString(LEGSTAR_PORT);
    }

}
