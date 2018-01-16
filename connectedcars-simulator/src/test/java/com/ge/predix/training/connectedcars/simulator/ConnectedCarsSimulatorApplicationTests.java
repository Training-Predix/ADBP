package com.ge.predix.training.connectedcars.simulator;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import com.ge.predix.training.connectedcars.simulator.model.ConnectedCar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConnectedCarsSimulatorApplicationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;

	private static final Log logger = LogFactory.getLog(ConnectedCarsSimulatorApplicationTests.class);

	@Before
	public void demoBefore() {
		logger.info("=========================== Running Tests ========================================");
	}

	@Test
	public void test() throws MalformedURLException, JSONException {
		
		ResponseEntity<String> response = null;
		
		try {
			response = restTemplate.getForEntity("/cars/simulator", String.class);    
		} catch (RestClientException restClientException) {
			logger.error("****************** ERROR: " + restClientException);
		}
		
		List<ConnectedCar> list = new ArrayList<ConnectedCar>();
		
		try {
			list = new Gson().fromJson(response.getBody(), new TypeToken<List<ConnectedCar>>() {}.getType());
		} catch (JsonSyntaxException e) {
			logger.error("****************** ERROR: Error converting JSON response to ConnectedCar list");
		}
		
		// Checking the name for the first car
		assertEquals("Ford Fusion", list.get(0).getName());
		
	}

}