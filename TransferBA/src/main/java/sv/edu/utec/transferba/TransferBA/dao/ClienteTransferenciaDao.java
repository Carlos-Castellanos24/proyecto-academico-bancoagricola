package sv.edu.utec.transferba.TransferBA.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.utec.transferba.TransferBA.entidades.ClienteTransferencia;

/**
 *
 * @author Carlos
 */
public interface ClienteTransferenciaDao extends JpaRepository<ClienteTransferencia, Integer>{
    
}
