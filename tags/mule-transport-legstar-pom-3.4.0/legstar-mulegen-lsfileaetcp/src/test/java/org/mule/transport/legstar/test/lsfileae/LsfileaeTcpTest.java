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
package org.mule.transport.legstar.test.lsfileae;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.http.ReleasingInputStream;
import org.mule.transport.legstar.tcp.LegstarTcpConnector;

import com.legstar.test.coxb.lsfileae.Dfhcommarea;
import com.legstar.test.coxb.lsfileae.ObjectFactory;

/**
 * Test the adapter for the LSFILEAE mainframe program.
 * <p/>
 * Adapter transport is TCP. The LegStar mainframe modules for TCP must be
 * installed on the mainframe: {@link http
 * ://www.legsem.com/legstar/legstar-distribution-zos/}.
 * <p/>
 * Client sends/receive serialized java objects.
 */
public class LsfileaeTcpTest extends FunctionalTestCase {

    /** {@inheritDoc} */
    protected String getConfigResources() {
        return "mule-adapter-config-lsfileaetcp-tcp-java-legstar.xml";
    }

    /**
     * Run the target LSFILEAE mainframe program. Client sends a serialized java
     * object and receive one as a reply.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testLsfileae() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        Map < String, Object > properties = new HashMap < String, Object >();
        properties.put(LegstarTcpConnector.HOST_USERID_PROPERTY, "P390");
        properties.put(LegstarTcpConnector.HOST_PASSWORD_PROPERTY, "STREAM2");
        MuleMessage message = client.send("http://localhost:3280/lsfileae",
                getSerializedJavaRequest(), properties);
        ObjectInputStream in = new ObjectInputStream(
                (ReleasingInputStream) message.getPayload());
        checkJavaObjectReply((Dfhcommarea) in.readObject());

    }

    /**
     * @return a serialized java request object in a byte array.
     * @throws IOException if serialization fails
     */
    private byte[] getSerializedJavaRequest() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(getJavaObjectRequest());
        out.close();
        return bos.toByteArray();
    }

    /**
     * @return an instance of a the java request object.
     */
    public static Dfhcommarea getJavaObjectRequest() {
        ObjectFactory of = new ObjectFactory();
        Dfhcommarea dfhcommarea = of.createDfhcommarea();
        dfhcommarea.setComNumber(100L);
        return dfhcommarea;
    }

    /**
     * Check the values returned from LSFILEAE after they were transformed to
     * Java.
     * 
     * @param dfhcommarea the java data object
     */
    public static void checkJavaObjectReply(final Dfhcommarea dfhcommarea) {
        assertEquals(100, dfhcommarea.getComNumber());
        assertEquals("$0100.11", dfhcommarea.getComAmount());
        assertEquals("*********", dfhcommarea.getComComment());
        assertEquals("26 11 81", dfhcommarea.getComDate());
        assertEquals("SURREY, ENGLAND", dfhcommarea.getComPersonal()
                .getComAddress());
        assertEquals("S. D. BORMAN", dfhcommarea.getComPersonal().getComName());
        assertEquals("32156778", dfhcommarea.getComPersonal().getComPhone());
    }
}
