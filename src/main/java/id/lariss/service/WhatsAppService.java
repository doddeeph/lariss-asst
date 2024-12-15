package id.lariss.service;

import reactor.core.publisher.Mono;

public interface WhatsAppService {
    Mono<Void> processIncomingMessage(String payload);

    Mono<Void> sendWhatsAppMessage(String number, String message);
}
