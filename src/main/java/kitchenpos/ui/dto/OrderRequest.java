package kitchenpos.ui.dto;

import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItem> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
