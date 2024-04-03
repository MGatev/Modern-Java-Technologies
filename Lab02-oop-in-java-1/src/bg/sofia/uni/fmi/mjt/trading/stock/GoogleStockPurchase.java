package bg.sofia.uni.fmi.mjt.trading.stock;

import com.sun.tools.javac.Main;

import java.time.LocalDateTime;

public class GoogleStockPurchase extends MainStockPurchase {

    public GoogleStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit){
        super(quantity, purchaseTimestamp, purchasePricePerUnit);
    }

    @Override
    public String getStockTicker() {
        return "GOOG";
    }
}
