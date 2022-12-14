package order.management.query;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Entity
@Table(name = "Order_table")
@Data
@Relation(collectionRelation = "orders")
public class OrderReadModel {

    @Id
    private Long id;

    private String productId;

    private String status;
}
