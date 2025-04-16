package com.onevoice.notification.application.dto.event;

import java.util.List;

public record DiscordMessage(
    String content,
    String username,
    String avatar_url,
    List<Embed> embeds
) {

    public record Embed(
        String title,
        String description,
        String url,
        int color
    ) {

    }
}
