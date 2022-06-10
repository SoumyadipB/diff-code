package com.ericsson.isf.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;


@Controller
public class HelloISFController {
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String sayHello(ModelMap model) {
		model.addAttribute("message","APIs are up. Please! go to the specified Handlers.");
		return "welcome";
	}
}
