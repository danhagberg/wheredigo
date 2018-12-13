package net.digitaltsunami.wheredigo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

@Configuration
@EnableElasticsearchRepositories(basePackages = "net.digitaltsunami.wheredigo.model")
@ComponentScan(basePackages = {"net.digitaltsunami.wheredigo.service"})
public class Config {
    private static Logger logger = LoggerFactory.getLogger(Config.class);


    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(ObjectMapper mapper, TransportClient transportClient) throws UnknownHostException {
        return new ElasticsearchTemplate(transportClient, new CustomEntityMapper(mapper));
    }

    @Bean
    public TransportClient transportClient(WheredigoElasticsearchProperties properties) throws Exception {
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterNodes(properties.getClusterNodes());
        Properties factoryProperties = new Properties();
        factoryProperties.put("cluster.name", properties.getClusterName());
        factory.setProperties(factoryProperties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    @Autowired
    public EntityMapper entityMapper(ObjectMapper mapper) {
        return new CustomEntityMapper(mapper);
    }

    public class CustomEntityMapper implements EntityMapper {

        private ObjectMapper objectMapper;

        public CustomEntityMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }
    }
}
