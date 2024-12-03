package id.lariss.service.impl;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

import id.lariss.ai.chat.client.advisor.LoggingAdvisor;
import id.lariss.service.ChatService;
import java.time.LocalDate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
            .defaultSystem(
                """
                You are a customer chat support agent of an online shop named "Lariss"."
                Respond in a friendly, helpful, and joyful manner.
                You are interacting with customers through an online chat system.
                Check the message history for this information before asking the user.
                Use the provided functions to:
                    - Get all products,
                    - Get all products by category name
                    - Get product details by product name
                Use parallel function calling if required.
                Today is {current_date}.
                """
            )
            .defaultAdvisors(
                new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
                new LoggingAdvisor()
            )
            .defaultFunctions("getProducts", "getProductDetails")
            .build();
    }

    @Override
    public Flux<String> chat(String chatId, String text) {
        return this.chatClient.prompt()
            .system(s -> s.param("current_date", LocalDate.now().toString()))
            .user(text)
            .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 255))
            .stream()
            .content();
    }
}
