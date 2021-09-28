# ADR-2: Use Casandra Database

# Status 
Accepted

# Context
Baby URL Generator System needs to handle millions of records. As system evolve, size of data increase rapidly. Need a database which support scalability and high availability. 
For example, If 1 million url created per month, then in 5 years 60 million url created. 

# Decision
System will use Casandra Database as consider following points:
- No Relational Data in System
- Read Heavy Application
- Data is relatively small
- In Future billions of records in system

**Advantages Of Casandra**
- High Availability
- Incremental Scalability
- Eventual Consistency
- Tradeoff between consistency and latency
- Minimal Administration
- No Single Point Of Failure

**Alternatives**
- **MongoDB**: We can use MongoDB for the system. MongoDB provides CP of CAP theorem. As our application require availability over consistency, we choose Casandra.
- **RDBMS**: As there are no relational data and billions of record, Prefer NoSQL DB over RDBMS. No case in the system for ACID transaction support. 
Base(Basically Available, Soft State, Eventual consistency) from nosql is enough for our application. 

# Consequence
- Casandra is AP of CAP theorem, it provides eventual Consistency, Full Consistency not support. In Baby URL generate full consistency not required. We can use 
[lightweight transaction](https://docs.datastax.com/en/cql-oss/3.3/cql/cql_using/useInsertLWT.html)


# References
- [NOSQL DB Selection](https://www.slideshare.net/EdurekaIN/no-sql-databases-35591065)
- [Casandra vs MongoDB](https://www.instaclustr.com/cassandra-vs-mongodb/)
- [Casandra vs HBase vs MongoDB](https://www.youtube.com/watch?v=QlqylUeqeis)




