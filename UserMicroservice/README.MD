User microservice ( Sample )
<HR>
Points to note: 
<BR>

`1: @SpringBootApplication =  @Configuration + @EnableAutoConfiguration + @ComponentScan`
<BR>
<BR>
` 2: @EnableEurekaClient vs @EnableDiscoveryService`
 <BR>
 _There are multiple implementations of "Discovery Service" (eureka, consul, zookeeper).
 <BR> @EnableDiscoveryClient lives in spring-cloud-commons and picks the implementation on the classpath.
 <BR> @EnableEurekaClient lives in spring-cloud-netflix and only works for eureka.
 If eureka is on your classpath, they are effectively the same._
 <BR>
 <BR>
 <HR>
 Related Configurations:
 <BR>
 <BR>
 
 `spring.application.name`=User-Service
 <BR>
  _This is Service-Id_
 <BR><BR>
`server.port`=0
<BR>
_port on which this application will be running
We can let Spring Boot choose a random port for us because later
we are accessing this service with its name.
0 indicates we want random port number; helpful in case of multiple instances_

`eureka.client.register-with-eureka`=true
<BR>
_registers itself into the discovery._
<BR>
<BR>

`eureka.client.fetch-registry`=true
<BR>
_TODO-documentation_
<BR>

`eureka.client.serviceUrl.defaultZone`: ${EUREKA_URI:http://localhost:8761/eureka}
<BR>
_fully qualified Eureka Server URL
"defaultZone" is a magic string fallback value that provides the service URL for any client
that doesn’t express a preference (i.e. it’s a useful default)._
<BR><BR>
`eureka.client.healthcheck.enabled`: true
<BR>
_TODO-documentation_
<BR><BR>

`eureka.client.lease.duration`: 5
<BR>
_Eureka allows custom HealthCheckHandlers to be plugged-in through theEurekaClientregisterHealthCheck() API.
Spring Cloud leverages this extension point to register a new handler - EurekaHealthCheckHandler - if the following property is set to true__
<BR><BR>

`eureka.instance.preferIpAddress`: true
<BR>
_In some cases, it is preferable for Eureka to advertise the IP addresses of services
rather than the hostname.
Set eureka.instance.preferIpAddress to true and,
when the application registers with eureka, it uses its IP address rather than its hostname._
<BR><BR>

`eureka.instance.leaseRenewalIntervalInSeconds`: 1
<BR>
_Registration takes up to 30 s because that is the default client refresh time_
<BR><BR>

`eureka.instance.leaseExpirationDurationInSeconds`: 2
<BR>
_TODO-documentation_
<BR><BR>

`spring.devtools.restart.enabled`=true
<BR>
_to allow restart and redeploy when changes are made in code._
<BR><BR>

`spring.devtools.restart.poll-interval`=2s
<BR>
`spring.devtools.restart.quiet-period`=1s
<BR>
_The monitored classpath directories are now polled every 2 seconds for changes,
and a 1 second quiet period is maintained to make sure there are no additional class changes._
<BR><BR>

`management.security.enabled`: false
<BR>
_disable the spring security on the management endpoints like /env, /refresh etc._
<BR><BR>

`logging.level.com.github.singhr2.services.user`: INFO
<BR>
_setting logging level for specific package_