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

import com.miaplicacion.feedbackapp.model.Evento;
import com.miaplicacion.feedbackapp.model.Feedback;
import com.miaplicacion.feedbackapp.repository.EventoRepository;

@RestController
@RequestMapping("/api/evento")
public class EventoController {

	@Autowired
	private EventoRepository eventoRepository;

	@PostMapping
	public ResponseEntity<Evento> createEvento(@RequestBody Evento evento) {
		if (evento.getFeedbacks() != null) {
			for (Feedback feedback : evento.getFeedbacks()) {
				feedback.setEvento(evento);
			}
		}
		Evento savedEvento = eventoRepository.save(evento);
		return ResponseEntity.ok(savedEvento);
	}

	@GetMapping
	public List<Evento> getAllEventos() {
		return eventoRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Evento> getEventById(@PathVariable Long id) {
		Optional<Evento> evento = eventoRepository.findById(id);
		if (evento.isPresent()) {
			return ResponseEntity.ok(evento.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Evento> updateEvent(@PathVariable Long id, @RequestBody Evento eventoDetails) {
		Optional<Evento> eventoOptional = eventoRepository.findById(id);
		if (!eventoOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Evento evento = eventoOptional.get();
		evento.setTitulo(eventoDetails.getTitulo());
		evento.setHora(eventoDetails.getHora());
		evento.setLocalizacion(eventoDetails.getLocalizacion());

		Evento updatedEvento = eventoRepository.save(evento);
		return ResponseEntity.ok(updatedEvento);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
		if (!eventoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		eventoRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
