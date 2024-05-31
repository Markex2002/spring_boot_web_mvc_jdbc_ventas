package org.iesvdm.modelo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PedidoMapper {
    
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);
    
    @Mapping(target = "id")
    PedidoDTO pedidoAPedidoDto(Pedido pedido);

}