package org.mule.transport.legstar.test.coxb;

import com.legstar.coxb.transform.AbstractXmlTransformers;
import com.legstar.coxb.transform.HostTransformException;

/**
 * XML Transformers provider for Record1.
 *
 */
public class Record1XmlTransformers extends AbstractXmlTransformers {

    /**
     * Create a set of directional transformers.
     * @throws HostTransformException if transformer cannot be created
     */
    public Record1XmlTransformers() throws HostTransformException {
        super(new Record1XmlToHostTransformer(),
                new Record1HostToXmlTransformer());
    }

}
