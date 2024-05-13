package com.miaplicacion.feedbackapp.model;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventos")
public class Evento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private LocalTime hora;
	private String localizacion;

	public Evento() {
	}
	
	public Evento(Long id, String titulo, LocalTime hora, String localizacion, List<Feedback> feedbacks) {
		this.id = id;
		this.titulo = titulo;
		this.hora = hora;
		this.localizacion = localizacion;
		this.feedbacks = feedbacks;
	}

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Feedback> feedbacks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

}