# platform4j-arch

## Introduce
The Platform4J is a open source fast build Java development platform which is the best suitable for fast building the distributed architecture system.  
  
The Platform4j-arch is the base project of the Platform4J, it provides the base framework reference and the most frequency used utils, the artifacts it provides as below:
* arch-base: provides essential frameworks like Spring Boot framwwork, Dubbo, ZooKeeper, aspectj.
* arch-Boot: a boot framework for dubbo service, include arch-base and Kafka framework.
* arch-Boot-Web: a web boot framework for web application service, include arch-base and Spring Boot Web Starter.
* arch-domain: provides the popular Money model (best suitable for eCommerce platform or payment transaction system)
* arch-common: provides Jackson Utils and Commons Utils.
* arch-data: provides MyBatis framework, Durid framework and Spring MongoDB framework.
* arch-redis: provides Jedis framework, Lettuce framework and Spring session redis framework.

## Reference Relationships
The relationships between each artifacts are as below:
* arch-Boot
  * arch-base
 
* arch-Boot-Web
  * arch-base

* arch-common
  * arch-domain
  
* arch-data
  * arch-common
  
* arch-redis
  * arch-common
