/*
 Group of the tests for Key-Value cache in Apache Ignite
 */
package org.alex;

import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

enum PutOperation {
    PUT, PUT_ALL, PUT_ASYNC, PUT_ALL_ASYNC
}

enum GetOperation {
    GET, GET_ALL, GET_ASYNC, GET_ALL_ASYNC
}

enum RemoveOperation {
    REMOVE, REMOVE_ALL, REMOVE_ASYNC, REMOVE_ALL_ASYNC
}

enum ReplaceOperation {
    REPLACE, REPLACE_ASYNC
}


public class KeyValueCacheTest {

    // clear cache before each iteration
    final static boolean CLEAR_CACHE_EACH_ITERATION = false;

    // number of test iterations
    final static int ITERATION_NUM = 5;

    // number keys for put in cache by one iteration (for put tests)
    final static int NUM_KEYS = 10_000;

    // print statistic after each iteration of test
    final static boolean NEED_STAT_FOR_EACH_ITERATION = true;

    // number of random keys to get from cache (for get tests)
    final static int NUM_GET_RND_KEYS = 10_000;

    // random gets from cache after puts on each iteration
    final static boolean RANDOM_GETS_ON_EACH_ITERATION = true;

    // Print key value for get operation into console
    // Useful if we need to check elapsed time of gets operation for big number of keys.
    // In this case spent time to console out is not affected to overall time.
    final static boolean PRINT_GET_KV_INTO_CONSOLE = false;

    // Thread sleep between iterations in ms (between gets and puts)
    final static long SLEEP = 0L;

    // Cache name for test
    final static String CACHE_NAME = "KeyValueCache";

    // Need to destroy the cache after completing tests
    final static boolean DESTROY_CACHE_AFTER_TEST = true;

    public static void startKVCacheTest(IgniteClient client) {

        ClientCache<Integer, String> cache = client.getOrCreateCache(CACHE_NAME);

        clearCacheWithMessage(cache);
        System.out.println("====== Start ClientCache.put test =====");
        putTest(cache, PutOperation.PUT);
        replaceTest(cache, ReplaceOperation.REPLACE);
        removeTest(cache, RemoveOperation.REMOVE);

        clearCacheWithMessage(cache);
        System.out.println("\n\n====== Start ClientCache.putAsync test =====");
        putTest(cache, PutOperation.PUT_ASYNC);
        replaceTest(cache, ReplaceOperation.REPLACE_ASYNC);
        removeTest(cache, RemoveOperation.REMOVE_ASYNC);

        clearCacheWithMessage(cache);
        System.out.println("\n\n====== Start ClientCache.putAll test =====");
        putTest(cache, PutOperation.PUT_ALL);
        removeTest(cache, RemoveOperation.REMOVE_ALL);

        clearCacheWithMessage(cache);
        System.out.println("\n\n====== Start ClientCache.putAllAsync test =====");
        putTest(cache, PutOperation.PUT_ALL_ASYNC);
        removeTest(cache, RemoveOperation.REMOVE_ALL_ASYNC);

        // TODO also maybe is useful not randomized (flat) get tests

        if (DESTROY_CACHE_AFTER_TEST) client.destroyCache(CACHE_NAME);
    }


