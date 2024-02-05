package sv.edu.utec.transferba.TransferBA.servicios;

import sv.edu.utec.transferba.TransferBA.entidades.Cliente;

/**
 *
 * @author Carlos
 */
public interface ClienteService {
    
    public Cliente buscarClientePorId(Integer idCliente) throws Exception;
}
