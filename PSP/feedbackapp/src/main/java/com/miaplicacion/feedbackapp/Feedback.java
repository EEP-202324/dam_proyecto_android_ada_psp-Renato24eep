package com.miaplicacion.feedbackapp;

import org.springframework.data.annotation.Id;


record Feedback(@Id Long id, String nombre, String evento, String opinion, Double puntuacion) {

}