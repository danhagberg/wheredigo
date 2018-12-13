package net.digitaltsunami.wheredigo.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WheredigoElasticsearchProperties {

    /**
     * Elasticsearch cluster name.
     */
    private String clusterName;

    /**
     * Comma-separated list of cluster node addresses.
     */
    private String clusterNodes;

    /**
     * Additional properties used to configure the client.
     */
    private Map<String, String> properties = new HashMap<>();

    public String getClusterName() {
        return this.clusterName;
    }

    @Value("${wheredigo.elasticsearch.cluster-name}")
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterNodes() {
        return this.clusterNodes;
    }

    @Value("${wheredigo.elasticsearch.cluster-nodes}")
    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

}
