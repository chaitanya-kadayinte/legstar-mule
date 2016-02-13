#summary Installation instructions.

# LegStar for Mule Distribution content #

This distribution provides a Mule transport for mainframe integration based
on the [LegStar open-source legacy integration product](http://www.legsem.com/legstar).
The distribution file contains the prerequisite LegStar modules in addition to the
Mule transport.

The transport is called legstar-mule-transport-x.jar, where x is the version
number, under the **lib** sub-directory. Under **lib** you will also find a
legstar-mule-transport-x-dist.jar file which contains the transport and all
its dependencies.

The distribution also contains a generation tool that creates ad-hoc Mule
transformers. Such transformers are ready to use in Mule configurations.
Without such a tool, you would have to manually map a mainframe program input
and output structures to ESB message payloads.

The LegStar for Mule generator tool also creates sample configuration files
to encapsulate the transformers and transport within services, ready for testing.

The generated services can either be adapters, wrapping a mainframe program
in a Mule service or proxies, giving mainframe programs outbound access
to Mule services.

The generation tool is called legstar-mule-generator-x.jar, where x is
the version number, under the **lib** sub-directory.

The **lib** sub-directory contains all the core LegStar libraries and their
dependencies ready to compile your own services.

You can learn more on how to use the LegStar core functionalities at
http://www.legsem.com/legstar.

In the following text, substitute $VARNAME by %VARNAME% if you are running
on Windows.

# Installing LegStar for Mule ESB #

## Pre requisites ##

JRE 1.6+ and ANT 1.6.5+ are both prerequisites for Mule and LegStar.
Make sure JAVA\_HOME and ANT\_HOME environment variables are set and that
$JAVA\_HOME/bin and $ANT\_HOME/bin are both in you system path.

LegStar requires a JDK, or an international version of the JRE,
that includes charsets.jar.

LegStar for Mule version 2.x requires Mule ESB 2.y while version 3.x requires Mule 3.x.

We assume the MULE\_HOME environment variable points to the location where
Mule is installed.
The LegStar for Mule generator will try to deploy sample services either to $MULE\_HOME/lib/user if you are using LegStar for Mule 2.x, or $MULE\_HOME/apps with LegStar for Mule 3.x.

If you want to use WebSphere MQ as a transport, you will need the following
WebSphere MQ client libraries (to be copied to $MULE\_HOME/lib/user):

  * com.ibm.mq.jar
  * com.ibm.mqjms.jar
  * dhbcore.jar
  * fscontext.jar
  * providerutil.jar

## Installing Java modules ##

Get the latest version from [LegStar for Mule ESB downloads](http://code.google.com/p/legstar-mule/downloads/list).

Pay attention that the major version you download matches your target ESB major version (either 2.x or 3.x).

Unzip the distribution file to a location of your choice.

Set the LEGSTAR\_HOME environment variable to point to the installation
sub-folder named legstar-mule-${version}.

Copy the legstar-mule-transport-x-dist.jar file from the **lib** folder to
$MULE\_HOME/lib/user. Alternatively you can deploy the bare bone
legstar-mule-transport-x.jar file but you will need to bring in dependencies
manually if you do.

If you use Mule IDE, please note that you need to
restart Eclipse after you copied the legstar jars. Mule IDE builds the list of Jars to put on the
classpath upon startup.

## Setup transport for Adapters ##

For adapters you have a choice of transports to invoke mainframe programs.

  * [HTTP](http://www.legsem.com/legstar/legstar-chttprt/release-notes.html)
  * [WebSphere MQ](http://www.legsem.com/legstar/legstar-cmqrt/release-notes.html)
  * [Sockets](http://www.legsem.com/legstar/legstar-csokrt/release-notes.html)

For testing purposes, you also have a mock transport that simulates a
predefined set of mainframe programs.

### Installing zOS CICS modules ###

The need to install the zOS CICS modules varies depending on the type of
transport that you intend to use and wether you want to generate adapters
or proxies:

|                | **Adapters**           | **Proxies**      |
|:---------------|:-----------------------|:-----------------|
| **HTTP**         | Yes                    | No               |
| **Websphere MQ** | No (with IBM Bridge)   | No               |
| **Sockets**      | Yes                    | N/A              |

You should have access to a CICS TS region where the CICS IVP resources,
such as file FILEA (CSD group DFH$FILEA), are installed.

Download the LegStar [distribution for z/OS](http://www.legsem.com/legstar/legstar-distribution-zos/index.html).

The release notes give directions on how to install the CICS modules.

# Go through the quickstarts #

There are runtime quickstarts and development time quickstarts.

Start by the runtime quickstarts which will help you complete and validate your
installation.

## Runtime quickstarts ##

They are located under $LEGSTAR\_HOME/samples/mule/quickstarts.

The legstar-adapter quickstart accesses a mainframe program from Mule and
the legstar-proxy quickstart accesses a POJO from a mainframe program using a
Mule proxy component.

1. From legstar\_adapter folder follow instructions in the readme.txt file.

2. From legstar\_proxy folder follow instructions in the readme.txt file.

## Development time quickstarts ##

Instructions are on the wiki page at http://code.google.com/p/legstar-mule/w/list.

The [MuleESBLegStarAdapterQuickStartAnt](MuleESBLegStarAdapterQuickStartAnt.md) will guide you though the process of generating
an adapter using ant scripts.

The [MuleESBLegStarAdapterQuickStartEclipse](MuleESBLegStarAdapterQuickStartEclipse.md) will guide you though the process of
generating an adapter using Eclipse plugins.

The [MuleESBLegStarProxyQuickStartAnt](MuleESBLegStarProxyQuickStartAnt.md) will guide you though the process of
generating a proxy using ant scripts.

The [MuleESBLegStarProxyQuickStartEclipse](MuleESBLegStarProxyQuickStartEclipse.md) will guide you though the process
of generating a proxy using Eclipse plugins.