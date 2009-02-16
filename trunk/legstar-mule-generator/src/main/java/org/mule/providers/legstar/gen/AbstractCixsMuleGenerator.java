/*******************************************************************************
 * $Id$
 * -----------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc. All rights reserved. http://www.mulesource.com
 * 
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file
 ******************************************************************************/
package org.mule.providers.legstar.gen;

import java.io.File;
import java.util.Map;

import org.mule.providers.legstar.model.CixsMuleComponent;
import org.mule.providers.legstar.model.AbstractAntBuildCixsMuleModel;

import com.legstar.cixs.gen.ant.AbstractCixsGenerator;
import com.legstar.cixs.gen.model.CixsOperation;
import com.legstar.cixs.jaxws.gen.Cixs2JaxwsGenerator;
import com.legstar.cixs.jaxws.model.CobolHttpClientType;
import com.legstar.codegen.CodeGenMakeException;
import com.legstar.codegen.CodeGenUtil;

/**
 * This class groups methods that are common to all generators.
 */
public abstract class AbstractCixsMuleGenerator extends AbstractCixsGenerator {

    /** This generator name. */
    public static final String CIXS_MULE_GENERATOR_NAME =
        "LegStar Mule Component generator";

    /** Velocity template for callable invoker implementation. */
    public static final String COMPONENT_CALLABLE_INVOKER_VLC_TEMPLATE =
        "vlc/cixsmule-component-callable-invoker.vm";

    /** Velocity template for ant build jar. */
    public static final String COMPONENT_ANT_BUILD_JAR_VLC_TEMPLATE =
        "vlc/cixsmule-component-ant-build-jar-xml.vm";

    /** Velocity template for standalone mule configuration xml. */
    public static final String COMPONENT_ADAPTER_STANDALONE_CONFIG_XML_VLC_TEMPLATE =
        "vlc/cixsmule-component-adapter-standalone-config-xml.vm";

    /** Velocity template for bridge mule configuration xml. */
    public static final String COMPONENT_ADAPTER_BRIDGE_CONFIG_XML_VLC_TEMPLATE =
        "vlc/cixsmule-component-adapter-bridge-config-xml.vm";

    /** Velocity template for local mule configuration xml. */
    public static final String COMPONENT_PROXY_CONFIG_XML_VLC_TEMPLATE =
        "vlc/cixsmule-component-proxy-config-xml.vm";

    /** Velocity template for holder. */
    public static final String OPERATION_HOLDER_VLC_TEMPLATE =
        "vlc/cixsmule-operation-holder.vm";

    /** Velocity template for object to host byte array transformer. */
    public static final String OPERATION_OBJECT_TO_HBA_VLC_TEMPLATE =
        "vlc/cixsmule-operation-object-to-hba-transformer.vm";

    /** Velocity template for host byte array to object transformer. */
    public static final String OPERATION_HBA_TO_OBJECT_VLC_TEMPLATE =
        "vlc/cixsmule-operation-hba-to-object-transformer.vm";

    /** Velocity template for object to http response transformer. */
    public static final String OPERATION_OBJECT_TO_HTTP_RESPONSE_VLC_TEMPLATE =
        "vlc/cixsmule-operation-object-to-http-response-transformer.vm";

    /** Velocity template for program invoker class. */
    public static final String OPERATION_PROGRAM_INVOKER_VLC_TEMPLATE =
        "vlc/cixsmule-operation-program-invoker.vm";

    /** The service model name is it appears in templates. */
    public static final String COMPONENT_MODEL_NAME = "muleComponent";

    /**
     * Constructor.
     * @param model an instance of a generation model
     */
    public AbstractCixsMuleGenerator(final AbstractAntBuildCixsMuleModel model) {
        super(model);
    }

