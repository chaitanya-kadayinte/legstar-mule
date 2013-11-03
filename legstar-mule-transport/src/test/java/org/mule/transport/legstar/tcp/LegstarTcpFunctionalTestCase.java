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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.junit4.FunctionalTestCase;

import com.legstar.test.coxb.LsfileaeCases;
import com.legstar.test.coxb.lsfileae.Dfhcommarea;
import com.legstar.test.coxb.lsfileae.ObjectFactory;
/**
 * Test a roundtrip on the legstar TCP transport.
 *
 */
public class LegstarTcpFunctionalTestCase extends FunctionalTestCase {
    
    /**
     * Increase the timeout to allow enough time for debugging.
     */
    public LegstarTcpFunctionalTestCase() {
        super();
        System.setProperty(AbstractMuleTestCase.TEST_TIMEOUT_SYSTEM_PROPERTY,
                "600");
    }
    
    /** {@inheritDoc}*/
    protected String getConfigResources() {
        return "legstar-tcp-test-config-lsfileae.xml";
    }

    /**
     * Perform round trip.
     * @throws Exception if test fails
     */
    @Test
    public void testSend() throws Exception {        

        Map < String, Object > properties = new HashMap < String, Object >();
        properties.put(LegstarTcpConnector.HOST_USERID_PROPERTY, "P390");
        properties.put(LegstarTcpConnector.HOST_PASSWORD_PROPERTY, "STREAM2");
        MuleClient client = new MuleClient(muleContext);
        MuleMessage result = client.send("lsfileaeClientEndpoint",
                getJavaRequest(), properties);

        assertTrue(null == result.getExceptionPayload());
        assertTrue(result.getPayload() instanceof Dfhcommarea);
        Dfhcommarea reply = (Dfhcommarea) result.getPayload();
        LsfileaeCases.checkJavaObjectReply100(reply);
    }

    /**
     * @return a java request object.
     */
    public static Dfhcommarea getJavaRequest() {
        ObjectFactory of = new ObjectFactory();
        Dfhcommarea dfhcommarea = of.createDfhcommarea();
        dfhcommarea.setComNumber(100L);
        return dfhcommarea;
    }

    /** 
     * Check the values returned from LSFILEAE after they were transformed to Java.
     * @param dfhcommarea the java data object
     */
    public static void checkJavaObjectReply(final Dfhcommarea dfhcommarea) {
        assertEquals(100, dfhcommarea.getComNumber());
        assertEquals("$0100.11", dfhcommarea.getComAmount());
        assertEquals("*********", dfhcommarea.getComComment());
        assertEquals("26 11 81", dfhcommarea.getComDate());
        assertEquals("SURREY, ENGLAND", dfhcommarea.getComPersonal().getComAddress());
        assertEquals("S. D. BORMAN", dfhcommarea.getComPersonal().getComName());
        assertEquals("32156778", dfhcommarea.getComPersonal().getComPhone());
    }
}
