package org.iesvdm.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComercialDTO implements Comparable<ComercialDTO>{
    //Atributos
    private int id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private float comision;


    private int pedidosPorCliente;

    public ComercialDTO(){}

    @Override
    public int compareTo(ComercialDTO o) {
        return o.pedidosPorCliente - this.pedidosPorCliente;
    }
}