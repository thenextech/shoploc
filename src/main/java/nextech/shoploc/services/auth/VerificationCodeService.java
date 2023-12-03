package nextech.shoploc.services.auth;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationCodeService {

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Génère un code aléatoire à 6 chiffres
        return String.valueOf(code);
    }
}
