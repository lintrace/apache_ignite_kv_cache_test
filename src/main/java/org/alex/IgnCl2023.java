package org.alex;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static org.alex.KeyValueCacheTest.KVCacheTest;


public class IgnCl2023 {

    public static void main(String[] args)  {

        ClientConfiguration cfg = new ClientConfiguration().setAddresses("192.168.111.3:10800");
        cfg.setPartitionAwarenessEnabled(true);

        try (IgniteClient client = Ignition.startClient(cfg)) {
            KVCacheTest(client);
        }
    }
}

