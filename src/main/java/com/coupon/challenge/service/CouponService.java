package com.coupon.challenge.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);
    private final MercadoLibreService mercadoLibreService;

    public CouponService(MercadoLibreService mercadoLibreService) {
        this.mercadoLibreService = mercadoLibreService;
    }

    public Mono<Map<String, Object>> calculateOptimalItems(List<String> itemIds, double amount) {
        return mercadoLibreService.getPrices(itemIds) // Obtén los precios como un Mono
            .map(prices -> { // Procesa el mapa de precios
                List<String> sortedItems = itemIds.stream()
                        .filter(prices::containsKey) // Filtra ítems que existen en el mapa
                        .sorted((a, b) -> Double.compare(prices.get(a), prices.get(b))) // Ordena por precio
                        .toList();

                List<String> selectedItems = new ArrayList<>();
                double total = 0.0;
                logger.info("Precios obtenidos: {}", prices);
                // Simulación de precios 
                // TODO: (luego esto se integrará con la API de Mercado Libre)
                /* Map<String, Double> prices = Map.of(
                        "MLA1", 100.0,
                        "MLA2", 210.0,
                        "MLA3", 260.0,
                        "MLA4", 80.0,
                        "MLA5", 90.0
                ); */

                // Ordena ítems por precio ascendente
                /* List<String> sortedItems = new ArrayList<>(itemIds);
                sortedItems.sort(Comparator.comparing(prices::get)); */
                /* List<String> sortedItems = itemIds.stream()
                        .filter(prices::containsKey)
                        .sorted((a, b) -> Double.compare(prices.get(a), prices.get(b)))
                        .toList(); */

                // Selecciona ítems sin exceder el monto del cupón
                for (String item : sortedItems) {
                    double price = prices.get(item);
                    if (total + price <= amount) {
                        selectedItems.add(item);
                        total += price;
                    }
                }

                // Prepara la respuesta
                /* Map<String, Object> response = new HashMap<>();
                response.put("item_ids", selectedItems);
                response.put("total", total);
                return response; */
                return Map.of(
                    "item_ids", selectedItems,
                    "total", total
                );
            });
    }        
}
