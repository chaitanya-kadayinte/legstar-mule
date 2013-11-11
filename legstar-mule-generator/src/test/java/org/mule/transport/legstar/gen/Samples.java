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
package org.mule.transport.legstar.gen;

import org.apache.commons.lang.StringUtils;
import org.mule.transport.legstar.model.CixsMuleComponent;

import com.legstar.cixs.gen.model.CixsOperation;
import com.legstar.cixs.gen.model.CixsStructure;
import com.legstar.test.cixs.JvmqueryOperationCases;
import com.legstar.test.cixs.LsfileacOperationCases;
import com.legstar.test.cixs.LsfileaeOperationCases;
import com.legstar.test.cixs.LsfilealOperationCases;

/**
 * Helper class. Creates models ready for code generation.
 */
public final class Samples {

    /** Target package for generated Mule component. */
    public static final String LEGS4MULE_PKG_PREFIX = "org.mule.transport.legstar.test";

    /** Defeats instantiation. */
    private Samples() {

    }

    /**
     * Create a service without any operations.
     * 
     * @param serviceName the service name
     * @return a new service
     */
    public static CixsMuleComponent getNewService(final String serviceName) {
        CixsMuleComponent service = new CixsMuleComponent();
        service.setPackageName(LEGS4MULE_PKG_PREFIX + '.' + serviceName);
        service.setName(serviceName);
        return service;
    }

    /**
     * Case with a regular commarea.
     * 
     * @return a Mule component to serve as model for velocity templates.
     */
    public static CixsMuleComponent getLsfileaeMuleComponent() {
        CixsMuleComponent muleComponent = getNewService("lsfileae");
        muleComponent.getCixsOperations().add(
                LsfileaeOperationCases.getOperation(muleComponent.getName(),
                        muleComponent.getPackageName()));
        return muleComponent;
    }

    /**
     * @return a Mule component to serve as model for velocity templates.
     */
    public static CixsMuleComponent getLsfilealMuleComponent() {
        CixsMuleComponent muleComponent = getNewService("lsfileal");
        muleComponent.getCixsOperations().add(
                LsfilealOperationCases.getOperation(muleComponent.getName(),
                        muleComponent.getPackageName()));
        return muleComponent;
    }

    /**
     * @return a Mule component to serve as model for velocity templates.
     */
    public static CixsMuleComponent getLsfileacMuleComponent() {
        CixsMuleComponent muleComponent = getNewService("lsfileac");
        muleComponent.getCixsOperations().add(
                LsfileacOperationCases.getOperation(muleComponent.getName(),
                        muleComponent.getPackageName()));
        return muleComponent;
    }

    /**
     * @return a Mule component to serve as model for velocity templates.
     */
    public static CixsMuleComponent getLsfileaxMuleComponent() {
        CixsMuleComponent muleComponent = getNewService("lsfileax");
        muleComponent.getCixsOperations().add(
                LsfileaeOperationCases.getOperation("lsfileae",
                        muleComponent.getPackageName()));
        muleComponent.getCixsOperations().add(
                LsfileacOperationCases.getOperation("lsfileac",
                        muleComponent.getPackageName()));
        return muleComponent;
    }

    /**
     * @return a Mule component to serve as model for velocity templates.
     */
    public static CixsMuleComponent getJvmQueryMuleComponent() {
        CixsMuleComponent muleComponent = getNewService("jvmquery");
        muleComponent.getCixsOperations().add(
                JvmqueryOperationCases.getOperation(muleComponent.getName(),
                        muleComponent.getPackageName()));
        return muleComponent;
    }

    /**
     * @return a Mule component with one operation an multiple inputs/outputs.
     */
    public static CixsMuleComponent getMultiStructMuleComponent() {
        CixsMuleComponent muleComponent = getNewService("multistruct");
        muleComponent.getCixsOperations().add(
                getMultiStructOperation(muleComponent.getName(),
                        muleComponent.getPackageName()));
        return muleComponent;
    }

    /**
     * @param serviceName the service name
     * @param operationPackageName the operation classes package name
     * @return an operation corresponding to a Web Service operation.
     */
    private static CixsOperation getMultiStructOperation(
            final String serviceName, final String operationPackageName) {
        CixsOperation cixsOperation = new CixsOperation();
        cixsOperation.setName(serviceName);
        cixsOperation.setCicsProgramName(StringUtils.upperCase(StringUtils
                .substring(serviceName, 0, 8)));
        cixsOperation.setPackageName(operationPackageName);

        CixsStructure recor1Struc = new CixsStructure();
        recor1Struc.setJaxbType("Record1");
        recor1Struc.setJaxbPackageName("org.mule.transport.legstar.test.jaxb");
        recor1Struc.setCoxbPackageName("org.mule.transport.legstar.test.coxb");
        CixsStructure record2Struc = new CixsStructure();
        record2Struc.setJaxbType("Record2");
        record2Struc.setJaxbPackageName("org.mule.transport.legstar.test.jaxb");
        record2Struc.setCoxbPackageName("org.mule.transport.legstar.test.coxb");

        cixsOperation.addInput(recor1Struc);
        cixsOperation.addInput(record2Struc);
        cixsOperation.addOutput(recor1Struc);
        cixsOperation.addOutput(record2Struc);

        return cixsOperation;
    }
}
