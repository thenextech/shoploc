package nextech.shoploc.services.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendHtmlEmail(String to, String subject, String verificationCode) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            // Modèle HTML avec le code de vérification ajouté dynamiquement
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"fr\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "    <tr>\n" +
                    "      <td style=\"text-align: center;\">\n" +
                    "        <img src=\"https://drive.google.com/uc?export=view&id=1PzzRBR78crqMa-T1nb1U1zeIhRgVNlnu\" alt=\"Logo Shoploc\" style=\"width: 100px; margin: 0 auto; display: block;\">\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "      <td style=\"text-align: center; padding: 20px 0;\">\n" +
                    "        <p>Votre code de vérification Shoploc est :</p>\n" +
                    "        <p style=\"font-size: 30px; font-weight: bold; color: #3C24D1;\"> "+verificationCode+ "</p>\n" +
                    "        <p>Merci de ne pas communiquer ce code à qui que ce soit.</p>\n" +
                    "        <p>Pour toute question, contactez l'équipe Shoploc :</p>\n" +
                    "        <p><a href=\"mailto:contact@shoploc.com\">contact@shoploc.com</a></p>\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "      <td style=\"background-color: #000; text-align: center; padding: 10px 0; color: #fff;\">\n" +
                    "        &copy; <span id=\"currentYear\"></span> Shoploc. Tous droits réservés.\n" +
                    "      </td>\n" +
                    "    </tr>\n" +
                    "  </table>\n" +
                    "\n" +
                    "  <script>\n" +
                    "    // Code JavaScript pour afficher l'année actuelle\n" +
                    "    document.getElementById(\"currentYear\").textContent = new Date().getFullYear();\n" +
                    "  </script>\n" +
                    "</body>\n" +
                    "</html>\n";

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Définit le contenu comme HTML

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }



}
