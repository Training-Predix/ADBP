package com.ge.predix.training.connectedcars.simulator;

public class AppConstants {
	
	/* 
	 * CCX(param1,param2,param3,param4,param5,param6,param7,param8,param9,param10,param11,param12,param13)
	 * param1: Name 											Ex. Ford Fusion
	 * param2: Current Odometer Reading [mi] 					Ex. 24532
	 * param3: Current Outside Temperature [°F] 				Ex. 78
	 * param4: Current Engine Temperature [°F]  				Ex. 198
	 * param5: Current Speed 									Ex. 45
	 * param6: Current Fuel Level 								Ex. 60
	 * param7: Current Parking Brake Status [0=Off,1=On]		Ex. 0
	 * param8: Current Gas Cap Status [0=Closed,1=Open] 		Ex. 0
	 * param9: Current Window Alert Status [0=Safe,1=Broken] 	Ex. 0
	 * param10: Min Outside Temperature [°F]					Ex. 55
	 * param11: Max Outside Temperature [°F]					Ex. 95	
	 * param12: Min Engine Temperature [°F]						Ex. 190
	 * param13: Max Engine Temperature [°F]						Ex. 220
	 */
	public enum ConnectedCarEnum {
		CC1("Ford Fusion",24532,78,198,45,60,0,0,0,55,95,190,220),
		CC2("Hyundai Sonata",61051,55,200,25,24,0,0,0,25,85,190,220),
		CC3("Toyota Prius",121457,50,195,0,45,1,0,0,15,80,190,220),
		CC4("Mercedes-Benz CLA",2806,85,210,70,90,0,0,0,55,130,190,220),
		CC5("Chevrolet Camaro",39450,72,220,75,75,0,0,0,40,100,190,220);
		
		private ConnectedCarEnum(String name, int odometerReading, double outsideTemp, double engineTemp, int speed, int fuelLevel, int parkingBrakeStatus,
				 int gasCapStatus, int windowStatus, double minOutsideTemp, double maxOutsideTemp, double minEngineTemp, double maxEngineTemp) {
			this.name = name;
			this.odometerReading = odometerReading;
			this.outsideTemp = outsideTemp;
			this.engineTemp = engineTemp;
			this.speed = speed;
			this.fuelLevel = fuelLevel;
			this.parkingBrakeStatus = parkingBrakeStatus;
			this.gasCapStatus = gasCapStatus;
			this.windowStatus = windowStatus;
			this.minOutsideTemp = minOutsideTemp;
			this.maxOutsideTemp = maxOutsideTemp;
			this.minEngineTemp = minEngineTemp;
			this.maxEngineTemp = maxEngineTemp;
		}
			
		private String name;
		private int odometerReading;
		private double outsideTemp;
		private double engineTemp;
		private int speed;
		private int fuelLevel;
		private int parkingBrakeStatus;
		private int gasCapStatus;
		private int windowStatus;
		
		private double minOutsideTemp;
		private double maxOutsideTemp;
		private double minEngineTemp;
		private double maxEngineTemp;
		
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

		public double getMinOutsideTemp() {
			return minOutsideTemp;
		}

		public void setMinOutsideTemp(double minOutsideTemp) {
			this.minOutsideTemp = minOutsideTemp;
		}

		public double getMaxOutsideTemp() {
			return maxOutsideTemp;
		}

		public void setMaxOutsideTemp(double maxOutsideTemp) {
			this.maxOutsideTemp = maxOutsideTemp;
		}
		
		public double getMinEngineTemp() {
			return minEngineTemp;
		}

		public void setMinEngineTemp(double minEngineTemp) {
			this.minEngineTemp = minEngineTemp;
		}

		public double getMaxEngineTemp() {
			return maxEngineTemp;
		}

		public void setMaxEngineTemp(double maxEngineTemp) {
			this.maxEngineTemp = maxEngineTemp;
		}
		
	}
	
}
