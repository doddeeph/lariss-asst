package id.lariss.service.impl;

import id.lariss.service.ChatService;
import id.lariss.service.TwilioService;
import id.lariss.service.dto.TwilioWebhookDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TwilioServiceImpl implements TwilioService {

    private final ChatService chatService;

    public TwilioServiceImpl(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Flux<String> webhook(TwilioWebhookDTO twilioWebhookDTO) {
        return chatService.chat(twilioWebhookDTO.getWaId(), twilioWebhookDTO.getBody());
    }
}
