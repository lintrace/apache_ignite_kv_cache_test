/*
 Set of the tests for KeyValue cache in Apache Ignite
 */
package org.alex;

import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;

import java.util.*;
import java.util.concurrent.ExecutionException;

enum UsePutOperation {
    PUT, PUT_ALL, PUT_ASYNC, PUT_ALL_ASYNC
}

enum UseGetOperation {
    GET, GET_ALL, GET_ASYNC, GET_ALL_ASYNC
}

public class KeyValueCacheTest {

    // number of iterations of tests by cache
    final static int ITERATION_NUM = 10;

    // number keys for put in cache by one iteration
    final static int NUM_KEYS = 10_000;
    // print statistic for each iteration of test
    final static boolean NEED_STAT_FOR_EACH_ITERATION = true;
    // number of random keys to get from cache
    final static int NUM_GET_RND_KEYS = 5;
    // random gets from cache on each iteration
    final static boolean RANDOM_GETS_ON_EACH_ITERATION = true;
    // Print key value for get operation into console
    // Useful if we need to check elapsed time of gets operation for big number of keys.
    // In this case spent time to console out is not affected to overall time.
    final static boolean PRINT_GET_KV_INTO_CONSOLE = true;
    // Thread sleep between iterations in ms.
    final static long SLEEP = 0L;
    // Cache name for test
    final static String CACHE_NAME = "KeyValueCache";


    public static void startKVCacheTest(IgniteClient client) {

        ClientCache<Integer, String> cache = client.getOrCreateCache(CACHE_NAME);

        cache.clear();
        System.out.println("====== Start ClientCache.put test =====");
        putTest(cache, UsePutOperation.PUT);

        cache.clear();
        System.out.println("\n\n====== Start ClientCache.putAsync test =====");
        putTest(cache, UsePutOperation.PUT_ASYNC);

        cache.clear();
        System.out.println("\n\n====== Start ClientCache.putAll test =====");
        putTest(cache, UsePutOperation.PUT_ALL);

        cache.clear();
        System.out.println("\n\n====== Start ClientCache.putAllAsync test =====");
        putTest(cache, UsePutOperation.PUT_ALL_ASYNC);
    }


    /*
    Test put operations.
    You can select desired method for put operation from these:
        ClientCache.put
        ClientCache.putAll
        ClientCache.putAsync
        ClientCache.putAllAsync
    by select via "putOperation"
     */
    private static void putTest(ClientCache<Integer, String> cache, UsePutOperation putOperation) {
        IntervalTimer itm = new IntervalTimer();

        Map<Integer, String> map = new HashMap<>(); // Map uses only putAll and putAllAsync methods
        String cacheValue;

        long iteration_period = 0; // for calculate average time

        for (int iteration = 1; iteration <= ITERATION_NUM; iteration++) {

            if (putOperation == UsePutOperation.PUT_ALL || putOperation == UsePutOperation.PUT_ALL_ASYNC) {
                map.clear();
            }

            itm.start(); //start timer

            // Put keys into cache be selected method
            for (int i = 0; i < NUM_KEYS; i++) {
                cacheValue = "Iteration: " + iteration + ", Value_" + i;
                if (putOperation == UsePutOperation.PUT) cache.put(i, cacheValue);
                else if (putOperation == UsePutOperation.PUT_ASYNC) cache.putAsync(i, cacheValue);
                else if (putOperation == UsePutOperation.PUT_ALL || putOperation == UsePutOperation.PUT_ALL_ASYNC)
                    map.put(i, cacheValue);
            }
            if (putOperation == UsePutOperation.PUT_ALL) cache.putAll(map);
            if (putOperation == UsePutOperation.PUT_ALL_ASYNC) cache.putAllAsync(map);

            if (NEED_STAT_FOR_EACH_ITERATION) iteration_period += itm.stopWithMessage();
            else iteration_period += itm.stop();

            // Delay between put data into a cache and get from it
            if (SLEEP > 0) {
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // Get from cache
            if (RANDOM_GETS_ON_EACH_ITERATION) {
                if (putOperation == UsePutOperation.PUT)
                    getRandomValuesFromCache(cache, UseGetOperation.GET);
                if (putOperation == UsePutOperation.PUT_ASYNC)
                    getRandomValuesFromCache(cache, UseGetOperation.GET_ASYNC);
                if (putOperation == UsePutOperation.PUT_ALL)
                    getRandomValuesFromCache(cache, UseGetOperation.GET_ALL);
                if (putOperation == UsePutOperation.PUT_ALL_ASYNC)
                    getRandomValuesFromCache(cache, UseGetOperation.GET_ALL_ASYNC);
            }
        }

        System.out.println("Average time for " + ITERATION_NUM + " iterations is: " + (iteration_period / ITERATION_NUM) + " ms.");
    }


    public static void getRandomValuesFromCache(ClientCache<Integer, String> cache, UseGetOperation getOperation) {
        if (cache.size(CachePeekMode.PRIMARY) == 0) {
            System.out.println("The cache \"" + cache.getName() + "\" is empty!");
            return;
        }

        Set<Integer> setKeys = new HashSet<>();    // Keys for getAll and getAllAsync
        Map<Integer, String> map; // For returned Keys and Values for getAll and getAllAsync

        System.out.println("Get " + NUM_GET_RND_KEYS + " values by random keys, type of operation: " + getOperation.toString());

        Random rnd = new Random(System.currentTimeMillis());
        int rnd_num;

        long startTime_ms = System.currentTimeMillis();

        for (int key = 0; key < NUM_GET_RND_KEYS; key++) {
            rnd_num = rnd.nextInt(NUM_KEYS);

            try {
                if (getOperation == UseGetOperation.GET) {
                    if (!PRINT_GET_KV_INTO_CONSOLE) cache.get(rnd_num);
                    else System.out.println("Key: " + rnd_num + "    Value: " + cache.get(rnd_num));
                }
                else if (getOperation == UseGetOperation.GET_ASYNC) {
                    if (!PRINT_GET_KV_INTO_CONSOLE) cache.getAsync(rnd_num).get();
                    else System.out.println("Key: " + rnd_num + "    Value: " + cache.getAsync(rnd_num).get());
                }
                else if (getOperation == UseGetOperation.GET_ALL || getOperation == UseGetOperation.GET_ALL_ASYNC)
                    setKeys.add(key);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        if (getOperation == UseGetOperation.GET_ALL) {
            map = cache.getAll(setKeys);
            if (PRINT_GET_KV_INTO_CONSOLE) map.forEach((k, v) -> {
                System.out.println("Key: " + k + "    Value: " + v);
            });
        }
        if (getOperation == UseGetOperation.GET_ALL_ASYNC) {
            try {
                map = cache.getAllAsync(setKeys).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            if (PRINT_GET_KV_INTO_CONSOLE) map.forEach((k, v) -> {
                System.out.println("Key: " + k + "    Value: " + v);
            });
        }
        long period = System.currentTimeMillis() - startTime_ms;
        System.out.println("Elapsed time for " + NUM_GET_RND_KEYS + " " + getOperation.toString() + " operations is " + period + " ms.");
    }
}


