package org.example.mailApi

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class IcsFileManager(private val attachment: Attachment){
    private val pattern = "yyyyMMdd'T'HHmmss"
    private val dateTimeLength = pattern.replace("'", "").length
    private val dateTimeFormater: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    fun getMeeting(): Meeting {
        lateinit var meeting: Meeting
        if(attachment.name.endsWith(".ics", ignoreCase = true)){
            val text = String(attachment.content, charset("UTF-8"))
            val startTimeLine = text.lines().single { it.startsWith("DTSTART;TZID", ignoreCase = true) }
            val endTimeLine = text.lines().single { it.startsWith("DTEND", ignoreCase = true) }
            val startTimeString = startTimeLine.takeLast(dateTimeLength)
            val endTimeString = endTimeLine.takeLast(dateTimeLength)
            val startTime = LocalDateTime.parse(startTimeString, dateTimeFormater)
            val endTime = LocalDateTime.parse(endTimeString, dateTimeFormater)
            val subjectLine = text.lines().single{ it.contains("Subject")}
            val descriptionLine = text.lines().single{ it.contains("Subject")}
            //val subject =
            //val description =
            //meeting = Meeting(subject, description, startTime, endTime)
            meeting = Meeting(startTime, endTime)
        }
        return meeting
    }

}