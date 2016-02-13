# QuickStart description #

The purpose of this quickstart document is to quickly build a functioning
sample integrating Mule ESB with a backend IBM CICS system.

Once deployed, the generated ESB component will act as an adapter for
the sample CICS COBOL program LSFILEAE. ESB clients can then consume the ESB
component without any knowledge of the actual legacy backend system:

![http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-adapter-http.png](http://legstar-mule.googlecode.com/svn/wiki/images/Legs4MuleESB-quickstart-adapter-http.png)

# Installation #

Follow instructions in:[InstallInstructions](InstallInstructions.md)

The installed CICS modules include the LSFILEAE COBOL sample program that we
will be using for this quickstart.

We will be using HTTP connectivity to CICS for this quickstart (Alternatively,
you can use Sockets or Websphere MQ).

# Generate a Mule ESB service adapter for a mainframe program #

These steps will guide you through the process of generating an ESB adapter
using ant scripts. There is an alternative method, using Eclipse plugins,
that is described in [MuleESBLegStarAdapterQuickStartEclipse](MuleESBLegStarAdapterQuickStartEclipse.md).

First step is to generate an XML schema from the source of the target COBOL
program LSFILEAE.

Go to folder samples/mule/quickstarts/legstar\_adapter.

  * From the ant folder, run command **ant -f build-cob2xsd.xml**.

> This should create a schema folder with a generated XML schema lsfileae.xsd.

If you open lsfileae.xsd you will notice that each COBOL data structure in
the LSFILEAE COBOL source has been mapped to an XML Schema type.

Next step turns the XML schema types into COBOL transformation classes.

  * From the ant folder, run command **ant -f build-coxb.xml**.

> As a result, the src folder contains generated java classes which
> implement COBOL to Java transformers.
> These were derived from the sample XML Schema in the schema folder.
> Sources are also compiled, with binaries stored under the bin folder.

The last step generates the legstar-lsfileae Mule ESB service.

  * From the ant folder run command **ant -f build-mule2cixs.xml**.

Under src/com/legstar/test/cixs/mule/lsfileae you will find
the generated Mule ESB transformers for serialized java object payloads.
These transformers can be used in any Mule configuration and are independent from the mainframe transport chosen.

Under legstar\_adapter, a Mule ESB configuration sample is generated:

  * mule-adapter-config-lsfileae-http-java-legstar.xml (LegStar for Mule 2.x) or mule-config.xml (LegStar for Mule 3.x)

> Notice that generated transformers are referenced. Access to mainframe uses the HTTP transport.
> The client is expected to send serialized java objects over an inbound HTTP connection.

Note that optionally, you can ask the generator to produce Mule transformers and configuration
for XML instead of serialized java objects.

The ant folder contains generated ant scripts which bundles the adapter for deployment, and the deploy.xml ant script which deploys the generated service
to your Mule ESB installation (Defined by the MULE\_HOME environment variable).

  * Go ahead and run **ant -f deploy.xml**, this will bundle and deploy the new service.

# Running the ESB service adapter #

You can now start Mule ESB and check logs for any errors. With LegStar for Mule 2.x you will need to point Mule to the mule-adapter-config-lsfileae-http-java-legstar.xml configuration file. This is not necessary with LegStar for Mule 3.x where the service benefits from hot deployment.

In your LegStar for Mule ESB installation, under the the samples/mule/quickstarts/legstar\_adapter folder, the src/org/mule/transport/legstar/test/lsfileae
sub folder contains the LsfileaeHttpClientTest.java junit class that you can use to invoke the
newly deployed service. Apart from JUnit, this class needs Apache HttpClient (You can find HttpClient and dependencies in $MULE\_HOME/lib/opt).