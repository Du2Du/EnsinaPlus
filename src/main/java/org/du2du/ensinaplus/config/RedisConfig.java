package org.du2du.ensinaplus.config;

import java.util.List;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.redis.client.RedisAPI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class RedisConfig {

    @Inject
    RedisAPI redisClient;
    
    void onStart(@Observes StartupEvent ev) {
        // Verificar conexão com Redis na inicialização
        try {
            redisClient.ping(List.of());
            System.out.println("Conexão com Redis estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao conectar com Redis: " + e.getMessage());
        }
    }
}