    /**
     * Create the Mule callable invoker class file.
     * @param component the Mule component description
     * @param parameters miscellaneous help parameters
     * @param componentClassFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateAdapterCallableInvoker(
            final CixsMuleComponent component,
            final Map < String, Object > parameters,
            final File componentClassFilesDir)
    throws CodeGenMakeException {
        generateFile(CIXS_MULE_GENERATOR_NAME,
                COMPONENT_CALLABLE_INVOKER_VLC_TEMPLATE,
                COMPONENT_MODEL_NAME,
                component,
                parameters,
                componentClassFilesDir,
                component.getInterfaceClassName() + "Callable.java");
    }

    /**
     * Create a holder classes for channel/containers.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateHolders(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir)
    throws CodeGenMakeException {

        if (operation.getCicsChannel() == null
                || operation.getCicsChannel().length() == 0) {
            return;
        }

        if (operation.getInput().size() > 0) {
            parameters.put("propertyName", "Request");
            generateFile(CIXS_MULE_GENERATOR_NAME,
                    OPERATION_HOLDER_VLC_TEMPLATE,
                    "cixsOperation",
                    operation,
                    parameters,
                    operationClassFilesDir,
                    operation.getRequestHolderType() + ".java");
        }
        if (operation.getOutput().size() > 0) {
            parameters.put("propertyName", "Response");
            generateFile(CIXS_MULE_GENERATOR_NAME,
                    OPERATION_HOLDER_VLC_TEMPLATE,
                    "cixsOperation",
                    operation,
                    parameters,
                    operationClassFilesDir,
                    operation.getResponseHolderType() + ".java");
        }
    }

    /**
     * Create the Mule Ant Build Jar file.
     * @param component the Mule component description
     * @param parameters miscellaneous help parameters
     * @param componentAntFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateAntBuildJar(
            final CixsMuleComponent component,
            final Map < String, Object > parameters,
            final File componentAntFilesDir)
    throws CodeGenMakeException {
        generateFile(CIXS_MULE_GENERATOR_NAME,
                COMPONENT_ANT_BUILD_JAR_VLC_TEMPLATE,
                COMPONENT_MODEL_NAME,
                component,
                parameters,
                componentAntFilesDir,
        "build.xml");
    }

    /**
     * Create the Mule adapter stand alone configuration XML file.
     * @param component the Mule component description
     * @param parameters miscellaneous help parameters
     * @param componentConfFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateAdapterStandaloneConfigXml(
            final CixsMuleComponent component,
            final Map < String, Object > parameters,
            final File componentConfFilesDir)
    throws CodeGenMakeException {
        generateFile(CIXS_MULE_GENERATOR_NAME,
                COMPONENT_ADAPTER_STANDALONE_CONFIG_XML_VLC_TEMPLATE,
                COMPONENT_MODEL_NAME,
                component,
                parameters,
                componentConfFilesDir,
                "mule-adapter-standalone-config-" + component.getName() + ".xml");
    }

    /**
     * Create objects to host byte array transformer for both request
     * and response objects.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateObjectToHbaTransformers(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir)
    throws CodeGenMakeException {

        if (operation.getInput().size() > 0) {
            generateObjectToHbaTransformer(operation, parameters,
                    operationClassFilesDir,
                    operation.getRequestHolderType(),
            "Request");
        }
        if (operation.getOutput().size() > 0) {
            generateObjectToHbaTransformer(operation, parameters,
                    operationClassFilesDir,
                    operation.getResponseHolderType(),
            "Response");
        }
    }

    /**
     * Create an object to host byte array transformer.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @param holderType the Java class name for the holder
     * @param propertyName either Request or Response
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateObjectToHbaTransformer(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir,
            final String holderType,
            final String propertyName)
    throws CodeGenMakeException {

        parameters.put("propertyName", propertyName);
        generateFile(CIXS_MULE_GENERATOR_NAME,
                OPERATION_OBJECT_TO_HBA_VLC_TEMPLATE,
                "cixsOperation",
                operation,
                parameters,
                operationClassFilesDir,
                holderType + "ToHostByteArray.java");
    }

    /**
     * Create host byte array to objects transformer for both request
     * and response objects.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateHbaToObjectTransformers(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir)
    throws CodeGenMakeException {

        if (operation.getInput().size() > 0) {
            generateHbaToObjectTransformer(operation, parameters,
                    operationClassFilesDir,
                    operation.getRequestHolderType(),
            "Request");
        }
        if (operation.getOutput().size() > 0) {
            generateHbaToObjectTransformer(operation, parameters,
                    operationClassFilesDir,
                    operation.getResponseHolderType(),
            "Response");
        }
    }

    /**
     * Create a host byte array to object transformer.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @param holderType the Java class name for the holder
     * @param propertyName either Request or Response
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateHbaToObjectTransformer(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir,
            final String holderType,
            final String propertyName)
    throws CodeGenMakeException {

        parameters.put("propertyName", propertyName);
        generateFile(CIXS_MULE_GENERATOR_NAME,
                OPERATION_HBA_TO_OBJECT_VLC_TEMPLATE,
                "cixsOperation",
                operation,
                parameters,
                operationClassFilesDir,
                "HostByteArrayTo" + holderType + ".java");
    }

    /**
     * Create objects to http response transformer for both request
     * and response objects.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateObjectToHttpResponseTransformers(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir)
    throws CodeGenMakeException {

        if (operation.getInput().size() > 0) {
            generateObjectToHttpResponseTransformer(operation, parameters,
                    operationClassFilesDir,
                    operation.getRequestHolderType(),
            "Request");
        }
        if (operation.getOutput().size() > 0) {
            generateObjectToHttpResponseTransformer(operation, parameters,
                    operationClassFilesDir,
                    operation.getResponseHolderType(),
            "Response");
        }
    }

    /**
     * Create an object to http response transformer.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @param holderType the Java class name for the holder
     * @param propertyName either Request or Response
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateObjectToHttpResponseTransformer(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir,
            final String holderType,
            final String propertyName)
    throws CodeGenMakeException {

        parameters.put("propertyName", propertyName);
        generateFile(CIXS_MULE_GENERATOR_NAME,
                OPERATION_OBJECT_TO_HTTP_RESPONSE_VLC_TEMPLATE,
                "cixsOperation",
                operation,
                parameters,
                operationClassFilesDir,
                holderType + "ToHttpResponse.java");
    }

    /**
     * Create the Mule adapter bridge configuration XML file.
     * @param component the Mule component description
     * @param parameters miscellaneous help parameters
     * @param componentConfFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateAdapterBridgeConfigXml(
            final CixsMuleComponent component,
            final Map < String, Object > parameters,
            final File componentConfFilesDir)
    throws CodeGenMakeException {
        generateFile(CIXS_MULE_GENERATOR_NAME,
                COMPONENT_ADAPTER_BRIDGE_CONFIG_XML_VLC_TEMPLATE,
                COMPONENT_MODEL_NAME,
                component,
                parameters,
                componentConfFilesDir,
                "mule-adapter-bridge-config-" + component.getName() + ".xml");
    }

    /**
     * Create the Mule proxy configuration XML file.
     * @param component the Mule component description
     * @param parameters miscellaneous help parameters
     * @param componentConfFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     */
    public static void generateProxyConfigXml(
            final CixsMuleComponent component,
            final Map < String, Object > parameters,
            final File componentConfFilesDir)
    throws CodeGenMakeException {
        generateFile(CIXS_MULE_GENERATOR_NAME,
                COMPONENT_PROXY_CONFIG_XML_VLC_TEMPLATE,
                COMPONENT_MODEL_NAME,
                component,
                parameters,
                componentConfFilesDir,
                "mule-proxy-config-" + component.getName() + ".xml");
    }

