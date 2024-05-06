package com.miaplicacion.feedbackapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackRepository feedbackRepository;

	@PostMapping
	public Feedback createFeedback(@RequestBody Feedback feedback) {
		return feedbackRepository.save(feedback);
	}

	@GetMapping
	public List<Feedback> getAllFeedbacks() {
		return feedbackRepository.findAll();
	}
}
