package com.agents.schedule.Entities;

import lombok.Builder;

@Builder(toBuilder = true)
public record Response(Boolean success, String message) {
}
