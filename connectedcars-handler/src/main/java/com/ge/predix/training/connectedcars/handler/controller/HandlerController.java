package com.ge.predix.training.connectedcars.handler.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ge.predix.training.connectedcars.handler.component.TimeSeriesService;
import com.ge.predix.training.connectedcars.handler.model.ConnectedCar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping(value = "/handler")
public class HandlerController {
	
	private static final Log logger = LogFactory.getLog(HandlerController.class);
	
	@Value("${connectedcars.simulator.url}")
	private String simulatorURL;
	
	@Autowired
	private TimeSeriesService timeSeriesService;
	
	@Scheduled(fixedDelay=3000)
	public void parseSimulatorData() {
		logger.debug("-------------------------------- parseSimulatorData --------------------------------");
		
		//Get the data simulator service URL specified in the config files
		final String uri = simulatorURL; 

		RestTemplate restTemplate = new RestTemplate();
	     
	    String result = "";
	    
		try {
			result = restTemplate.getForObject(uri, String.class);
			
			logger.debug(" HandlerController :: generateSimulatorData - json result: " + result);
			
			// Converting the JSON response to a list of type ConnectedCar
			List<ConnectedCar> ccList = new ArrayList<ConnectedCar>();
			
			try {
				ccList = new Gson().fromJson(result, new TypeToken<List<ConnectedCar>>(){}.getType());
			} catch (JsonSyntaxException e) {
				logger.error("Error converting json response to ConnectedCar list");
			}
			
			logger.debug("ConnectedCar list size: " + ccList.size());
			
			// Call ingest method
			timeSeriesService.pushDatatoTimeSeries(ccList);
		} catch (RestClientException e) {
			
		}
	}
	
	@RequestMapping(value = "/tags")
	@ResponseBody
	public Response getConnectedCarsDataTags(@RequestParam(value="authorization", required= false) String authorization) {
		logger.debug("-------------------------------- getConnectedCarsDataTags --------------------------------");
		
		if(StringUtils.isBlank(authorization)) {
			authorization = "client_credentials";
		}
		
		return timeSeriesService.getConnectedCarsDataTags(authorization);
	}
	
	@RequestMapping(value = "/latest_data/sensor_id/{id}")
	@ResponseBody
    public Response getLatestConnectedCarsDataPoints(@PathVariable("id") String id) { 
		logger.debug("-------------------------------- getLatestConnectedCarsDataPoints --------------------------------");
		
		logger.debug("id: " + id);
		
		return timeSeriesService.getLatestConnectedCarsDataPoints(id);
	}
	 
	@RequestMapping(value = "/yearly_data/sensor_id/{id}")
	@ResponseBody
    public Response getYearlyConnectedCarsDataPoints(@PathVariable("id") String id,
    		@RequestParam(value="authorization", required= false) String authorization,
    		@RequestParam(value="starttime", required= false, defaultValue="1y-ago") String starttime,
    		@RequestParam(value="tagLimit", required= false, defaultValue="25") String tagLimit,
    		@RequestParam(value="tagOrder", required= false, defaultValue="desc") String tagOrder) {
		
		logger.debug("-------------------------------- getYearlyConnectedCarsDataPoints --------------------------------");
		
		logger.debug("id: " + id);
		logger.debug("authorization: " + authorization);
		logger.debug("starttime: " + starttime);
		logger.debug("taglimit: " + tagLimit);
		logger.debug("order: " + tagOrder);
		
		return timeSeriesService.getYearlyConnectedCarsDataPoints(id, authorization, starttime, tagLimit, tagOrder);
	}
	
	/*@RequestMapping(value = "/chart_data/sensor_id/{id1}/{id2}/{id3}")
	@ResponseBody
	public Response getChartData(	@PathVariable("id1") String id1,
								@PathVariable("id2") String id2,
								@PathVariable("id3") String id3,
					    		@RequestParam(value="authorization", required= false) String authorization,
					    		@RequestParam(value="starttime", required= false, defaultValue="1y-ago") String starttime,
					    		@RequestParam(value="tagLimit", required= false, defaultValue="25") String tagLimit,
					    		@RequestParam(value="tagOrder", required= false, defaultValue="desc") String tagOrder) {
		
		return timeSeriesService.getChartData(id1, id2, id3, authorization, starttime, tagLimit, tagOrder);
	}*/
	
}
