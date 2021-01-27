package org.example.mailApi

import java.time.Duration
import java.time.Instant


class IcsMeeting(
    var subject: String? = null,
    var description: String? = null,
    var startTime: Instant?,
    var duration: Duration?,
    val organizer: String,
    var attendees: List<String>,
)