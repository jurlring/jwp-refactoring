package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void 테이블이_없는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> new TableGroup(null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    void 테이블의_수가_2개_이하인_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> new TableGroup(List.of(new OrderTable(0, true))))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }
}