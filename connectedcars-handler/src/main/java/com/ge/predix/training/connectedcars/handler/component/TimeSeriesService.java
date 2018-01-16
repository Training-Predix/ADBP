package com.ge.predix.training.connectedcars.handler.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.Body;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.DatapointsIngestion;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.DatapointsQuery;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.DatapointsLatestQuery;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.ge.predix.entity.timeseries.tags.TagsList;
import com.ge.predix.entity.util.map.Map;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.ge.predix.solsvc.timeseries.bootstrap.client.TimeseriesClient;
import com.ge.predix.solsvc.timeseries.bootstrap.config.ITimeseriesConfig;
import com.ge.predix.training.connectedcars.handler.model.ConnectedCar;

@Component
public class TimeSeriesService {
	
	private static final Log logger = LogFactory.getLog(TimeSeriesService.class);
	
	@Autowired
	private RestClient restClient;
	
	@Autowired
	private TimeseriesClient timeseriesClient;

	@Autowired
	@Qualifier("defaultTimeseriesConfig")
	private ITimeseriesConfig timeseriesConfig;
	
	public void pushDatatoTimeSeries(List<ConnectedCar> ccList) {
		this.timeseriesClient.createConnectionToTimeseriesWebsocket();
		
		for(ConnectedCar cc: ccList) {
			try {
				pushDataToTimeSeries(cc);
			} catch (Exception e) {
				throw new RuntimeException("unable to set up timeseries Websocket Pool", e); // + this.timeseriesConfig
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void pushDataToTimeSeries(ConnectedCar cc) {
		logger.info("Connected Car Id: " + cc.getId());
		
		// -------  Ingest Odometer Reading data to Time series ------- 
		 
		DatapointsIngestion odometerIngestion = new DatapointsIngestion();
		odometerIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> odometerBodies = new ArrayList<Body>();
		
		Body odometerBody = new Body();
		odometerBody.setName("NAME_" + cc.getId() + "_odometer_reading"); // Tag Name
		
		List<Object> odometerDatapoint = new ArrayList<Object>();
		odometerDatapoint.add(cc.getCurrentTime()); // Timestamp
		odometerDatapoint.add(cc.getOdometerReading()); // Value
		/*odometerDatapoint.add(3); */ // Quality

		List<Object> odometerDatapoints = new ArrayList<Object>();
		odometerDatapoints.add(odometerDatapoint);

		odometerBody.setDatapoints(odometerDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map odometerMap = new Map();
		odometerMap.put("carId", cc.getId());
		odometerMap.put("description", "Connected Cars");
		
		odometerBody.setAttributes(odometerMap);
		
		odometerBodies.add(odometerBody);

		odometerIngestion.setBody(odometerBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(odometerIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting odometer reading data to time series", e);
		}	
		
		// -------  Ingest Outside Temperature data to Time series  ------- 
		 
		DatapointsIngestion outsideTempIngestion = new DatapointsIngestion();
		outsideTempIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> outsideTempBodies = new ArrayList<Body>();
		
		Body outsideTempBody = new Body();
		outsideTempBody.setName("NAME_" + cc.getId() + "_outside_temperature"); // Tag Name
		
		List<Object> outsideTempDatapoint = new ArrayList<Object>();
		outsideTempDatapoint.add(cc.getCurrentTime()); // Timestamp
		outsideTempDatapoint.add(cc.getOutsideTemp()); // Value
		/*outsideTempDatapoint.add(3); */ // Quality

		List<Object> outsideTempDatapoints = new ArrayList<Object>();
		outsideTempDatapoints.add(outsideTempDatapoint);

		outsideTempBody.setDatapoints(outsideTempDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map outsideTempMap = new Map();
		outsideTempMap.put("carId", cc.getId());
		outsideTempMap.put("description", "Connected Cars");
		
		outsideTempBody.setAttributes(outsideTempMap);
		
		outsideTempBodies.add(outsideTempBody);

		outsideTempIngestion.setBody(outsideTempBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(outsideTempIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting outsideTemp reading data to time series", e);
		}	
		
		// -------  Ingest Engine Temperature data to Time series  ------- 
		 
		DatapointsIngestion engineTempIngestion = new DatapointsIngestion();
		engineTempIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> engineTempBodies = new ArrayList<Body>();
		
		Body engineTempBody = new Body();
		engineTempBody.setName("NAME_" + cc.getId() + "_engine_temperature"); // Tag Name
		
		List<Object> engineTempDatapoint = new ArrayList<Object>();
		engineTempDatapoint.add(cc.getCurrentTime()); // Timestamp
		engineTempDatapoint.add(cc.getEngineTemp()); // Value
		/*engineTempDatapoint.add(3); */ // Quality

		List<Object> engineTempDatapoints = new ArrayList<Object>();
		engineTempDatapoints.add(engineTempDatapoint);

		engineTempBody.setDatapoints(engineTempDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map engineTempMap = new Map();
		engineTempMap.put("carId", cc.getId());
		engineTempMap.put("description", "Connected Cars");
		
		engineTempBody.setAttributes(engineTempMap);
		
		engineTempBodies.add(engineTempBody);

		engineTempIngestion.setBody(engineTempBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(engineTempIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting engineTemp reading data to time series", e);
		}	
		
		// -------  Ingest Speed data to Time series  ------- 
		 
		DatapointsIngestion speedIngestion = new DatapointsIngestion();
		speedIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> speedBodies = new ArrayList<Body>();
		
		Body speedBody = new Body();
		speedBody.setName("NAME_" + cc.getId() + "_speed"); // Tag Name
		
		List<Object> speedDatapoint = new ArrayList<Object>();
		speedDatapoint.add(cc.getCurrentTime()); // Timestamp
		speedDatapoint.add(cc.getSpeed()); // Value
		/*speedDatapoint.add(3); */ // Quality

		List<Object> speedDatapoints = new ArrayList<Object>();
		speedDatapoints.add(speedDatapoint);

		speedBody.setDatapoints(speedDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map speedMap = new Map();
		speedMap.put("carId", cc.getId());
		speedMap.put("description", "Connected Cars");
		
		speedBody.setAttributes(speedMap);
		
		speedBodies.add(speedBody);

		speedIngestion.setBody(speedBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(speedIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting speed reading data to time series", e);
		}	
		
		// -------  Ingest Fuel Level to Time series  ------- 
		 
		DatapointsIngestion fuelLevelIngestion = new DatapointsIngestion();
		fuelLevelIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> fuelLevelBodies = new ArrayList<Body>();
		
		Body fuelLevelBody = new Body();
		fuelLevelBody.setName("NAME_" + cc.getId() + "_fuel_level"); // Tag Name
		
		List<Object> fuelLevelDatapoint = new ArrayList<Object>();
		fuelLevelDatapoint.add(cc.getCurrentTime()); // Timestamp
		fuelLevelDatapoint.add(cc.getFuelLevel()); // Value
		/*fuelLevelDatapoint.add(3); */ // Quality

		List<Object> fuelLevelDatapoints = new ArrayList<Object>();
		fuelLevelDatapoints.add(fuelLevelDatapoint);

		fuelLevelBody.setDatapoints(fuelLevelDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map fuelLevelMap = new Map();
		fuelLevelMap.put("carId", cc.getId());
		fuelLevelMap.put("description", "Connected Cars");
		
		fuelLevelBody.setAttributes(fuelLevelMap);
		
		fuelLevelBodies.add(fuelLevelBody);

		fuelLevelIngestion.setBody(fuelLevelBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(fuelLevelIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting fuelLevel reading data to time series", e);
		}	
		
		// -------  Ingest Parking Brake Status to Time series  ------- 
		 
		DatapointsIngestion parkingBrakeIngestion = new DatapointsIngestion();
		parkingBrakeIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> parkingBrakeBodies = new ArrayList<Body>();
		
		Body parkingBrakeBody = new Body();
		parkingBrakeBody.setName("NAME_" + cc.getId() + "_parking_brake"); // Tag Name
		
		List<Object> parkingBrakeDatapoint = new ArrayList<Object>();
		parkingBrakeDatapoint.add(cc.getCurrentTime()); // Timestamp
		parkingBrakeDatapoint.add(cc.getParkingBrakeStatus()); // Value
		/*parkingBrakeDatapoint.add(3); */ // Quality

		List<Object> parkingBrakeDatapoints = new ArrayList<Object>();
		parkingBrakeDatapoints.add(parkingBrakeDatapoint);

		parkingBrakeBody.setDatapoints(parkingBrakeDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map parkingBrakeMap = new Map();
		parkingBrakeMap.put("carId", cc.getId());
		parkingBrakeMap.put("description", "Connected Cars");
		
		parkingBrakeBody.setAttributes(parkingBrakeMap);
		
		parkingBrakeBodies.add(parkingBrakeBody);

		parkingBrakeIngestion.setBody(parkingBrakeBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(parkingBrakeIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting parkingBrake reading data to time series", e);
		}
		
		// -------  Ingest Gas Cap Status to Time series  ------- 
		 
		DatapointsIngestion gasCapIngestion = new DatapointsIngestion();
		gasCapIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> gasCapBodies = new ArrayList<Body>();
		
		Body gasCapBody = new Body();
		gasCapBody.setName("NAME_" + cc.getId() + "_gas_cap"); // Tag Name
		
		List<Object> gasCapDatapoint = new ArrayList<Object>();
		gasCapDatapoint.add(cc.getCurrentTime()); // Timestamp
		gasCapDatapoint.add(cc.getGasCapStatus()); // Value
		/*gasCapDatapoint.add(3); */ // Quality

		List<Object> gasCapDatapoints = new ArrayList<Object>();
		gasCapDatapoints.add(gasCapDatapoint);

		gasCapBody.setDatapoints(gasCapDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map gasCapMap = new Map();
		gasCapMap.put("carId", cc.getId());
		gasCapMap.put("description", "Connected Cars");
		
		gasCapBody.setAttributes(gasCapMap);
		
		gasCapBodies.add(gasCapBody);

		gasCapIngestion.setBody(gasCapBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(gasCapIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting gasCap reading data to time series", e);
		}
		
		// -------  Ingest Window Alarm Status to Time series  ------- 
		 
		DatapointsIngestion windowIngestion = new DatapointsIngestion();
		windowIngestion.setMessageId(cc.getCurrentTime() + "");
		
		List<Body> windowBodies = new ArrayList<Body>();
		
		Body windowBody = new Body();
		windowBody.setName("NAME_" + cc.getId() + "_window"); // Tag Name
		
		List<Object> windowDatapoint = new ArrayList<Object>();
		windowDatapoint.add(cc.getCurrentTime()); // Timestamp
		windowDatapoint.add(cc.getWindowStatus()); // Value
		/*windowDatapoint.add(3); */ // Quality

		List<Object> windowDatapoints = new ArrayList<Object>();
		windowDatapoints.add(windowDatapoint);

		windowBody.setDatapoints(windowDatapoints);
		
		/* NOTE: Cannot have spaces in Time Series Attributes */
		Map windowMap = new Map();
		windowMap.put("carId", cc.getId());
		windowMap.put("description", "Connected Cars");
		
		windowBody.setAttributes(windowMap);
		
		windowBodies.add(windowBody);

		windowIngestion.setBody(windowBodies);
		
		try {
			this.timeseriesClient.postDataToTimeseriesWebsocket(windowIngestion);
		} catch (Exception e) {
			logger.error("Error ingesting window reading data to time series", e);
		}
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ge.predix.solsvc.api.WindDataAPI#getConnectedCarsDataTags()
	 */
	@SuppressWarnings("nls")
	public Response getConnectedCarsDataTags(String authorization) {
		try {
			List<Header> headers = generateHeaders();
			TagsList tagsList = this.timeseriesClient.listTags(headers);
			logger.info("tagsList: " + tagsList);
			
			List<String> tagList = tagsList.getResults();
			logger.info("tagsListSize: " + tagList.size());
			
			for(String tag : tagList) {
				logger.info("Tag: " + tag);
			}

			return handleResult(tagsList);
		} catch (Throwable e) {
			logger.error("unable to get wind data, ", e);
			throw new RuntimeException("unable to get wind data, errorMsg=" + e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("nls")
	public Response getYearlyConnectedCarsDataPoints(String id, String authorization, String starttime, String taglimit, String tagorder) {
		try {
			if (id == null) {
				return null;
			}

			List<Header> headers = generateHeaders();

			DatapointsQuery dpQuery = buildDatapointsQueryRequest(id, starttime, getInteger(taglimit), tagorder);
			DatapointsResponse response = this.timeseriesClient.queryForDatapoints(dpQuery, headers);
			logger.debug(response.toString());
			return handleResult(response);
		} catch (Throwable e) {
			logger.error("unable to get wind data, ", e);
			throw new RuntimeException("unable to get wind data, errorMsg=" + e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("nls")
	public Response getLatestConnectedCarsDataPoints(String id) {
		try {
			if (id == null) {
				return null;
			}
			List<Header> headers = generateHeaders();

			DatapointsLatestQuery dpQuery = buildLatestDatapointsQueryRequest(id);
			DatapointsResponse response = this.timeseriesClient.queryForLatestDatapoint(dpQuery, headers);
			logger.debug(response.toString());

			return handleResult(response);
		} catch (Throwable e) {
			logger.error("unable to get connected cars data, ", e);
			throw new RuntimeException("unable to get connected cars data, errorMsg=" + e.getMessage(), e);

		}
	}
	
	private DatapointsLatestQuery buildLatestDatapointsQueryRequest(String id) {
		DatapointsLatestQuery datapointsLatestQuery = new DatapointsLatestQuery();

		com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag();
		tag.setName(id);
		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag> tags = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag>();
		tags.add(tag);
		datapointsLatestQuery.setTags(tags);
		return datapointsLatestQuery;
	}
	
	/**
	 * 
	 * @param id
	 * @param startDuration
	 * @param tagorder
	 * @return
	 */
	private DatapointsQuery buildDatapointsQueryRequest(String id, String startDuration, int taglimit,
			String tagorder) {
		DatapointsQuery datapointsQuery = new DatapointsQuery();
		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag> tags = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag>();
		datapointsQuery.setStart(startDuration);
		// datapointsQuery.setStart("1y-ago"); //$NON-NLS-1$
		String[] tagArray = id.split(","); //$NON-NLS-1$
		List<String> entryTags = Arrays.asList(tagArray);

		for (String entryTag : entryTags) {
			com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag();
			tag.setName(entryTag);
			tag.setLimit(taglimit);
			tag.setOrder(tagorder);
			tags.add(tag);
		}
		datapointsQuery.setTags(tags);
		return datapointsQuery;
	}
	
	/**
	 * 
	 * @param id
	 * @param startDuration
	 * @param tagorder
	 * @return
	 */
	@SuppressWarnings("unused")
	private DatapointsQuery buildDatapointsQueryRequestForChart(String id, String startDuration, int taglimit, String tagorder) {
		DatapointsQuery datapointsQuery = new DatapointsQuery();
		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag> tags = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag>();
		datapointsQuery.setStart(startDuration);
		datapointsQuery.setEnd(startDuration);
		// datapointsQuery.setStart("1y-ago"); //$NON-NLS-1$
		
		String[] tagArray = id.split(","); //$NON-NLS-1$
		List<String> entryTags = Arrays.asList(tagArray);

		for (String entryTag : entryTags) {
			com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag();
			tag.setName(entryTag);
			tag.setLimit(1);
			tag.setOrder(tagorder);
			tags.add(tag);
		}
		datapointsQuery.setTags(tags);
		
		return datapointsQuery;
	}
	
	/**
	 * 
	 * @param s
	 *            -
	 * @return
	 */
	private int getInteger(String s) {
		int inValue = 25;
		try {
			inValue = Integer.parseInt(s);

		} catch (NumberFormatException ex) {
			// s is not an integer
		}
		return inValue;
	}
	
	@SuppressWarnings("javadoc")
	protected Response handleResult(Object entity) {
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type(MediaType.APPLICATION_JSON);
		responseBuilder.entity(entity);
		return responseBuilder.build();
	}
	
	@SuppressWarnings({})
	private List<Header> generateHeaders() {
		List<Header> headers = this.restClient.getSecureTokenForClientId();
		this.restClient.addZoneToHeaders(headers, this.timeseriesConfig.getZoneId());
		return headers;
	}
}