package org.example.mailApi

import org.example.main.Main
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import javax.mail.Flags

class IcsMeetingManager(private val mailboxManager: MailboxManager) {
    fun getMeetingsFromMailbox(): List<IcsMeeting>{
        val logger: Logger = LoggerFactory.getLogger(Main::class.java)
        val messages = mailboxManager.receiveMessages()
        val unreadEmailCount = messages.size
        logger.info("Received $unreadEmailCount new email messages")
        val meetings = mutableListOf<IcsMeeting>()
        for (message in messages) {
            val attachmentManager = AttachmentManager(message)
            val attachments = attachmentManager.getAttachments()
            attachments.forEach{
                val icsFileManager = IcsFileManager(it)
                try {
                    logger.info("Started attachment analysis in ${message.messageNumber} message...")
                    val meeting = icsFileManager.getMeeting()
                    if(meeting != null) {
                        meetings.add(meeting)
                    }
                } catch (ex: Exception){
                    println(ex)
                }
            }
            message.setFlag(Flags.Flag.SEEN, true)
        }
        logger.info("Found ${meetings.size} .ics file(s)")
        return meetings
    }
}