    /*
    Test put operations with selected method (use "putOp" for this)
     */
    private static void putTest(ClientCache<Integer, String> cache, PutOperation putOp) {

        Map<Integer, String> map = new HashMap<>(); // Map used for putAll and putAllAsync methods
        String cacheValue;

        long totalPutTime = 0; // for calculate average time with all iterations
        long onePutIterationTime;

        for (int iteration = 1; iteration <= ITERATION_NUM; iteration++) {

            if (putOp == PutOperation.PUT_ALL || putOp == PutOperation.PUT_ALL_ASYNC) {
                map.clear();
            }

            if (CLEAR_CACHE_EACH_ITERATION) cache.clear();

            onePutIterationTime = System.currentTimeMillis();  //start interval measurement

            // Put keys into cache by selected method
            for (int i = 0; i < NUM_KEYS; i++) {
                cacheValue = "Iteration: " + iteration + ", Value_" + i;
                if (putOp == PutOperation.PUT) cache.put(i, cacheValue);
                else if (putOp == PutOperation.PUT_ASYNC) cache.putAsync(i, cacheValue);
                else if (putOp == PutOperation.PUT_ALL || putOp == PutOperation.PUT_ALL_ASYNC)
                    map.put(i, cacheValue);
            }
            if (putOp == PutOperation.PUT_ALL) cache.putAll(map);
            if (putOp == PutOperation.PUT_ALL_ASYNC) cache.putAllAsync(map);

            onePutIterationTime = System.currentTimeMillis() - onePutIterationTime; // delta time end-begin
            totalPutTime += onePutIterationTime;
            if (NEED_STAT_FOR_EACH_ITERATION) {
                System.out.println(putOp.toString() + " iteration: " + iteration +
                        " with " + NUM_KEYS + " keys was completed in " + onePutIterationTime + " ms. [ " +
                        LocalTime.ofNanoOfDay(onePutIterationTime * 1000000).toString() + " ]");
            }
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
                if (putOp == PutOperation.PUT)
                    getRandomValuesFromCache(cache, GetOperation.GET);
                if (putOp == PutOperation.PUT_ASYNC)
                    getRandomValuesFromCache(cache, GetOperation.GET_ASYNC);
                if (putOp == PutOperation.PUT_ALL)
                    getRandomValuesFromCache(cache, GetOperation.GET_ALL);
                if (putOp == PutOperation.PUT_ALL_ASYNC)
                    getRandomValuesFromCache(cache, GetOperation.GET_ALL_ASYNC);
            }
        }

