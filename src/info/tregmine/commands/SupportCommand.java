package info.tregmine.commands;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SupportCommand extends AbstractCommand
{
    public SupportCommand(Tregmine tregmine)
    {
        super(tregmine, "support");
    }

    @Override
    public boolean handlePlayer(TregminePlayer p, String[] args)
    {
        if (args.length == 0) {
            return false;
        }

        String text = Arrays.toString(args).replace("[", "").replaceAll("[],,]", "");

        email(text, p);

        return true;
    }

    public void email(String text, TregminePlayer p){
        final String username = tregmine.getConfig().getString("support.user");
        final String password = tregmine.getConfig().getString("support.password");
        final List<?> to = tregmine.getConfig().getList("support.to");
        String[] recipients = to.toArray(new String[to.size()]);

        String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", 
                new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        try {
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++)
            {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            Message message = new MimeMessage(session);
            message.setRecipients(RecipientType.TO, addressTo); 
            message.setSubject("[Help Request] from " + p.getName());
            message.setText(
                    "Help request from: " + p.getName() + " at " + time + "\n" +
                            "\n" +
                            "Message: " + text + "\n"
                    );
            Transport.send(message);
            p.sendMessage(ChatColor.GREEN + "Help request sent, we will get back to you ASAP :)");
        } catch (MessagingException e) {
            p.sendMessage(ChatColor.RED + "Error sending message, please try again.");
            throw new RuntimeException(e);
        }
    }
}
