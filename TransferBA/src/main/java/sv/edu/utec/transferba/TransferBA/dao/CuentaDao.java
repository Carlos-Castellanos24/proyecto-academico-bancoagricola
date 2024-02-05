package sv.edu.utec.transferba.TransferBA.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.utec.transferba.TransferBA.entidades.Cuenta;

/**
 *
 * @author Carlos
 */
public interface CuentaDao extends JpaRepository<Cuenta, Integer>{
    
}
