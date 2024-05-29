package org.iesvdm.controlador;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDTO;
import org.iesvdm.service.ComercialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public RedirectView submitEditar(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.replaceComercial(comercial);

		return new RedirectView("/comerciales");
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
				PedidoDTO pedidoDTO = new PedidoDTO();
				pedidoDTO.setId(pedido.getId());
				pedidoDTO.setFecha(pedido.getFecha());
				pedidoDTO.setIdComercial(pedido.getIdComercial());
				pedidoDTO.setIdCliente(pedido.getIdCliente());
				pedidoDTO.setTotal(pedido.getTotal());
				pedidoDTO.setNombreCliente(comercialService.sacarNombreClientePedido(pedido.getIdCliente()));

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
	public RedirectView submitCrear(@ModelAttribute("comercial") Comercial comercial) {

		comercialService.crearComercial(comercial);

		return new RedirectView("/comerciales") ;
	}

	//Borrar Comercial
	@PostMapping("/comerciales/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		comercialService.deleteComercial(id);

		return new RedirectView("/comerciales");
	}
}