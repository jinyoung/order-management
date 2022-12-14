package order.management.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderCancelledEvent {

    private String id;
    private String productId;
    private String status;
}
