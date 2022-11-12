package self.aviral.kafka.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class StockDBService {

    private final JdbcTemplate jdbcTemplate;


    public StockDBService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public StockDetails insert(StockDetails stockDetails) {
        String sql = "INSERT INTO stock" +
                "(symbol,news,price_now)  " +
                "values(\""+stockDetails.getSymbol()+"\",\""+stockDetails.getNews()+"\","+ stockDetails.getPriceNow()+");";
        int rowsInserted = jdbcTemplate.update(sql);
        System.out.println("Number of rows updated = " + rowsInserted);
        // has to be one only limit 1 in query is set
        return queryBySymbol(stockDetails.getSymbol(),true).get(0);

    }

//    /*Inserting multiple records in single statements*/
//    public void insertMultiple() {
//        int rowsInserted = jdbcTemplate.update("insert into user_entity (id,first_name,last_name) values (5, 'Marge','Simpson'),(6, 'Maggie','Simpson')");
//        System.out.println("Number of rows updated = " + rowsInserted);
//    }

    /*Demonstrating prepared statements*/
//    public void insertUsingPreparedStatement(Integer id, String firstName, String lastName) {
//        int rowsInserted = jdbcTemplate.update("insert into user_entity (id,first_name,last_name) values (?,?,?)", ps -> {
//            ps.setInt(1, id);
//            ps.setString(2, firstName);
//            ps.setString(3, lastName);
//        });
//        System.out.println("Number of rows updated = " + rowsInserted);
//    }

    /*To execute multiple insert or update statements in a single Go*/
//    public void batchInsert() {
//        jdbcTemplate.batchUpdate("insert into user_entity (id,first_name,last_name) values (5, 'Marge','Simpson')", "insert into user_entity (id,first_name,last_name) values (6, 'Maggie','Simpson')");
//    }

    /*To batch execute same statement with multiple parameter sets*/
//    public void batchInsertWithPreparedStatements() {
//        List<Object[]> arr = Arrays.asList(
//                new Object[]{5, "Marge", "Simpson"},
//                new Object[]{6, "Maggie", "Simpson"},
//                new Object[]{7, "Montgomery", "Burns "}
//        );
//        jdbcTemplate.batchUpdate("insert into user_entity (id,first_name,last_name) values (?,?,?)",arr);
//    }

    /*Safe way to insert objects into inserts or updates in batch of specified size*/
    public void batchInsertWithDto(List<StockDetails> stocks) {
        // executing 10 inserts at a time
        jdbcTemplate.batchUpdate("insert into stock (symbol,news,price_now,price_30sec,price_2min,price_5min,price_close) " +
                "values (?,?,?,?,?,?,?)", stocks, 10, (ps, row) -> {
            ps.setString(1, row.getSymbol());
            ps.setString(2, row.getNews());
            ps.setBigDecimal(3, row.getPriceNow());
            ps.setBigDecimal(4, row.getPrice30Sec());
            ps.setBigDecimal(5, row.getPrice2min());
            ps.setBigDecimal(6, row.getPrice5min());
            ps.setBigDecimal(7, row.getPriceClose());
        });
    }


    public List<StockDetails> queryBySymbol(String symbol,boolean latest) {

        String query = "select * from stock where symbol=\""+symbol+"\" order by create_time desc";
        if(latest)
            query+="  limit 1";
        List<StockDetails> stocks = jdbcTemplate.query(query, (resultSet, i) -> getStockDetails(resultSet));
        return stocks;
    }

    private static StockDetails getStockDetails(ResultSet resultSet) throws SQLException {
        StockDetails stockDetails = new StockDetails();
        stockDetails.setId(resultSet.getInt("id"));
        stockDetails.setSymbol(resultSet.getString("symbol"));
        stockDetails.setNews(resultSet.getString("news"));
        stockDetails.setPriceNow(resultSet.getBigDecimal("price_now"));
        stockDetails.setPrice30Sec(resultSet.getBigDecimal("price_30sec"));
        stockDetails.setPrice2min(resultSet.getBigDecimal("price_2min"));
        stockDetails.setPrice5min(resultSet.getBigDecimal("price_5min"));
        stockDetails.setPriceClose(resultSet.getBigDecimal("price_close"));
        return stockDetails;
    }

    public List<StockDetails> queryFromDatabase(int idLessThan) {

        List<StockDetails> stockDetails = jdbcTemplate.query("select * from stock where id<? ",
                preparedStatement -> preparedStatement.setInt(1, idLessThan), (resultSet, i) -> getStockDetails(resultSet));
        Collections.sort(stockDetails,(s1,s2) -> findMaxPercentChange(s1)-findMaxPercentChange(s2) > 0.0 ? -1 :1);

    return stockDetails;
    }

    public void delete() {
        int rowsDeleted = jdbcTemplate.update("delete from stock where id<10");
        System.out.println("Number of rows deleted = " + rowsDeleted);

    }

    public void update(int id, String fieldName, BigDecimal price) {
        String sql = "update stock set " + fieldName + " = " + price +" where id = " + id;
        int rowsUpdated = jdbcTemplate.update(sql);
    }
   static double  findMaxPercentChange(StockDetails s1){
        double s1max = 0.0;
        double priceNow = s1.getPriceNow().doubleValue();
        BigDecimal price30sec = s1.getPrice30Sec();
        if(price30sec != null) {
            s1max = Math.max(s1max,(price30sec.doubleValue()-priceNow)*100/priceNow);
        }
       BigDecimal price2min = s1.getPrice2min();
        if(price2min != null) {
            s1max = Math.max(s1max,(price2min.doubleValue()-priceNow)*100/priceNow);
        }
       BigDecimal price5min = s1.getPrice5min();
        if(price5min != null) {
            s1max = Math.max(s1max,(price5min.doubleValue()-priceNow)*100/priceNow);
        }
        return s1max;
    }

}
