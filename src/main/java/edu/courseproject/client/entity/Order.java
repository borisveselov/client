package edu.courseproject.client.entity;

import java.util.Objects;

public class Order {
    private long idOrder;
    private long idSklad;
    private long idCustomer;
    private String orderDate;
    private String deliveryDate;
    private String processing;
    private double cost;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Order() {
    }

    public Order(long idSklad, long idCustomer, String orderDate, String deliveryDate, String processing) {
        this.idSklad = idSklad;
        this.idCustomer = idCustomer;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.processing = processing;
    }

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long idOrder) {
        this.idOrder = idOrder;
    }

    public long getIdSklad() {
        return idSklad;
    }

    public void setIdSklad(long idSklad) {
        this.idSklad = idSklad;
    }

    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return idOrder == order.idOrder &&
                idSklad == order.idSklad &&
                idCustomer == order.idCustomer &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(deliveryDate, order.deliveryDate) &&
                Objects.equals(processing, order.processing);
    }

    @Override
    public int hashCode() {
        final int factor = 31;
        int result = 1;
        result += factor * idOrder;
        result *= factor + idSklad;
        result += factor * idCustomer;
        result += factor + ((processing == null) ? 0 : processing.hashCode());
        result += factor + ((orderDate == null) ? 0 : orderDate.hashCode());
        result += ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
        return result;
    }
}
