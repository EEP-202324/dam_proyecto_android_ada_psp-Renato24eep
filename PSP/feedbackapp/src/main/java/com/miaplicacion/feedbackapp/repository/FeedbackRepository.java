package com.miaplicacion.feedbackapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miaplicacion.feedbackapp.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
	List<Feedback> findByEventoId(Long eventoId);
}