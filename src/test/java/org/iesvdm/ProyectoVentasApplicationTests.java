package org.iesvdm;

import org.iesvdm.dao.ClienteDAOImpl;
import org.iesvdm.modelo.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootTest
class ProyectoVentasApplicationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ClienteDAOImpl clienteDAO;
    @Test
    void test_recargar_id_auto_increment_por_ps() {


        Cliente cliente = new Cliente(0
                , "José M."
                , "Martín"
                , "Tejero"
                , "Málaga"
                , 1);


        this.clienteDAO.create_CON_RECARGA_DE_ID_POR_PS(cliente);
        Assertions.assertTrue(cliente.getId()>0);
        System.out.println("ID AUTO_INCREMENT:" + cliente.getId());


    }
    @Test
    void test_recargar_id_auto_increment_por_simplejdbcinsert() {

        Cliente cliente = new Cliente(0
                , "José M."
                , "Martín"
                , "Tejero"
                , "Málaga"
                , 1);


        this.clienteDAO.create_CON_RECARGA_DE_ID_POR_SIMPLEJDBCINSERT(cliente);
        Assertions.assertTrue(cliente.getId()>0);
        System.out.println("ID AUTO_INCREMENT:" + cliente.getId());
    }
}