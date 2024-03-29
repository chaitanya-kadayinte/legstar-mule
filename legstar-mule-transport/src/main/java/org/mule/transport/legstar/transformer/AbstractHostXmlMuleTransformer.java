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

import java.util.List;
import java.util.Map;

import com.legstar.coxb.transform.AbstractTransformers;
import com.legstar.coxb.transform.AbstractXmlTransformers;

/**
 * Transform XML to and from host data.
 * <p/>
 * Esb messages, in and out, are assumed to hold XML content at the default
 * body location.
 */
public abstract class AbstractHostXmlMuleTransformer extends AbstractHostMuleTransformer {

    /**
     * Single part transformers are associated with a set of binding transformers
     * which are responsible for marshaling/unmarshaling the data payload.
     */
    private final AbstractXmlTransformers mXmlBindingTransformers;

    /**
     * Multi-part transformers are associated with a map of binding transformers
     * which are responsible to marshaling/unmarshaling the data payload.
     * Each binding transformers set is identified by a unique name.
     */
    private final Map < String, AbstractXmlTransformers > mXmlBindingTransformersMap;

    /**
     * Multi-structures transformers are associated with a list of binding
     * transformers which are responsible to marshaling/unmarshaling the data
     * payload.
     */
    private final List < AbstractTransformers > mBindingTransformersList;
    
    /**
     * Constructor for single part transformers.
     * @param xmlBindingTransformers a set of transformers for the part type.
     */
    public AbstractHostXmlMuleTransformer(
            final AbstractXmlTransformers xmlBindingTransformers) {
        mXmlBindingTransformers = xmlBindingTransformers;
        mXmlBindingTransformersMap = null;
        mBindingTransformersList = null;
    }
    
    /**
     * Constructor for multi-part transformers.
     * @param xmlBindingTransformersMap map of transformer sets, one for each part type.
     */
    public AbstractHostXmlMuleTransformer(
            final Map < String, AbstractXmlTransformers > xmlBindingTransformersMap) {
        mXmlBindingTransformers = null;
        mXmlBindingTransformersMap = xmlBindingTransformersMap;
        mBindingTransformersList = null;
    }

    /**
     * Constructor for multi-structures transformers.
     * 
     * @param bindingTransformersList list of transformers, to be applied in
     *            sequence.
     */
    public AbstractHostXmlMuleTransformer(
            final List < AbstractTransformers > bindingTransformersList) {
        mXmlBindingTransformers = null;
        mXmlBindingTransformersMap = null;
        mBindingTransformersList = bindingTransformersList;
    }

    /**
     * @return the transformers set to use for xml to host transformations
     */
    public AbstractXmlTransformers getXmlBindingTransformers() {
        return mXmlBindingTransformers;
    }

    /**
     * @return the transformers sets map to use for xml to host transformations
     */
    public Map < String, AbstractXmlTransformers > getXmlBindingTransformersMap() {
        return mXmlBindingTransformersMap;
    }

    /**
     * @return the list of transformers for java to host transformations
     */
    public List < AbstractTransformers > getBindingTransformersList() {
        return mBindingTransformersList;
    }

}
