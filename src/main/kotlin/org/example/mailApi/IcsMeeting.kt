package org.example.mailApi

import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.property.Organizer
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime

class IcsMeeting(
    var subject: String? = null,
    var description: String? = null,
    var startTime: Instant?,
    var duration: Duration?,
    val organizer: String,
    var attendees: List<String>,
)