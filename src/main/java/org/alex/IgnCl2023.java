package org.alex;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;


public class IgnCl2023 {

        public static void main(String[] args) {
            System.out.println("Test");

            ClientConfiguration cfg = new ClientConfiguration().setAddresses("192.168.111.3");

            try (IgniteClient client = Ignition.startClient(cfg)) {
                ClientCache <Integer, String> cache = client.getOrCreateCache("Cache_1");
                if (cache.size(CachePeekMode.PRIMARY) == 0) {
                    System.out.println("Cache is empty!");
                    cache.put(1,"First string");
                } else {
                    System.out.println("Cache contain data");
                }
            }
        }
}
