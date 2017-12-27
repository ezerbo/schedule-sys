package com.ss.schedulesys.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeResource {

	@GetMapping("/")
	public String home() {
		return "home";
	}
}
