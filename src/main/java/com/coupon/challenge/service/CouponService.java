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
    
    public List<Map<String, Object>> getTopFavorites() {
        // Simulación de datos en memoria
        Map<String, Integer> favoritesCount = Map.of(
            "MLA1", 15,
            "MLA2", 10,
            "MLA3", 8,
            "MLA4", 5,
            "MLA5", 3,
            "MLA6", 2
        );

        // Ordena los ítems por cantidad (descendente) y toma los primeros 5
        return favoritesCount.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(5)
            .map(entry -> {
                // Construye el mapa con los tipos correctos
                Map<String, Object> item = new HashMap<>();
                item.put("id", entry.getKey());
                item.put("quantity", entry.getValue());
                return item;
            })
            .toList();
    }
}
