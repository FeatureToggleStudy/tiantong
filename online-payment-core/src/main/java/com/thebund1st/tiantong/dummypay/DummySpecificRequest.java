package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import lombok.Data;

@Data
public class DummySpecificRequest implements ProviderSpecificRequest {
    private String dummyId;
}
