package self.aviral.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import self.aviral.kafka.jdbc.StockDBService;
import self.aviral.kafka.jdbc.StockDetails;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaService {
@Autowired
StockService stockService;

@Autowired
    ScheduledStockExecutorService scheduledStockExecutorService;
@Autowired
StockDBService stockDBService;
    // Annotation required to listen the
    // message from kafka server
    @KafkaListener(topics = "stock",
            groupId = "id")
    public void
    publish(String message)
    {
        System.out.println(
                "You have a new message: "
                        + message);
        if(message!=null && message.length() > 0 ) {
            String symbol = message.indexOf(":")>0 ? message.split(":")[0] : "SPY";
            try {
                BigDecimal currentPrice = stockService.getStockPrice(symbol);

                final StockDetails insert = stockDBService.insert(new StockDetails(symbol, message, currentPrice));

                scheduledStockExecutorService.executeTask(
                        symbol,30,insert.getId(),"price_30sec");
                scheduledStockExecutorService.executeTask(
                        symbol,2*60,insert.getId(),"price_2min");
                scheduledStockExecutorService.executeTask(
                        symbol,5*60,insert.getId(),"price_5min");
            } catch (IOException e) {
                //should return -1 ?
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
