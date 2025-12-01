package by.paulouskaya.webproject.dao;

import by.paulouskaya.webproject.entity.Order;

import java.util.List;
import java.util.Objects;

public class OrderDao extends AbstractDao<Integer, Order> {
    private int orderNumber;
    private int userId;
    private double totalAmount;
    private String status;

    public int getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public OrderDao(String id) {
        super(id);
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public Order findEntityById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(Order entity) {
        return false;
    }

    @Override
    public boolean create(Order entity) {
        return false;
    }

    @Override
    public Order update(Order entity) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDao orderDao = (OrderDao) o;
        return orderNumber == orderDao.orderNumber && userId == orderDao.userId && Double.compare(totalAmount, orderDao.totalAmount) == 0 && Objects.equals(status, orderDao.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, userId, totalAmount, status);
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer("OrderDao{");
        stringBuffer.append("orderNumber=").append(orderNumber);
        stringBuffer.append(", userId=").append(userId);
        stringBuffer.append(", totalAmount=").append(totalAmount);
        stringBuffer.append(", status='").append(status).append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}


