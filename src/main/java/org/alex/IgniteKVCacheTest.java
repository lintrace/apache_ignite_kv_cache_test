/*
Group of tests for understanding how to work with
Apache Ignite via thin client
 */

package org.alex;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

import static org.alex.KeyValueCacheTest.startKVCacheTest;

public class IgniteKVCacheTest {
    final public static String SERVER_NODE_ADDRESS = "192.168.111.3:10800";

    public static void main(String[] args) {

        ClientConfiguration cfg = new ClientConfiguration().setAddresses(SERVER_NODE_ADDRESS);
        cfg.setPartitionAwarenessEnabled(true);

        try (IgniteClient client = Ignition.startClient(cfg)) {
            startKVCacheTest(client);
        }
    }
}

