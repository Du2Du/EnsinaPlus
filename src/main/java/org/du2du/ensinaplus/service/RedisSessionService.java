package org.du2du.ensinaplus.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.session.Session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.vertx.mutiny.redis.client.RedisAPI;
import io.vertx.mutiny.redis.client.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RedisSessionService {

    @Inject
    RedisAPI redisClient;
    
    private final ObjectMapper objectMapper;
    private static final int SESSION_TTL = 86400; // 24 horas
    
    public RedisSessionService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public void saveSession(Session session) {
        try {
            String sessionJson = objectMapper.writeValueAsString(session);
            redisClient.setex(session.getUuid().toString(), String.valueOf(SESSION_TTL), sessionJson)
                .subscribe().with(
                    response -> System.out.println("Sessão salva com sucesso: " + session.getUuid()),
                    error -> System.err.println("Erro ao salvar sessão: " + error.getMessage())
                );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar sessão", e);
        }
    }
    
    public Optional<Session> getSession(UUID sessionId) {
        try {
            Response response = redisClient.get(sessionId.toString())
                .await().indefinitely();
                
            if (response == null) {
                return Optional.empty();
            }
            
            String sessionJson = response.toString();
            Session session = objectMapper.readValue(sessionJson, Session.class);
            return Optional.of(session);
        } catch (Exception e) {
            System.err.println("Erro ao recuperar sessão: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    public void deleteSession(UUID sessionId) {
        redisClient.del(List.of(sessionId.toString()))
            .subscribe().with(
                response -> System.out.println("Sessão removida com sucesso: " + sessionId),
                error -> System.err.println("Erro ao remover sessão: " + error.getMessage())
            );
    }
    
    public void updateSessionExpiry(UUID sessionId) {
        redisClient.get(sessionId.toString())
            .subscribe().with(
                response -> {
                    if (response != null) {
                        redisClient.expire(List.of(sessionId.toString(), String.valueOf(SESSION_TTL)))
                            .subscribe();
                    }
                },
                error -> System.err.println("Erro ao verificar sessão: " + error.getMessage())
            );
    }
}