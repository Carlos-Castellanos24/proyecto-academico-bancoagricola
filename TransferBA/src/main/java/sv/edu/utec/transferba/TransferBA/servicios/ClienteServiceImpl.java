package sv.edu.utec.transferba.TransferBA.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.edu.utec.transferba.TransferBA.entidades.Cliente;
import sv.edu.utec.transferba.TransferBA.dao.ClienteDao;

/**
 *
 * @author Carlos
 */
@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    ClienteDao clienteDao;
    
    @Override
    public Cliente buscarClientePorId(Integer idCliente) throws Exception {
        try {
            return clienteDao.buscarClientePorId(idCliente);
        } catch (Exception e) {
            throw new Exception(e);
        }
    } 
}
