package nextech.shoploc.controllers.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import nextech.shoploc.services.stripe.StripeService;

@RestController
@RequestMapping("/stripe")
public class StripeController {
	
	@Value("${stripe.api.secret_key}")
    private String stripeSecretKey;
	
	@Autowired
	private StripeService stripeService;
	
	
	@PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, List<Map<String, Object>>> requestBody) throws StripeException {
		try {
			return stripeService.createCheckoutSession(requestBody, stripeSecretKey);
		} catch (StripeException e) {
			e.printStackTrace();
			return null;
		}
    }

}
