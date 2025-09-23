package com.agents.schedule.Nodes;

import com.agents.schedule.MyState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.List;
import java.util.Map;

public class ResponderNode implements NodeAction<MyState> {

    @Override
    public Map<String, Object> apply(MyState state) {
        List<String> currentMessages = state.messages();
        if (currentMessages.contains("Slot available") || currentMessages.contains("Slot not available")) {
            return Map.of(MyState.MESSAGES_KEY, "Acknowledged logic!");
        }
        return Map.of(MyState.MESSAGES_KEY, "No logic found.");
    }
}
