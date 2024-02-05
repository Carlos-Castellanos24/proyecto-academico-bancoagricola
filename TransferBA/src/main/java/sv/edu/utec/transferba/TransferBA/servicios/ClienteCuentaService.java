package sv.edu.utec.transferba.TransferBA.servicios;

import java.util.List;
import sv.edu.utec.transferba.TransferBA.entidades.ClienteCuenta;
/**
 *
 * @author Carlos
 */
public interface ClienteCuentaService {
    
    public List<ClienteCuenta> buscarCuentaPorIdCliente(Integer idCliente) throws Exception;
    
    public List<ClienteCuenta> buscarCuentasDeCliente(Integer idCliente) throws Exception;
    
    ClienteCuenta buscarClientePorNumeroCuenta(String numeroCuenta) throws Exception;
}
