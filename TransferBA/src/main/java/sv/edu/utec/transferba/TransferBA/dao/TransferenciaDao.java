package sv.edu.utec.transferba.TransferBA.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.edu.utec.transferba.TransferBA.entidades.Transferencia;
/**
 *
 * @author Carlos
 */
public interface TransferenciaDao extends JpaRepository<Transferencia, Integer>{
    
    @Query("SELECT t FROM Transferencia t WHERE t.referencia = :referencia")
    Transferencia buscarTransferenciaPorIdReferencia(String referencia) throws Exception;
    
    @Query("SELECT t FROM Transferencia t WHERE t.cuenta.cliente.idCliente = :idCliente")
    List<Transferencia> listarTransferenciasSalientesPorIdCliente(Integer idCliente) throws Exception;
    
    @Query("SELECT t FROM Transferencia t WHERE t.cuentaBeneficiaria IN (SELECT c.numeroCuenta FROM Cuenta c WHERE c.cliente.idCliente = :idCliente)")
    List<Transferencia> listarTransferenciasEntrantesPorIdCliente(Integer idCliente) throws Exception;
}
