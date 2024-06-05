package org.iesvdm;

import java.math.BigDecimal;
import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringBootWebMvcJdbcVentasApplication implements CommandLineRunner{


	//ARREGLAR ERROR
	// Error resolving template [comerciales],
	// template might not exist or might not be accessible by any of the configured Template Resolvers


	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private ComercialDAO comercialDAO;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebMvcJdbcVentasApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("*******************************");
		log.info("*Prueba de arranque ClienteDAO*");
		log.info("*******************************");
		
		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));
		
		int idCliente = 1;
		Optional<Cliente> cliente = clienteDAO.find(idCliente);
		
		if (cliente.isPresent()) {
			log.info("Cliente {}: {}", idCliente, cliente.get());
			
			String nombreOld = cliente.get().getNombre();
			
			cliente.get().setNombre("Jose M");
			
			clienteDAO.update(cliente.get());
			
			cliente = clienteDAO.find(idCliente);
			
			log.info("Cliente {}: {}", idCliente, cliente.get());
			
			//Volvemos a cargar el nombre antiguo..
			cliente.get().setNombre(nombreOld);
			clienteDAO.update(cliente.get());
			
		}
		
		// Como es un cliente nuevo a persistir, id a 0
		Cliente clienteNew = new Cliente(0, "Jose M", "Martín", null, "Málaga", 100, "");
		
		//create actualiza el id
		clienteDAO.create(clienteNew);
		
		log.info("Cliente nuevo con id = {}", clienteNew.getId());
		
		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));
		
		//borrando por el id obtenido de create
		clienteDAO.delete(clienteNew.getId());
		
		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));
		
		log.info("************************************");
		log.info("*FIN: Prueba de arranque ClienteDAO*");
		log.info("************************************");




		log.info("*******************************");
		log.info("*Prueba de arranque ComercialDao*");
		log.info("*******************************");

		comercialDAO.getAll().forEach(c -> log.info("comercial: {}", c));

		int idComercial = 1;
		Optional<Comercial> comercial = comercialDAO.find(idComercial);

		if (comercial.isPresent()) {
			log.info("Comercial {}: {}", idComercial, comercial.get());

			String nombreOld = comercial.get().getNombre();

			comercial.get().setNombre("Jose M");

			comercialDAO.update(comercial.get());

			comercial = comercialDAO.find(idComercial);

			log.info("Comercial {}: {}", idComercial, comercial.get());

			//Volvemos a cargar el nombre antiguo..
			comercial.get().setNombre(nombreOld);
			comercialDAO.update(comercial.get());

		}

		// Como es un comercial nuevo a persistir, id a 1
		Comercial comercialNew = new Comercial(21, "Mario M", "Mario", "Bros", BigDecimal.valueOf(0.4));

		//create actualiza el id
		comercialDAO.create(comercialNew);

		log.info("Cliente nuevo con id = {}", comercialNew.getId());

		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		//borrando por el id obtenido de create
		comercialDAO.delete(comercialNew.getId());
		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));


		log.info("************************************");
		log.info("*FIN: Prueba de arranque ComercialDAO*");
		log.info("************************************");
	}
}