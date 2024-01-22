package org.iesvdm.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Comercial;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ComercialService {

	private ComercialDAO comercialDAO;

	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ComercialService(ComercialDAO comercialDAO) {
		this.comercialDAO = comercialDAO;
	}
	
	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	//Método que comprueba si el cliente que hemos buscado existe
	public Comercial one(Integer id) {
		Optional<Comercial> optCom = comercialDAO.find(id);
        return optCom.orElse(null);
	}

	//Editar un cliente existente
	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}


	//Crear Comercial
	public void crearComercial(Comercial comercial){
		comercialDAO.create(comercial);
		log.info("ID comercial creado {}", comercial.getId());
	}

	//Borrar Cliente
	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}
}

