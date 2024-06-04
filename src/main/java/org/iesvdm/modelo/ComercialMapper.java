package org.iesvdm.modelo;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ComercialMapper {

    ComercialMapper INSTANCE = Mappers.getMapper(ComercialMapper.class);


    @Mapping(source = "id", target = "id")
    ComercialDTO comercialAComercialDTO(Comercial comercial);
}