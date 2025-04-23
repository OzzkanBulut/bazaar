package com.ozkan.bazaar.service.impl;

import java.util.HashMap;
import java.util.Map;

public class CategoryMapper {
    private static final Map<String, String> categoryMap = new HashMap<>();

    static {
        // Mapping FakeStoreAPI categories to detailed subcategories
        categoryMap.put("men's clothing", "men_topwear_tshirts");
        categoryMap.put("women's clothing", "women_tshirts");
        categoryMap.put("electronics", "electronics_gaming_laptops");
        categoryMap.put("jewelery", "men_watches");

        // Men's categories
        categoryMap.put("t-shirt", "men_topwear_tshirts");
        categoryMap.put("casual shirt", "men_topwear_casual_shirts");
        categoryMap.put("formal shirt", "men_topwear_formal_shirts");
        categoryMap.put("sweatshirt", "men_topwear_sweatshirts");
        categoryMap.put("sweater", "men_topwear_sweaters");
        categoryMap.put("jacket", "men_topwear_jackets");
        categoryMap.put("blazer", "men_blazers_coats");
        categoryMap.put("suit", "men_suits");
        categoryMap.put("rain jacket", "men_rain_jackets");
        categoryMap.put("jean", "men_jeans");
        categoryMap.put("casual trousers", "men_casual_trousers");
        categoryMap.put("formal trousers", "men_formal_trousers");
        categoryMap.put("shorts", "men_shorts");
        categoryMap.put("track pants", "men_track_pants_joggers");
        categoryMap.put("briefs", "men_briefs_trunks");
        categoryMap.put("boxer", "men_boxers");
        categoryMap.put("vest", "men_vests");
        categoryMap.put("sleepwear", "men_sleepwear_loungewear");
        categoryMap.put("thermal", "men_thermals");
        categoryMap.put("plus size", "men_plus_size");
        categoryMap.put("casual shoes", "men_casual_shoes");
        categoryMap.put("sports shoes", "men_sports_shoes");
        categoryMap.put("formal shoes", "men_formal_shoes");
        categoryMap.put("sneaker", "men_sneakers");
        categoryMap.put("sandals", "men_sandals_floaters");
        categoryMap.put("flip flops", "men_flip_flops");
        categoryMap.put("socks", "men_socks");
        categoryMap.put("sunglasses", "men_sunglasses");
        categoryMap.put("watch", "men_watches");
        categoryMap.put("belt", "men_belts");
        categoryMap.put("wallet", "men_wallets");
        categoryMap.put("hat", "men_caps_hats");
        categoryMap.put("tie", "men_ties");
        categoryMap.put("keychain", "men_keychains");

        // Women's categories
        categoryMap.put("t-shirt", "women_tshirts");
        categoryMap.put("casual shirt", "women_casual_shirts");
        categoryMap.put("formal shirt", "women_formal_shirts");
        categoryMap.put("sweatshirt", "women_sweatshirts");
        categoryMap.put("sweater", "women_sweaters");
        categoryMap.put("dress", "women_dresses");
        categoryMap.put("jacket", "women_jackets_coats");
        categoryMap.put("jean", "women_jeans");
        categoryMap.put("trouser", "women_trousers_capris");
        categoryMap.put("shorts", "women_shorts");
        categoryMap.put("skirt", "women_skirts");
        categoryMap.put("track pants", "women_trackpants_joggers");
        categoryMap.put("leggings", "women_leggings");
        categoryMap.put("casual shoes", "women_casual_shoes");
        categoryMap.put("heels", "women_heels");
        categoryMap.put("sneakers", "women_sneakers");
        categoryMap.put("flip flops", "women_flip_flops");
        categoryMap.put("boots", "women_boots");
        categoryMap.put("sports shoes", "women_sports_shoes");
        categoryMap.put("sports clothing", "women_sports_clothing");

        // Furniture categories
        categoryMap.put("bed", "furniture_bedsheets");
        categoryMap.put("mattress", "furniture_mattress_protectors");
        categoryMap.put("blanket", "furniture_blankets");
        categoryMap.put("pillow", "furniture_pillows_pillow_covers");
        categoryMap.put("carpet", "furniture_carpets");
        categoryMap.put("curtain", "furniture_curtains");
        categoryMap.put("lamp", "furniture_floor_lamps");
        categoryMap.put("clock", "furniture_clocks");
        categoryMap.put("mirror", "furniture_mirrors");

        // Electronics categories
        categoryMap.put("phone", "electronics_mobile_cases");
        categoryMap.put("headphone", "electronics_headphones_headsets");
        categoryMap.put("power bank", "electronics_power_banks");
        categoryMap.put("charger", "electronics_mobile_chargers");
        categoryMap.put("laptop", "electronics_gaming_laptops");
        categoryMap.put("desktop", "electronics_desktop_pcs");
        categoryMap.put("pendrive", "electronics_pendrives");
        categoryMap.put("mouse", "electronics_mouse");
        categoryMap.put("keyboard", "electronics_keyboards");
        categoryMap.put("monitor", "electronics_monitors");
    }

    public static String mapCategory(String fakeCategory, String title) {
        if (categoryMap.containsKey(fakeCategory)) {
            return categoryMap.get(fakeCategory);
        }
        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            if (title.toLowerCase().contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "uncategorized";
    }
}
