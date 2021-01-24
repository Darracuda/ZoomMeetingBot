package org.example.mailApi

import org.example.main.Main
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*
import javax.mail.Folder
import javax.mail.Session


class MeetingManager {
    fun getMeetingsFromMailbox(host: String?, port: String?, login: String?, password: String?): List<IcsMeeting?>{
        val logger: Logger = LoggerFactory.getLogger(Main::class.java)
        logger.info("Creating mail session...")
        val session = getSession(host, port)
        logger.info("Creating mail session is successful")
        logger.info("Receiving mail store...")
        val store = session.getStore("imap")
        logger.info("Receiving is successful")
        logger.info("Connecting to mail store...")
        store.connect(login, password)
        logger.info("Connection is successful")
        val folder = store.getFolder("INBOX")
        logger.info("Opening mail folder...")
        folder.open(Folder.READ_ONLY)
        logger.info("Mail folder is successfully opened")
        val count = folder.messageCount
        logger.info("Received $count mail messages")

        val meetings = mutableListOf<IcsMeeting?>()
        for (i in 1..count) {
            val message = folder.getMessage(i)
            val attachmentManager = AttachmentManager(message)
            val attachments = attachmentManager.getAttachments()
            attachments.forEach{
                val icsFileManager = IcsFileManager(it)
                try {
                    logger.info("Started attachment analysis in $i message of $count...")
                    val meeting = icsFileManager.getMeeting()
                    if(meeting != null) {
                        meetings.add(meeting)
                    }
                } catch (ex: Exception){
                    println(ex)
                }
            }
        }
        logger.info("Found ${meetings.size} .ics file(s) in $count messages")

        folder.close(false)
        store.close()
        return meetings
    }

    private fun getSession(host: String?, port: String?): Session {
        val properties = Properties()
        // server setting
        properties["mail.imap.host"] = host
        properties["mail.imap.port"] = port
        // SSL setting
        properties["mail.imap.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.imap.socketFactory.fallback"] = "false"
        properties["mail.imap.socketFactory.port"] = port
        properties["mail.mime.base64.ignoreerrors"] = "true"
        properties["mail.imap.partialfetch"] = "false"

        return Session.getInstance(properties)
    }


}