        System.out.println("--- Average time for " + ITERATION_NUM + " iterations of the " + putOp.toString() +
                " type (each with " + NUM_KEYS + " keys) was: " + (totalPutTime / ITERATION_NUM) + " ms.");
    }

    private static void replaceTest(ClientCache<Integer, String> cache, ReplaceOperation replaceOp) {

        String cacheValue;

        long totalReplaceTime = 0; // for calculate average time with all iterations
        long oneReplaceIterationTime;

        for (int iteration = 1; iteration <= ITERATION_NUM; iteration++) {

            oneReplaceIterationTime = System.currentTimeMillis();  //start interval measurement

            for (int i = 0; i < NUM_KEYS; i++) {
                cacheValue = "Iteration: " + iteration + ", Replaced_" + i;
                if (replaceOp == ReplaceOperation.REPLACE) cache.replace(i, cacheValue);
                else cache.replaceAsync(i, cacheValue);
            }

            oneReplaceIterationTime = System.currentTimeMillis() - oneReplaceIterationTime; // delta time end-begin
            totalReplaceTime += oneReplaceIterationTime;
            if (NEED_STAT_FOR_EACH_ITERATION) {
                System.out.println(replaceOp.toString() + " iteration: " + iteration +
                        " with " + NUM_KEYS + " keys was completed in " + oneReplaceIterationTime + " ms. [ " +
                        LocalTime.ofNanoOfDay(oneReplaceIterationTime * 1000000).toString() + " ]");
            }
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
                if (replaceOp == ReplaceOperation.REPLACE)
                    getRandomValuesFromCache(cache, GetOperation.GET);
                else
                    getRandomValuesFromCache(cache, GetOperation.GET_ASYNC);
            }
        }

        System.out.println("--- Average time for " + ITERATION_NUM + " iterations of the " + replaceOp.toString() +
                " type (each with " + NUM_KEYS + " keys) was: " + (totalReplaceTime / ITERATION_NUM) + " ms.");
    }

    private static void removeTest(ClientCache<Integer, String> cache, RemoveOperation remOp) {

        Set<Integer> keyset = new HashSet<>(); // Uses for putAll and putAllAsync methods

        long totalRemoveTime  = System.currentTimeMillis();

        for (int i = 0; i < NUM_KEYS; i++) {
            if (remOp == RemoveOperation.REMOVE) cache.remove(i);
            else if (remOp == RemoveOperation.REMOVE_ASYNC) cache.removeAsync(i);
            else if (remOp == RemoveOperation.REMOVE_ALL || remOp == RemoveOperation.REMOVE_ALL_ASYNC)
                keyset.add(i);
        }
        if (remOp == RemoveOperation.REMOVE_ALL) cache.removeAll(keyset);
        if (remOp == RemoveOperation.REMOVE_ALL_ASYNC) cache.removeAllAsync(keyset);

        totalRemoveTime = System.currentTimeMillis() - totalRemoveTime; // delta time end-begin

        System.out.println("--- " + remOp.toString() + " time for " + NUM_KEYS +
                " keys was: " + (totalRemoveTime / ITERATION_NUM) + " ms.");
    }

    /*
    Test get operations by random keys with selected method (use "getOp" for this)
     */
    public static void getRandomValuesFromCache(ClientCache<Integer, String> cache, GetOperation getOp) {
        if (cache.size(CachePeekMode.PRIMARY) == 0) {
            System.out.println(getOp.toString() + " Can't get anything from the cache \"" + cache.getName() +
                    "\" because it's empty");
            return;
        }

        Set<Integer> keyset = new HashSet<>();    // Keys for getAll and getAllAsync
        Map<Integer, String> map; // For returned Keys and Values for getAll and getAllAsync

        if (PRINT_GET_KV_INTO_CONSOLE)
            System.out.println("== Get " + NUM_GET_RND_KEYS +
                    " values by random keys, type of operation: " + getOp.toString());

        Random rnd = new Random(System.currentTimeMillis());
        int rnd_num;

        long getOperationsTime = System.currentTimeMillis();

        for (int key = 0; key < NUM_GET_RND_KEYS; key++) {
            rnd_num = rnd.nextInt(NUM_KEYS);

            try {
                if (getOp == GetOperation.GET) {
                    if (!PRINT_GET_KV_INTO_CONSOLE) cache.get(rnd_num);
                    else System.out.println("Key: " + rnd_num + "    Value: " + cache.get(rnd_num));
                } else if (getOp == GetOperation.GET_ASYNC) {
                    if (!PRINT_GET_KV_INTO_CONSOLE) cache.getAsync(rnd_num).get();
                    else System.out.println("Key: " + rnd_num + "    Value: " + cache.getAsync(rnd_num).get());
                } else if (getOp == GetOperation.GET_ALL || getOp == GetOperation.GET_ALL_ASYNC)
                    keyset.add(key);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        if (getOp == GetOperation.GET_ALL) {
            map = cache.getAll(keyset);
            if (PRINT_GET_KV_INTO_CONSOLE) map.forEach((k, v) -> {
                System.out.println("Key: " + k + "    Value: " + v);
            });
        }
        if (getOp == GetOperation.GET_ALL_ASYNC) {
            try {
                map = cache.getAllAsync(keyset).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            if (PRINT_GET_KV_INTO_CONSOLE) map.forEach((k, v) -> {
                System.out.println("Key: " + k + "    Value: " + v);
            });
        }
        getOperationsTime = System.currentTimeMillis() - getOperationsTime;
        System.out.println(getOp.toString() + " with " + NUM_GET_RND_KEYS +
                " random keys was completed in " + getOperationsTime + " ms.       [ " +
                LocalTime.ofNanoOfDay(getOperationsTime * 1000000).toString() + " ]");
    }

    private static void clearCacheWithMessage(ClientCache cache) {
        cache.clear();
        System.out.println(" >>> Cleared cache \"" + cache.getName() + "\" <<<");
    }
}


