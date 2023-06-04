/*
 A group of tests to measure the execution time of cache operations
 in Apache Ignite through a thin client
 */

package org.alex;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.ClientConfiguration;

import java.util.Map;

public class IgniteCacheTest {
    final public static String SERVER_NODE_ADDRESS = "localhost:10800";

    public static void main(String[] args) {

        ClientConfiguration cfg = new ClientConfiguration().setAddresses(SERVER_NODE_ADDRESS);
        cfg.setPartitionAwarenessEnabled(true);

        try (IgniteClient client = Ignition.startClient(cfg)) {
            Map<String, Object> attr = client.cluster().node().attributes();
            System.out.println("=======================================================");
            System.out.println("OS: " + attr.get("os.name") + " \t\t Ignite ver: " + attr.get("org.apache.ignite.build.ver"));
            System.out.println("=======================================================");

            System.out.println("Cluster is in " + client.cluster().state().name() + " state.\n");
            if (client.cluster().state() == ClusterState.INACTIVE || client.cluster().state() == ClusterState.ACTIVE_READ_ONLY) {
                client.cluster().state(ClusterState.ACTIVE);
                // Maybe we need there some delay to wait for cluster activation
                System.out.println("Now cluster is in " + client.cluster().state().name() + " state.\n");
            }


            if (client.cacheNames().size() > 0) {
                System.out.println("Caches detected:");
                client.cacheNames().forEach((v) -> System.out.println(v));
                System.out.println();
            }

            // Tests with Key Value cache
            new KeyValueCacheTest(client).startKVCacheTest();
        }
    }
}

