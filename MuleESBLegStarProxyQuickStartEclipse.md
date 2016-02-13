# QuickStart description #

This is an alternative to [MuleESBLegStarProxyQuickStartAnt](MuleESBLegStarProxyQuickStartAnt.md)
using Eclipse plugins rather than ant scripts.

Using Eclipse plugins, we will walk you through the process of integrating a backend IBM CICS system with a Mule ESB service.

Once deployed, the generated ESB service will act as a proxy for
a CICS COBOL program. In this example, the COBOL program acts as a client and
the proxy ESB service invokes a POJO (plain old java object) method on
behalf of the COBOL client.

![http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-proxy-http.png](http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-proxy-http.png)

# Installation #

Follow instructions in:[InstallInstructions](InstallInstructions.md).

Under the samples/mule/quickstarts/legstar\_proxy/src folder of your legstar-mule installation, the
com.legstar.xsdc.test.cases.jvmquery package contains a sample POJO called
**JVMQuery.java**. As the name implies, JVMQuery queries a JVM for system variables such as environment variables and locale.
A caller can specify an array of environment variables for which values should be queried. If you look at the **queryJvm**
method, you will notice that it takes a single data object called **JVMQueryRequest** as input and produces a data object
called **JVMQueryReply**.

This example uses HTTP connectivity from CICS to Mule ESB using the LegStar transport. Alternatively, you can use Websphere MQ
and the Mule JMS transport.

Start Eclipse and select menu option
```
Help-->Software Updates-->Find and Install...
```

From there check the **Search for new features to install** option and click next.

Create a **New Remote Site** named legstar-mule and type the URL depending on the version of LegStar for Mule you want to use:

  * LegStar for Mule 3.x: http://www.legsem.com/legstar/mule/eclipse/update

  * LegStar for Mule 2.x: http://www.legsem.com/legstar/mule/eclipse/update/2.x

With this new site selected, when you press the finish button, you are
presented with all the plugins you need. Select the entire LegStar group,
click next, accept the license terms and then click finish which should
end the installation process.

# Generate a Mule ESB service proxy for a mainframe program #

The first step is to deploy the target POJO to the ESB.

In a command window, go to the product installation location folder called **samples/mule/quickstarts/legstar\_proxy**.

From the ant folder, run command **ant -f build-pojo.xml**, this will compile
the target POJO and will bundle the POJO in a jar archive in the **dist** folder.

If you are using LegStar for Mule 2.x, copy the legstar-jvmquery-pojo.jar archive to $MULE\_HOME/lib/user. With LegStar for Mule 3.x it will be bundled automatically in the application archive.

In Eclipse, create a new Java project called MuleJvmquery and select the project.

Update the project build path by adding the legstar-jvmquery-pojo.jar external archive. This
jar file was generated in **samples/mule/quickstarts/legstar\_proxy/dist** in the previous step.

The sources for the sample POJO are in the **samples/mule/quickstarts/legstar\_proxy/src** folder of the
product installation location, under package name **com.legstar.xsdc.test.cases.jvmquery**:

The JVMQuery POJO exports a single **queryJvm** method. It takes a single data object called **JVMQueryRequest** as
input and produces a data object called **JVMQueryReply**.

We will be generating all the artifacts that are needed to give a CICS
COBOL program the ability to call the **queryJvm** method.

## Map the Java data objects to XML Schema ##

First step is to generate an XML schema from the target Java data objects
**JVMQueryRequest** and **JVMQueryReply**.

Select the Structures Mapping plugin:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectStructuresMapping.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectStructuresMapping.png)

Then select the "Java classes" source type and type the name of the target XML Schema, jvmquery.xsd:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseJavaClassesMappingPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseJavaClassesMappingPage1.png)

Click next and click on the **Select java classes from workbench** link. This
will pop up a class selection dialog which is empty at first. Type **JVMQuery** and the dialog will list all matching classes.

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseJavaClassesMappingPage2.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseJavaClassesMappingPage2.png)

Select **JVMQueryRequest** and **JVMQueryReply** then click OK. You are presented with a recap of the classes you selected:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseJavaClassesMappingPage3.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseJavaClassesMappingPage3.png)

Click on the Finish button, this starts the mapping process and the generated
XML Schema is opened. You will notice that each Java data object has been
mapped to a COBOL data structure as well as an XML Schema type.

## Bind the XML Schema to Java annotated classes ##

Next step turns the XML schema types into Java annotated classes.

From the package explorer view, expand your Java project. The generated
jvmquery.xsd file should appear. Select the file, right click to show
the context menu and then select LegStar-->Generate Transformers:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyContextMenuSelectBinding.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyContextMenuSelectBinding.png)