    /**
     * Create a COBOl CICS HTTP Client program to use for testing.
     * @param component the Mule service description
     * @param operation the operation for which a program is to be generated
     * @param parameters the set of parameters to pass to template engine
     * @param cobolFilesDir location where COBOL code should be generated
     * @param cobolHttpClientType the type of cobol http client to generate
     * @throws CodeGenMakeException if generation fails
     */
    protected static void generateCobolSampleHttpClient(
            final CixsMuleComponent component,
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File cobolFilesDir,
            final CobolHttpClientType cobolHttpClientType) throws CodeGenMakeException {
        String template;
        switch(cobolHttpClientType) {
        case DFHWBCLI:
            template = Cixs2JaxwsGenerator.OPERATION_COBOL_CICS_DFHWBCLI_CLIENT_VLC_TEMPLATE;
            break;
        case WEBAPI:
            template = Cixs2JaxwsGenerator.OPERATION_COBOL_CICS_WEBAPI_CLIENT_VLC_TEMPLATE;
            break;
        default:
            template = Cixs2JaxwsGenerator.OPERATION_COBOL_CICS_LSHTTAPI_CLIENT_VLC_TEMPLATE;
        }
        generateFile(CIXS_MULE_GENERATOR_NAME,
                template,
                COMPONENT_MODEL_NAME,
                component,
                parameters,
                cobolFilesDir,
                operation.getCicsProgramName() + ".cbl");
    }

    /**
     * Create a program invoker class.
     * @param operation the cixs operation
     * @param parameters miscellaneous help parameters
     * @param operationClassFilesDir where to store the generated file
     * @throws CodeGenMakeException if generation fails
     * TODO merge with LegStar when it is ready
     */
    public static void generateProgramInvoker(
            final CixsOperation operation,
            final Map < String, Object > parameters,
            final File operationClassFilesDir)
    throws CodeGenMakeException {
        generateFile(CIXS_MULE_GENERATOR_NAME,
                OPERATION_PROGRAM_INVOKER_VLC_TEMPLATE,
                "cixsOperation",
                operation,
                parameters,
                operationClassFilesDir,
                operation.getClassName() + "ProgramInvoker.java");
    }

