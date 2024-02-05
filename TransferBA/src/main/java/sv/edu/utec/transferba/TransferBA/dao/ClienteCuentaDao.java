package sv.edu.utec.transferba.TransferBA.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.edu.utec.transferba.TransferBA.entidades.ClienteCuenta;

/**
 *
 * @author Carlos
 */
public interface ClienteCuentaDao extends JpaRepository<ClienteCuenta, Integer>{
    
    @Query("SELECT c FROM ClienteCuenta c WHERE c.cliente.idCliente = :idCliente")
    List<ClienteCuenta> buscarCuentaPorIdCliente(Integer idCliente) throws Exception;
    
    @Query("SELECT c FROM ClienteCuenta c WHERE c.cliente.idCliente = :idCliente")
    List<ClienteCuenta> buscarCuentasDeCliente(Integer idCliente) throws Exception;
    
    @Query("SELECT c FROM ClienteCuenta c WHERE c.cuenta.numeroCuenta = :numeroCuenta")
    ClienteCuenta buscarClientePorNumeroCuenta(String numeroCuenta) throws Exception;
}
