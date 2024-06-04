package org.iesvdm.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAOImpl;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dao.PedidoDAOImpl;
import org.iesvdm.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ClienteService {

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private PedidoDAOImpl pedidoDAO;

	@Autowired
	private ComercialDAOImpl comercialDAO;
	
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







	//METODOS RELACIONADOS CON EL USO DE PEDIDOS EN Cliente
	public List<Pedido> mostrarPedidos(long id){
		return pedidoDAO.getAllByClienteId((int) id);
	}

	//METODO PARA OBTENER UNA LISTA DE LOS COMERCIALES DE UN CLIENTE EN BASE A SUS PEDIDOS
	public List<ComercialDTO> listaDeComerciales(int idCliente){
		List<Comercial> comerciales = comercialDAO.getAll();
		List<Pedido> pedidos = pedidoDAO.getAllByClienteId(idCliente);
		List<ComercialDTO> ordenacionPedidos = new ArrayList<>();

		for(Pedido pedido : pedidos){
			for(Comercial comercial : comerciales){
				//Creamos un ClienteDTO usando Mapper
				ComercialDTO comercialDTO = ComercialMapper.INSTANCE.comercialAComercialDTO(comercial);
				boolean meterEnLista = false;
				boolean yaSeHaMetido = false;

				//Si el idCliente de pedido y el Cliente tienen el mismo código, hay que meterlo en la lista
				if (pedido.getIdComercial() == comercial.getId()){
					//Miramos si la lista esta vacía
					if (ordenacionPedidos.isEmpty()){
						comercialDTO.setPedidosPorCliente(1);
						ordenacionPedidos.add(comercialDTO);
					} else {
						//Recorremos la nueva lista y comprobamos si el cliente ya estaba en la lista
						for (ComercialDTO comercialDTO1 : ordenacionPedidos){
							//Si ya está en lista le aumentamos la cantidad de Pedidos
							if (comercialDTO1.getId() == comercial.getId()){
								comercialDTO1.setPedidosPorCliente(comercialDTO1.getPedidosPorCliente() + 1);
								//COMO EL BUCLE SIGUE CORRIENDO DEBEMOS CONTROLAR QUE NO SE VUELVA A METER AL USUARIO POR SEGUNDA VEZ
								yaSeHaMetido = true;
							} else {
								meterEnLista = true;
							}
						}
					}
					//CONTROLAMOS QUE HAYA QUE METERLO Y QUE NO SE HAYA METIDO YA
					if (meterEnLista && !yaSeHaMetido){
						comercialDTO.setPedidosPorCliente(1);
						ordenacionPedidos.add(comercialDTO);
					}
				}
			}
		}

		Collections.sort(ordenacionPedidos);
		return ordenacionPedidos;
	}


	//METODO PARA OBTENER LAS FECHAS DE LOS DISTINTOS PEDIDOS
	public void insertarFechaPedidos(ClienteDTO clienteDTO){
		List<Pedido> listaPedidos = mostrarPedidos(clienteDTO.getId());

		LocalDate fechaActual = LocalDate.now();
		Date hoy = Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant());

		//COMENZAMOS PREPARANDO LAS FECHAS QUE VAMOS A COMPARAR A CONTINUACIÓN
		Calendar calendarTrimestre = Calendar.getInstance();
		calendarTrimestre.setTime(hoy);
		calendarTrimestre.add(Calendar.MONTH, -3);

		Calendar calendarSemestre = Calendar.getInstance();
		calendarSemestre.setTime(hoy);
		calendarSemestre.add(Calendar.MONTH, -6);

		Calendar calendarAnual = Calendar.getInstance();
		calendarAnual.setTime(hoy);
		calendarAnual.add(Calendar.YEAR, -1);

		Calendar calendarLustro = Calendar.getInstance();
		calendarLustro.setTime(hoy);
		calendarLustro.add(Calendar.YEAR, -5);

		for (Pedido pedido : listaPedidos){
			Date fechaPedido = pedido.getFecha();

			if (fechaPedido.after(calendarLustro.getTime()) && fechaPedido.before(calendarAnual.getTime())){
				clienteDTO.setPedidosUltimoLustro(clienteDTO.getPedidosUltimoLustro() + 1);

			} else if (fechaPedido.after(calendarAnual.getTime()) && fechaPedido.before(calendarSemestre.getTime())){
				clienteDTO.setPedidosUltimoYear(clienteDTO.getPedidosUltimoYear() + 1);

			} else if (fechaPedido.after(calendarSemestre.getTime()) && fechaPedido.before(calendarTrimestre.getTime())){
				clienteDTO.setPedidosUltimoSemestre(clienteDTO.getPedidosUltimoSemestre() + 1);

			} else if (fechaPedido.after(calendarTrimestre.getTime())){
				clienteDTO.setPedidosUltimoTrimestre(clienteDTO.getPedidosUltimoTrimestre() + 1);
			}
		}
	}
}