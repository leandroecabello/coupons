package com.coupon.challenge.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CouponService {

    public Map<String, Object> calculateOptimalItems(List<String> itemIds, double amount) {
        // Simulación de precios 
        // TODO: (luego esto se integrará con la API de Mercado Libre)
        Map<String, Double> prices = Map.of(
                "MLA1", 100.0,
                "MLA2", 210.0,
                "MLA3", 260.0,
                "MLA4", 80.0,
                "MLA5", 90.0
        );

        // Ordena ítems por precio ascendente
        List<String> sortedItems = itemIds.stream()
                .filter(prices::containsKey)
                .sorted((a, b) -> Double.compare(prices.get(a), prices.get(b)))
                .toList();

        // Selecciona ítems sin exceder el monto del cupón
        List<String> selectedItems = new ArrayList<>();
        double total = 0.0;

        // Selecciona ítems sin exceder el monto del cupón
        for (String item : sortedItems) {
            double price = prices.get(item);
            if (total + price <= amount) {
                selectedItems.add(item);
                total += price;
            }
        }

        // Prepara la respuesta
        return Map.of(
            "item_ids", selectedItems,
            "total", total
        );
            
    }        
}
