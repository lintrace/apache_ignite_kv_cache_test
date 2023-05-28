package org.alex;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;


public class IgnCl2023 {

    public static void main(String[] args) {

        IntervalTimer itm = new IntervalTimer();
        System.out.println("Test");

        ClientConfiguration cfg = new ClientConfiguration().setAddresses("192.168.111.3:10800");
        cfg.setPartitionAwarenessEnabled(true);

        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Integer, String> cache = client.getOrCreateCache("Cache_1");
            if (cache.size(CachePeekMode.PRIMARY) == 0) {
                System.out.println("The cache \"Cache_1\" is empty!");
            } else {
                System.out.println("The cache \"Cache_1\" already contain data. All data will be dropped and reinserted");
                cache.clear();
            }

            int iteration_num = 10;
            long iteration_period = 0;
            for (int iteration = 0; iteration < iteration_num; iteration++) {
                // add 10000 keys in cache and get time for this operation
                itm.Start();
                for (int i = 0; i < 10000; i++) {
                    cache.put(i, "Value_" + i);
                }
                System.out.println("Iteration " + iteration);
                iteration_period += itm.Stop();
            }
            System.out.println("Average time for " + iteration_num + "iterations is: " + (iteration_period/iteration_num));
        }
    }
}
