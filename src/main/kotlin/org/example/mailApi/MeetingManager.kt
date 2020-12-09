package org.example.mailApi

import java.lang.Exception
import java.util.*
import javax.mail.Folder
import javax.mail.Session
import javax.mail.Store

class MeetingManager {
    fun getMeetingsFromMailbox(host: String?, port: String?, login: String?, password: String?): MutableList<Meeting>{
        val meetings = mutableListOf<Meeting>()
        val session: Session = getSession(host, port)
        val store: Store = session.getStore("imap")
        store.connect(login, password)
        val folder: Folder = store.getFolder("INBOX")
        folder.open(Folder.READ_ONLY)
        val count = folder.messageCount

        for (i in 1..count) {
            val message = folder.getMessage(i)
            val attachmentManager = AttachmentManager(message)
            val attachments = attachmentManager.getAttachments()
            attachments.forEach{
                val icsFileManager = IcsFileManager(it)
                try {
                    val meeting = icsFileManager.getMeeting()
                    meetings.add(meeting)
                } catch (ex: Exception){

                }
            }
        }
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
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        properties.setProperty("mail.imap.socketFactory.fallback", "false")
        properties.setProperty("mail.imap.socketFactory.port", port)
        properties.setProperty("mail.mime.base64.ignoreerrors", "true")
        properties.setProperty("mail.imap.partialfetch", "false")

        return Session.getDefaultInstance(properties)
    }


}