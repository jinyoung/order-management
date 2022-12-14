package order.management.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import order.management.aggregate.*;
import order.management.event.*;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("order")
public class OrderCQRSHandlerReusingAggregate {

    @Autowired
    private OrderReadModelRepository repository;

    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    @QueryHandler
    public List<OrderReadModel> handle(OrderQuery query) {
        return repository.findAll();
    }

    @QueryHandler
    public Optional<OrderReadModel> handle(OrderSingleQuery query) {
        return repository.findById(query.getId());
    }

    @EventHandler
    public void whenOrderPlaced_then_CREATE(OrderPlacedEvent event)
        throws Exception {
        OrderReadModel entity = new OrderReadModel();
        OrderAggregate aggregate = new OrderAggregate();
        aggregate.on(event);

        BeanUtils.copyProperties(aggregate, entity);

        repository.save(entity);

        queryUpdateEmitter.emit(OrderQuery.class, query -> true, entity);
    }

    @EventHandler
    public void whenOrderCancelled_then_UPDATE(OrderCancelledEvent event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                OrderAggregate aggregate = new OrderAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);

                queryUpdateEmitter.emit(
                    OrderSingleQuery.class,
                    query -> query.getId().equals(event.getId()),
                    entity
                );
            });
    }

    @EventHandler
    public void whenOrderApproved_then_UPDATE(OrderApprovedEvent event)
        throws Exception {
        repository
            .findById(event.getId())
            .ifPresent(entity -> {
                OrderAggregate aggregate = new OrderAggregate();

                BeanUtils.copyProperties(entity, aggregate);
                aggregate.on(event);
                BeanUtils.copyProperties(aggregate, entity);

                repository.save(entity);

                queryUpdateEmitter.emit(
                    OrderSingleQuery.class,
                    query -> query.getId().equals(event.getId()),
                    entity
                );
            });
    }
}
