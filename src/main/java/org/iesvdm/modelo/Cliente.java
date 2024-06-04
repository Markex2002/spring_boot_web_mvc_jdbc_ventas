package org.iesvdm.modelo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

	@NotBlank(message = "Por Favor, Introduzca su nombre")
	@Size(max = 30, message = "Nombre con Maximo de 30 Caracteres")
	private String nombre;

	@NotBlank(message = "Por Favor, Introduzca su apellido")
	@Size(max = 30, message = "apellido con Maximo de 30 Caracteres")
	private String apellido1;

	private String apellido2;

	@NotBlank(message = "Por Favor, introduzca el nombre de su ciudad")
	@Size(max = 50, message = "ciudad con Maximo de 30 Caracteres")
	private String ciudad;

	@Min(value = 100, message = "Categoria debe ser al menos de 100")
	@Max(value = 1000, message = "Categoria debe ser menor de 100")
	private int categoria;


	public Cliente(){
	}
}