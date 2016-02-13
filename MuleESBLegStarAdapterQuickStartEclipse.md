# QuickStart description #

This is an alternative to [MuleESBLegStarAdapterQuickStartAnt](MuleESBLegStarAdapterQuickStartAnt.md)
using Eclipse plugins rather than ant scripts.

Using Eclipse plugins, we will walk you through the process of integrating
Mule ESB with a backend IBM CICS system

Once deployed, the generated ESB component will act as an adapter for
the sample CICS COBOL program LSFILEAE. ESB clients can then consume the ESB
component without any knowledge of the actual legacy backend system:

![http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-adapter-http.png](http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-adapter-http.png)

# Installation #

Follow instructions in:[InstallInstructions](InstallInstructions.md).

The installed z/OS CICS modules include the LSFILEAE COBOL sample program that we
will be using for this quickstart.

We will be using HTTP connectivity to CICS for this quickstart (Alternatively,
you can use Sockets or Websphere MQ).

Start Eclipse and select menu option:
```
Help-->Software Updates-->Find and Install...
```

From there check the **Search for new features to install** option and click next.

Create a **New Remote Site** named legstar-mule and type the URL depending on the version of LegStar for Mule you want to use:

  * LegStar for Mule 3.x: http://www.legsem.com/legstar/mule/eclipse/update

  * LegStar for Mule 2.x: http://www.legsem.com/legstar/mule/eclipse/update/2.x

With this new site selected, when you press the finish button, you are
presented with all the features you need. Select both LegStar and LegStar for Mule ESB groups,
click next, accept the license terms and then click finish which should
end the installation process.

# Generate a Mule ESB service adapter for a mainframe program #

Start by creating a new Java project called MuleLsfileae and select the project.

## Map the LSFILEAE Structures to XML Schema ##

First step is to generate an XML schema from the source of the target COBOL
program LSFILEAE.

Select the Structures Mapping plugin:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectStructuresMapping.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectStructuresMapping.png)

Then type the name of the target XML Schema, lsfileae.xsd, in the **XSD file name** field.
This should pre-fill the other fields:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseStructuresMappingPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseStructuresMappingPage1.png)

Click next and click on the **Select Cobol fragments from file system** link. This
will pop up a standard file selection dialog. Look for your LegStar installation
folder. Under the samples/mule/quickstarts/legstar\_adapter/cobol sub-folder,
you will find the lsfileae sample. When you select that file, the content is
displayed for your review:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseStructuresMappingPage2.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseStructuresMappingPage2.png)

Although the entire COBOL program is displayed, only the data description will
be processed.

Click on the Finish button, this starts the mapping process and the generated
XML Schema is opened. You will notice that each COBOL data structure in
the LSFILEAE COBOL source has been mapped to an XML Schema type.

## Bind the XML Schema to Java annotated classes ##

Next step turns the XML schema types into Java annotated classes.

From the package explorer view, expand your Java project. The generated
lsfileae.xsd file should appear. Select the file, right click to show
the context menu and then select LegStar-->Generate Transformers:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseContextMenuSelectBinding.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseContextMenuSelectBinding.png)

Lsfileae.xsd contains 2 complex types: Dfhcommarea and ComPersonal which
is a sub-type of Dfhcommarea. Binding a type will automatically bind
all sub-types so all we have to do is to select Dfhcommarea and click
Finish:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseBindingPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseBindingPage1.png)

Your package explorer should now display 2 new packages. A library containing
the LegStar dependencies should have been added to the project Build path.

If you open Dfhcommarea.java, you will notice that classes exhibit
annotations that bind java types both to XML and COBOL:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseBindingResults.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseBindingResults.png)

## Generate an ESB service adapter for LSFILEAE ##

The last step generates the Mule ESB service.

With your MuleLsfileae project selected, select the Operations mapping plugin:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectOperationsMapping.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseToolBarSelectOperationsMapping.png)

Enter name lsfileae.cixs for the operations mapping file.

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage1.png)

After you click finish, use the Add... button to create a new operation. Ultimately,
this operation will become a service in the ESB model.
Type in operation name lsfileae:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage2.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage2.png)

An operation maps to a mainframe program in a request/response exchange pattern.
What you need to do at this stage is specify the input and output structures. You
select these structures from the set that was bound during the previous step.
These are displayed when you click on the Add button, successively for Input and
for Output:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage3.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage3.png)

In our case, we select Dfhcommarea both for Input and for Output, because
the LSFILEAE program uses a single structure both for input and output:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage4.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseOperationsMappingPage4.png)

After you click on ok, the Generate button is now enabled. If you click on it,
you should be able to select the Mule Mainframe adapter target:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleAdapterGenerationSelect.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleAdapterGenerationSelect.png)

Clicking on ok again will get you to a multi-tab dialog showing pre-filled generation
options (There are minor differences if you are using LegStar for Mule 3.x):

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleAdapterGenerationPage1.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleAdapterGenerationPage1.png)

This dialog allows you to select options for the generated ESB service.

Among other artifacts, LegStar for Mule generates sample Mule configurations that you
can use to test the generated service. The sample configuration can expect java serialized
objects from clients or XML. Lets keep the JAVA option here.

You have the choice between 3 mainframe transports for the adapter sample configurations: HTTP,
WebSphere MQ and TCP. In addition, the MOCK transport can be used if you don't have
access to an actual mainframe. In our case, we will be using HTTP.

Click on Finish to generate the Mule ESB adapter:

![http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleAdapterGenerationPage2.png](http://legstar-mule.googlecode.com/svn/wiki/images/EclipseMuleAdapterGenerationPage2.png)

Under the **com.legstar.test.cixs.lsfileae** package you will find
the generated !Mule ESB transformers.

Under conf, Mule ESB a configuration sample is generated:

  * mule-adapter-config-lsfileae-http-java-legstar.xml (LegStar for Mule 2.x) or mule-config.xml (LegStar for Mule 3.x)

> Notice that generated transformers are referenced. Access to mainframe uses the HTTP transport.
> The client is expected to send serialized java objects over an inbound HTTP connection.

## Deploy the ESB service adapter for LSFILEAE ##

The ant folder contains generated ant scripts which bundles the adapter for deployment, and the deploy.xml ant script which deploys the generated service
to your Mule ESB installation (Defined by the MULE\_HOME environment variable).

Go ahead and run deploy.xml from Eclipse.

## Test the ESB service adapter ##

You can now start Mule ESB and check logs for any errors. With LegStar for Mule 2.x you will need to point Mule to the mule-adapter-config-lsfileae-http-java-legstar.xml configuration file. This is not necessary with LegStar for Mule 3.x where the service benefits from hot deployment.

In your LegStar for Mule ESB installation, under the the samples/mule/quickstarts/legstar\_adapter folder, the src/org/mule/transport/legstar/test/lsfileae
sub folder contains the LsfileaeHttpClientTest.java junit class that you can use to invoke the
newly deployed service. Apart from JUnit, this class needs Apache HttpClient (You can find HttpClient and dependencies in $MULE\_HOME/lib/opt).