package id.lariss.web.rest;

import id.lariss.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public/whatsapp")
public class WhatsAppResource {

    @Autowired
    private WhatsAppService whatsAppService;

    @GetMapping("/webhook")
    public Mono<String> verifyWebhook(
        @RequestParam("hub.mode") String mode,
        @RequestParam("hub.verify_token") String token,
        @RequestParam("hub.challenge") String challenge
    ) {
        return verifyWebhook(mode, token, challenge);
    }

    @PostMapping("/webhook")
    public Mono<Void> receiveMessage(@RequestBody String payload) {
        return whatsAppService.processIncomingMessage(payload);
    }
}
