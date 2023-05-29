/*
Group of tests for understanding how to work with
Apache Ignite via thin client
 */

package org.alex;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.ClientConfiguration;

import static org.alex.KeyValueCacheTest.startKVCacheTest;

public class IgniteCacheTest {
    final public static String SERVER_NODE_ADDRESS = "192.168.111.3:10800";

    public static void main(String[] args) {

        ClientConfiguration cfg = new ClientConfiguration().setAddresses(SERVER_NODE_ADDRESS);
        cfg.setPartitionAwarenessEnabled(true);

        try (IgniteClient client = Ignition.startClient(cfg)) {
            System.out.println("Cluster is in " + client.cluster().state().name() + " state.\n");
            if (client.cluster().state() == ClusterState.INACTIVE || client.cluster().state() == ClusterState.ACTIVE_READ_ONLY) {
                client.cluster().state(ClusterState.ACTIVE);
                // Maybe we need some delay for cluster activation
                System.out.println("Now cluster is in " + client.cluster().state().name() + " state.\n");
            }


            if (client.cacheNames().size() > 0) {
                System.out.println("Caches detected:");
                client.cacheNames().forEach((v) -> System.out.println(v));
                System.out.println();
            }

            // Tests with Key Value cache
            startKVCacheTest(client);
        }
    }
}

