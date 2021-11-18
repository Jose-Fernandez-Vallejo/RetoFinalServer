package com.example.microservicioRetoFinalServer.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import starterRetoFinal.ListaMiembros;
import starterRetoFinal.Persona;
import starterRetoFinal.PersonaBuilder;

@Component
@Endpoint(id = "miembros")
public class ListaMiembrosEndpoint {

	
	List<String> lista = new ArrayList<String>();
	PersonaBuilder builder = new PersonaBuilder();

	
	
	@ReadOperation
	public List<String> status()
	{
		return lista;
	}
	
	@WriteOperation
	public void write(@Selector String add)
	{
		lista.add(add);

	}
	
	@DeleteOperation
	public void delete(@Selector String delete)
	{
		lista.add(delete);
	}
	
}
