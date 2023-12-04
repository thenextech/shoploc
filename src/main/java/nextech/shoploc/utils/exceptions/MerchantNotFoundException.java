package nextech.shoploc.utils.exceptions;

public class MerchantNotFoundException extends RuntimeException {

    public MerchantNotFoundException() {
        super();
    }

    public MerchantNotFoundException(final String message) {
        super(message);
    }

}
