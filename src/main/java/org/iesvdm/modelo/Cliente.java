package org.iesvdm.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mapstruct.Mapper;

//La anotación @Data de lombok proporcionará el código de:
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
@Mapper
public class Cliente {
	
	private long id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String ciudad;
	private int categoria;


	public Cliente(){
	}
}