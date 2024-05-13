package com.miaplicacion.feedbackapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miaplicacion.feedbackapp.model.Feedback;
import com.miaplicacion.feedbackapp.repository.FeedbackRepository;

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

	@GetMapping("/{id}")
	public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
		Optional<Feedback> feedback = feedbackRepository.findById(id);
		if (feedback.isPresent()) {
			return ResponseEntity.ok(feedback.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedbackDetails) {
		Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
		if (!feedbackOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Feedback feedback = feedbackOptional.get();
		feedback.setNombre(feedbackDetails.getNombre());
		feedback.setOpinion(feedbackDetails.getOpinion());
		feedback.setPuntuacion(feedbackDetails.getPuntuacion());

		Feedback updatedFeedback = feedbackRepository.save(feedback);
		return ResponseEntity.ok(updatedFeedback);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
		if (!feedbackRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		feedbackRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
