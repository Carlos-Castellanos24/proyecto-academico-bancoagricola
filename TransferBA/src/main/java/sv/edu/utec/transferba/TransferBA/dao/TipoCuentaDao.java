package sv.edu.utec.transferba.TransferBA.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.utec.transferba.TransferBA.entidades.TipoCuenta;
/**
 *
 * @author Carlos
 */
public interface TipoCuentaDao extends JpaRepository<TipoCuenta, Integer>{
    
}
