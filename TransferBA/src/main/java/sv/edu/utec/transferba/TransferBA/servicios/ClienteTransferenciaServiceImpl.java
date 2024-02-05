package sv.edu.utec.transferba.TransferBA.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.edu.utec.transferba.TransferBA.entidades.ClienteTransferencia;
import sv.edu.utec.transferba.TransferBA.dao.ClienteTransferenciaDao;

/**
 *
 * @author Carlos
 */
@Service
public class ClienteTransferenciaServiceImpl implements ClienteTransferenciaService{

    @Autowired
    ClienteTransferenciaDao clienteTransferenciaDao;
    
    @Override
    public void insertarClienteTransferencia(ClienteTransferencia clienteTransferencia) throws Exception {
       try{
            clienteTransferenciaDao.save(clienteTransferencia);
       }catch(Exception e){
           throw new Exception(e);
       }
    }   
}
