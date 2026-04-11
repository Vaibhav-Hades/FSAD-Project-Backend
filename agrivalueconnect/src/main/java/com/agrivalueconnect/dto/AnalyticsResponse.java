package com.agrivalueconnect.dto;

public class AnalyticsResponse {

    private long totalUsers;
    private long totalFarmers;
    private long totalBuyers;
    private long totalProducts;
    private long totalOrders;
    private long totalReviews;
    private long totalInquiries;
    private long pendingOrders;
    private long deliveredOrders;

    public AnalyticsResponse() {}

    public AnalyticsResponse(long totalUsers, long totalFarmers, long totalBuyers,
                              long totalProducts, long totalOrders, long totalReviews,
                              long totalInquiries, long pendingOrders, long deliveredOrders) {
        this.totalUsers = totalUsers; this.totalFarmers = totalFarmers;
        this.totalBuyers = totalBuyers; this.totalProducts = totalProducts;
        this.totalOrders = totalOrders; this.totalReviews = totalReviews;
        this.totalInquiries = totalInquiries; this.pendingOrders = pendingOrders;
        this.deliveredOrders = deliveredOrders;
    }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalFarmers() { return totalFarmers; }
    public void setTotalFarmers(long totalFarmers) { this.totalFarmers = totalFarmers; }
    public long getTotalBuyers() { return totalBuyers; }
    public void setTotalBuyers(long totalBuyers) { this.totalBuyers = totalBuyers; }
    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(long totalReviews) { this.totalReviews = totalReviews; }
    public long getTotalInquiries() { return totalInquiries; }
    public void setTotalInquiries(long totalInquiries) { this.totalInquiries = totalInquiries; }
    public long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }
    public long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(long deliveredOrders) { this.deliveredOrders = deliveredOrders; }
}
