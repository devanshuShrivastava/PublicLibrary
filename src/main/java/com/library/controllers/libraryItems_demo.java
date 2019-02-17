package com.library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class libraryItems_demo {
	@RequestMapping("/")
	String entry() {
		return "Home.jsp";
	}
	@RequestMapping("/signIn")
	String signIn() {
		return "signInView.jsp";
	}
}
