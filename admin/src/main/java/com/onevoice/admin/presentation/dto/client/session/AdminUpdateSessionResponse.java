package com.onevoice.admin.presentation.dto.client.session;

import java.util.UUID;

public record AdminUpdateSessionResponse (
        UUID sessionId,
        String status
) {}