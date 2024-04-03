package bg.sofia.uni.fmi.mjt.trading.stock;

import com.sun.tools.javac.Main;

import java.time.LocalDateTime;

public abstract class MainStockPurchase implements StockPurchase{
    protected int quantity;
   protected LocalDateTime purchaseTimestamp;
   protected double purchasePricePerUnit;

    MainStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit){
        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit() {
        return Math.round(purchasePricePerUnit * 100.0) / 100.0;
    }

    @Override
    public double getTotalPurchasePrice() {
        return Math.round((double) quantity * purchasePricePerUnit * 100.0) / 100.0;
    }


}
