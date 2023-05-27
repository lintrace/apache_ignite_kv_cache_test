# apache_ignite_kv_cache_test

This application uses a [thin client](https://ignite.apache.org/docs/latest/thin-clients/java-thin-client) to connect to Apache Ignite cluster. [Quick start guide is here.](https://ignite.apache.org/docs/latest/quick-start/java)
It is assumed that the cluster node is running on your local host with the default configuration.
By default thin client will try to connect to **localhost:10800**, but you can change the IP address of the server node to another one on your network.

If your server node and client are on different hosts and you are having network communication problems, you probably need to check your firewall settings. You need these ports to be opened on the server side:
- 10800/tcp - default port for [thin client connection](https://ignite.apache.org/docs/latest/thin-clients/getting-started-with-thin-clients#configuring-thin-client-connector)
- 47500/tcp/udp - default [discovery port](https://ignite.apache.org/docs/latest/clustering/network-configuration#discovery) 
- 47100/tcp - default [communication port](https://ignite.apache.org/docs/latest/clustering/network-configuration#communication)
- 11211/tcp - port for Apache Ignite system console utilities such as [contol.sh](https://ignite.apache.org/docs/latest/tools/control-script) etc. This is not necessary if you are not using these tools.

![Apache Ignite Logo](https://ignite.apache.org/img/logo.svg)

[Apache Ignite Download Links](https://ignite.apache.org/download.cgi "Official Apache Ignite Download Page")

This application is used to measure how much time is spent on typical key value cache operations such as put, get, replace, remove.
By the way, this will help you decide whether the new version of Apache Ignite starts faster or not.

Sample output for Apache Ignite 2.14.0, thin client 2.15. The Ignite server node is not hosted on localhost (another server is used on the physical local network running Linux)

```
=======================================================
OS: Linux 		 Ignite ver: 2.14.0
=======================================================
Cluster is in ACTIVE state.

 >>> Cleared cache "KeyValueCache" <<<
====== Start ClientCache.put test =====
PUT iteration: 1 with 10000 keys was completed in 2414 ms. [ 00:00:02.414 ]
GET with 10000 random keys was completed in 2260 ms.       [ 00:00:02.260 ]
PUT iteration: 2 with 10000 keys was completed in 2372 ms. [ 00:00:02.372 ]
GET with 10000 random keys was completed in 2347 ms.       [ 00:00:02.347 ]
PUT iteration: 3 with 10000 keys was completed in 2362 ms. [ 00:00:02.362 ]
GET with 10000 random keys was completed in 2192 ms.       [ 00:00:02.192 ]
PUT iteration: 4 with 10000 keys was completed in 2488 ms. [ 00:00:02.488 ]
GET with 10000 random keys was completed in 1998 ms.       [ 00:00:01.998 ]
PUT iteration: 5 with 10000 keys was completed in 2265 ms. [ 00:00:02.265 ]
GET with 10000 random keys was completed in 2399 ms.       [ 00:00:02.399 ]
--- Average time for 5 iterations of the PUT type (each with 10000 keys) was: 2380 ms.
REPLACE iteration: 1 with 10000 keys was completed in 2429 ms. [ 00:00:02.429 ]
GET with 10000 random keys was completed in 2277 ms.       [ 00:00:02.277 ]
REPLACE iteration: 2 with 10000 keys was completed in 2492 ms. [ 00:00:02.492 ]
GET with 10000 random keys was completed in 2225 ms.       [ 00:00:02.225 ]
REPLACE iteration: 3 with 10000 keys was completed in 2581 ms. [ 00:00:02.581 ]
GET with 10000 random keys was completed in 2322 ms.       [ 00:00:02.322 ]
REPLACE iteration: 4 with 10000 keys was completed in 2562 ms. [ 00:00:02.562 ]
GET with 10000 random keys was completed in 2264 ms.       [ 00:00:02.264 ]
REPLACE iteration: 5 with 10000 keys was completed in 2483 ms. [ 00:00:02.483 ]
GET with 10000 random keys was completed in 2191 ms.       [ 00:00:02.191 ]
--- Average time for 5 iterations of the REPLACE type (each with 10000 keys) was: 2509 ms.
--- REMOVE time for 10000 keys was: 377 ms.
 >>> Cleared cache "KeyValueCache" <<<


====== Start ClientCache.putAsync test =====
PUT_ASYNC iteration: 1 with 10000 keys was completed in 85 ms. [ 00:00:00.085 ]
GET_ASYNC with 10000 random keys was completed in 2015 ms.       [ 00:00:02.015 ]
PUT_ASYNC iteration: 2 with 10000 keys was completed in 18 ms. [ 00:00:00.018 ]
GET_ASYNC with 10000 random keys was completed in 2212 ms.       [ 00:00:02.212 ]
PUT_ASYNC iteration: 3 with 10000 keys was completed in 14 ms. [ 00:00:00.014 ]
GET_ASYNC with 10000 random keys was completed in 2007 ms.       [ 00:00:02.007 ]
PUT_ASYNC iteration: 4 with 10000 keys was completed in 9 ms. [ 00:00:00.009 ]
GET_ASYNC with 10000 random keys was completed in 2213 ms.       [ 00:00:02.213 ]
PUT_ASYNC iteration: 5 with 10000 keys was completed in 47 ms. [ 00:00:00.047 ]
GET_ASYNC with 10000 random keys was completed in 2086 ms.       [ 00:00:02.086 ]
--- Average time for 5 iterations of the PUT_ASYNC type (each with 10000 keys) was: 34 ms.
REPLACE_ASYNC iteration: 1 with 10000 keys was completed in 24 ms. [ 00:00:00.024 ]
GET_ASYNC with 10000 random keys was completed in 2357 ms.       [ 00:00:02.357 ]
REPLACE_ASYNC iteration: 2 with 10000 keys was completed in 19 ms. [ 00:00:00.019 ]
GET_ASYNC with 10000 random keys was completed in 2645 ms.       [ 00:00:02.645 ]
REPLACE_ASYNC iteration: 3 with 10000 keys was completed in 44 ms. [ 00:00:00.044 ]
GET_ASYNC with 10000 random keys was completed in 2631 ms.       [ 00:00:02.631 ]
REPLACE_ASYNC iteration: 4 with 10000 keys was completed in 39 ms. [ 00:00:00.039 ]
GET_ASYNC with 10000 random keys was completed in 2607 ms.       [ 00:00:02.607 ]
REPLACE_ASYNC iteration: 5 with 10000 keys was completed in 11 ms. [ 00:00:00.011 ]
GET_ASYNC with 10000 random keys was completed in 2356 ms.       [ 00:00:02.356 ]
--- Average time for 5 iterations of the REPLACE_ASYNC type (each with 10000 keys) was: 27 ms.
--- REMOVE_ASYNC time for 10000 keys was: 1 ms.
 >>> Cleared cache "KeyValueCache" <<<


====== Start ClientCache.putAll test =====
PUT_ALL iteration: 1 with 10000 keys was completed in 51 ms. [ 00:00:00.051 ]
GET_ALL with 10000 random keys was completed in 31 ms.       [ 00:00:00.031 ]
PUT_ALL iteration: 2 with 10000 keys was completed in 36 ms. [ 00:00:00.036 ]
GET_ALL with 10000 random keys was completed in 27 ms.       [ 00:00:00.027 ]
PUT_ALL iteration: 3 with 10000 keys was completed in 37 ms. [ 00:00:00.037 ]
GET_ALL with 10000 random keys was completed in 24 ms.       [ 00:00:00.024 ]
PUT_ALL iteration: 4 with 10000 keys was completed in 36 ms. [ 00:00:00.036 ]
GET_ALL with 10000 random keys was completed in 24 ms.       [ 00:00:00.024 ]
PUT_ALL iteration: 5 with 10000 keys was completed in 36 ms. [ 00:00:00.036 ]
GET_ALL with 10000 random keys was completed in 30 ms.       [ 00:00:00.030 ]
--- Average time for 5 iterations of the PUT_ALL type (each with 10000 keys) was: 39 ms.
--- REMOVE_ALL time for 10000 keys was: 8 ms.
 >>> Cleared cache "KeyValueCache" <<<


====== Start ClientCache.putAllAsync test =====
PUT_ALL_ASYNC iteration: 1 with 10000 keys was completed in 6 ms. [ 00:00:00.006 ]
GET_ALL_ASYNC Can't get anything from the cache "KeyValueCache" because it's empty
PUT_ALL_ASYNC iteration: 2 with 10000 keys was completed in 5 ms. [ 00:00:00.005 ]
GET_ALL_ASYNC with 10000 random keys was completed in 56 ms.       [ 00:00:00.056 ]
PUT_ALL_ASYNC iteration: 3 with 10000 keys was completed in 7 ms. [ 00:00:00.007 ]
GET_ALL_ASYNC with 10000 random keys was completed in 23 ms.       [ 00:00:00.023 ]
PUT_ALL_ASYNC iteration: 4 with 10000 keys was completed in 4 ms. [ 00:00:00.004 ]
GET_ALL_ASYNC with 10000 random keys was completed in 32 ms.       [ 00:00:00.032 ]
PUT_ALL_ASYNC iteration: 5 with 10000 keys was completed in 6 ms. [ 00:00:00.006 ]
GET_ALL_ASYNC with 10000 random keys was completed in 22 ms.       [ 00:00:00.022 ]
--- Average time for 5 iterations of the PUT_ALL_ASYNC type (each with 10000 keys) was: 5 ms.
--- REMOVE_ALL_ASYNC time for 10000 keys was: 0 ms.
```
