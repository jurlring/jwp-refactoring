package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = toOrderLineItemResponse(order.getOrderLineItems());
    }

    private List<OrderLineItemResponse> toOrderLineItemResponse(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(orderLineItemResponse -> new OrderLineItem(
                        orderLineItemResponse.getSeq(),
                        orderLineItemResponse.getOrderId(),
                        orderLineItemResponse.getMenuId(),
                        orderLineItemResponse.getMenuName(),
                        orderLineItemResponse.getMenuPrice(),
                        orderLineItemResponse.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
