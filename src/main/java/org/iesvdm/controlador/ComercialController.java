package org.iesvdm.controlador;

import jakarta.validation.Valid;
import org.iesvdm.modelo.*;
import org.iesvdm.service.ComercialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ComercialController {

	private final ComercialService comercialService;

	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired
	//@Autowired
	public ComercialController(ComercialService comercialService) {
		this.comercialService = comercialService;
	}
	
	//@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping("/comerciales") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {
		
		List<Comercial> listaComerciales =  comercialService.listAll();
		model.addAttribute("listaComerciales", listaComerciales);
				
		return "comerciales";
	}

	//Editar un Comercial
	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		Comercial comercial = comercialService.one(id);
		model.addAttribute("comercial", comercial);

		return "editar-comercial";
	}

	@PostMapping("/comerciales/editar/{id}")
	public String submitEditar(@Valid @ModelAttribute("comercial") Comercial comercial, BindingResult bindingResult, Model model) {
		//En caso de que la validación falle
		if (bindingResult.hasErrors()) {
			model.addAttribute("comercial", comercial);

			return "editar-comercial";
		}

		comercialService.replaceComercial(comercial);

		List<Comercial> listaComercial =  comercialService.listAll();
		model.addAttribute("listaComerciales", listaComercial);

		//return "comerciales";
		return "redirect:/comerciales";
	}

	//Mostrar detalles del Comercial
	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {
		//Conseguimos el Comercial en específico
		Comercial comercial = comercialService.one(id);
		model.addAttribute("comercial", comercial);

		//Conseguimos la Lista de Pedidos a la que está asociado el Comercial
		List<Pedido> listaPedido = comercialService.mostrarPedidos(id);

		//Usamos PedidoDTO para sacar el nombre de los Clientes de los Pedidos
		List<PedidoDTO> listaPedidoNombreClientes = new ArrayList<>();

		//Controlamos que haya más de un elemento en la lista, para que no pete
		if (!listaPedido.isEmpty()){
			double totalMaximo = 0;
			double totalMinimo = listaPedido.getFirst().getTotal();


			//Comprobamos cuál es el Maximo y el Minimo de la Lista
			for (Pedido p : listaPedido) {
				if (p.getTotal() > totalMaximo) {
					totalMaximo = p.getTotal();
				}
				if (p.getTotal()<totalMinimo){
					totalMinimo = p.getTotal();
				}
			}

			//Hacemos un bucle que nos ayude a rellenar la Lista de PedidoDTO
			for (Pedido pedido : listaPedido) {
				//USAMOS MAPPER PARA CREAR PEDIDO DTO
				PedidoDTO pedidoDTO = PedidoMapper.INSTANCE.pedidoAPedidoDto(pedido);
				pedidoDTO.setNombreCliente(comercialService.sacarNombreClientePedido(pedido.getIdCliente()));

				//ESTO YA NO HACE FALTA HACERLO GRACIAS AL MAPPER
					//PedidoDTO pedidoDTO = new PedidoDTO();
					//pedidoDTO.setId(pedido.getId());
					//pedidoDTO.setFecha(pedido.getFecha());
					//pedidoDTO.setIdComercial(pedido.getIdComercial());
					//pedidoDTO.setIdCliente(pedido.getIdCliente());
					//pedidoDTO.setTotal(pedido.getTotal());

				if (listaPedido.size() > 1) {
					if(pedido.getTotal()==totalMaximo){
						pedidoDTO.setTotalEsMaximo(true);
					}
					if(pedido.getTotal()==totalMinimo){
						pedidoDTO.setTotalEsMinimo(true);
					}
				} else {
					pedidoDTO.setTotalEsMaximo(true);
				}

				listaPedidoNombreClientes.add(pedidoDTO);
			}
		}

		model.addAttribute("listaPedido", listaPedidoNombreClientes);
		model.addAttribute("totalPedidos", comercialService.totalPedidos(id));
		model.addAttribute("mediaPedidos", comercialService.mediaTotalPedidos(id));
		model.addAttribute("listaClientes", comercialService.listaDeCLientes(id));

		return "detalle-comercial";
	}

	//Crear nuevo Comercial
	@GetMapping("/comerciales/crear")
	public String crear(Model model) {

		Comercial comercial = new Comercial();
		model.addAttribute("comercial", comercial);

		return "crear-comercial";
	}

	@PostMapping("/comerciales/crear")
	public String submitCrear(@Valid @ModelAttribute("comercial") Comercial comercial, BindingResult bindingResult, Model model) {
		//En caso de que la validación falle
		if (bindingResult.hasErrors()) {
			model.addAttribute("comercial", comercial);

			return "crear-comercial";
		}

		comercialService.crearComercial(comercial);

		List<Comercial> listaComercial =  comercialService.listAll();
		model.addAttribute("listaComerciales", listaComercial);

		//return "comerciales";
		return "redirect:/comerciales";
	}

	//Borrar Comercial
	@PostMapping("/comerciales/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		comercialService.deleteComercial(id);

		return new RedirectView("/comerciales");
	}
}