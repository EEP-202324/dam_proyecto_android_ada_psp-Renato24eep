package com.miaplicacion.feedbackapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedbacks")
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String opinion;
	private Double puntuacion;

	public Feedback() {
	}

	public Feedback(Long id, String nombre, String evento, String opinion, Double puntuacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.opinion = opinion;
		this.puntuacion = puntuacion;
	}

	@ManyToOne
	@JoinColumn(name = "evento_id", nullable = false)
    @JsonBackReference
	private Evento evento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Double getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Double puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}