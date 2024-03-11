package nextech.shoploc.services.stripe;

import java.util.List;
import java.util.Map;

import com.stripe.exception.StripeException;

public interface StripeService {
	
	public Map<String, String> createCheckoutSession(Map<String, List<Map<String, Object>>> requestBody, String stripeSecretKey) throws StripeException;

}
