package order.management.command;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Data
public class OrderCommand {

    private String productId;
    private String status;
    private String id; // Please comment here if you want user to enter the id directly
}
