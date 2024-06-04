package org.iesvdm.controlador;

import java.util.List;

import jakarta.validation.Valid;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.ClienteDTO;
import org.iesvdm.modelo.ClienteMapper;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ClienteController {


	private final ClienteService clienteService;
	
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	//@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping({"/clientes", "/clients"}) //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {
		List<Cliente> listaClientes =  clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);

		return "clientes";
	}

	//Editar un Cliente
	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		Cliente cliente = clienteService.one(id);
		model.addAttribute("cliente", cliente);

		return "editar-cliente";
	}
	@PostMapping("/clientes/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("cliente") Cliente cliente) {
		clienteService.replaceCliente(cliente);

		return new RedirectView("/clientes");
	}

	//Mostrar detalles del cliente
	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {

		Cliente cliente = clienteService.one(id);
		ClienteDTO clienteDTO = ClienteMapper.INSTANCE.clienteAClienteDTO(cliente);
		model.addAttribute("cliente", cliente);

		//Conseguimos la Lista de Pedidos a la que está asociado el Comercial
		List<Pedido> listaPedido = clienteService.mostrarPedidos(id);
		model.addAttribute("listaPedido", listaPedido);
		model.addAttribute("listaComerciales", clienteService.listaDeComerciales(id));

		//CONTROLAMOS LAS FECHAS EN LAS QUE SE REALIZARON EL PEDIDO
		clienteService.insertarFechaPedidos(clienteDTO);
		model.addAttribute("pedidosLustro", clienteDTO.getPedidosUltimoLustro());
		model.addAttribute("pedidosYear", clienteDTO.getPedidosUltimoYear());
		model.addAttribute("pedidosSemestre", clienteDTO.getPedidosUltimoSemestre());
		model.addAttribute("pedidosTrimestre", clienteDTO.getPedidosUltimoTrimestre());

		return "detalle-cliente";
	}

	//Crear nuevo Cliente
	@GetMapping("/clientes/crear")
	public String crear(Model model) {

		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);

		return "crear-cliente";
	}

	@PostMapping("/clientes/crear")
	public String submitCrear(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult bindingResult, Model model) {
		//En caso de que la validación falle
		if (bindingResult.hasErrors()) {

			List<Cliente> listaClientes =  clienteService.listAll();
			model.addAttribute("listaClientes", listaClientes);

			model.addAttribute("cliente", cliente);

			return "crear-cliente";
		}

		clienteService.crearCliente(cliente);

		//return "redirect:/clientes" ;
		return "clientes";
	}

	//Borrar Cliente
	@PostMapping("/clientes/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		clienteService.deleteCliente(id);

		return new RedirectView("/clientes");
	}
}