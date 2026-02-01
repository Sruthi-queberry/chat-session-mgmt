package io.chatsessionmgmt.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class ApiKeyService {

    private final Dotenv dotenv = Dotenv.load();

    public String resolveUser(String apiKey) {
        if (apiKey == null) return null;
        if (apiKey.equals(dotenv.get("API_KEY_USER1"))) return dotenv.get("API_KEY_USER1");
        if (apiKey.equals(dotenv.get("API_KEY_USER2"))) return dotenv.get("API_KEY_USER1");
        return null;
    }
}

