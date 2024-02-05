package sv.edu.utec.transferba.TransferBA.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.edu.utec.transferba.TransferBA.entidades.Cliente;
/**
 *
 * @author Carlos
 */
public interface ClienteDao extends JpaRepository<Cliente, Integer>{
    
    @Query("SELECT c FROM Cliente c WHERE c.idCliente = :idCliente")
    Cliente buscarClientePorId(Integer idCliente) throws Exception;  
}
