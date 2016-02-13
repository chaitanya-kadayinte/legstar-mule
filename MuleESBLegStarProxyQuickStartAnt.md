# QuickStart description #

The purpose of this quickstart document is to quickly build a functioning
sample integrating a backend IBM CICS system with a Mule ESB service.

Once deployed, the generated ESB service will act as a proxy for
a CICS COBOL program. In this example, the COBOL program acts as a client and
the proxy ESB service invokes a POJO (plain old java object) method on
behalf of the COBOL client.

![http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-proxy-http.png](http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-proxy-http.png)

If you would rather use WebSphere MQ instead of HTTP as the wire protocol to reach Mule from
the mainframe then the Mule configuration will look like:

![http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-proxy-wmq.png](http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-proxy-wmq.png)

# Installation #

Follow instructions in:[InstallInstructions](InstallInstructions.md)

Under the samples/mule/quickstarts/legstar\_proxy/src folder of your legstar-mule installation, the
com.legstar.xsdc.test.cases.jvmquery package contains a sample POJO called
**JVMQuery.java**. As the name implies, JVMQuery queries a JVM for system variables such as environment variables and locale.
A caller can specify an array of environment variables for which values should be queried. If you look at the **queryJvm**
method, you will notice that it takes a single data object called **JVMQueryRequest** as input and produces a data object
called **JVMQueryReply**.

This example uses HTTP connectivity from CICS to Mule ESB using the LegStar transport. Alternatively, you can use Websphere MQ
and the Mule JMS transport.

# Generate a Mule ESB service proxy for a mainframe program #

These steps will guide you through the process of generating an ESB proxy
using ant scripts. There is an alternative method, using Eclipse plugins,
described in [MuleESBLegStarProxyQuickStartEclipse](MuleESBLegStarProxyQuickStartEclipse.md).

We will be generating all the artifacts that are needed to give a CICS
COBOL program the ability to call the **queryJvm** method of JVMQuery.

Go to folder samples/mule/quickstarts/legstar\_proxy.

We first need to compile and deploy our test target POJO (The one we
want to access from the mainframe):

  * From the ant folder, run command **ant -f build-pojo.xml**.

> This will compile the target POJO in the **bin** folder.

> This will also create an archive called legstar-jvmquery-pojo.jar under the **dist** folder.

  * If you are using LegStar for Mule 2.x, copy the legstar-jvmquery-pojo.jar archive to $MULE\_HOME/lib/user. With LegStar for Mule 3.x it will be bundled automatically in the application archive.

We can now generate the various Mule artifacts.

First step is to generate an XML schema from the target Java data objects
**JVMQueryRequest** and **JVMQueryReply**.

  * From the ant folder, run command **ant -f build-java2xs.xml**.

> This should create a schema folder with a generated XML schema jvmquery.xsd.

If you open jvmquery.xsd you will notice that each Java data object has been
mapped to a COBOL data structure as well as an XML Schema type.

Next steps turn the XML schema types into COBOL transformation classes.

  * Run command **ant -f build-coxb.xml**.

> As a result, the src folder contains generated JAXB classes which
> implement COBOL to Java transformers.
> These were derived from the generated XML Schema in the schema folder.
> Sources are also compiled, with binaries stored under the bin folder.

The last step generates the legstar-jvmquery Mule ESB service.

  * From the ant folder run command **ant -f build-cixs2mule.xml**.

Under src/com/legstar/test/cixs/mule/jvmquery you will find the Mule ESB transformers for serialized java object payloads. These transformers can be used in any Mule
configuration and are independent from the transport chosen.

Under legstar\_proxy, a Mule configuration sample is generated:

  * mule-proxy-config-jvmquery-http.xml (LegStar for Mule 2.x) or mule-config.xml (LegStar for Mule 3.x)

> This implements a Request/Response exchange pattern.
> Upon receiving data from the mainframe, a transformer turns that data into a **JvmQueryRequest**
> java data object. On return from invoking the POJO, another transformer turns the **JvmQueryReply**
> java data object into a mainframe reply.

> Check the connectivity parameters in the configuration file to see if they fit your needs.

The ant folder contains generated ant scripts which bundles the
proxy for deployment, and the deploy.xml ant script which deploys the generated service
to your Mule ESB installation (Defined by the MULE\_HOME environment variable).

  * Go ahead and run **ant -f deploy.xml**, this will bundle and deploy the new service.

# Running the ESB service proxy #

You can now start Mule ESB and check logs for any errors. With LegStar for Mule 2.x you will need to point Mule to the mule-proxy-config-jvmquery-http.xml configuration file. This is not necessary with LegStar for Mule 3.x where the service benefits from hot deployment.

The **cobol** folder contains a generated skeleton COBOL CICS program
that you can use to test the ESB proxy. Edit this code and perform the
following changes:

  * Change the W00-SERVICE-URI size and value to point to the server running Mule ESB.
  * Right after the "TODO set input values in COM-REQUEST" comment, insert these lines of code:

```
           MOVE 2 TO envVarNames--C OF COM-REQUEST.           
           MOVE 'MULE_HOME' TO envVarNames OF COM-REQUEST(1). 
           MOVE 'JAVA_HOME' TO envVarNames OF COM-REQUEST(2).
```
  * Right after the "TODO do something useful with data returned in COM-REPLY" comment, insert these lines of code:

```
           STRING 'INVOKE-SERVICE success. Server language is '     
                  DELIMITED BY SIZE                                 
                  language OF COM-REPLY                             
                  DELIMITED BY SPACE                                
                  INTO ERROR-MESSAGE.                               
           EXEC CICS SEND TEXT FROM(ERROR-MESSAGE) FREEKB END-EXEC. 
                                                                    
           DISPLAY 'country=' country OF COM-REPLY.                 
           DISPLAY 'currencySymbol=' currencySymbol OF COM-REPLY.   
           DISPLAY 'formattedDate=' formattedDate OF COM-REPLY.     
           DISPLAY 'language=' language OF COM-REPLY.               
           DISPLAY 'envVarValues--C=' envVarValues--C OF COM-REPLY. 
           DISPLAY 'envVarValues(1)=' envVarValues OF COM-REPLY (1).
           DISPLAY 'envVarValues(2)=' envVarValues OF COM-REPLY (2).
```
You should now be able to upload this program onto your mainframe and
get it compiled and defined to your CICS region. Please note that this program
calls the CICS DFHWBCLI program defined in the CICS standard DFHWEB group.
Alternatively, legstar-mule supports the new EXEC CICS WEB API or even
supports older version of CICS with its own HTTP library.

If you associate the JVMQUERY program to a CICS transaction and run that
transaction, it should return with a short message telling you which locale
the Mule server is actually using. The standard CICS/LE CEEMSG and CEEOUT
should show detailed traces.