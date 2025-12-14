package com.ecommerce.Order.services;

import com.ecommerce.Order.dto.OrderItemDTO;
import com.ecommerce.Order.dto.OrderResponse;
import com.ecommerce.Order.models.*;
import com.ecommerce.Order.repository.OrderRepository;
import com.ecommerce.Order.models.OrderItem;
import com.ecommerce.Order.models.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;


    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItem=cartService.getCart(userId);
        if(cartItem.isEmpty()){
            return Optional.empty();
        }

//        Optional<User> userOptional=userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user=userOptional.get();

        BigDecimal totalPrice=cartItem.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order=new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItem=cartItem.stream()
                .map(item->new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItem);
        Order savedOrder=orderRepository.save(order);

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}
