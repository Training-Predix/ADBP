package com.ge.predix.training.connectedcars.handler.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;

public class ConnectedCar {
	
	@Expose private Long currentTime;
	@Expose private String id;
	@Expose private String name;
	@Expose private int odometerReading;
	@Expose private double outsideTemp;
	@Expose private double engineTemp;
	@Expose private int speed;
	@Expose private int fuelLevel;
	@Expose private int parkingBrakeStatus;
	@Expose private int gasCapStatus;
	@Expose private int windowStatus;
	
	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOdometerReading() {
		return odometerReading;
	}

	public void setOdometerReading(int odometerReading) {
		this.odometerReading = odometerReading;
	}

	public double getOutsideTemp() {
		return outsideTemp;
	}

	public void setOutsideTemp(double outsideTemp) {
		this.outsideTemp = outsideTemp;
	}

	public double getEngineTemp() {
		return engineTemp;
	}

	public void setEngineTemp(double engineTemp) {
		this.engineTemp = engineTemp;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getFuelLevel() {
		return fuelLevel;
	}

	public void setFuelLevel(int fuelLevel) {
		this.fuelLevel = fuelLevel;
	}

	public int getParkingBrakeStatus() {
		return parkingBrakeStatus;
	}

	public void setParkingBrakeStatus(int parkingBrakeStatus) {
		this.parkingBrakeStatus = parkingBrakeStatus;
	}

	public int getGasCapStatus() {
		return gasCapStatus;
	}

	public void setGasCapStatus(int gasCapStatus) {
		this.gasCapStatus = gasCapStatus;
	}

	public int getWindowStatus() {
		return windowStatus;
	}

	public void setWindowStatus(int windowStatus) {
		this.windowStatus = windowStatus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this.id);
	}

}