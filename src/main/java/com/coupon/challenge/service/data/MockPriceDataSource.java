package com.coupon.challenge.service.data;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import com.coupon.challenge.interfaces.PriceDataSource;

import java.util.Map;

@Component("mockPriceDataSource")
@Profile("mock") // Este componente se usará solo si el perfil activo es "mock"
public class MockPriceDataSource implements PriceDataSource {

    @Override
    public Map<String, Double> getPrices() {
        return Map.of(
            "MLA1", 100.0,
            "MLA2", 210.0,
            "MLA3", 260.0,
            "MLA4", 80.0,
            "MLA5", 90.0
        );
    }
}