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
package org.mule.transport.legstar.transformer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.types.DataTypeFactory;

import com.legstar.coxb.transform.AbstractTransformers;
import com.legstar.coxb.transform.HostTransformException;
import com.legstar.coxb.transform.HostTransformStatus;

/**
 * This ESB Transformer converts host data into a java object using the regular
 * LegStar binding transformers.
 */
/**
 * @author Fady
 * 
 */
public abstract class AbstractHostToJavaMuleTransformer extends
        AbstractHostJavaMuleTransformer {

    /**
     * a specialized class that knows how to assign objects to a holder (a bag
     * of objects)
     */
    private HolderSetter holderSetter;

    /**
     * Constructor for single part transformers.
     * <p/>
     * Host to Java transformers expect byte array sources corresponding to
     * mainframe raw data. Alternatively, source can be an inputStream in which
     * case, it is assumed to stream a byte array. Inheriting classes will set
     * the return class.
     * 
     * @param bindingTransformers a set of transformers for the part type.
     */
    public AbstractHostToJavaMuleTransformer(
            final AbstractTransformers bindingTransformers) {
        super(bindingTransformers);
        registerSourceType(DataTypeFactory.create(InputStream.class));
        registerSourceType(DataTypeFactory.BYTE_ARRAY);
    }

    /**
     * Constructor for multi-part transformers.
     * <p/>
     * Host to Java transformers expects a map of byte arrays for multi part
     * payloads. Inheriting classes will set the return class.
     * 
     * @param bindingTransformersMap map of transformer sets, one for each part
     *            type.
     */
    public AbstractHostToJavaMuleTransformer(
            final Map < String, AbstractTransformers > bindingTransformersMap) {
        super(bindingTransformersMap);
        registerSourceType(DataTypeFactory.create(Map.class));
    }

    /**
     * Constructor for multi-structures transformers.
     * <p/>
     * Host to Java transformers expects byte array sources. Inheriting classes
     * will set the return class.
     * 
     * @param bindingTransformersList ordered list of transformers to be applied
     *            in sequence.
     * @param describes the holder and how to set its inner objects
     */
    public AbstractHostToJavaMuleTransformer(
            final List < AbstractTransformers > bindingTransformersList,
            HolderSetter holderSetter) {
        super(bindingTransformersList);
        registerSourceType(DataTypeFactory.create(InputStream.class));
        registerSourceType(DataTypeFactory.BYTE_ARRAY);
        this.holderSetter = holderSetter;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object hostTransform(final MuleMessage esbMessage,
            final String encoding) throws TransformerException {

        try {

            Object src = esbMessage.getPayload();

            if (src instanceof byte[]) {
                return toJava((byte[]) src, getHostCharset(esbMessage));

            } else if (src instanceof InputStream) {
                return toJava(IOUtils.toByteArray((InputStream) src),
                        getHostCharset(esbMessage));

            } else if (src instanceof Map) {
                return toJava((Map < String, byte[] >) src,
                        getHostCharset(esbMessage));

            } else {
                return null;
            }

        } catch (HostTransformException e) {
            throw new TransformerException(getI18NMessages()
                    .hostTransformFailure(), this, e);
        } catch (Exception e) {
            throw new TransformerException(getI18NMessages()
                    .hostTransformFailure(), this, e);
        }

    }

    /**
     * Host is sending a byte sequence so when apply either a single transformer
     * or a list of transformers to the entire payload.
     * 
     * @param hostBytes the byte sequence returned from the host
     * @param hostCharset the host character set
     * @return the java object resulting from the transformation
     * @throws HostTransformException if tranformation failed
     */
    public Object toJava(byte[] hostBytes, String hostCharset)
            throws HostTransformException {
        if (getBindingTransformers() != null) {
            return getBindingTransformers().toJava(hostBytes, hostCharset);
        }
        if (getBindingTransformersList() != null) {
            return toJava(getBindingTransformersList(), hostBytes, hostCharset);
        }
        return null;
    }

    /**
     * When the host is sending an multiple parts part, this code transforms
     * each part. Each part is transformed individually then they are all
     * wrapped in a holder object which is returned.
     * 
     * @param hostParts a map of byte arrays, one for each part
     * @param hostCharset the host character set
     * @return a java object
     * @throws TransformerException if transformation failed
     */
    public Object toJava(final Map < String, byte[] > hostParts,
            final String hostCharset) throws TransformerException {

        try {
            Map < String, Object > transformedParts = new HashMap < String, Object >();
            for (Entry < String, byte[] > hostPart : hostParts.entrySet()) {
                transformedParts.put(hostPart.getKey(),
                        getBindingTransformersMap().get(hostPart.getKey())
                                .toJava(hostPart.getValue(), hostCharset));
            }
            return createHolder(transformedParts);
        } catch (HostTransformException e) {
            throw new TransformerException(getI18NMessages()
                    .hostTransformFailure(), this, e);
        }

    }

    /**
     * Host is sending a single byte sequence but multiple transformers must be
     * applied to decode the complete byte stream.
     * 
     * @param transformers ordered list of transformers that need to be applied
     * @param hostBytes the single host bytes sequence
     * @param hostCharset the host character set
     * @return the java object, a holder of other java objects, that results
     *         from the complete transformation
     * @throws HostTransformException
     */
    public Object toJava(final List < AbstractTransformers > transformers,
            byte[] hostBytes, String hostCharset) throws HostTransformException {
        int replyBytePos = 0;
        int index = 0;
        HostTransformStatus status = new HostTransformStatus();
        for (AbstractTransformers xf : transformers) {
            holderSetter.set(
                    xf.toJava(hostBytes, replyBytePos, hostCharset, status),
                    index);
            replyBytePos += status.getHostBytesProcessed();
            index++;
        }
        return holderSetter.getHolder();
    }

    /**
     * When multiple part were received from the host, each transformed part is
     * stored in a map. Inheriting classes are responsible from wrapping these
     * parts into a typed holder object. Multi part transformers must override
     * this method.
     * 
     * @param transformedParts a map of transformed types
     * @return a holder object
     * @throws TransformerException if creating holder fails
     */
    /** {@inheritDoc} */
    public Object createHolder(final Map < String, Object > transformedParts)
            throws TransformerException {
        throw new TransformerException(getI18NMessages()
                .noMultiPartSupportFailure(), this);
    }

}
