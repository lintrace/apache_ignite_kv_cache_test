/*
 Set of the tests for KeyValue cache in Apache Ignite
 */
package org.alex;

import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class KeyValueCacheTest {

    // number of iterations of tests by cache
    final static int iteration_num = 10;

    // number keys for put in cache by one iteration
    final static int num_keys = 10_000;
    // print statistic for each iteration of test
    final static boolean need_stat_for_each_iteration = true;
    // number of random keys to get from cache
    final static int num_get_rnd_keys = 5;
    // random gets from cache on each iteration
    final static boolean random_gets_on_each_iteration = false;
    // Thread sleep between iterations in ms.
    final static long sleep = 0L;
    // Cache name for test
    final static String cacheName = "KeyValueCache";

    public static void StartKVCacheTest(IgniteClient client) {

        ClientCache<Integer, String> cache = client.getOrCreateCache(cacheName);

        cache.clear();
        System.out.println("====== Start ClientCache.put test =====");
        PutTest(cache);

        cache.clear();
        System.out.println("\n\n====== Start ClientCache.putAsync test =====");
        PutAsyncTest(cache);

        cache.clear();
        System.out.println("\n\n====== Start ClientCache.putAll test =====");
        PutAllTest(cache);

        cache.clear();
        System.out.println("\n\n====== Start ClientCache.putAllAsync test =====");
        PutAllAsyncTest(cache);
    }

    /*
    ClientCache.put test (this method is very slowly)
     */
    private static void PutTest(ClientCache<Integer,String> cache) {
        IntervalTimer itm = new IntervalTimer();

        long iteration_period = 0;

        for (int iteration = 1; iteration <= iteration_num; iteration++) {
            itm.start();
            for (int i = 0; i < num_keys; i++) {
                cache.put(i, "Iteration: " + iteration + ", Value_" + i);
            }
            if (need_stat_for_each_iteration) iteration_period += itm.stopWithMessage();
            else iteration_period += itm.stop();

            if (random_gets_on_each_iteration) GetRandomValuesFromCache(cache);

            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Average time for " + iteration_num + " iterations is: " + (iteration_period / iteration_num) + " ms.");

        if (!random_gets_on_each_iteration) GetRandomValuesFromCache(cache);
    }

    /*
    ClientCache.putAsync test
     */
    private static void PutAsyncTest(ClientCache<Integer,String> cache) {
        IntervalTimer itm = new IntervalTimer();

        long iteration_period = 0;

        for (int iteration = 1; iteration <= iteration_num; iteration++) {
            itm.start();
            for (int i = 0; i < num_keys; i++) {
                cache.putAsync(i, "Iteration: " + iteration + ", Value_" + i);
            }
            if (need_stat_for_each_iteration) iteration_period += itm.stopWithMessage();
            else iteration_period += itm.stop();

            if (random_gets_on_each_iteration) GetRandomValuesFromCache(cache);

            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Average time for " + iteration_num + " iterations is: " + (iteration_period / iteration_num) + " ms.");

        if (!random_gets_on_each_iteration) GetRandomValuesFromCache(cache);
    }

    /*
ClientCache.putAll test
 */
    private static void PutAllTest(ClientCache<Integer,String> cache) {
        IntervalTimer itm = new IntervalTimer();

        long iteration_period = 0;
        Map<Integer, String> map = new HashMap<>();

        for (int iteration = 1; iteration <= iteration_num; iteration++) {
            map.clear();
            itm.start();
            for (int i = 0; i < num_keys; i++) {
                map.put(i, "Iteration: " + iteration + ", Value_" + i);
            }
            cache.putAll(map);
            if (need_stat_for_each_iteration) iteration_period += itm.stopWithMessage();
            else iteration_period += itm.stop();

            if (random_gets_on_each_iteration) GetRandomValuesFromCache(cache);

            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Average time for " + iteration_num + " iterations is: " + (iteration_period / iteration_num) + " ms.");

        if (!random_gets_on_each_iteration) GetRandomValuesFromCache(cache);
    }

        /*
    ClientCache.putAllAsync test
     */

    private static void PutAllAsyncTest(ClientCache<Integer,String> cache) {
        IntervalTimer itm = new IntervalTimer();

        long iteration_period = 0;
        Map<Integer, String> map = new HashMap<>();

        for (int iteration = 1; iteration <= iteration_num; iteration++) {
            map.clear();
            itm.start();
            for (int i = 0; i < num_keys; i++) {
                map.put(i, "Iteration: " + iteration + ", Value_" + i);
            }
            cache.putAllAsync(map);
            if (need_stat_for_each_iteration) iteration_period += itm.stopWithMessage();
            else iteration_period += itm.stop();

            if (random_gets_on_each_iteration) GetRandomValuesFromCache(cache);

            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Average time for " + iteration_num + " iterations is: " + (iteration_period / iteration_num) + " ms.");

        if (!random_gets_on_each_iteration) GetRandomValuesFromCache(cache);
    }

    public static void GetRandomValuesFromCache(ClientCache<Integer,String> cache) {
        System.out.println("Get " + num_get_rnd_keys + " values by random keys");
        Random rnd = new Random(System.currentTimeMillis());
        int rnd_num;
        for (int i = 0; i < num_get_rnd_keys; i++) {
            rnd_num = rnd.nextInt(num_keys);
            try {
                System.out.println("Key: " + rnd_num + "    Value: " + cache.getAsync(rnd_num).get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


