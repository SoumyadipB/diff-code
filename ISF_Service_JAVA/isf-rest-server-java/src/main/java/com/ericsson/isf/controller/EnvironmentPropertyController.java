package com.ericsson.isf.controller;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.EnvironmentPropertyModel;
import com.ericsson.isf.service.EnvironmentPropertyService;

/**
 *
 * @author eakinhm
 */

@RestController
@RequestMapping("/environmentPropertyManagement")

public class EnvironmentPropertyController {

	@Autowired /* Bind to bean/pojo */
	private EnvironmentPropertyService environmentPropertyService;

	/**
	 * 
	 * @param key
	 * @return List<EnvironmentPropertyModel>
	 */

	@RequestMapping(value = "/getEnvironmentPropertyModelByKey", method = RequestMethod.GET)
	public List<EnvironmentPropertyModel> getEnvironmentPropertyModelByKey(
			@RequestParam(value = "key", required = true) String key) {

		return environmentPropertyService.getEnvironmentPropertyModelByKey(key);
	}

}
