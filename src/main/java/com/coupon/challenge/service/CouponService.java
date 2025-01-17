package com.coupon.challenge.service;

import com.coupon.challenge.interfaces.FavoritesDataSource;
import com.coupon.challenge.interfaces.PriceDataSource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final FavoritesDataSource favoritesDataSource;
    private final PriceDataSource priceDataSource;

    public CouponService(
        @Qualifier("mockFavoritesDataSource") FavoritesDataSource favoritesDataSource, 
        @Qualifier("mockPriceDataSource") PriceDataSource priceDataSource
    ) {
        this.favoritesDataSource = favoritesDataSource;
        this.priceDataSource = priceDataSource;
    }

    public Map<String, Object> calculateOptimalItems(List<String> itemIds, double amount) {
        Map<String, Double> prices = priceDataSource.getPrices();

        List<String> sortedItems = sortItemsByPrice(itemIds, prices);

        List<String> selectedItems = selectItemsWithinAmount(sortedItems, prices, amount);

        return prepareResponse(selectedItems, calculateTotal(selectedItems, prices));
    }

    private List<String> sortItemsByPrice(List<String> itemIds, Map<String, Double> prices) {
        return itemIds.stream()
                .filter(prices::containsKey)
                .sorted(Comparator.comparingDouble(prices::get))
                .toList();
    }

    private List<String> selectItemsWithinAmount(List<String> sortedItems, Map<String, Double> prices, double amount) {
        List<String> selectedItems = new ArrayList<>();
        double total = 0.0;

        for (String item : sortedItems) {
            double price = prices.get(item);
            if (total + price <= amount) {
                selectedItems.add(item);
                total += price;
            }
        }

        return selectedItems;
    }

    private double calculateTotal(List<String> selectedItems, Map<String, Double> prices) {
        return selectedItems.stream()
                .mapToDouble(prices::get)
                .sum();
    }

    private Map<String, Object> prepareResponse(List<String> selectedItems, double total) {
        return Map.of(
                "item_ids", selectedItems,
                "total", total
        );
    }

    public List<Map<String, Object>> getTopFavorites() {
        Map<String, Integer> favoritesCount = favoritesDataSource.getFavoritesCount();

        return favoritesCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(this::mapToFavoriteItem)
                .toList();
    }

    private Map<String, Object> mapToFavoriteItem(Map.Entry<String, Integer> entry) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", entry.getKey());
        item.put("quantity", entry.getValue());
        return item;
    }
}
