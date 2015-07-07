package com.nike.mm

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.node.NodeBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate

@Configuration
class RepositoryConfig {

    /**
     * Remote Elasticsearch address
     */
    @Value('${mm.elasticsearch.address}')
    private String elasticsearchAddress;

    /**
     * Remote elasticsearch port
     */
    @Value('${mm.elasticsearch.port}')
    private int elasticsearchPort;

    /**
     * Elasticsearch cluster name
     */
    @Value('${mm.elasticsearch.cluster.name}')
    private String elasticsearchClusterName;

    /**
     * Switch to use the embeded elasticsaerch or a remote instance
     */
    @Value('${mm.elasticsearch.embedded}')
    private boolean elasticsearchEmbedded;

    /**
     * Override the default elasticsearch autoconfig elasticsearchTemplate
     * @return ElasticsearchOperations elasticsearchTemplate
     */
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }

    /**
     * Override the default elasticsearch autoconfig client
     * @return Client elasticsearch client
     */
    @Bean
    public Client client(){
        if(elasticsearchEmbedded) {
            return new NodeBuilder().local(true).node().client();
        }
        Settings settings = ImmutableSettings.settingsBuilder().put('cluster.name', elasticsearchClusterName).build();
        TransportClient client= new TransportClient(settings);
        TransportAddress address = new InetSocketTransportAddress(elasticsearchAddress, elasticsearchPort);
        client.addTransportAddress(address);
        return client;
    }
}
