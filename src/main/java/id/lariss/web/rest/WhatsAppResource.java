package id.lariss.web.rest;

import id.lariss.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public/whatsapp")
public class WhatsAppResource {

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping("/webhook")
    public Mono<Void> receiveMessage(@RequestBody String payload) {
        return whatsAppService.processIncomingMessage(payload);
    }
}
