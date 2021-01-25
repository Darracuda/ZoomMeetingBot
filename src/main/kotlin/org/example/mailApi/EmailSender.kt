package org.example.mailApi

import com.sun.mail.smtp.SMTPTransport
import org.example.main.Main
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender{
    fun sendMessage(emailToAddresses: Array<String>){
        val logger: Logger = LoggerFactory.getLogger(Main::class.java)

        val propFile = File("/Users/d.kolpakova/Documents/config.properties")
        val prop = Properties()
        FileInputStream(propFile).use { prop.load(it) }

        val smtpHost = prop.getProperty("smtpHost")
        val smtpPort = prop.getProperty("smtpPort")
        val smtpLogin = prop.getProperty("smtpLogin")
        val smtpPassword = prop.getProperty("smtpPassword")
        val emailAddressFrom = prop.getProperty("emailAddressFrom")
        val emailAddressesToCC = emptyArray<String>()

        val emailSubject = "Test Send Email via SMTP"
        val emailText = "Hello Java Mail \n ABC123"

        val session = getSession(smtpHost, smtpPort)
        val msg: Message = MimeMessage(session)

        try {
            msg.setFrom(InternetAddress(emailAddressFrom))
            msg.setRecipients(Message.RecipientType.TO, emailToAddresses.map{ a -> InternetAddress(a) }.toTypedArray())
            msg.setRecipients(Message.RecipientType.CC, emailAddressesToCC.map{ a -> InternetAddress(a) }.toTypedArray())
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

    private fun getSession(host: String?, port: String?): Session {
        val properties = Properties()
        properties["mail.smtp.host"] = host
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = port
        properties["mail.smtp.starttls.enable"] = "true"
        return Session.getInstance(properties)
    }
}