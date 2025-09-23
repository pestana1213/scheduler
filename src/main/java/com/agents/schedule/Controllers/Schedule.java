package com.agents.schedule.Controllers;

import com.agents.schedule.Entities.Response;
import com.agents.schedule.Services.ScheduleService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class Schedule {

    private final ScheduleService scheduleService;

    public Schedule(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @MutationMapping
    public Response schedule(@Argument String date,@Argument String hour,@Argument String description) {
        return scheduleService.schedule(hour, date, description);
    }

}