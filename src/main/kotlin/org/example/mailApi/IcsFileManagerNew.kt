package org.example.mailApi

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.ComponentList
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.util.MapTimeZoneCache
import java.io.StringReader
import java.time.LocalDateTime

class IcsFileManagerNew(private val attachment: Attachment){

    fun getMeeting(): MutableList<Meeting> {
        lateinit var meeting: Meeting
        lateinit var meetingList: MutableList<Meeting>

        if(attachment.name.endsWith(".ics", ignoreCase = true)){
            System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache::class.java.name)
            val icsString = String(attachment.content, charset("UTF-8"))
            val sin = StringReader(icsString)
            val builder = CalendarBuilder()
            val calendar: Calendar = builder.build(sin)
            val listEvent: ComponentList<*> = calendar.getComponents<CalendarComponent>(Component.VEVENT)

            for (elem in listEvent) {
                val event = elem as VEvent
                val description = event.description.value
                val title = event.summary.value
                val startDateTime = event.startDate.date as LocalDateTime
                val endDateTime = event.endDate.date as LocalDateTime
                meeting = Meeting(startDateTime, endDateTime)
                meetingList.add(meeting)

                println("$title : $description")
            }
        }
            //val startTime =
            //val endTime =
            //val subject =
            //val description =
            //meeting = Meeting(subject, description, startTime, endTime)


        return meetingList
    }

}