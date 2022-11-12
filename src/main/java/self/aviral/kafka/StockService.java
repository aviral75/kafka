package self.aviral.kafka;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Service
public class StockService {

    BigDecimal getStockPrice(String symbol) throws IOException {
        Stock stock = YahooFinance.get(symbol);

        BigDecimal price = stock.getQuote().getPrice();
        BigDecimal change = stock.getQuote().getChangeInPercent();
        BigDecimal peg = stock.getStats().getPeg();
        BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
        stock.print();
        return price;
    }



    public static  <E extends Comparable<E>> Optional<E> findMax(Collection<E> collection){
        if(collection == null || collection.size() == 0){
            return Optional.empty();
        }
        E max  = null;
        for(E e: collection){
            max = e.compareTo(max) > 0 ? e : max ;
        }
        return Optional.ofNullable(max);

    }
}
