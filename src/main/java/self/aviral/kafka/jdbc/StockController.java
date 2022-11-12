package self.aviral.kafka.jdbc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockController {

    private final StockDBService stockDBService;

    public StockController(StockDBService stockDBService) {
        this.stockDBService = stockDBService;
    }
    @GetMapping("/stocks/{symbol}")
    List<StockDetails> getStock(@PathVariable String symbol) {
        return stockDBService.queryBySymbol(symbol,false);
    }

    @GetMapping("/stocks")
    List<StockDetails> getStocks() {
        return stockDBService.queryFromDatabase(50);
    }
}