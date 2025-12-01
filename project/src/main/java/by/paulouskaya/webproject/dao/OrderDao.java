package by.paulouskaya.webproject.dao;

import java.util.Objects;

// DAO для заказаў
public class OrderDao extends AbstractDao {
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

    @Override
    public void insert(Object entity) {
    }

    @Override
    public void update(Object entity) {
    }

    @Override
    public void delete(Object entity) {
    }

    @Override
    public Object findById(int id) {
        return null;
    }
}


