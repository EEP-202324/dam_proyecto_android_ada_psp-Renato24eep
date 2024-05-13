package com.miaplicacion.feedbackapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miaplicacion.feedbackapp.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

}
