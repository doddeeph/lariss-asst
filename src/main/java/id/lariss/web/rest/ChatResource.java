package id.lariss.web.rest;

import id.lariss.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/public/chat")
public class ChatResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChatResource.class);

    private final ChatService chatService;

    public ChatResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> chat(@RequestParam String chatId, @RequestParam String text) {
        LOG.debug("REST request chat -> chatId: {}, text: {}", chatId, text);
        return chatService.chat(chatId, text);
    }
}
