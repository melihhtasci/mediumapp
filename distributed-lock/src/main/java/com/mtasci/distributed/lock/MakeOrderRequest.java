package com.mtasci.distributed.lock;

import java.io.Serializable;

public class MakeOrderRequest implements Serializable {

    private Long id;
    private int stock;
    private String updatedBy;
    private int delay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "UpdateProductRequest{" +
                "id=" + id +
                ", stock=" + stock +
                ", updatedBy='" + updatedBy + '\'' +
                ", delay=" + delay +
                '}';
    }
}
