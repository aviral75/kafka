package self.aviral.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@SpringBootApplication
//@EnableAsync
public class KafkaApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(KafkaApplication.class, args);

	}

	@Bean
	JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.setQueryTimeout(20); //20 seconds
		jdbcTemplate.setFetchSize(10);  //fetch 10 rows at a time
		return jdbcTemplate;
	}
//	@Bean("scheduledAsync")
//	public TaskExecutor getScheduledExecutor() {
//		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor();
//		executor.setCorePoolSize(10);
//		executor.setMaximumPoolSize(100);
//		executor.setThreadFactory(r -> {
//			Thread thread = new Thread();
//			thread.setName("scheduledAsync-");
//			return thread;
//		});
//
//		System.out.println("Task scheduled to execute after 10 seconds at : " + LocalDateTime.now().toString());
//		return null;
//	}
}
