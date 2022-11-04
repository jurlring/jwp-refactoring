package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class Fixture {

    public static Product 상품의_가격은(BigDecimal price) {
        return Product.of("맥도날드 페페로니 피자 버거", price);
    }

    public static MenuGroup 메뉴집합() {
        return new MenuGroup("햄버거");
    }

    public static MenuProduct 메뉴의_상품은(Product product) {
        return MenuProduct.of(product.getId(), 1, product.getPrice());
    }

    public static Menu 메뉴(MenuGroup menuGroup, MenuProduct... menuProducts) {
        return Menu.of("햄버억", new BigDecimal(20_000), menuGroup.getId(), List.of(menuProducts));
    }

    public static OrderTable 테이블(Long tableGroupId, boolean empty) {
        return new OrderTable(1L, tableGroupId, 1, empty);
    }

    public static OrderTable 테이블(boolean empty) {
        return OrderTable.of(1, empty);
    }

    public static OrderLineItem 주문정보(Long menuId) {
        return new OrderLineItem(1L, menuId, "햄버거", new BigDecimal(100_000), 1);
    }

    public static Order 주문(Long tableId, Long menuId) {
        return Order.of(tableId, OrderStatus.COOKING, List.of(주문정보(menuId)));
    }

    public static TableGroup 테이블집합(OrderTable... orderTables) {
        return TableGroup.of(List.of(orderTables));
    }
}
