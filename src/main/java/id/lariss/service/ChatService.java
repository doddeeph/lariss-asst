package id.lariss.service;

import reactor.core.publisher.Flux;

public interface ChatService {
    Flux<String> chat(String chatId, String text);
}
