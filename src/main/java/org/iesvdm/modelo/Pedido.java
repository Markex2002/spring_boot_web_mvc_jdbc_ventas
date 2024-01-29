package org.iesvdm.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

//La anotación @Data de lombok proporcionará el código de:
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
public class Pedido {

	private long id;
	private Double total;
	private Date fecha;
	private int idCliente;
	private int idComercial;

	public Pedido(){

	}
}