package ashteam.farm_leftover.order.model;

import ashteam.farm_leftover.user.model.UserAccount;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "orderId")
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserAccount user;

    @ManyToOne
    @JoinColumn(name = "farm_id", nullable = false)
    UserAccount farm;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    String cancellationReason;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    BigDecimal totalPrice;

    LocalDateTime readyForPickupTime;
    LocalDateTime userConfirmedTime;
    LocalDateTime farmConfirmedTime;
    LocalDateTime cancelledTime;

    public void calculateTotalPrice(){
        this.totalPrice = this.items
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
