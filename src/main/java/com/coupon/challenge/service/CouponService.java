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

    /**
     * Calculates the optimal items that can be purchased with the given amount.
     *
     * @param itemIds List of item IDs to consider for purchase.
     * @param amount Total amount available for purchase.
     * @return A map containing two keys: 'items' with the list of item IDs to purchase and 'total' with the total amount spent.
     */
    public Map<String, Object> calculateOptimalItems(List<String> itemIds, double amount) {
        Map<String, Double> prices = priceDataSource.getPrices();

        List<String> sortedItems = sortItemsByPrice(itemIds, prices);

        List<String> selectedItems = selectItemsWithinAmount(sortedItems, prices, amount);

        return prepareResponse(selectedItems, calculateTotal(selectedItems, prices));
    }

    /**
     * Sorts a list of item IDs by their price in ascending order.
     *
     * @param itemIds List of item IDs to be sorted.
     * @param prices Map containing item IDs as keys and their corresponding prices as values.
     * @return A list of item IDs sorted by price in ascending order.
     */

    private List<String> sortItemsByPrice(List<String> itemIds, Map<String, Double> prices) {
        return itemIds.stream()
                .filter(prices::containsKey)
                .sorted(Comparator.comparingDouble(prices::get))
                .toList();
    }

    /**
     * Selects items from the sorted list that can be purchased without exceeding the given amount.
     *
     * @param sortedItems List of item IDs sorted by price in ascending order.
     * @param prices Map containing item IDs as keys and their corresponding prices as values.
     * @param amount Total amount available for purchase.
     * @return A list of item IDs that can be purchased within the given amount.
     */

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

    /**
     * Calculates the total amount of the selected items.
     *
     * @param selectedItems List of item IDs that were selected for purchase.
     * @param prices Map containing item IDs as keys and their corresponding prices as values.
     * @return The total amount of the selected items.
     */
    private double calculateTotal(List<String> selectedItems, Map<String, Double> prices) {
        return selectedItems.stream()
                .mapToDouble(prices::get)
                .sum();
    }

    /**
     * Prepares a response map containing selected item IDs and their total amount.
     *
     * @param selectedItems List of item IDs that were selected for purchase.
     * @param total The total amount spent on the selected items.
     * @return A map containing the selected item IDs under the key "item_ids" and the total amount under the key "total".
     */

    private Map<String, Object> prepareResponse(List<String> selectedItems, double total) {
        return Map.of(
                "item_ids", selectedItems,
                "total", total
        );
    }

    /**
     * Retrieves the top 5 favorite items based on the number of users who have favorited them.
     *
     * @return A list of maps containing the item IDs under the key "id" and the number of users who have favorited them under the key "quantity", sorted in descending order.
     */
    public List<Map<String, Object>> getTopFavorites() {
        Map<String, Integer> favoritesCount = favoritesDataSource.getFavoritesCount();

        return favoritesCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(this::mapToFavoriteItem)
                .toList();
    }

    /**
     * Maps a {@link Map.Entry} containing an item ID and the number of users who have favorited it
     * to a map containing the item ID under the key "id" and the number of users who have favorited it
     * under the key "quantity".
     *
     * @param entry A map entry containing an item ID as key and the number of users who have favorited it
     *              as value.
     * @return A map containing the item ID under the key "id" and the number of users who have favorited it
     *         under the key "quantity".
     */
    private Map<String, Object> mapToFavoriteItem(Map.Entry<String, Integer> entry) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", entry.getKey());
        item.put("quantity", entry.getValue());
        return item;
    }
}
