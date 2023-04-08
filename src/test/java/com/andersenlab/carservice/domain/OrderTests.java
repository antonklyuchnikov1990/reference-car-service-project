package com.andersenlab.carservice.domain;

import com.andersenlab.carservice.extension.CarServiceExtension;
import com.andersenlab.carservice.extension.ManualClock;
import com.andersenlab.carservice.extension.PredictableUUIDExtension;
import com.andersenlab.carservice.port.usecase.ListOrdersUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({CarServiceExtension.class, PredictableUUIDExtension.class})
class OrderTests {

    @Test
    void givenNoOrders_whenCreateOrder_thenItShouldBeListed(
            CarServiceModule module,
            UUID orderId1,
            ManualClock clock
    ) {
        module.createOrderUseCase().create(orderId1, 100);

        assertThat(module.listOrdersUseCase().list(ListOrdersUseCase.Sort.ID))
                .containsExactly(orderView(orderId1, 100, clock.instant()));
    }

    @Test
    void givenSomeOrders_whenListById_thenTheyShouldBeSortedById(
            CarServiceModule module,
            UUID orderId1,
            UUID orderId2,
            ManualClock clock
    ) {
        module.createOrderUseCase().create(orderId1, 100);
        module.createOrderUseCase().create(orderId2, 100);

        assertThat(module.listOrdersUseCase().list(ListOrdersUseCase.Sort.ID))
                .containsExactly(
                        orderView(orderId1, 100, clock.instant()),
                        orderView(orderId2, 100, clock.instant())
                );
    }

    @Test
    void givenSomeOrders_whenListByPrice_thenTheyShouldBeSortedByPrice(
            CarServiceModule module,
            UUID orderId1,
            UUID orderId2,
            ManualClock clock
    ) {
        module.createOrderUseCase().create(orderId1, 200);
        module.createOrderUseCase().create(orderId2, 100);

        assertThat(module.listOrdersUseCase().list(ListOrdersUseCase.Sort.PRICE))
                .containsExactly(
                        orderView(orderId2, 100, clock.instant()),
                        orderView(orderId1, 200, clock.instant())
                );
    }

    @Test
    void givenSomeOrders_whenListByCreationTimestamp_thenTheyShouldBeSortedByCreationTimestamp(
            CarServiceModule module,
            UUID orderId1,
            UUID orderId2,
            ManualClock clock
    ) {
        var timestamp = clock.instant();
        module.createOrderUseCase().create(orderId2, 100);
        clock.waitOneHour();
        module.createOrderUseCase().create(orderId1, 100);

        assertThat(module.listOrdersUseCase().list(ListOrdersUseCase.Sort.CREATION_TIMESTAMP))
                .containsExactly(
                        orderView(orderId2, 100, timestamp),
                        orderView(orderId1, 100, timestamp.plus(1, ChronoUnit.HOURS))
                );
    }

    private ListOrdersUseCase.OrderView orderView(UUID id, int price, Instant timestamp) {
        return new ListOrdersUseCase.OrderView(
                id,
                price,
                ListOrdersUseCase.OrderStatus.IN_PROCESS,
                timestamp,
                Optional.empty()
        );
    }
}