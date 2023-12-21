package net.barrage.school.java.ecatalog.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("db")
@AutoConfigureObservability
public class PerformanceTest {
    private int requestsPerMinute = 50;
    private String endpoint = "/e-catalog/api/v1/products";

    @Test
    public void sendRequests() throws InterruptedException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        try {
            for (int i = 0; i < requestsPerMinute; i++) {
                restTemplate.getForObject("http://localhost:" + 8080 + endpoint, String.class);
                Thread.sleep(60000 / requestsPerMinute);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
