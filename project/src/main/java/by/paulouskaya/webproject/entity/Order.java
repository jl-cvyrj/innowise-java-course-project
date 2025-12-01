package by.paulouskaya.webproject.entity;

public class Order extends BeautySalonEntity {
    private int clientId;
    private int serviceId;
    private String date;
    private double totalAmount;
    private String status;

    public Order(int id, int clientId, int serviceId, String date, double totalAmount, String status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

