package org.example.main

import org.example.mailApi.MeetingManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger: Logger = LoggerFactory.getLogger(Main::class.java)
            logger.info("Hello World")
            val host = "imap.gmail.com"
            val port = "993"
            val login = ""
            val password = ""
            val meetingManager = MeetingManager()
            val meetings = meetingManager.getMeetingsFromMailbox(host, port, login, password)
            meetings.forEach{
                print("тема: ")
                //print(it.subject)
                print(", описание: ")
                //print(it.description)
                print(", начало встречи: ")
                print(it.startTime)
                print(", конец встречи: ")
                print(it.endTime)
                println()
                print("***")
                println()
            }
        }
    }
}