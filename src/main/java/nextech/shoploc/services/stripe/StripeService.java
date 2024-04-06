package nextech.shoploc.services.stripe;

import com.stripe.exception.StripeException;

import java.util.List;
import java.util.Map;

public interface StripeService {
	
	public Map<String, String> createCheckoutSession(Map<String, List<Map<String, Object>>> requestBody, String stripeSecretKey) throws StripeException;

}
