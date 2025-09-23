package com.agents.schedule.Services;

import com.agents.schedule.Entities.Response;
import com.agents.schedule.MyState;
import com.agents.schedule.Nodes.GreeterNode;
import com.agents.schedule.Nodes.LogicNode;
import com.agents.schedule.Nodes.ResponderNode;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Service
public class ScheduleService {

    private final CompiledGraph<MyState> compiledGraph;

    public ScheduleService() throws GraphStateException {
        GreeterNode greeterNode = new GreeterNode();
        ResponderNode responderNode = new ResponderNode();
        LogicNode logicNode = new LogicNode();

        StateGraph<MyState> stateGraph = new StateGraph<>(MyState.SCHEMA, MyState::new)
                .addNode("greeter", node_async(greeterNode))
                .addNode("responder", node_async(responderNode))
                .addNode("validator", node_async(logicNode))
                .addEdge(START, "greeter")
                .addEdge("greeter", "validator")
                .addEdge("validator", "responder")
                .addEdge("responder", END);

        compiledGraph = stateGraph.compile();
    }

    public Response schedule(String hour, String date, String description) {
        Map<String,Object> inputData = new HashMap<>();

        inputData.put(MyState.MESSAGES_KEY, new ArrayList<String>());

        ((List<String>) inputData.get(MyState.MESSAGES_KEY))
                .add("Agendar " + description + " em " + date + " às " + hour);

        inputData.put("scheduleInput", Map.of(
                "date", date,
                "hour", hour,
                "description", description
        ));

        List<String> finalMessages = new ArrayList<>();

        for (var output : compiledGraph.stream(inputData)) {
            List<String> messages = (List<String>) output.state()
                    .value(MyState.MESSAGES_KEY)
                    .orElse(List.of());

            finalMessages.addAll(messages);
            System.out.println(messages);
        }
        return Response.builder().
                message(finalMessages.getLast())
                .success(Boolean.TRUE).
                build();
    }
}
