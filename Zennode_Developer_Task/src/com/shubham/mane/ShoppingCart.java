package com.shubham.mane;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ShoppingCart {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> cart = new HashMap<>();
        Map<String, Double> prices = Map.of("Product A", 20.0, "Product B", 40.0, "Product C", 50.0);
        double subtotal = 0;

        for (String product : prices.keySet()) {
            System.out.print("Enter the quantity of " + product + ": ");
            int quantity = scanner.nextInt();
            cart.put(product, quantity);
            scanner.nextLine(); 
            subtotal += prices.get(product) * quantity;

            System.out.print("Is " + product + " wrapped as a gift? (yes/no): ");
            String giftWrapInput = scanner.nextLine().toLowerCase();
            if (giftWrapInput.equals("yes")) {
                subtotal = subtotal + quantity;  
            }
        }

     
        double discount = calculateDiscount(cart, prices, subtotal);

        double shippingFee = calculateShippingFee(cart);

        double total = subtotal - discount + shippingFee;

        System.out.println("\nProduct Details:");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String product = entry.getKey();
            int quantity = entry.getValue();
            double totalAmount = prices.get(product) * quantity;
            System.out.println(product + " - Quantity: " + quantity + ", Total Amount: $" + totalAmount);
        }

        System.out.println("\nSubtotal: $" + subtotal);
        System.out.println("Discount Applied: $" + discount);
        System.out.println("Shipping Fee: $" + shippingFee);
        System.out.println("Total: $" + total);
        
        scanner.close();
	}

    private static double calculateDiscount(Map<String, Integer> cart, Map<String, Double> prices, double subtotal) {
        int totalQuantity = cart.values().stream().mapToInt(Integer::intValue).sum();

        double flat10Discount = totalQuantity > 20 ? 10.0 : 0;

        double bulk5Discount = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            if (entry.getValue() > 10) {
                bulk5Discount += prices.get(entry.getKey()) * entry.getValue() * 0.05;
            }
        }

        double bulk10Discount = totalQuantity > 20 ? subtotal * 0.1 : 0;

        double tiered50Discount = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            if (totalQuantity > 30 && entry.getValue() > 15) {
                int above15Quantity = entry.getValue() - 15;
                tiered50Discount += prices.get(entry.getKey()) * above15Quantity * 0.5;
            }
        }

        double maxDiscount = Math.max(flat10Discount, Math.max(bulk5Discount, Math.max(bulk10Discount, tiered50Discount)));

        return maxDiscount;
    }

    private static double calculateShippingFee(Map<String, Integer> cart) {
        int totalQuantity = cart.values().stream().mapToInt(Integer::intValue).sum();

        int numberOfPackages = (int) Math.ceil((double) totalQuantity / 10);
        return numberOfPackages * 5.0;
    }
}
