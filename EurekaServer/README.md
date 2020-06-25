The Eureka server is a service discovery pattern implementation.

The Eureka server works in two modes:

1. Standalone: in local, we configure a stand-alone mode where we have only one Eureka server (localhost) and the same cloning property from itself.

2. Clustered: we have multiple Eureka servers, each cloning its states from its peer.

The Eureka server can have a properties file and communicate with a config server as other microservices do.

================================

`Spring Configuration:`
<BR>
application.properties/application.yml vs. bootstrap.properties/bootstrap.yml.
<BR>

Spring Cloud application features a bootstrap context that is the parent of the application context. 
the configuration properties of the bootstrap context load before the configuration properties of the application context.

We use application.yml or application.properties for configuring the application context.

application properties files have the lowest precedence compared to other forms of overriding application context properties.

We use bootstrap.yml or bootstrap.properties for configuring the bootstrap context. This way we keep the external configuration for bootstrap and main context nicely separated.
The bootstrap context is responsible for loading configuration properties from the external sources and for decrypting properties in the local external configuration files.

When the Spring Cloud application starts, it creates a bootstrap context. The first thing to remember is that the bootstrap context is the parent context for the main application.

==================================
`@EnableEurekaServer: `
<BR>Implementing a Eureka Server for service registry

The Eureka server does not have a back end store,
but the service instances in the registry all have to send heartbeats
to keep their registrations up to date (so this can be done in memory).

Clients also have an in-memory cache of Eureka registrations
(so they do not have to go to the registry for every request to a service).

Ref: https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html
   
<HR>

#### Related Configurations:

`spring.application.name`=sample-eureka-server
<BR>_application name_
<BR>
<BR>
`server.port`=${PORT:8761}
<BR>
_TCP port on which this application will be running
8761 is the default one for Eureka servers
Access using http://localhost:8761/_
<BR>
<BR>
`eureka.client.register-with-eureka`=false
<BR>
_By default, the Eureka Server registers itself into the discovery.
We are telling the built-in Eureka Client not to register with itself'
because our application should be acting as a server.
The value being false means it prevents itself from acting as a client._
<BR>
<BR>
`eureka.client.fetch-registry`=false
<BR>
_Does not register itself in the service registry._
<BR>
<BR>
`eureka.server.waitTimeInMsWhenSyncEmpty`: 0
<BR>
_wait time for subsequent sync
This is required in our case as starting an Eureka service without any peer will by default
wait for 5 minutes for sync purposes,
i.e. to give a chance to all the existent microservices to register._
<BR>
<BR>
`logging.level.com.netflix.eureka`=OFF
<BR>
`logging.level.com.netflix.discovery`=OFF
<BR>
OPTIONAL logging levels configuration