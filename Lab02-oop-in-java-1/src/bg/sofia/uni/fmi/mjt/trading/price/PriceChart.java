package bg.sofia.uni.fmi.mjt.trading.price;


public class PriceChart implements PriceChartAPI{
   private double microsoftStockPrice;
    private double googleStockPrice;
    private double amazonStockPrice;
   public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice){
        this.microsoftStockPrice = microsoftStockPrice;
        this.googleStockPrice = googleStockPrice;
        this.amazonStockPrice = amazonStockPrice;
    }

    @Override
    public double getCurrentPrice(String stockTicker) {
       if(stockTicker == null)
           return 0.00;

        switch(stockTicker){
            case "MSFT" -> {
                return Math.round(microsoftStockPrice * 100.0) / 100.0;
            }
            case "AMZ" -> {
                return Math.round(amazonStockPrice * 100.0) / 100.0;
            }
            case "GOOG" -> {
                return Math.round(googleStockPrice * 100.0) / 100.0;
            }
            default -> {
                return 0.00;
            }
        }
    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange) {
        if(percentChange <= 0 || stockTicker == null)
            return false;

        switch(stockTicker){
            case "MSFT" -> {
                microsoftStockPrice += (microsoftStockPrice * (double) percentChange / 100.0);
                return true;
            }
            case "AMZ" -> {
                amazonStockPrice += (amazonStockPrice * (double) percentChange / 100.0);
                return true;
            }
            case "GOOG" -> {
                googleStockPrice += (googleStockPrice * (double) percentChange / 100.0);
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
