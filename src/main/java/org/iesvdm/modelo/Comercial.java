package org.iesvdm.modelo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mapstruct.Mapper;

@Data
@AllArgsConstructor
@Mapper
public class Comercial {

	private int id;

	@NotBlank(message = "Por Favor, introduzca su nombre")
	@Size(max = 30, message = "nombre con Maximo de 30 Caracteres")
	private String nombre;

	@NotBlank(message = "Por Favor, introduzca su apellido")
	@Size(max = 30, message = "apellido con Maximo de 30 Caracteres")
	private String apellido1;

	private String apellido2;

//	@DecimalMax()
//	@DecimalMin()
	private float comision;

	public Comercial(){

	}
}