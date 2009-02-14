package org.mule.providers.legstar.transformers;

import org.mule.impl.RequestContext;
import org.mule.tck.AbstractTransformerTestCase;
import org.mule.umo.UMOEvent;
import org.mule.umo.transformer.UMOTransformer;

import com.legstar.coxb.host.HostData;
import com.legstar.test.coxb.LsfileaeCases;
import com.legstar.test.coxb.lsfileae.Dfhcommarea;
import com.legstar.test.coxb.lsfileae.bind.DfhcommareaTransformers;

/**
 * Test AbstractJavaToHostTransformer class.
 *
 */
public class LsfileaeJavaToLegStarMessageTransformerTest extends AbstractTransformerTestCase {

    /** A LegStarMessage serialization. */
    private static final String LSFILEAE_MESSAGE_HOST_DATA =
        /*L S O K H E A D                        8       1      0 L S O K C O M M A R E A         */
        "d3e2d6d2c8c5c1c44040404040404040000000080000000100000000d3e2d6d2c3d6d4d4c1d9c5c140404040"
        /*      79 0 0 0 1 0 0 T O T O                                 L A B A S   S T R E E T                */
        + "0000004ff0f0f0f1f0f0e3d6e3d640404040404040404040404040404040d3c1c2c1e240e2e3d9c5c5e34040404040404040"
        /* 8 8 9 9 3 3 1 4 1 0 0 4 5 8     0 0 1 0 0 . 3 5 A   V O I R      */
        + "f8f8f9f9f3f3f1f4f1f0f0f4f5f84040f0f0f1f0f04bf3f5c140e5d6c9d9404040";

    /**
     * A simplistic implementation of the abstract class being tested.
     *
     */
    public static class LsfileaeJavaToHostTransformer extends AbstractJavaToHostEsbTransformer {

        /**
         * Pass binding transformers for lsfileae single part Dfhcommarea.
         */
        public LsfileaeJavaToHostTransformer() {
            super(new DfhcommareaTransformers());
            registerSourceType(Dfhcommarea.class);
        }

    }

    /** {@inheritDoc} */
    protected void doSetUp() throws Exception {
        /* Override to set message properties */
        UMOEvent event = getTestEvent("test");
        event.getMessage().setBooleanProperty(
                AbstractHostEsbTransformer.IS_LEGSTAR_MESSAGING, true);
        RequestContext.setEvent(event);
    }

    /** {@inheritDoc} */
    public UMOTransformer getTransformer() throws Exception {
        return new LsfileaeJavaToHostTransformer();
    }

    /** {@inheritDoc} */
    public Object getResultData() {
        return HostData.toByteArray(LSFILEAE_MESSAGE_HOST_DATA);
    }

    /** {@inheritDoc} */
    public UMOTransformer getRoundTripTransformer() throws Exception {
        return null;
    }

    /** {@inheritDoc} */
    public Object getTestData() {
        return LsfileaeCases.getJavaObject();
    }

}
