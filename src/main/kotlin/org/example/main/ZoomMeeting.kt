package org.example.main

import java.time.Duration
import java.time.ZonedDateTime

class ZoomMeeting(
    val topic: String? = null,
    val agenda: String? = null,
    val startTime: ZonedDateTime? = null,
    val duration: Duration? = null,
)