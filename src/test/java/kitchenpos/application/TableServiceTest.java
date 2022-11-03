package kitchenpos.application;

import static kitchenpos.Fixture.주문정보;
import static kitchenpos.Fixture.테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTableRequest = new OrderTable(1, true);
        OrderTableResponse actual = tableService.create(orderTableRequest);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 테이블을_모두_조회한다() {
        테이블_생성(true);

        List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 테이블을_비운다() {
        OrderTable orderTable = 테이블_생성(false);

        OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), 테이블(true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블을_비울때_테이블이_존재하지_않을_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 비우기위한 테이블이 존재하지 않습니다.");
    }

    @Test
    void 테이블을_비울때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(false);
        Order request = Order.of(savedTable.getId(), null, List.of(주문정보(메뉴_생성().getId())));
        orderService.create(request);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중인 테이블이 있는 경우 테이블을 비울 수 없습니다.");
    }

    @Test
    void 테이블을_비울때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(false);
        Order savedOrder = 주문_생성(savedTable.getId());
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중인 테이블이 있는 경우 테이블을 비울 수 없습니다.");
    }

    @Test
    void 테이블의_손님수를_변경한다() {
        OrderTable savedTable = 테이블_생성(false);
        테이블집합_생성(savedTable);

        savedTable.changeNumberOfGuests(1);
        OrderTableResponse result = tableService.changeNumberOfGuests(savedTable.getId(), savedTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 테이블의_손님수를_변경할때_수정할_손님수가_0보다_작을때_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                savedTable.getId(), new OrderTable(-1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 음수일 수 없습니다.");
    }

    @Test
    void 테이블의_손님수를_변경할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, 테이블(false)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 손님 수를 변경하기 위한 테이블이 존재하지 않습니다.");
    }

    @Test
    void 테이블의_손님수를_변경할때_이미_비어있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), 테이블(true)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 손님 수를 변경하기 위한 테이블이 비어있습니다.");
    }
}
