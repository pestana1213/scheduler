package com.agents.schedule.Nodes;

import com.agents.schedule.MyState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.List;
import java.util.Map;

public class LogicNode implements NodeAction<MyState> {

    @Override
    public Map<String, Object> apply(MyState myState) throws Exception {
        Map<String, Object> inputMap = (Map<String,Object>) myState.value("scheduleInput").orElse(Map.of());
        ScheduleInput input = new ScheduleInput(
                inputMap.getOrDefault("date","").toString(),
                inputMap.getOrDefault("hour","").toString(),
                inputMap.getOrDefault("description","").toString()
        );
        boolean available = checkAvailability(input);
        myState.<List<String>>value(MyState.MESSAGES_KEY)
                .ifPresent(list -> list.add(available ? "Slot available" : "Slot not available"));

        return Map.of(MyState.MESSAGES_KEY, available ? "Slot available" : "Slot not available");
    }

    private boolean checkAvailability(ScheduleInput input) {
        int hour = Integer.parseInt(input.hour());
        return hour % 2 == 1;
    }

    record ScheduleInput(String date, String hour, String description) {}
}
