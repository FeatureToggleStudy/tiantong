package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.MethodMatcher;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentGateway;

public interface MethodBasedOnlinePaymentProviderGateway
        extends ProviderSpecificCreateOnlinePaymentGateway, MethodMatcher {
}
