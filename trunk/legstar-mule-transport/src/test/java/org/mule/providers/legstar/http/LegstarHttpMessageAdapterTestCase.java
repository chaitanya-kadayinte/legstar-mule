/*******************************************************************************
 * $Id$
 * -----------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc. All rights reserved. http://www.mulesource.com
 * 
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file
 ******************************************************************************/

package org.mule.providers.legstar.http;

import org.mule.tck.providers.AbstractMessageAdapterTestCase;
import org.mule.umo.provider.UMOMessageAdapter;
import org.mule.umo.MessagingException;

/**
 * Test the LegstarHttpMessageAdapter class.
 * For general guidelines on writing transports see http://mule.mulesource.org/display/MULE/Writing+Transports
 */
public class LegstarHttpMessageAdapterTestCase extends AbstractMessageAdapterTestCase {

    /** {@inheritDoc} */
    public Object getValidMessage() throws Exception {
        return new String("mok message").getBytes();
    }

    /** {@inheritDoc} */
    public UMOMessageAdapter createAdapter(final Object payload) throws MessagingException {
        return new LegstarHttpMessageAdapter(payload);
    }

}
