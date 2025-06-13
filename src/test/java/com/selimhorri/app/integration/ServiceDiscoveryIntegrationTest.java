package com.selimhorri.app.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceDiscoveryIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void actuatorHealthEndpoint_ShouldReturnUpStatus() {
        // Arrange
        String url = "http://localhost:" + port + "/actuator/health";

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"status\":\"UP\""));
    }

    @Test
    public void actuatorHealthEndpoint_ShouldContainServiceDiscoveryCircuitBreakerDetails() {
        // Arrange
        String url = "http://localhost:" + port + "/actuator/health";

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"circuitBreakers\""));
        assertTrue(response.getBody().contains("\"serviceDiscovery\""));
        assertTrue(response.getBody().contains("\"state\":\"CLOSED\""));
        assertTrue(response.getBody().contains("\"failureRateThreshold\":\"50.0%\""));
    }

    @Test
    public void actuatorHealthEndpoint_ShouldContainDiscoveryCompositeDetails() {
        // Arrange
        String url = "http://localhost:" + port + "/actuator/health";

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"discoveryComposite\""));
        assertTrue(response.getBody().contains("\"discoveryClient\""));
        assertTrue(response.getBody().contains("\"eureka\""));
        assertTrue(response.getBody().contains("\"status\":\"UNKNOWN\""));
        assertTrue(response.getBody().contains("\"applications\":{}"));
    }

    @Test
    public void actuatorHealthEndpoint_ShouldContainDiskSpaceDetails() {
        // Arrange
        String url = "http://localhost:" + port + "/actuator/health";

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"diskSpace\""));
        assertTrue(response.getBody().contains("\"total\""));
        assertTrue(response.getBody().contains("\"free\""));
        assertTrue(response.getBody().contains("\"exists\":true"));
    }

    @Test
    public void actuatorHealthEndpoint_ShouldContainEmptyServicesList() {
        // Arrange
        String url = "http://localhost:" + port + "/actuator/health";

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"services\":[]"));
    }
}