    /**
     * Check input values that are common to all derived classes.
     * Check that we are provided with valid locations to
     * generate in.
     * @throws CodeGenMakeException if input is invalid
     */
    public final void checkExtendedInput() throws CodeGenMakeException {

        CodeGenUtil.checkDirectory(
                getTargetSrcDir(), true, "TargetSrcDir");
        CodeGenUtil.checkDirectory(
                getTargetAntDir(), true, "TargetAntDir");
        CodeGenUtil.checkDirectory(
                getTargetMuleConfigDir(), true, "TargetMuleConfigDir");

        /* Check that we are provided with valid locations to
         * reference.*/
        if (getTargetBinDir() == null) {
            throw (new IllegalArgumentException(
            "TargetBinDir: No directory name was specified"));
        }
        if (getTargetJarDir() == null) {
            throw (new IllegalArgumentException(
            "TargetJarDir: No directory name was specified"));
        }

        /* Check that we have at least one operation. */
        if (getCixsMuleComponent().getCixsOperations().size() == 0) {
            throw new CodeGenMakeException(
            "No operation was specified");
        }

        /* Check that we have CICS program names mapped to operations */
        for (CixsOperation operation 
                : getCixsMuleComponent().getCixsOperations()) {
            String cicsProgramName = operation.getCicsProgramName();
            if (cicsProgramName == null || cicsProgramName.length() == 0) {
                throw new CodeGenMakeException(
                "Operation must specify a CICS program name");
            }
        }

        checkExtendedExtendedInput();

    }

    /**
     * Give the inheriting generators a chance to add more controls.
     * @throws CodeGenMakeException if control fails
     */
    public abstract void checkExtendedExtendedInput() throws CodeGenMakeException;

    /**
     * Create all artifacts for Mule service.
     * @param parameters a predefined set of parameters useful for generation
     * @throws CodeGenMakeException if generation fails
     */
    public final void generate(
            final Map < String, Object > parameters) throws CodeGenMakeException {

        addParameters(parameters);

        /* Determine target files locations */
        File serviceAntFilesDir = getTargetAntDir();

        generateAntBuildJar(
                getCixsMuleComponent(), parameters, serviceAntFilesDir);

        generateExtended(parameters);
    }

    /**
     * Create more artifacts for a Mule component.
     * @param parameters a predefined set of parameters useful for generation
     * @throws CodeGenMakeException if generation fails
     */
    public abstract void generateExtended(
            final Map < String, Object > parameters) throws CodeGenMakeException;


    /**
     * Add common parameters expected by templates to come from a parameters map.
     * @param parameters a parameters map to which parameters must be added
     */
    private void addParameters(final Map < String, Object > parameters) {
        parameters.put("generateBaseDir", getGenerateBuildDir());
        parameters.put("targetJarDir", getTargetJarDir());
        parameters.put("targetMuleConfigDir", getTargetMuleConfigDir());
        parameters.put("hostCharset", getHostCharset());

        addExtendedParameters(parameters);
    }


    /**
     * Add common parameters expected by templates to come from a parameters map.
     * @param parameters a parameters map to which parameters must be added
     */
    public abstract void addExtendedParameters(final Map < String, Object > parameters);

    /**
     * @return the Mule component 
     */
    public final CixsMuleComponent getCixsMuleComponent() {
        return (CixsMuleComponent) getCixsService();
    }

    /**
     * @param cixsMuleComponent the Mule component to set
     */
    public final void setCixsMuleComponent(
            final CixsMuleComponent cixsMuleComponent) {
        setCixsService(cixsMuleComponent);
    }

    /**
     * @param cixsMuleComponent the Mule component to set
     */
    public final void add(final CixsMuleComponent cixsMuleComponent) {
        setCixsMuleComponent(cixsMuleComponent);
    }

    /**
     * @param cixsMuleComponent the Mule component to set
     */
    public final void addCixsMuleComponent(
            final CixsMuleComponent cixsMuleComponent) {
        setCixsMuleComponent(cixsMuleComponent);
    }

    /**
     * @return the target mule jar files location
     */
    public final File getTargetJarDir() {
        return getAntModel().getTargetJarDir();
    }

    /**
     * @param targetJarDir the target mule jar files location to set
     */
    public final void setTargetJarDir(final File targetJarDir) {
        getAntModel().setTargetJarDir(targetJarDir);
    }

    /**
     * @return the target configuration files location
     */
    public final File getTargetMuleConfigDir() {
        return getAntModel().getTargetMuleConfigDir();
    }

    /**
     * @param targetMuleConfigDir the target configuration files location to set
     */
    public final void setTargetMuleConfigDir(final File targetMuleConfigDir) {
        getAntModel().setTargetMuleConfigDir(targetMuleConfigDir);
    }

    /**
     * @return the model representing all generation parameters
     */
    public AbstractAntBuildCixsMuleModel getAntModel() {
        return (AbstractAntBuildCixsMuleModel) super.getAntModel();
    }

    /** {@inheritDoc} */
    public final String getGeneratorName() {
        return CIXS_MULE_GENERATOR_NAME;
    }

    /**
     * @return the directory from which this ant script is start
     */
    public final String getGenerateBuildDir() {
        if (getProject() == null) {
            return ".";
        } else {
            return getProject().getBaseDir().getAbsolutePath();
        }
    }

}