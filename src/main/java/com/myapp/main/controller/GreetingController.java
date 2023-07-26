package com.myapp.main.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myapp.main.XSSDetector;
import com.myapp.main.Exception.XSSException;
import com.myapp.main.service.EmployeeService;

@Controller
public class GreetingController {

	@ExceptionHandler(XSSException.class)
	public ResponseEntity<String> handleXSSException(XSSException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

	// ...
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private XSSDetector xssDetector;

	@GetMapping("/greeting")
	public String index(@RequestParam(name = "name", required = true) String name, Model model) throws XSSException {

		String cleanedName = cleanHtml(name);
		boolean isXssAttack = xssDetector.detectXSSAttack(name);
		if (isXssAttack) {
			employeeService.saveLogToFile("XSS attack attempt detected for input: " + name);
			throw new XSSException("XSS attack attempt detected");
		}

		model.addAttribute("name", cleanedName);
		return "greeting";
	}

	private String cleanHtml(String html) throws XSSException {
		Whitelist whitelist = Whitelist.relaxed();
		Cleaner cleaner = new Cleaner(whitelist);
		Document dirtyDocument = Jsoup.parseBodyFragment(html);
		Document cleanDocument = cleaner.clean(dirtyDocument);
		String cleanedHtml = cleanDocument.body().html();

		// Check if the cleaned HTML differs from the original HTML
		if (!cleanedHtml.equals(html)) {
			// An XSS attack attempt detected
			LOGGER.warn("XSS attack attempt detected with input: {}", html);
			throw new XSSException("XSS attack attempt detected");
		}

		return cleanedHtml;
	}

	// ...
}
