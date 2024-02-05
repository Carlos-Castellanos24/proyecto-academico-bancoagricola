package sv.edu.utec.transferba.TransferBA.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.utec.transferba.TransferBA.entidades.ClienteCuenta;
import sv.edu.utec.transferba.TransferBA.dao.ClienteCuentaDao;

/**
 *
 * @author Carlos
 */
@Service
public class ClienteCuentaServiceImpl implements ClienteCuentaService {

    @Autowired
    ClienteCuentaDao clienteCuentaDao;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteCuenta> buscarCuentasDeCliente(Integer idCliente) throws Exception {
        try {
            return (List<ClienteCuenta>) clienteCuentaDao.buscarCuentasDeCliente(idCliente);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<ClienteCuenta> buscarCuentaPorIdCliente(Integer idCliente) throws Exception {
        try {
            return (List<ClienteCuenta>) clienteCuentaDao.buscarCuentaPorIdCliente(idCliente);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ClienteCuenta buscarClientePorNumeroCuenta(String numeroCuenta) throws Exception {
        try{
            return clienteCuentaDao.buscarClientePorNumeroCuenta(numeroCuenta);
        }catch(Exception e){
            throw new Exception(e);
        }
    }
}
