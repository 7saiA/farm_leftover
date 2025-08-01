package ashteam.farm_leftover.order.model;


public enum OrderStatus {
    CREATED,
    READY_FOR_PICKUP,
    CONFIRMED_BY_USER,
    CONFIRMED_BY_FARM,
    COMPLETED,
    CANCELLED_BY_USER,
    CANCELLED_BY_FARM,
    REFUNDED
}
