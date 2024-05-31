package org.iesvdm.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.dao.ClienteDAOImpl;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAOImpl;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.ClienteDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ComercialService {
	private final ComercialDAO comercialDAO;

	@Autowired
	private PedidoDAOImpl pedidoDAO;

	@Autowired
	private ClienteDAOImpl clienteDAO;

	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ComercialService(ComercialDAO comercialDAO) {
		this.comercialDAO = comercialDAO;
	}
	
	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	//Método que comprueba si el comercial que hemos buscado existe
	public Comercial one(Integer id) {
		Optional<Comercial> optCom = comercialDAO.find(id);
        return optCom.orElse(null);
	}

	//Editar un comercial existente
	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	//Crear Comercial
	public void crearComercial(Comercial comercial){
		comercialDAO.create(comercial);
		log.info("ID comercial creado {}", comercial.getId());
	}

	//Borrar comercial
	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}






	//METODOS RELACIONADOS CON EL USO DE PEDIDOS EN COMERCIAL
	public List<Pedido> mostrarPedidos(int id){
		return pedidoDAO.getAllByComercialId(id);
	}

	//Método para sacar la media de el Total de los Pedidos del Comerciante
	public double mediaTotalPedidos(int id){
		List<Pedido> pedidos = pedidoDAO.getAllByComercialId(id);
		double total = 0;
		double media = 0;
		if (!pedidos.isEmpty()){
			for(Pedido pedido : pedidos){
				total += pedido.getTotal();
			}
			media = total/pedidos.size();
		}
		return media;
	}

	//Método para sacar el Total de los Pedidos del Comerciante
	public double totalPedidos(int id){
		List<Pedido> pedidos = pedidoDAO.getAllByComercialId(id);
		double total = 0;
		for(Pedido pedido : pedidos){
			total += pedido.getTotal();
		}
		return total;
	}

	//Método para sacar el nombre del Cliente de un Pedido en especifico
	public String sacarNombreClientePedido(int id){
		return comercialDAO.sacarNombrePorID(id);
	}


	//METODO PARA OBTENER UNA LISTA DE LOS CLIENTES DE UN COMERCIAL EN BASE A SUS PEDIDOS
	public Set<ClienteDTO> listaDeCLientes(int idComercial){
		List<Cliente> clientes = clienteDAO.getAll();
		List<Pedido> pedidos = pedidoDAO.getAllByComercialId(idComercial);
		List<ClienteDTO> listadoDeClientesConPedidos = new ArrayList<>();
		Set<ClienteDTO> ordenacionPedidos;
		ordenacionPedidos = new TreeSet<>((o1, o2) ->  o2.getPedidosPorComercial() - o1.getPedidosPorComercial());

		for(Pedido pedido : pedidos){
			for(Cliente cliente : clientes){
				ClienteDTO clienteDTO = new ClienteDTO();
				clienteDTO.setId(cliente.getId());
				clienteDTO.setNombre(cliente.getNombre());
				clienteDTO.setApellido1(cliente.getApellido1());
				clienteDTO.setApellido2(cliente.getApellido2());
				clienteDTO.setCategoria(cliente.getCategoria());
				clienteDTO.setCiudad(cliente.getCiudad());


				boolean meterEnLista = false;

				//Si el pedido y el Cliente tienen el mismo codigo, hay que meterlo en la lista
				if (pedido.getIdCliente() == cliente.getId()){
					if (ordenacionPedidos.isEmpty()){
						clienteDTO.setPedidosPorComercial(1);
						ordenacionPedidos.add(clienteDTO);
					} else {
						//Comprobamos si el cliente ya estaba en la lista
						for (ClienteDTO clienteDTO1 : ordenacionPedidos){
							if (clienteDTO1.getId()==cliente.getId()){
								clienteDTO1.setPedidosPorComercial(clienteDTO1.getPedidosPorComercial() + 1);
							} else {
								meterEnLista = true;
							}
						}
					}
					if (meterEnLista){
						clienteDTO.setPedidosPorComercial(1);
						ordenacionPedidos.add(clienteDTO);
					}
				}
			}
		}

		return ordenacionPedidos;
	}
}