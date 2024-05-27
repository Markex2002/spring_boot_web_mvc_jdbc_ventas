package org.iesvdm.dao;

import org.iesvdm.modelo.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoDAO {
	public void create(Pedido pedido);
	
	public List<Pedido> getAll();
	public List<Pedido> getAllByComercialId(int id);

	public Optional<Pedido>  find(int id);
	
	public void update(Pedido pedido);
	
	public void delete(long id);
	
}
