const readlineSync = require('readline-sync');

function main() {
    const cart = {};
    const prices = {"Product A": 20.0, "Product B": 40.0, "Product C": 50.0};
    let subtotal = 0;

    for (const product in prices) {
        const quantity = parseInt(readlineSync.question(`Enter the quantity of ${product}: `), 10);
        cart[product] = quantity;
        subtotal += prices[product] * quantity;

        const giftWrapInput = readlineSync.question(`Is ${product} wrapped as a gift? (yes/no): `).toLowerCase();
        if (giftWrapInput === "yes") {
            subtotal += quantity;
        }
    }

    const discount = calculateDiscount(cart, prices, subtotal);
    const shippingFee = calculateShippingFee(cart);
    const total = subtotal - discount + shippingFee;

    console.log("\nProduct Details:");
    for (const [product, quantity] of Object.entries(cart)) {
        const totalAmount = prices[product] * quantity;
        console.log(`${product} - Quantity: ${quantity}, Total Amount: $${totalAmount}`);
    }

    console.log(`\nSubtotal: $${subtotal}`);
    console.log(`Discount Applied: $${discount}`);
    console.log(`Shipping Fee: $${shippingFee}`);
    console.log(`Total: $${total}`);
}

function calculateDiscount(cart, prices, subtotal) {
    const totalQuantity = Object.values(cart).reduce((sum, quantity) => sum + quantity, 0);

    const flat10Discount = totalQuantity > 20 ? 10.0 : 0;

    let bulk5Discount = 0;
    for (const [product, quantity] of Object.entries(cart)) {
        if (quantity > 10) {
            bulk5Discount += prices[product] * quantity * 0.05;
        }
    }

    const bulk10Discount = totalQuantity > 20 ? subtotal * 0.1 : 0;

    let tiered50Discount = 0;
    for (const [product, quantity] of Object.entries(cart)) {
        if (totalQuantity > 30 && quantity > 15) {
            const above15Quantity = quantity - 15;
            tiered50Discount += prices[product] * above15Quantity * 0.5;
        }
    }

    const maxDiscount = Math.max(flat10Discount, bulk5Discount, bulk10Discount, tiered50Discount);

    return maxDiscount;
}

function calculateShippingFee(cart) {
    const totalQuantity = Object.values(cart).reduce((sum, quantity) => sum + quantity, 0);
    const numberOfPackages = Math.ceil(totalQuantity / 10);

    return numberOfPackages * 5.0;
}

main();
