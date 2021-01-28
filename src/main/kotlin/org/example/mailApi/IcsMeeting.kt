package org.example.mailApi

import java.time.Duration
import java.time.Instant


class IcsMeeting(
    var subject: String,
    var description: String?,
    var startTime: Instant,
    var duration: Duration,
    val organizer: String,
    var attendees: List<String>,
)