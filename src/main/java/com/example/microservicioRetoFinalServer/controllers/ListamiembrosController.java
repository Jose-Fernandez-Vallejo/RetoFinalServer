package com.example.microservicioRetoFinalServer.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservicioRetoFinalServer.endpoints.ListaMiembrosEndpoint;
import com.google.gson.Gson;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import starterRetoFinal.ListaMiembros;
import starterRetoFinal.Persona;
import starterRetoFinal.PersonaBuilder;

@RestController
public class ListamiembrosController {
	
	@Autowired
	ListaMiembrosEndpoint endpoint;
	
	Counter counter;
	Counter counterAdd;
	Counter counterDelete;
	
	final static Logger logger = LoggerFactory.getLogger(ListamiembrosController.class);
	PersonaBuilder builder = new PersonaBuilder();
	
	
	public ListamiembrosController(MeterRegistry registry)
	{
		this.counter = Counter.builder("llamadas.miembros").description("Total llamadas a miembros").register(registry);
		this.counterAdd = Counter.builder("llamadas.miembrosAdd").description("Llamadas a Añadir miembro").register(registry);
		this.counterDelete = Counter.builder("llamadas.miembrosDelete").description("Llamadas a borrar miembro").register(registry);
	}
	
	ListaMiembros lista = ListaMiembros.getInstance(10L);
	
	
	//añade miembros desde el servicio del cliente
	
	@PostMapping(path = "/miembros/add")
	public void addMiembros(@RequestBody Persona persona)
	{
		
		if (lista.addPersona(persona)) {
			endpoint.write("añadir miembro: " + persona.getNombre());
		}
		else
		{
			endpoint.write("nose a podido añadir miembro: " + persona.getNombre() + " la lista esta llena");
		}
		logger.info("llamada a añadir miembro.");
		counterAdd.increment();
		
	}
	
	//añade desde postman
	
	@PostMapping(path = "/miembros/add/{nombre}/{edad}/{tipo}")
	public void addMiembros(@PathVariable(value = "nombre") String nombre,@PathVariable(value = "edad") int edad,@PathVariable(value = "tipo") String tipo)
	{
		Persona persona = builder.nombre(nombre).edad(edad).tipoMiembro(tipo).build();
		
		if (lista.addPersona(persona)) {
			endpoint.write("añadir miembro: " + persona.getNombre());
		}
		else
		{
			endpoint.write("nose a podido añadir miembro: " + persona.getNombre() + " la lista esta llena");
		}
		logger.info("llamada a añadir miembro.");
		counterAdd.increment();
		
	}
	
	//borra desde postman
	
	@DeleteMapping(path = "/miembros/delete/{nombre}/{edad}/{tipo}")
	public void deleteMiembros(@PathVariable(value = "nombre") String nombre,@PathVariable(value = "edad") int edad,@PathVariable(value = "tipo") String tipo)
	{
		Persona persona = builder.nombre(nombre).edad(edad).tipoMiembro(tipo).build();
		
		 endpoint.delete(lista.deletePersona(persona) + ": " + persona.getNombre());
		 logger.info("llamada a borrar miembro.");
		 counterDelete.increment();
		
	}
	
	
	@PostMapping(path = "/miembros/delete")
	public void deleteMiembros(@RequestBody Persona persona)
	{

		 endpoint.delete(lista.deletePersona(persona) + ": " + persona.getNombre());
		 logger.info("llamada a borrar miembro.");
		 counterDelete.increment();
		
	}
	
	
	@GetMapping(path = "/miembros")
	public String showMiembros()
	{
		List<String> listaTmp = new ArrayList<>();
		
		
		for (int i = 0; i < lista.getMiembros().size(); i++) {
			listaTmp.add(lista.getMiembros().get(i).toString());
			
		}

		
		String json = new Gson().toJson(listaTmp);
		
		counter.increment();
		return json;
		
	}
	

}
