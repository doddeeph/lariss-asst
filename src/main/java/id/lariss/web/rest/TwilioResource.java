package id.lariss.web.rest;

import id.lariss.service.TwilioService;
import id.lariss.service.dto.TwilioWebhookDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/public/twilio")
public class TwilioResource {

    private final TwilioService twilioService;

    public TwilioResource(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping(path = "/webhook", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Flux<String> webhook(TwilioWebhookDTO twilioWebhookDTO) {
        return twilioService.webhook(twilioWebhookDTO);
    }
}
