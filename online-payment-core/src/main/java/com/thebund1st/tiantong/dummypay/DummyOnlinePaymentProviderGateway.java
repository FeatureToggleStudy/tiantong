package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;

public class DummyOnlinePaymentProviderGateway implements MethodBasedOnlinePaymentProviderGateway {
    @Override
    public List<OnlinePayment.Method> matchedMethods() {
        return singletonList(OnlinePayment.Method.of("DUMMY"));
    }

    @Override
    public ProviderSpecificRequest request(OnlinePayment onlinePayment) {
        DummySpecificRequest dummySpecificRequest = new DummySpecificRequest();
        dummySpecificRequest.setDummyId(UUID.randomUUID().toString());
        return dummySpecificRequest;
    }
}
