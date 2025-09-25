package com.agents.schedule.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ResponderAgent {

    @SystemMessage("You are a receptionist that is going to reply if the appointment was scheduled or not")
    String replyMessage(@UserMessage String message);

}
