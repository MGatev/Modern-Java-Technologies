package bg.sofia.uni.fmi.mjt.trading;


import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Portfolio implements PortfolioAPI {

    public static final int PERCENT_INCREASE = 5;
    String owner;
    double budget;

    PriceChartAPI priceChart;
    int maxSize;

    StockPurchase[] stockPurchases;
    int stockSize;

    private static int getArrCount(StockPurchase[] stockPurchases) {
        int counter = 0;
        while (stockPurchases[counter] != null)
            counter++;

        return counter;
    }

    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;

        if (maxSize != 0){
            stockPurchases = new StockPurchase[maxSize];
            this.stockSize = 0;
        }
        else{
            stockPurchases = new StockPurchase[1];
            this.stockSize = 0;
        }

    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;

        if(stockPurchases.length == 0) {
            this.stockPurchases = new StockPurchase[maxSize];
            this.stockSize = 0;
        }
        else{
            this.stockPurchases = new StockPurchase[maxSize];
            this.stockPurchases = Arrays.copyOf(stockPurchases, stockPurchases.length);
            this.stockSize = stockPurchases.length;
        }


        this.maxSize = maxSize;
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (quantity <= 0)
            return null;

        if (stockTicker == null)
            return null;

       /* int amountPurchases = 0;
        for (int i = 0; i < stockSize; i++) {
            amountPurchases += stockPurchases[i].getQuantity();
        }
        amountPurchases += quantity;*/

        if (stockSize >= maxSize)
            return null;

        double currentPrice = priceChart.getCurrentPrice(stockTicker);

        if ( Math.round(quantity * currentPrice * 100.0) / 100.0 > Math.round(budget * 100.0) / 100.0)
            return null;

        budget -= (quantity * currentPrice);

        switch (stockTicker) {
            case "MSFT" -> {
                stockPurchases[stockSize] = new MicrosoftStockPurchase(quantity, LocalDateTime.now(), currentPrice);
                priceChart.changeStockPrice("MSFT", PERCENT_INCREASE);
                return stockPurchases[stockSize++];
            }
            case "GOOG" -> {
                stockPurchases[stockSize] = new GoogleStockPurchase(quantity, LocalDateTime.now(), currentPrice);
                priceChart.changeStockPrice("GOOG", PERCENT_INCREASE);
                return stockPurchases[stockSize++];
            }
            case "AMZ" -> {
                stockPurchases[stockSize] = new AmazonStockPurchase(quantity, LocalDateTime.now(), currentPrice);
                priceChart.changeStockPrice("AMZ", PERCENT_INCREASE);
                return stockPurchases[stockSize++];
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return stockPurchases;
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        StockPurchase[] tempArr = new StockPurchase[stockSize];
        int newIndex = 0;
        for (int i = 0; i < stockSize; i++) {
            LocalDateTime temp = stockPurchases[i].getPurchaseTimestamp();
            if ((temp.isAfter(startTimestamp) || temp.equals(startTimestamp))
                    && (temp.isBefore(endTimestamp)) || temp.equals(endTimestamp)) {
                tempArr[newIndex++] = stockPurchases[i];
            }
        }

        StockPurchase[] purchasesInTimeStamp = new StockPurchase[newIndex];
        for(int i = 0; i < newIndex; i++){
            purchasesInTimeStamp[i] = tempArr[i];
        }

        return purchasesInTimeStamp;
    }

    @Override
    public double getNetWorth() {
        double netWorth = 0.00;
        for (int i = 0; i < stockSize; i++) {
            double currPrice = priceChart.getCurrentPrice(stockPurchases[i].getStockTicker());
            netWorth += stockPurchases[i].getQuantity() * currPrice;
        }


        return Math.round(netWorth * 100.0) / 100.0;
    }

    @Override
    public double getRemainingBudget() {
        return Math.round(budget * 100.0) / 100.0;
    }

    @Override
    public String getOwner() {
        return owner;
    }
}
