# Key Generator  Service
Key Generator Service generate unique keys offline. On request, it gives random key from  offline keys.
Service cache some keys in memory for performance.  

- store offline keys in keys table and move used key in another table 


**Strategy to generate keys offline**
- Use Base62 with random, in this case need to POC how many records we can generate in DB.
- Generate all possible keys and shuffle them, in that case, more storage required to save DB.
- we can generate some amount of keys (50 millions) and based on use will generate more keys by job  
---

