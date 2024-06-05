package org.iesvdm.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

//La anotación @Data de lombok proporcionará el código de:
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClienteDTO implements Comparable<ClienteDTO> {

	@EqualsAndHashCode.Include
	private long id;

	private String nombre;
	private String apellido1;
	private String apellido2;
	private String ciudad;
	private int categoria;
	private String correo;

	private int pedidosPorComercial = 0;

	private int pedidosUltimoTrimestre = 0;
	private int pedidosUltimoSemestre = 0;
	private int pedidosUltimoYear = 0;
	private int pedidosUltimoLustro = 0;

	public ClienteDTO(){
	}

	@Override
	public int compareTo(ClienteDTO o) {
		return o.pedidosPorComercial - this.pedidosPorComercial;
	}
}