package order.management.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderPlacedEvent {

    private Long id;
    private String productId;
    private String status;
}
