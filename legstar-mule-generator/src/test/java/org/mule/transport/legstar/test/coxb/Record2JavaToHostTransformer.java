package org.mule.transport.legstar.test.coxb;

import com.legstar.coxb.CobolContext;
import com.legstar.coxb.ICobolComplexBinding;
import com.legstar.coxb.transform.AbstractJavaToHostTransformer;
import com.legstar.coxb.CobolBindingException;

/**
 * Transforms a Record2 data object to mainframe data.
 * <p/>
 * This is a typical use of this class:
 * <pre>
 *  Record2JavaToHostTransformer transformer = new Record2JavaToHostTransformer();
 *  byte[] hostByteArray = transformer.transform(javaValue);
 * </pre>
 *
 */
public class Record2JavaToHostTransformer extends AbstractJavaToHostTransformer {

    
    /**
     * Create a Java to Host transformer using default COBOL parameters.
     */
    public Record2JavaToHostTransformer() {
        super();
    }
    
    /**
     * Create a Java to Host transformer using a specific COBOL parameters set.
     * @param cobolContext the COBOL parameters set.
     */
    public Record2JavaToHostTransformer(final CobolContext cobolContext) {
        super(cobolContext);
    }

    /**
     * Create a Java to Host transformer using a specific host character set while
     * other COBOL parameters are set by default.
     * @param hostCharset the host character set
     */
    public Record2JavaToHostTransformer(final String hostCharset) {
        super(hostCharset);
    }
    
    /**
     * Binding is statically produced by {@link com.legstar.coxb.gen.CoxbBindingGenerator}.
     * @return a new binding corresponding to the host structure type.
     * @throws CobolBindingException if binding cannot be returned
     */
    public ICobolComplexBinding newBinding() throws CobolBindingException {
        return new Record2Binding();
    }
    
}
