package nextech.shoploc.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nextech.shoploc.models.merchant.MerchantResponseDTO;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendHtmlEmail(String to, String verificationCode) throws MessagingException {
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
                    "    <hr/>"+
                    "    <tr>\n" +
                    "      <td style=\"text-align: center; padding: 20px 0;\">\n" +
                    "        <p>Votre code de vérification Shoploc est :</p>\n" +
                    "        <p style=\"font-size: 30px;font-weight: bold; color: #3C24D1; margin: 0; border: 1px  solid #3C24D1;background: rgba(59, 36, 209, 0.15); padding: 10px; display: inline-block;\"> "+verificationCode+ "</p>\n" +
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
                    "    document.getElementById(\"currentYear\").textContent = new Date().getFullYear();\n" +
                    "  </script>\n" +
                    "</body>\n" +
                    "</html>\n";

            helper.setTo(to);
            helper.setSubject("Code de vérification Shoploc");
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public void sendPartnerVerificationEmail(String to, MerchantResponseDTO merchant) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String activationLink = "http://localhost:8080/merchant/activate/" + merchant.getUserId();
        String activationButton = "<a href=\"" + activationLink + "\" style=\"text-decoration: none;\">\n" +
                "<button style=\"background-color: #3C24D1; color: white; padding: 10px 20px; border: none; border-radius: 5px;\">Activer son compte</button>\n" +
              "</a>";
        try {
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
                    "    <hr/>"+
                    "    <tr>\n" +
                    "      <td style=\"text-align: center; padding: 20px 0;\">\n" +
                    "        <p><strong>Nouveau partenaire ! Veuillez vérifier ces informations</strong></p>\n" +
                    "        <p>Un nouveau partenaire veut faire partie de l'aventure Shoploc !</p>\n" +
                    "        <p>Veuillez vérifier ses informations et de cliquer sur le bouton Activer son compte ensuite :</p>\n" +
                    "        <ul style=\"list-style-type: none; padding-left: 0;\">\n" +
                    "          <li><strong>Nom de l'entreprise :</strong> " + merchant.getBusinessName() + "</li>\n" +
                    "          <li><strong>Adresse e-mail :</strong> " + merchant.getEmail() + "</li>\n" +
                    "          <li><strong>Adresse Ligne 1 :</strong> " + merchant.getLineAddress1() + "</li>\n" +
                    "          <li><strong>Adresse Ligne 2 :</strong> " + merchant.getLineAddress2() + "</li>\n" +
                    "          <li><strong>Ville :</strong> " + merchant.getCity() + "</li>\n" +
                    "          <li><strong>Code postal :</strong> " + merchant.getPostalCode() + "</li>\n" +
                    "        </ul>\n" +
                    "        <p>" + activationButton + "</p>\n" +
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
                    "    document.getElementById(\"currentYear\").textContent = new Date().getFullYear();\n" +
                    "    function activateMerchant(activationLink) {\n" +
                    "      fetch(" + activationLink + ", {\n" +
                    "        method: 'POST'\n" +
                    "      })\n" +
                    "      .then(response => {\n" +
                    "		 console.log(response);" + 
                    "        if (!response.ok) {\n" +
                    "          throw new Error('Erreur lors de l\'activation du compte du marchand.');\n" +
                    "        }\n" +
                    "      })\n" +
                    "      .catch(error => {\n" +
                    "        console.error('Erreur:', error);\n" +
                    "      });\n" +
                    "}\n" +                    
                    "  </script>\n" +
                    "</body>\n" +
                    "</html>\n";

            helper.setTo(to);
            helper.setSubject("Nouveau partenaire ! Veuillez vérifier ces informations");
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMerchantAccountActivatedEmail(String to) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
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
                    "    <hr/>"+
                    "    <tr>\n" +
                    "      <td style=\"text-align: center; padding: 20px 0;\">\n" +
                    "        <p>Votre compte marchand a été activé avec succès !</p>\n" +
                    "        <p>Vous pouvez désormais accéder à votre compte en cliquant sur le bouton ci-dessous :</p>\n" +
                    "        <p><a href=\"http://localhost:8080/merchant/login\" style=\"text-decoration: none;\"><button style=\"background-color: #3C24D1; color: white; padding: 10px 20px; border: none; border-radius: 5px;\">Accéder à mon compte</button></a></p>\n" +
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
                    "    document.getElementById(\"currentYear\").textContent = new Date().getFullYear();\n" +
                    "  </script>\n" +
                    "</body>\n" +
                    "</html>\n";

            helper.setTo(to);
            helper.setSubject("Votre compte marchand a été activé !");
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
