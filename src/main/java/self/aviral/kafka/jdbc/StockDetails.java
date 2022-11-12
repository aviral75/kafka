package self.aviral.kafka.jdbc;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockDetails {


    int id;
    String symbol;
    String news;
    long currentTime;
    BigDecimal priceNow;
    BigDecimal price30Sec;
    BigDecimal price2min;
    BigDecimal price5min;
    BigDecimal priceClose;

    public StockDetails(){
    }
    public StockDetails(String symbol,String news,BigDecimal priceNow){
        this.symbol=symbol;
        this.news=news;
        this.priceNow=priceNow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public BigDecimal getPriceNow() {
        return priceNow;
    }

    public void setPriceNow(BigDecimal priceNow) {
        this.priceNow = priceNow;
    }

    public BigDecimal getPrice30Sec() {
        return price30Sec;
    }

    public void setPrice30Sec(BigDecimal price30Sec) {
        this.price30Sec = price30Sec;
    }

    public BigDecimal getPrice2min() {
        return price2min;
    }

    public void setPrice2min(BigDecimal price2min) {
        this.price2min = price2min;
    }

    public BigDecimal getPrice5min() {
        return price5min;
    }

    public void setPrice5min(BigDecimal price5min) {
        this.price5min = price5min;
    }

    public BigDecimal getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(BigDecimal priceClose) {
        this.priceClose = priceClose;
    }


}