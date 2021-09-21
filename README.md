# baby-url-generator
Baby URL Generator is used to create shorter url for long URLs. We call these shortened url “baby links.” Users are redirected to the original URL when they hit these baby link. Baby links save a lot of space when displayed, printed, messaged, or tweeted. Additionally, users are less likely to mistype shorter URL.

**User**
  - thousands, one day millions
 
**Requirments**
  - Create portal for baby url generator, User generate baby url from long url. User will redirect to original URL when they hit baby url.
  - User can share baby link through Social Media. In future user can create QR code for baby link. 
  - Baby url should not be guessable
  - User can see all created baby link and can delete baby link. User can change long url
  - Guest User also can generate baby link   

**Choose Database**
  - No Relational Data
  - Read Heavy Application
  - Data is relatively small 
  - in future billions of Records
  
   Relational Database : CA
   Mongo, Hbase, Redis: CP (master/slave)
   Casandra, Riak, CouchDB AP
   
   https://www.slideshare.net/EdurekaIN/no-sql-databases-35591065
   https://www.youtube.com/watch?v=QlqylUeqeis

