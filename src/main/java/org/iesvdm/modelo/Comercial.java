package org.iesvdm.modelo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

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


	@DecimalMin(value = "0.276", inclusive = false , message = "Comision minima de 0.276")
	@DecimalMax(value = "0.946", inclusive = false , message = "Comision máxima de 0.946")
	private float comision;

	public Comercial(){
	}

	//public float getComision() {
	//	Double d = (Double) get("amd1");
	//	return BigDecimal.valueOf(d);
	//}



}