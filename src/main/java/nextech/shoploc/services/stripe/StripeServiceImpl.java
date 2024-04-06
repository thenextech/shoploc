package nextech.shoploc.services.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StripeServiceImpl implements StripeService {
	
	
	@Value("${env.local}")
    private String envUrl;
	
	private Long priceValue;

	@Override
	public Map<String, String> createCheckoutSession(Map<String, List<Map<String, Object>>> requestBody, String stripeSecretKey) throws StripeException {
		Stripe.apiKey = stripeSecretKey;
		Integer orderId = (Integer) requestBody.get("orderId").get(0).get("orderId");
		

		List<Map<String, Object>> products = requestBody.get("products");
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (Map<String, Object> product : products) {
        	Integer productQuantityInteger = (Integer) product.get("quantity");
        	if (product.get("price").getClass().getName() == "java.lang.Double") {
        		Double productPrice = (Double) product.get("price") * 100;
        		this.priceValue = productPrice.longValue();
        	} else if (product.get("price").getClass().getName() == "java.lang.Integer") {
        		Integer productPrice = (Integer) product.get("price") * 100;
        		this.priceValue = productPrice.longValue();
        	}
        	Long longValue = productQuantityInteger.longValue();
        	
            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName((String) product.get("nom"))
                    .build();

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("eur")
                    .setProductData(productData)
                    .setUnitAmount(priceValue)
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(longValue)
                    .build();

            lineItems.add(lineItem);
        }
        
        SessionCreateParams.Builder builder = new SessionCreateParams.Builder();
        builder.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);
        builder.addAllLineItem(lineItems);
        builder.setMode(SessionCreateParams.Mode.PAYMENT);
        builder.setSuccessUrl("" + envUrl + "orderPaid/" + hashOrderId(orderId));
        builder.setCancelUrl("" + envUrl + "client/dashboard/");

        SessionCreateParams createParams = builder.build();
        
        Session session = Session.create(createParams);
        
        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        
        return response;
	}
	
	public static String hashOrderId(Integer number) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_=.~";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(25);
        for (int i = 0; i < 50; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        sb.append(number);
        for (int i = 0; i < 50; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
	
	

}
