package com.agents.schedule.Nodes;

import com.agents.schedule.MyState;
import com.agents.schedule.Services.CalendarService;
import org.bsc.langgraph4j.action.NodeAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class LogicNode implements NodeAction<MyState> {

    private final CalendarService calendarService;

    public LogicNode(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Override
    public Map<String, Object> apply(MyState myState) throws Exception {
        Map<String, Object> inputMap = (Map<String,Object>) myState.value("scheduleInput").orElse(Map.of());
        ScheduleInput input = new ScheduleInput(
                inputMap.getOrDefault("date","").toString(),
                inputMap.getOrDefault("hour","").toString(),
                inputMap.getOrDefault("description","").toString()
        );
        LocalDateTime start = buildLocalDateTime(inputMap.getOrDefault("date","").toString(), inputMap.getOrDefault("hour","").toString());
        LocalDateTime end = start.plusMinutes(30);
        boolean available = calendarService.isTimeSlotAvailable("primary", start, end);
        String ret = "";
        if (available) {
            ret = calendarService.createEvent("primary", "New schedule", start, end);
        }
        myState.<List<String>>value(MyState.MESSAGES_KEY)
                .ifPresent(list -> list.add(available ? "Slot available" : "Slot not available"));

        return Map.of(MyState.MESSAGES_KEY, available ? "Slot available: " + ret : "Slot not available");
    }

    public LocalDateTime buildLocalDateTime(String dateStr, String hourStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        LocalTime time = LocalTime.parse(hourStr, timeFormatter);

        return LocalDateTime.of(date, time);
    }

    record ScheduleInput(String date, String hour, String description) {}
}
