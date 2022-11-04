package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.infra.JdbcTemplateOrderDao;
import kitchenpos.order.domain.infra.JdbcTemplateOrderLineItemDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderLineItemDao ordersOrderLineItemDao;

    public OrderRepository(JdbcTemplateOrderDao jdbcTemplateOrderDao,
                           JdbcTemplateOrderLineItemDao ordersOrderLineItemDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.ordersOrderLineItemDao = ordersOrderLineItemDao;
    }

    @Override
    public Order save(Order entity) {
        Order savedOrder = jdbcTemplateOrderDao.save(entity);

        List<OrderLineItem> orderLineItems = saveOrderLineItems(entity, savedOrder);

        savedOrder.changeOrderLineItems(orderLineItems);
        return savedOrder;
    }

    private List<OrderLineItem> saveOrderLineItems(Order entity, Order savedOrder) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItem orderLineItem : entity.getOrderLineItems()) {
            OrderLineItem savedOrderLineItem = ordersOrderLineItemDao.save(
                    new OrderLineItem(
                            savedOrder.getId(),
                            orderLineItem.getMenuId(),
                            orderLineItem.getMenuName(),
                            orderLineItem.getMenuPrice(),
                            orderLineItem.getQuantity()
                    )
            );
            orderLineItems.add(savedOrderLineItem);
        }
        return orderLineItems;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jdbcTemplateOrderDao.findById(id);
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplateOrderDao.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(ordersOrderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Override
    public Order updateStatus(Order order) {
        Order savedOrder = jdbcTemplateOrderDao.save(order);
        order.changeOrderLineItems(ordersOrderLineItemDao.findAllByOrderId(savedOrder.getId()));

        return savedOrder;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}
