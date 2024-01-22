package org.iesvdm.service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ClienteService {
	
	private ClienteDAO clienteDAO;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ClienteService(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	
	public List<Cliente> listAll() {
		return clienteDAO.getAll();
	}


	//Método que comprueba si el cliente que hemos buscado existe
	public Cliente one(Integer id) {
		Optional<Cliente> optCli = clienteDAO.find(id);
        return optCli.orElse(null);
	}

	//Editar un cliente existente
	public void replaceCliente(Cliente cliente) {

		clienteDAO.update(cliente);
	}


	//Crear Cliente
	public void crearCliente(Cliente cliente){
		clienteDAO.create(cliente);
		log.info("ID cliente creado {}", cliente.getId());
	}

	//Borrar Cliente
	public void deleteCliente(int id) {
		clienteDAO.delete(id);
	}
}