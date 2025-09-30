package com.agents.schedule.Services;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class CalendarService {

    private final Calendar calendar;

    public CalendarService(Calendar calendar) {
        this.calendar = calendar;
    }

    public String createEvent(String calendarId, String summary, LocalDateTime start, LocalDateTime end) throws IOException {
        Event event = new Event()
                .setSummary(summary);

        DateTime startDateTime = new DateTime(
                Date.from(start.atZone(ZoneId.systemDefault()).toInstant())
        );
        DateTime endDateTime = new DateTime(
                Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        );

        EventDateTime startTime = new EventDateTime().setDateTime(startDateTime);
        EventDateTime endTime = new EventDateTime().setDateTime(endDateTime);

        event.setStart(startTime);
        event.setEnd(endTime);

        Event created = calendar.events().insert(calendarId, event).execute();
        return created.getHtmlLink();
    }


    public boolean isTimeSlotAvailable(String calendarId, LocalDateTime start, LocalDateTime end) throws IOException {
        DateTime startDateTime = new DateTime(
                Date.from(start.atZone(ZoneId.systemDefault()).toInstant())
        );
        DateTime endDateTime = new DateTime(
                Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        );

        FreeBusyRequest request = new FreeBusyRequest()
                .setTimeMin(startDateTime)
                .setTimeMax(endDateTime)
                .setItems(List.of(new FreeBusyRequestItem().setId(calendarId)));

        FreeBusyResponse response = calendar.freebusy().query(request).execute();

        List<TimePeriod> busyPeriods = response.getCalendars().get(calendarId).getBusy();

        return busyPeriods == null || busyPeriods.isEmpty();
    }
}
