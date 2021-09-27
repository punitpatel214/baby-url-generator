# ADR-1 Use the microservice architecture style

# Status
Accepted

# Context
The overall architecture of the baby url generation is simple, easy to create, maintain and **evolve**. Also require easy to scale and ressilence features.
For the small number of urls, we can use the monolithic architecture. But as system evolve we need to support millons of url redirection. So we need to consider any distributed
architecture solution.

# Decision
We use the microservice architecture for the backend service of the baby url generation. Microservice will be containerized with Docker.
Positive consequences of the wide use of microservices contribute to this decision:
- large number of tools and frameworks for microservice development, as well as for monitoring, logging and distributed tracing.
- increasing number of engineers familiar with the style and respective dev tools and frameworks.
- support for containerized microservices available in public cloud providers.

Alternatives:
- **Serverless** : we can create many of backend service as Servless architecture. But 
  
