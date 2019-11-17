package tdc;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
  private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);
  private final Random random = new Random();
  private AtomicInteger index = new AtomicInteger(0);

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private TopicExchange topicExchange;

  @Value("${binding.list}")
  private String[] keys;

  @Scheduled(fixedRate = 2000)
  public void send() {
    if (this.index.incrementAndGet() == keys.length) {
      this.index.set(0);
    }
    String key = keys[this.index.get()];
    rabbitTemplate.convertAndSend(topicExchange.getName(), key, String.valueOf(random.nextInt(100)));
    LOGGER.info("Sent to Routing Key: " + key);
  }
}
