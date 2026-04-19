package com.example.demo.service;

import com.example.demo.model.InventoryVehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Builds a different vehicle lineup per Google {@code place_id} (stable RNG seed).
 * For production, turn off {@code app.inventory.demo-seed} and load real rows only.
 */
public final class DemoInventorySeeder {

    private record Template(String make, String model, int basePrice, String imageUrl) {
    }

    private static final Template[] POOL = {
            new Template("Honda", "Civic", 19500,
                    "https://images.unsplash.com/photo-1563720223185-11003d516935?w=800"),
            new Template("Toyota", "Camry", 24800,
                    "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800"),
            new Template("Ford", "Escape", 22900,
                    "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800"),
            new Template("Chevrolet", "Equinox", 21400,
                    "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800"),
            new Template("Subaru", "Outback", 31900,
                    "https://images.unsplash.com/photo-1619405399517-d7fce0f13302?w=800"),
            new Template("Hyundai", "Tucson", 26900,
                    "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800"),
            new Template("Mazda", "CX-5", 28500,
                    "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?w=800"),
            new Template("Nissan", "Rogue", 23800,
                    "https://images.unsplash.com/photo-1609521263047-f8f205293f24?w=800"),
            new Template("Volkswagen", "Jetta", 21200,
                    "https://images.unsplash.com/photo-1619682817481-e5faedeb234d?w=800"),
            new Template("Kia", "Sportage", 25500,
                    "https://images.unsplash.com/photo-1606611013016-ce4d8b44927f?w=800"),
            new Template("Toyota", "RAV4", 29900,
                    "https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800"),
            new Template("Honda", "CR-V", 31200,
                    "https://images.unsplash.com/photo-1511910849309-0dffbabc57e2?w=800"),
            new Template("Ford", "F-150", 38900,
                    "https://images.unsplash.com/photo-1559416523-140ddc3d238c?w=800"),
            new Template("Jeep", "Grand Cherokee", 35900,
                    "https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800"),
            new Template("BMW", "3 Series", 36900,
                    "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800"),
            new Template("Mercedes-Benz", "C-Class", 38900,
                    "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800"),
            new Template("Audi", "A4", 37900,
                    "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800"),
            new Template("Lexus", "RX", 42900,
                    "https://images.unsplash.com/photo-1580273916550-e323be2ae537?w=800"),
            new Template("Acura", "TLX", 34900,
                    "https://images.unsplash.com/photo-1609521263047-f8f205293f24?w=800"),
            new Template("GMC", "Sierra", 42900,
                    "https://images.unsplash.com/photo-1559416523-140ddc3d238c?w=800"),
    };

    private DemoInventorySeeder() {
    }

    public static List<InventoryVehicle> synthesizeForPlaceId(String placeId) {
        long seed = placeId.hashCode();
        Random r = new Random(seed);

        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < POOL.length; i++) {
            idx.add(i);
        }
        Collections.shuffle(idx, r);

        int count = 2 + r.nextInt(3);
        List<InventoryVehicle> out = new ArrayList<>();

        for (int j = 0; j < count; j++) {
            Template t = POOL[idx.get(j)];
            int year = 2018 + r.nextInt(7);
            boolean isNew = year >= 2024 && r.nextDouble() < 0.35;
            int mileage = isNew ? r.nextInt(3500) : 12000 + r.nextInt(62000);
            int priceAdj = (r.nextInt(9001) - 2000);
            BigDecimal price = BigDecimal.valueOf(Math.max(7999, t.basePrice + priceAdj));

            InventoryVehicle v = new InventoryVehicle();
            v.setDealerPlaceId(placeId);
            v.setYear(year);
            v.setMake(t.make);
            v.setModel(t.model);
            v.setPrice(price);
            v.setMileage(mileage);
            v.setConditionType(isNew ? "new" : "used");
            v.setImageUrl(t.imageUrl);
            v.setVin(syntheticVin(r));

            out.add(v);
        }

        return out;
    }

    private static String syntheticVin(Random r) {
        String chars = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(17);
        for (int i = 0; i < 17; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
