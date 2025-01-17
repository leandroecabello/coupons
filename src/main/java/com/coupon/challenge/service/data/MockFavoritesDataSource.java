package com.coupon.challenge.service.data;

import org.springframework.stereotype.Component;
import com.coupon.challenge.interfaces.FavoritesDataSource;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Component("mockFavoritesDataSource")
@Profile("mock") // Este componente se usará solo si el perfil activo es "mock"
public class MockFavoritesDataSource implements FavoritesDataSource {

    @Override
    public Map<String, Integer> getFavoritesCount() {
        return Map.of(
            "MLA1", 15,
            "MLA2", 10,
            "MLA3", 8,
            "MLA4", 5,
            "MLA5", 3,
            "MLA6", 2
        );
    }
}