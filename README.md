# Baby-Url-Generator
Baby URL Generator creates shorter url for long URLs. We call these shortened url “baby links.” Users redirect to the original URL when they hit these baby link. Baby links save a lot of space when displayed, printed, messaged, or tweeted. Additionally, users are less likely to mistype shorter URL.

**User**
  - thousands, one day millions
 
**Requirements**
  - Create portal for baby url generator, User generate baby url from long url. User will redirect to original URL when they hit baby url.
  - User can share baby link through Social Media. In future user can create QR code for baby link. 
  - Baby url should not be guessable.
  - User can see all created baby link and can delete baby link. User can change long url.
  - Guest User also can generate baby link.

**Choose Database**
  - No Relational Data
  - Read Heavy Application
  - Data is relatively small 
  - in future billions of Records
  
   Relational Database : CA
   
   Mongo, Hbase, Redis: CP (Master/Slave)
   
   Casandra, Riak, CouchDB AP
   
   https://www.slideshare.net/EdurekaIN/no-sql-databases-35591065
   
   https://www.youtube.com/watch?v=QlqylUeqeis
   
   https://www.instaclustr.com/cassandra-vs-mongodb/

   Use Casandra for Baby URL Generation Service 
   
  **For Key Generation Service**
    - We can Use Casandra with ZooKeeper as we have 2 table 1 for unused keys and other for used keys.
    - second option MongoDB or equivalent CP based NoSQL DB.
    - RDBMS can be used.
    
  **Strategy to generate keys offline**
    - Use Base62 with random, in this case need to POC how many records we can generate in DB.
    - Generate all possible keys and shuffle them, in that case, more storage required to save DB.
    - The best solution that we can generate 5 million records and generate more when some threshold reaches.
