package org.iesvdm.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.dao.ClienteDAOImpl;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAOImpl;
import org.iesvdm.modelo.*;
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
	public List<ClienteDTO> listaDeCLientes(int idComercial){
		//TREESET permite ordenar, pero por algun motivo no mete algunos pedidos
		//PriorityQueu Funciona, aunque no Ordena.
		//ArrayList, implementando un Comparable y haciendo Sort al final, es la opcion mas sencilla y funciona.

		List<Cliente> clientes = clienteDAO.getAll();
		List<Pedido> pedidos = pedidoDAO.getAllByComercialId(idComercial);
		List<ClienteDTO> ordenacionPedidos = new ArrayList<>();
		//Set<ClienteDTO> ordenacionPedidos;
		//ordenacionPedidos = new TreeSet<>((o1, o2) ->  o2.getPedidosPorComercial() - o1.getPedidosPorComercial());
		//PriorityQueue<ClienteDTO> ordenacionPedidos = new PriorityQueue<>((o1, o2) -> o2.getPedidosPorComercial() - o1.getPedidosPorComercial());

		for(Pedido pedido : pedidos){
			for(Cliente cliente : clientes){
				//Creamos un ClienteDTO usando Mapper
				ClienteDTO clienteDTO = ClienteMapper.INSTANCE.clienteAClienteDTO(cliente);
				boolean meterEnLista = false;
				boolean yaSeHaMetido = false;

				//Si el idCliente de pedido y el Cliente tienen el mismo código, hay que meterlo en la lista
				if (pedido.getIdCliente() == cliente.getId()){
					//Miramos si la lista esta vacía
					if (ordenacionPedidos.isEmpty()){
						clienteDTO.setPedidosPorComercial(1);
						ordenacionPedidos.add(clienteDTO);
					} else {
						//Recorremos la nueva lista y comprobamos si el cliente ya estaba en la lista
						for (ClienteDTO clienteDTO1 : ordenacionPedidos){
							//Si ya está en lista le aumentamos la cantidad de Pedidos
							if (clienteDTO1.getId()==cliente.getId()){
								clienteDTO1.setPedidosPorComercial(clienteDTO1.getPedidosPorComercial() + 1);
								//COMO EL BUCLE SIGUE CORRIENDO DEBEMOS CONTROLAR QUE NO SE VUELVA A METER AL USUARIO POR SEGUNDA VEZ
								yaSeHaMetido = true;
							} else {
								meterEnLista = true;
							}
						}
					}
					//CONTROLAMOS QUE HAYA QUE METERLO Y QUE NO SE HAYA METIDO YA
					if (meterEnLista && !yaSeHaMetido){
						clienteDTO.setPedidosPorComercial(1);
						ordenacionPedidos.add(clienteDTO);
					}
				}
			}
		}

		Collections.sort(ordenacionPedidos);
		return ordenacionPedidos;
	}
}