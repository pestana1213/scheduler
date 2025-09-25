package com.agents.schedule.Nodes;

import com.agents.schedule.MyState;
import com.agents.schedule.agents.ResponderAgent;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResponderNode implements NodeAction<MyState> {

    private final ResponderAgent responderAgent;

    public ResponderNode() {
        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
                .baseUrl("http://192.168.1.195:31434")
                .modelName("mistral")
                .build();

        this.responderAgent = AiServices.create(ResponderAgent.class, ollamaChatModel);
    }

    @Override
    public Map<String, Object> apply(MyState state) {
        List<String> currentMessages = state.messages();

        Optional<String> matchedMessage = currentMessages.stream()
                .filter(msg -> msg.contains("Slot available") || msg.contains("Slot not available"))
                .findFirst();

        String reply;
        if (matchedMessage.isPresent()) {
            reply = responderAgent.replyMessage(matchedMessage.get());
        } else {
            reply = "No logic found.";
        }

        List<String> updatedMessages = new ArrayList<>(currentMessages);
        updatedMessages.add(reply);

        return Map.of(MyState.MESSAGES_KEY, updatedMessages);
    }

}
