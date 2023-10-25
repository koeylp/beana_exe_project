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
