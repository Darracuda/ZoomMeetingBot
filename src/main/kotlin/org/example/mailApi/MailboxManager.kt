package org.example.mailApi

import com.sun.mail.smtp.SMTPTransport
import org.example.main.Main
import org.example.settings.SettingsManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.search.FlagTerm

class MailboxManager(private val settingsManager: SettingsManager){
    private val logger: Logger = LoggerFactory.getLogger(Main::class.java)
    private val propFile = File(javaClass.classLoader.getResource("config.properties").file)

    fun sendMessage(meeting: IcsMeeting ){
        val prop = Properties()
        FileInputStream(propFile).use { prop.load(it) }

        val settingsManager = SettingsManager(propFile)
        settingsManager.loadProp()

        val smtpHost = settingsManager.smtpHost
        val smtpPort = settingsManager.smtpPort
        val smtpLogin = settingsManager.smtpLogin
        val smtpPassword = settingsManager.smtpPassword
        val emailAddressFrom = settingsManager.smtpEmailAddressFrom
        val emailAddressesToCC = emptyArray<String>()

        val emailSubject = meeting.subject
        val emailText = meeting.description
        val emailToAddresses = meeting.attendees.toTypedArray()

        val session = getSmtpSession(smtpHost, smtpPort)
        val msg: Message = MimeMessage(session)

        try {
            msg.setFrom(InternetAddress(emailAddressFrom))
            msg.setRecipients(Message.RecipientType.TO, emailToAddresses.map { a -> InternetAddress(a) }.toTypedArray())
            msg.setRecipients(
                Message.RecipientType.CC,
                emailAddressesToCC.map { a -> InternetAddress(a) }.toTypedArray()
            )
            msg.subject = emailSubject
            msg.setText(emailText)
            msg.sentDate = Date()
            val t = session.getTransport("smtp") as SMTPTransport
            t.connect(smtpHost, smtpLogin, smtpPassword)
            t.sendMessage(msg, msg.allRecipients)
            logger.info("Message sent - Response: " + t.lastServerResponse)
            t.close()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    fun receiveMessages(): Array<Message> {
        val host = settingsManager.imapHost
        val port = settingsManager.imapPort
        val login = settingsManager.imapLogin
        val password = settingsManager.imapPassword

        logger.info("Creating mail session...")
        val session = getImapSession(host, port)
        logger.info("Receiving mail store...")
        val store = session.getStore("imap")
        logger.info("Connecting to mail store...")
        store.connect(login, password)
        val inboxFolder = store.getFolder("INBOX")
        logger.info("Opening mail folder...")
        inboxFolder.open(Folder.READ_WRITE)
        logger.info("Mail folder is successfully opened")
        val messages: Array<Message> = inboxFolder.search(
            FlagTerm(Flags(Flags.Flag.SEEN), false)
        )
        //inboxFolder.close(false)
        //store.close()
        return messages
    }

    private fun getSmtpSession(host: String?, port: String?): Session {
        val properties = Properties()
        properties["mail.smtp.host"] = host
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = port
        properties["mail.smtp.starttls.enable"] = "true"
        return Session.getInstance(properties)
    }

    private fun getImapSession(host: String?, port: String?): Session {
        val properties = Properties()
        // server setting
        properties["mail.imap.host"] = host
        properties["mail.imap.port"] = port
        // SSL setting
        properties["mail.imap.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.imap.socketFactory.fallback"] = "false"
        properties["mail.imap.socketFactory.port"] = port
        properties["mail.mime.base64.ignoreerrors"] = "true"
        properties["mail.imap.partialfetch"] = "true"

        return Session.getInstance(properties)
    }
}