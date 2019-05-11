There are two caching mechanisms in place in this project.

Firstly there is just a cache-control header being added to all GET methods. Client must take this into account for it to be effective.
Second there is Spring cache using caffeine. It is currently disabled and can be enabled by uncommenting line  

//@EnableCaching 

in BeeniusDemoApplication.java. This caching actually caches result of function where annotation @Cacheable is present and prevents the function call if cache is still valid.