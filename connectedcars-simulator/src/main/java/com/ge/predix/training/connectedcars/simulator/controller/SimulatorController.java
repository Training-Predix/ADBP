package com.ge.predix.training.connectedcars.simulator.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ge.predix.training.connectedcars.simulator.AppConstants.ConnectedCarEnum;
import com.ge.predix.training.connectedcars.simulator.model.ConnectedCar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
@RequestMapping("/cars")
public class SimulatorController {
	
	private static final Log logger = LogFactory.getLog(SimulatorController.class);
	private List<ConnectedCar> ccList = new ArrayList<ConnectedCar>();
	
	/*
	 * Initializing the Connected Cars List
	 */
	public SimulatorController() {		
		for (ConnectedCarEnum cce : ConnectedCarEnum.values()) {
			ConnectedCar cc = new ConnectedCar();
			
			cc.setCurrentTime(System.currentTimeMillis());
			cc.setEngineTemp(cce.getEngineTemp());
			cc.setFuelLevel(cce.getFuelLevel());
			cc.setGasCapStatus(cce.getGasCapStatus());
			cc.setId(cce.name());
			cc.setName(cce.getName());
			cc.setOutsideTemp(cce.getOutsideTemp());
			cc.setParkingBrakeStatus(cce.getParkingBrakeStatus());
			cc.setSpeed(cce.getSpeed());
			cc.setWindowStatus(cce.getWindowStatus());
			cc.setOdometerReading(cce.getOdometerReading());
			cc.setMinOutsideTemp(cce.getMinOutsideTemp());
			cc.setMaxOutsideTemp(cce.getMaxOutsideTemp());
			cc.setMinEngineTemp(cce.getMinEngineTemp());
			cc.setMaxEngineTemp(cce.getMaxEngineTemp());
			
			ccList.add(cc);
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/simulator", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String generateSimulatedData() {
		logger.info("-------------------------------- Inside generateSimulatedData --------------------------------");
		
		// Convert the Connected Cars list to JSON
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String jsonData = gson.toJson(ccList);
		
		for (ConnectedCar cc : ccList) {
			cc.setCurrentTime(System.currentTimeMillis());
		}
		
		logger.info("**** JSON Output: ");
		logger.info(jsonData);
		
		logger.info("--------------------------------------------------------------------------------------------");
		
		return jsonData;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String status() {
		logger.info("-------------------------------- Inside status --------------------------------");
		
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
		Date currentDate = new Date(currentTime);
		
		String jsonStatusString = "{\"status\":\"UP\",\"currentTime\":\"" + sdf.format(currentDate) + "\"}";
		
		logger.info("--------------------------------------------------------------------------------------------");
		
		return jsonStatusString;
	}
	
	@Scheduled(fixedDelay=2000)
	public void generateSimulatorData() {
		//updateCarIsRunning();
		updateOdometerReading();
		updateOutsideTemp();
		updateEngineTemp();
		updateSpeed();
		updateFuelLevel();
		updateParkingBrakeStatus();
		updateGasCapStatus();
		updateWindowStatus();
	}
	
	public void updateOdometerReading() {
		for(ConnectedCar cc : ccList) {
			cc.setOdometerReading(cc.getOdometerReading() + 1);
		}
	}
	
	public void updateOutsideTemp() {
		for(ConnectedCar cc : ccList) {
			double currTemp = cc.getOutsideTemp();

			double minTemp = currTemp - 1;
			double maxTemp = currTemp + 1;
			
			if(minTemp < cc.getMinOutsideTemp()) {
				minTemp = cc.getMinOutsideTemp();
			}
			
			if(maxTemp > cc.getMaxOutsideTemp()) {
				maxTemp = cc.getMaxOutsideTemp();
			}
			
			int randomNum = Double.valueOf(currTemp).intValue();
			
			try {
				randomNum = ThreadLocalRandom.current().nextInt(Double.valueOf(minTemp).intValue(), Double.valueOf(maxTemp).intValue() + 1);
			} catch (Exception e) {
				logger.error("updateOutsideTemp- error generating random number", e);
			}
			
			cc.setOutsideTemp(new Double(randomNum)); 
		}
	}
	
	public void updateEngineTemp() {
		for(ConnectedCar cc : ccList) {
			double currTemp = cc.getEngineTemp();

			double minTemp = currTemp - 2;
			double maxTemp = currTemp + 2;
			
			if(minTemp < cc.getMinEngineTemp()) {
				minTemp = cc.getMinEngineTemp();
			}
			
			if(maxTemp > cc.getMaxEngineTemp()) {
				maxTemp = cc.getMaxEngineTemp();
			}
			
			int randomNum = Double.valueOf(currTemp).intValue();
			
			try {
				randomNum = ThreadLocalRandom.current().nextInt(Double.valueOf(minTemp).intValue(), Double.valueOf(maxTemp).intValue() + 1);
			} catch (Exception e) {
				logger.error("updateEngineTemp- error generating random number", e);
			}
			
			cc.setEngineTemp(new Double(randomNum)); 
		}
	}
	
	public void updateSpeed() {
		for(ConnectedCar cc : ccList) {
			int ccSpeed = cc.getSpeed();

			int minSpeed = ccSpeed - 5;
			int maxSpeed = ccSpeed + 5;
			
			if(ccSpeed < 5) {
				minSpeed = 0;
			}
			
			if(ccSpeed > 85) {
				maxSpeed = 85;
			}
			
			int randomNum = ccSpeed;
			
			try {
				randomNum = ThreadLocalRandom.current().nextInt(Double.valueOf(minSpeed).intValue(), Double.valueOf(maxSpeed).intValue() + 1);
			} catch (Exception e) {
				logger.error("updateSpeed- error generating random number", e);
			}
			
			cc.setSpeed(randomNum); 
		}
	}
	
	public void updateFuelLevel() {
		for(ConnectedCar cc : ccList) {
			int currLevel = cc.getFuelLevel();

			int minLevel = currLevel - 3;
			
			if(currLevel < 10) {
				cc.setFuelLevel(100);
			} else {
				int randomNum = currLevel;
				
				try {
					randomNum = ThreadLocalRandom.current().nextInt(minLevel, currLevel + 1);
				} catch (Exception e) {
					logger.error("updateFuelLevel- error generating random number", e);
				}
				
				cc.setFuelLevel(randomNum);
			}
		}
	}
	
	public void updateParkingBrakeStatus() {
		for(ConnectedCar cc : ccList) {
			int ccSpeed = cc.getSpeed();
			
			if((ccSpeed < 5) && (StringUtils.equalsIgnoreCase(cc.getId(), "CC3") || StringUtils.equalsIgnoreCase(cc.getId(), "CC2"))) {
				cc.setParkingBrakeStatus(1);
			}
			
			if(ccSpeed > 5) {
				cc.setParkingBrakeStatus(0);
			}
		}
	}
	
	public void updateGasCapStatus() {
		for(ConnectedCar cc : ccList) {
			int ccSpeed = cc.getSpeed();
			
			if(ccSpeed == 0) {
				int randomNum = cc.getGasCapStatus();
				
				try {
					randomNum = ThreadLocalRandom.current().nextInt(0, 2);
				} catch (Exception e) {
					logger.error("updateGasCapStatus- error generating random number", e);
				}
				
				cc.setGasCapStatus(randomNum);
			}
		}
	}
	
	public void updateWindowStatus() {
		
	}
	
	public List<ConnectedCar> getCcList() {
		return ccList;
	}

	public void setCcList(List<ConnectedCar> ccList) {
		this.ccList = ccList;
	}
	
}
