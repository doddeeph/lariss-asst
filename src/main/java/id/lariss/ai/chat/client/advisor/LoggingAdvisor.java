package id.lariss.ai.chat.client.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

public class LoggingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        LOG.debug("Request: {}", advisedRequest);
        AdvisedResponse response = chain.nextAroundCall(advisedRequest);
        LOG.debug("Response: {}", response);
        return response;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        LOG.debug("Request: {}", advisedRequest);
        Flux<AdvisedResponse> responses = chain.nextAroundStream(advisedRequest);
        return new MessageAggregator()
            .aggregateAdvisedResponse(responses, aggregatedAdvisedResponse -> LOG.debug("Response: {}", aggregatedAdvisedResponse));
    }

    @Override
    public String getName() {
        return "LoggingAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
