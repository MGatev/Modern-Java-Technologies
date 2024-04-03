import bg.sofia.uni.fmi.mjt.trading.Portfolio;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;
import java.time.LocalDateTime;

public class main {
    public static void main(String... args){
        PriceChart p1 = new PriceChart(1.0, 2.0, 3.0);
        Portfolio marti = new Portfolio("Marti", p1, 1000.0, 100);
        marti.buyStock("GOOG", 5);
        marti.buyStock("GOOG",1);
        marti.buyStock("GOOG", 5);
        marti.buyStock("GOOG", 10);
        marti.buyStock("GOOG", 18);
        StockPurchase[] stockPurchases = new StockPurchase[0];

        Portfolio veni = new Portfolio("Veni", p1, stockPurchases, 14.20, 5);
        veni.buyStock("GOOG", 5);
        veni.buyStock("AMZ", 1);
        veni.buyStock("GOOG", 5);


        //System.out.println(marti.getAllPurchases()[0].getStockTicker());
        System.out.println("The remaining budget is: " + veni.getRemainingBudget());
        //System.out.println("Your net worth is " + marti.getNetWorth());
       System.out.println(veni.getAllPurchases(LocalDateTime.MIN, LocalDateTime.MAX)[0].getStockTicker());
    }
}
