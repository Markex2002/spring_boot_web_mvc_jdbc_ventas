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

	@NotBlank(message = "{comercial.error.nombre}")
	@Size(max = 30, message = "{comercial.error.nombre.size.max}")
	private String nombre;

	@NotBlank(message = "{comercial.error.apellido}")
	@Size(max = 30, message = "{comercial.error.apellido.size.max}")
	private String apellido1;

	private String apellido2;


	@DecimalMin(value = "0.276", message = "{comercial.error.comision.size.min}")
	@DecimalMax(value = "0.946", message = "{comercial.error.comision.size.max}")
	private BigDecimal comision;

	public Comercial(){
	}



}