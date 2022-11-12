package self.aviral.kafka;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.aviral.kafka.jdbc.StockDBService;
import self.aviral.kafka.jdbc.StockDetails;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.delayedExecutor;

@Service
public class ScheduledStockExecutorService {
    @Autowired
    StockService stockService;
    @Autowired
    StockDBService stockDBService;

  public void  executeTask(String symbol,int delayInSeconds,int id,String fieldName) throws ExecutionException, InterruptedException, TimeoutException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        System.out.println("Task scheduled to execute after "+delayInSeconds + " seconds at : " + LocalDateTime.now().toString());


      Executor scheduledExecutor = delayedExecutor(delayInSeconds, TimeUnit.SECONDS);
      CompletableFuture<BigDecimal> future
              = CompletableFuture.supplyAsync(() -> {
          try {
              return stockService.getStockPrice(symbol);
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
      }, scheduledExecutor);

      future.whenComplete((p,e)->{
          if(e!=null){
              e.printStackTrace();
          }
          stockDBService.update(id,fieldName,p);

        });


        System.out.println("Shutdown and await requested at : " + LocalDateTime.now().toString());
        shutdownAndAwaitTermination(executor);
    }

    static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
