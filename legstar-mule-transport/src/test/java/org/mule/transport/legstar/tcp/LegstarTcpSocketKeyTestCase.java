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
package org.mule.transport.legstar.tcp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.security.Credentials;
import org.mule.endpoint.EndpointURIEndpointBuilder;
import org.mule.endpoint.URIBuilder;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.mule.transport.legstar.config.HostCredentials;
/**
 * Test the LegstarTcpSocketKey class.
 *
 */
public class LegstarTcpSocketKeyTestCase extends AbstractMuleContextTestCase {
    
    /**
     * Check that null credentials are accepted.
     * @throws Exception if test fails
     */
    @Test
    public void testNullCredentials() throws Exception {
        LegstarTcpSocketKey key1 = new LegstarTcpSocketKey(getEndpointA(), null);
        LegstarTcpSocketKey key2 = new LegstarTcpSocketKey(getEndpointA(), null);
        assertTrue(key1.equals(key2));
        assertEquals(1415151024, key1.hashCode());
        assertEquals(1415151024, key2.hashCode());
    }

    /**
     * Check that null credentials are accepted 2.
     * @throws Exception if test fails
     */
    @Test
    public void testDiffHostNullCredentials() throws Exception {
        LegstarTcpSocketKey key1 = new LegstarTcpSocketKey(getEndpointA(), null);
        LegstarTcpSocketKey key2 = new LegstarTcpSocketKey(getEndpointB(), null);
        assertFalse(key1.equals(key2));
        assertEquals(1415151024, key1.hashCode());
        assertEquals(1415151055, key2.hashCode());
    }
    /**
     * Check same credentials.
     * @throws Exception if test fails
     */
    @Test
    public void testSameCredentials() throws Exception {
        Credentials credentials = new HostCredentials("toutan", "khamon".toCharArray());
        LegstarTcpSocketKey key1 = new LegstarTcpSocketKey(getEndpointA(), credentials);
        LegstarTcpSocketKey key2 = new LegstarTcpSocketKey(getEndpointA(), credentials);
        assertTrue(key1.equals(key2));
        assertEquals(35736435, key1.hashCode());
        assertEquals(35736435, key2.hashCode());
    }

    /**
     * Check different host same credentials.
     * @throws Exception if test fails
     */
    @Test
    public void testDiffHostSameCredentials() throws Exception {
        Credentials credentials = new HostCredentials("toutan", "khamon".toCharArray());
        LegstarTcpSocketKey key1 = new LegstarTcpSocketKey(getEndpointA(), credentials);
        LegstarTcpSocketKey key2 = new LegstarTcpSocketKey(getEndpointB(), credentials);
        assertFalse(key1.equals(key2));
        assertEquals(35736435, key1.hashCode());
        assertEquals(35736466, key2.hashCode());
    }

    /**
     * Check same host different credentials.
     * @throws Exception if test fails
     */
    @Test
    public void testSameHostDiffCredentials() throws Exception {
        Credentials credentials1 = new HostCredentials("toutan", "khamon".toCharArray());
        LegstarTcpSocketKey key1 = new LegstarTcpSocketKey(getEndpointA(), credentials1);
        Credentials credentials2 = new HostCredentials("amen", "ofis".toCharArray());
        LegstarTcpSocketKey key2 = new LegstarTcpSocketKey(getEndpointA(), credentials2);
        assertFalse(key1.equals(key2));
        assertEquals(35736435, key1.hashCode());
        assertEquals(1512412926, key2.hashCode());
    }

    /**
     * @return a LegStar socket endpoint.
     * @throws Exception if test fails
     */
    private OutboundEndpoint getEndpointA() throws Exception {
        EndpointBuilder endpointBuilder = new EndpointURIEndpointBuilder(
                new URIBuilder("legstar-tcp://mainframe:3011",
                        muleContext));
        return muleContext.getEndpointFactory().getOutboundEndpoint(
                endpointBuilder);
    }

    /**
     * @return a LegStar socket endpoint.
     * @throws Exception if test fails
     */
    private OutboundEndpoint getEndpointB() throws Exception {
        EndpointBuilder endpointBuilder = new EndpointURIEndpointBuilder(
                new URIBuilder("legstar-tcp://mainframe:3012",
                        muleContext));
        return muleContext.getEndpointFactory().getOutboundEndpoint(
                endpointBuilder);
    }
}
