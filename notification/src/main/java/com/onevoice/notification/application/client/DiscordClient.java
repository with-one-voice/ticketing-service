package com.onevoice.notification.application.client;

import com.onevoice.notification.application.dto.event.DiscordMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.name}", url = "${discord.webhook.url}")
public interface DiscordClient {

    @PostMapping
    void push(@RequestBody DiscordMessage message);
}
