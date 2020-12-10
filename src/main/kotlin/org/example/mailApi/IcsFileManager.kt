package org.example.mailApi

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.Dur
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.property.DtEnd
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.util.MapTimeZoneCache
import java.io.StringReader
import java.time.Duration
import java.time.ZonedDateTime

class IcsFileManager(private val attachment: Attachment) {

    fun getMeeting(): IcsMeeting? {
        if (!attachment.name.endsWith(".ics", ignoreCase = true) || attachment.content == null) {
            return null
        }

        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache::class.java.name)
        val content = String(attachment.content, charset("UTF-8"))
        val reader = StringReader(content)
        val builder = CalendarBuilder()
        val calendar = builder.build(reader)
        val event = calendar.getComponents<VEvent>(Component.VEVENT).single()
        val description = event.description.value
        val subject = event.summary.value
        val attendees = event
            .getProperties<Attendee>(Property.ATTENDEE)
            .filter { it.calAddress.scheme.equals("MAILTO", ignoreCase = true) }
            .map { x -> x.calAddress.schemeSpecificPart }
        val organizer = event.organizer.calAddress.schemeSpecificPart

        val dur = calculateDuration(event.startDate, event.endDate, event.duration)
        val duration = Duration.ofSeconds(dur.seconds.toLong()) +
                Duration.ofMinutes(dur.minutes.toLong()) +
                Duration.ofHours(dur.hours.toLong())

        val startDateTime = event.startDate?.date?.toInstant()
        val meeting = IcsMeeting(subject, description, startDateTime, duration, organizer, attendees)
        return meeting
    }

    private fun calculateDuration(startDate: DtStart?, endDate: DtEnd?, duration: net.fortuna.ical4j.model.property.Duration?): Dur {
        if (duration != null) {
            return duration.duration
        }
        if (startDate == null){
            return Dur(0,0,0,0)
        }
        if (endDate == null) {
            return Dur(0,0,0,0)
        }
        return Dur(startDate.date, endDate.date)
    }
}

