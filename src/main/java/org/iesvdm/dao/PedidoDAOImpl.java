package org.iesvdm.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.modelo.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
@Repository
//Utilizo lombok para generar el constructor
@AllArgsConstructor
public class PedidoDAOImpl implements PedidoDAO {

	//JdbcTemplate se inyecta por el constructor de la clase automáticamente
	//
	@Autowired
	private JdbcTemplate jdbcTemplate;


	public void create_CON_RECARGA_DE_ID_POR_PS(Pedido pedido) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection
					.prepareStatement("""
                               INSERT INTO pedido
                               (total, fecha, id_cliente, id_comercial)
                               VALUE
                               (?, ?, ?, ?)
                               """, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			ps.setDouble(idx++, pedido.getTotal());
			ps.setDate(idx++, (Date) pedido.getFecha());
			ps.setInt(idx++, pedido.getIdCliente());
			ps.setInt(idx++, pedido.getIdComercial());
			return ps;
		}, keyHolder);
		//SE ACTUALIZA EL ID AUTO_INCREMENT DE MYSQL EN EL BEAN DE CLIENTE MEDIANTE EL KEYHOLDER
		pedido.setId(keyHolder.getKey().intValue());
	}


	
	@Override
	public void create(Pedido pedido) {
		// TODO Auto-generated method stub


		//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO pedido (id, total, fecha, id_cliente, id_comercial)\s
							VALUES  (     ?,         ?,         ?,       ?,         ?)
									""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setLong(idx++, pedido.getId());
			ps.setDouble(idx++, pedido.getTotal());
			ps.setDate(idx++, (Date) pedido.getFecha());
			ps.setInt(idx++, pedido.getIdCliente());
			ps.setInt(idx, pedido.getIdComercial());
			return ps;
		},keyHolder);

		pedido.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

		//Sin recuperación de id generado
		//		int rows = jdbcTemplate.update(sqlInsert,
		//							cliente.getNombre(),
		//							cliente.getApellido1(),
		//							cliente.getApellido2(),
		//							cliente.getCiudad(),
		//							cliente.getCategoria()
		//					);

		log.info("Insertados {} registros.", rows);
	}



	@Override
	public List<Pedido> getAll() {
		
		List<Pedido> listPedido = jdbcTemplate.query(
                "SELECT * FROM pedido",
                (rs, rowNum) -> new Pedido(rs.getInt("id"),
                							  rs.getDouble("total"),
                							  rs.getDate("fecha"),
                							  rs.getInt("id_cliente"),
                							  rs.getInt("id_comercial"))
        );
		
		log.info("Devueltos {} registros.", listPedido.size());
        return listPedido;
	}


	//Conseguir una lista de pedidos segun el comercial asignado
	@Override
	public List<Pedido> getAllByComercialId(int id) {
		List<Pedido> listPedido = jdbcTemplate.query(
				"SELECT * FROM pedido WHERE pedido.id_comercial = ?",
				(rs, rowNum) -> new Pedido(rs.getInt("id"),
						rs.getDouble("total"),
						rs.getDate("fecha"),
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial")), id
		);

		log.info("Devueltos {} registros.", listPedido.size());
		return listPedido;
	}


	@Override
	public Optional<Pedido> find(int id) {
		// TODO Auto-generated method stub
		Pedido fab =  jdbcTemplate
				.queryForObject("SELECT * FROM pedido WHERE id = ?"
						, (rs, rowNum) -> new Pedido(rs.getInt("id"),
								rs.getDouble("total"),
								rs.getDate("fecha"),
								rs.getInt("id_cliente"),
								rs.getInt("id_comercial"))
						, id
				);

		if (fab != null) {
			return Optional.of(fab);}
		else {
			log.info("Pedido no encontrado.");
			return Optional.empty(); }
	}

	@Override
	public void update(Pedido pedido) {
		// TODO Auto-generated method stub
		int rows = jdbcTemplate.update("""
										UPDATE pedido SET
														total = ?,
														fecha = ?,
														id_cliente = ?,
														id_comercial = ?
												WHERE id = ?
										""", pedido.getTotal()
										, pedido.getFecha()
										, pedido.getIdCliente()
										, pedido.getIdComercial()
										, pedido.getId());

		log.info("Update de Comercial con {} registros actualizados.", rows);
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);

		log.info("Delete de Comercial con {} registros eliminados.", rows);
	}
}