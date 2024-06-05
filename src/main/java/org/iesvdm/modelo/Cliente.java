package org.iesvdm.modelo;

import jakarta.validation.constraints.*;
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

	@NotBlank(message = "{cliente.error.nombre}")
	@Size(max = 30, message = "{cliente.error.nombre.size.max}")
	private String nombre;

	@NotBlank(message = "{cliente.error.apellido}")
	@Size(max = 30, message = "{cliente.error.apellido.size.max}")
	private String apellido1;

	private String apellido2;

	@NotBlank(message = "{cliente.error.ciudad}")
	@Size(max = 50, message = "{cliente.error.ciudad.size.max}")
	private String ciudad;

	@Min(value = 100, message = "{cliente.error.categoria.size.min}")
	@Max(value = 1000, message = "{cliente.error.categoria.size.max}")
	private int categoria;


	@Email(message = "{cliente.error.correo}",
	regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
	private String correo;


	public Cliente(){
	}
}