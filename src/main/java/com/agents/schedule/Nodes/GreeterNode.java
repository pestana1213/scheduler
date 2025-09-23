package com.agents.schedule.Nodes;

import com.agents.schedule.MyState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class GreeterNode implements NodeAction<MyState> {

    @Override
    public Map<String, Object> apply(MyState state) {
        System.out.println("GreeterNode executing. Current messages: " + state.messages());
        return Map.of(MyState.MESSAGES_KEY, "Hello from GreeterNode!");
    }
}