jvmquery.xsd contains 2 complex types, select them both and click
Finish:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyBindingPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyBindingPage1.png)

Your package explorer should now display 2 new packages. A library containing
the LegStar dependencies should have been added to the project Build path.

If you open **JvmQueryReply.java**, you will notice that classes exhibit
annotations that bind java types both to XML and COBOL:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyBindingResults.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyBindingResults.png)

## Generate an ESB service proxy for the JVMQuery POJO ##

The last step generates the legstar-jvmquery Mule ESB service.

With your MuleJvmquery project selected, select the Operations mapping plugin:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectOperationsMapping.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectOperationsMapping.png)

Enter name jvmquery.cixs for the operations mapping file.

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage1.png)

After you click finish, use the Add... button to create a new operation. The operation
name can be any name you please but we will be using the target POJO method name.
In our case, the **JVMQuery** POJO exposes a method called **queryJvm** so type in operation
name queryJvm:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage2.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage2.png)

An operation maps to a mainframe program in a request/response exchange pattern.
What you need to do at this stage is specify the input and output structures. You
select these structures from the set that was bound during the previous step.
These are displayed when you click on the Add button, successively for Input and
for Output:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage3.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage3.png)

In our case, we select **JVMQueryRequest** for Input and **JVMQueryReply** for Output,
because the JVMQuery POJO expects these data objects as input and output:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage4.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseProxyOperationsMappingPage4.png)

After you click on ok, the Generate button is now enabled. If you click on it,
you should be able to select the Mule ESB proxy target:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationSelect.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationSelect.png)

Clicking on ok again will get you to a multi-tab dialog showing pre-filled generation
options (There are minor differences if you are using LegStar for Mule 3.x):

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage1.png)

You need to provide the generator with the class name of the target JVMQuery POJO.
In our case, Mule ESB will directly use the POJO to process mainframe requests.
You can use any Mule component class name.
If you click on the browse button, you are presented with a dialog that allows you to
pick up a class from the Eclipse build path. If you type JVMQuery, you should see:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage1-1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage1-1.png)

The first class in the list is our implementation so select it and  click OK.

Check the Deployment options tab. You have the choice between HTTP or WebSphere MQ
as transports for the mainframe client. In our case, we select HTTP.
Your Mule ESB proxy service will be listening for incoming
requests from the mainframe on the host name, port number and path provided here:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage2.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage2.png)

There is also a choice of COBOL client sample programs you can generate. These
programs are examples you can use to invoke the service from your own program.
DFHWBCLI is a simple API that comes with CICS since version TS 2.3 and is still
available in recent CICS versions. The CICS WEB API is part of CICS since TS 3.1 and
above. Finally, you can use the LegStar own HTTP client API. These are C libraries
that are part of the LegStar z/OS distribution.

If you selected WebSphere MQ rather than HTTP, the generated COBOL sample uses MQ APIs.

When you are satisfied, click on Finish which will generate the Mule ESB proxy:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage3.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleProxyGenerationPage3.png)

Under src/com/legstar/test/cixs/mule/jvmquery you will find the generated transformer classes
that turn java value objects to mainframe byte arrays. These transformers can be used
in any Mule configuration and are independent from the transport chosen.

Under conf, a Mule configuration sample is generated:

  * mule-proxy-config-jvmquery-http.xml (LegStar for Mule 2.x) or mule-config.xml (LegStar for Mule 3.x)

> This implements a Request/Response exchange pattern.
> Upon receiving data from the mainframe, a transformer turns that data into a **JvmQueryRequest**
> java data object. On return from invoking the POJO, another transformer turns the **JvmQueryReply**
> java data object into a mainframe reply.

> Check the connectivity parameters in the configuration file to see if they fit your needs.

## Deploy the ESB service proxy for the JVMQuery POJO ##

The ant folder contains generated ant scripts which bundles the
proxy for deployment, and the deploy.xml ant script which deploys the generated service
to your Mule ESB installation (Defined by the MULE\_HOME environment variable).

Go ahead and run deploy.xml from Eclipse.

You can now start Mule ESB and check logs for any errors. With LegStar for Mule 2.x you will need to point Mule to the mule-proxy-config-jvmquery-http.xml configuration file. This is not necessary with LegStar for Mule 3.x where the service benefits from hot deployment.

## Test the ESB service proxy ##

The **cobol** folder contains a generated skeleton COBOL CICS program
that you can use to test the ESB proxy. Edit this code and perform the
following changes:

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

If you associate the QUERYJVM program to a CICS transaction and run that
transaction, it should return with a short message telling you which locale
the Mule server is actually using. The standard CICS/LE CEEMSG and CEEOUT
should show detailed traces.

If you get an HTTP 302, just add a trailing / to the URL in the COBOL program.