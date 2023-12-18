package nextech.shoploc.services.auth;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nextech.shoploc.domains.Invoice;
import nextech.shoploc.domains.OrderLine;
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
            String htmlContent = "<!DOCTYPE html>\n" + "<html lang=\"fr\">\n" + "<head>\n" + "  <meta charset=\"UTF-8\">\n" + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + "</head>\n" + "<body>\n" + "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" + "    <tr>\n" + "      <td style=\"text-align: center;\">\n" + "        <img src=\"https://drive.google.com/uc?export=view&id=1PzzRBR78crqMa-T1nb1U1zeIhRgVNlnu\" alt=\"Logo Shoploc\" style=\"width: 100px; margin: 0 auto; display: block;\">\n" + "      </td>\n" + "    </tr>\n" + "    <hr/>" + "    <tr>\n" + "      <td style=\"text-align: center; padding: 20px 0;\">\n" + "        <p>Votre code de vérification Shoploc est :</p>\n" + "        <p style=\"font-size: 30px;font-weight: bold; color: #3C24D1; margin: 0; border: 1px  solid #3C24D1;background: rgba(59, 36, 209, 0.15); padding: 10px; display: inline-block;\"> " + verificationCode + "</p>\n" + "        <p>Merci de ne pas communiquer ce code à qui que ce soit.</p>\n" + "        <p>Pour toute question, contactez l'équipe Shoploc :</p>\n" + "        <p><a href=\"mailto:contact@shoploc.com\">contact@shoploc.com</a></p>\n" + "      </td>\n" + "    </tr>\n" + "    <tr>\n" + "      <td style=\"background-color: #000; text-align: center; padding: 10px 0; color: #fff;\">\n" + "       " +
                    " &copy; <span id=\"currentYear\"></span> Shoploc. Tous droits réservés.\n" + "      </td>\n" + "    </tr>\n" + "  </table>\n" + "\n" + "  <script>\n" + "    document.getElementById(\"currentYear\").textContent = new Date().getFullYear();\n" + "  </script>\n" + "</body>\n" + "</html>\n";

            helper.setTo(to);
            helper.setSubject("Code de vérification Shoploc");
            helper.setText(htmlContent, true); // Définit le contenu comme HTML

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }

    public void sendInvoiceEmail(Invoice invoice) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Facture</title>\n" +
                    "<style>\n" +
                    "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; }\n" +
                    "  .container { width: 90%; margin: 0 auto; background-color: #fff; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); overflow: hidden; }\n" +
                    "  .invoice-header { background-color: #3C24D1; color: #fff; padding: 15px; text-align: center; }\n" +
                    "  .invoice-details { padding: 15px; }\n" +
                    "  .table { width: 100%; border-collapse: collapse; font-size: 14px; }\n" +
                    "  .table th, .table td { padding: 5px; text-align: left; border-bottom: 1px solid #ddd; }\n" +
                    "  .thead-dark { background-color: #3C24D1; color: #fff; }\n" +
                    "  .total-amount { background-color: #f9f9f9; padding: 15px; text-align: right; }\n" +
                    "  .footer { padding: 15px; }\n" +
                    "  @media screen and (max-width: 768px) {\n" +
                    "    .container { width: 100%; }\n" +
                    "    .table { font-size: 12px; }\n" +
                    "  }\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div class=\"container\">\n" +
                    "<div class=\"invoice-header\">\n" +
                    "<h2>Facture</h2>\n" +
                    "<p>N° de facture : <b>#" + invoice.getInvoiceId() + "</b></p>\n" +
                    "</div>\n" +
                    "<div class=\"invoice-details\">\n" +
                    "<p class=\"bold\">Client : <b>" + invoice.getOrder().getUser().getFirstName() + " " + invoice.getOrder().getUser().getLastName() + "</b></p>\n" +
                    "<p>Email : <b>" + invoice.getOrder().getUser().getEmail() + "</b></p>\n" +
                    "<p>Date : <b>" + formatLocalDateTime(invoice.getDateInvoice()) + "</b></p>\n" +
                    "</div>\n" +
                    "<div class=\"table-responsive\">\n" +
                    "<table class=\"table\">\n" +
                    "<thead class=\"thead-dark\">\n" +
                    "<tr>\n" +
                    "<th>Produit</th>\n" +
                    "<th>Commerçant</th>\n" +
                    "<th>Quantité</th>\n" +
                    "<th>Prix unitaire</th>\n" +
                    "<th>Total</th>\n" +
                    "</tr>\n" +
                    "</thead>\n" +
                    "<tbody>\n";

            for (OrderLine orderLine : invoice.getOrder().getOrderLines()) {
                htmlContent += "<tr>\n" +
                        "<td>" + orderLine.getProduct().getName() + "</td>\n" +
                        "<td>" + orderLine.getProduct().getCategoryProduct().getMerchant().getBusinessName() + "</td>\n" +
                        "<td>" + orderLine.getQuantity() + "</td>\n" +
                        "<td>" + orderLine.getUnitPrice() + " €" + "</td>\n" +
                        "<td>" + (orderLine.getQuantity() * orderLine.getUnitPrice()) + " €" + "</td>\n" +
                        "</tr>\n";
            }

            htmlContent += "</tbody>\n" +
                    "</table>\n" +
                    "</div>\n" +
                    "<div class=\"total-amount\">\n" +
                    "<p>Montant total : <b>" + invoice.getPriceInvoice() + " €" + "</b></p>\n" +
                    "<p>Méthode de paiement : <b>" + invoice.getPaymentMethod() + "</b></p>\n" +
                    "</div>\n" +
                    "<div class=\"footer\">\n" +
                    "<p>Shoploc - Tous les droits sont réservés.</p>\n" +
                    "<p>Pour plus d'informations concernant votre commande, merci d'accéder au site internet ou de nous contacter directement par mail à l'adresse : <a href=\"mailto:contact@shoploc.com\">contact@shoploc.com</a></p>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";

            helper.setTo(invoice.getOrder().getUser().getEmail());
            helper.setSubject("Votre facture Shoploc #" + invoice.getInvoiceId());
            helper.setText(htmlContent, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            ByteArrayResource invoicePDF = new ByteArrayResource(outputStream.toByteArray());
            helper.addAttachment("invoice-" + invoice.getInvoiceId() + ".pdf", invoicePDF);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
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
    
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        LocalDate date = localDateTime.toLocalDate();
        LocalTime heure = localDateTime.toLocalTime();
        String dateFormate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String heureFormate = heure.format(DateTimeFormatter.ofPattern("HH:mm"));
        return dateFormate + ", à " + heureFormate;
    }


}
