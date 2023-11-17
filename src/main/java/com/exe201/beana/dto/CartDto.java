package com.exe201.beana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDto {
    private List<CartItemDto> items;

    public void addItem(ItemDto item, int quantity) {
        if (items == null)
            items = new ArrayList<>();
        items.add(new CartItemDto(item, quantity));
    }

    public void removeItem(ItemDto item) {
        items.removeIf(cartItem -> Objects.equals(cartItem.getItem().getId(), item.getId()));
    }

    public void updateQuantity(ItemDto item, String type) {
        CartItemDto cartItem = findCartItem(item);

        if (cartItem != null) {
            int currentQuantity = cartItem.getQuantity();

            if ("increase".equalsIgnoreCase(type)) {
                cartItem.setQuantity(currentQuantity + 1);
            } else if ("decrease".equalsIgnoreCase(type)) {
                if (currentQuantity > 1) {
                    cartItem.setQuantity(currentQuantity - 1);
                } else {
                    // Optionally, you can remove the item if the quantity is 1 or less
                    removeItem(item);
                }
            }
        }
    }

    private CartItemDto findCartItem(ItemDto item) {
        return items.stream()
                .filter(cartItem -> Objects.equals(cartItem.getItem().getId(), item.getId()))
                .findFirst()
                .orElse(null);
    }

    public void clearCart() {
        items.clear();
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (CartItemDto cartItem : items) {
            total += cartItem.getQuantity() * cartItem.getItem().getPrice();
        }
        return total;
    }


}
