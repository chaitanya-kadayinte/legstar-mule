package org.mule.transport.legstar.test.coxb;

import com.legstar.coxb.transform.AbstractXmlTransformers;
import com.legstar.coxb.transform.HostTransformException;

/**
 * XML Transformers provider for Record2.
 *
 */
public class Record2XmlTransformers extends AbstractXmlTransformers {

    /**
     * Create a set of directional transformers.
     * @throws HostTransformException if transformer cannot be created
     */
    public Record2XmlTransformers() throws HostTransformException {
        super(new Record2XmlToHostTransformer(),
                new Record2HostToXmlTransformer());
    }

}
