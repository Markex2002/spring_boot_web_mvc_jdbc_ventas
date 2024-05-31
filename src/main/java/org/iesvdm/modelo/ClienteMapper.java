package org.iesvdm.modelo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

        @Mapping(source = "id", target = "id")
    ClienteDTO clienteAClienteDTO(Cliente cliente);
}