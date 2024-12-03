package id.lariss.service;

import id.lariss.service.dto.TwilioWebhookDTO;
import reactor.core.publisher.Flux;

public interface TwilioService {
    Flux<String> webhook(TwilioWebhookDTO twilioWebhookDTO);
}
