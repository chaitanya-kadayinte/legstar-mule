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
package org.mule.transport.legstar.test.lsfileax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.http.ReleasingInputStream;

import com.legstar.test.coxb.LsfileacCases;
import com.legstar.test.coxb.LsfileaeCases;
import com.legstar.test.coxb.lsfileac.QueryData;
import com.legstar.test.coxb.lsfileac.QueryLimit;
import com.legstar.test.coxb.lsfileae.Dfhcommarea;

/**
 * Test the adapter for the LSFILEAC/LSFILEAE mainframe programs.
 * <p/>
 * Adapter transport is HTTP. The LegStar mainframe modules for HTTP must be
 * installed on the mainframe: {@link http
 * ://www.legsem.com/legstar/legstar-distribution-zos/}.
 * <p/>
 * Client sends/receive serialized java objects.
 */
public class LsfileaxHttpTest extends FunctionalTestCase {

    /** {@inheritDoc} */
    protected String getConfigResources() {
        return "mule-adapter-config-lsfileax-http-java-legstar.xml";
    }

    /**
     * Run the target LSFILEAC mainframe program. Client sends a serialized java
     * object and receive one as a reply.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testLsfileac() throws Exception {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage message = client.send("http://localhost:3280/lsfileac",
                getSerializedJavaRequest(getJavaObjectLsfileacRequest()), null);
        ObjectInputStream in = new ObjectInputStream(
                (ReleasingInputStream) message.getPayload());
        checkJavaObjectLsfileacReply((LsfileacResponseHolder) in.readObject());

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
        MuleMessage message = client.send("http://localhost:3280/lsfileae",
                getSerializedJavaRequest(getJavaObjectLsfileaeRequest()), null);
        ObjectInputStream in = new ObjectInputStream(
                (ReleasingInputStream) message.getPayload());
        checkJavaObjectLsfileaeReply((Dfhcommarea) in.readObject());

    }

    /**
     * @return a serialized java request object in a byte array.
     * @throws IOException if serialization fails
     */
    private byte[] getSerializedJavaRequest(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.close();
        return bos.toByteArray();
    }

    /**
     * @return an instance of a the java request object.
     */
    public static LsfileacRequestHolder getJavaObjectLsfileacRequest() {
        QueryData qdt = LsfileacCases.getJavaObjectQueryData();
        QueryLimit qlt = LsfileacCases.getJavaObjectQueryLimit();

        LsfileacRequestHolder result = new LsfileacRequestHolder();
        result.setQueryData(qdt);
        result.setQueryLimit(qlt);
        return result;
    }

    /**
     * Check the values returned from LSFILEAC after they were transformed to
     * Java.
     * 
     * @param reply the java data object
     */
    public static void checkJavaObjectLsfileacReply(
            final LsfileacResponseHolder reply) {
        LsfileacCases.checkJavaObjectReplyData(reply.getReplyData());
        LsfileacCases.checkJavaObjectReplyStatus(reply.getReplyStatus());

    }

    /**
     * @return an instance of a the java request object.
     */
    public static Dfhcommarea getJavaObjectLsfileaeRequest() {
        return LsfileaeCases.getJavaObjectRequest100();
    }

    /**
     * Check the values returned from LSFILEAE after they were transformed to
     * Java.
     * 
     * @param dfhcommarea the java data object
     */
    public static void checkJavaObjectLsfileaeReply(
            final Dfhcommarea dfhcommarea) {
        LsfileaeCases.checkJavaObjectReply100(dfhcommarea);
    }
}
