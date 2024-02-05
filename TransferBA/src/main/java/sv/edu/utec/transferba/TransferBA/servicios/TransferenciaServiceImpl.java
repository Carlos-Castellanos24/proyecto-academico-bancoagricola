package sv.edu.utec.transferba.TransferBA.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.edu.utec.transferba.TransferBA.dao.TransferenciaDao;
import sv.edu.utec.transferba.TransferBA.entidades.Transferencia;

/**
 *
 * @author Carlos
 */
@Service
public class TransferenciaServiceImpl implements TransferenciaService{

    @Autowired
    TransferenciaDao transferenciaDao;
    
    @Override
    public void insertarTransferencia(Transferencia transferencia) throws Exception{
        try{
            transferenciaDao.save(transferencia);
        }catch(Exception e){
            throw new Exception(e);
        }
    }  

    @Override
    public Transferencia buscarTransferenciaPorIdReferencia(String referencia) throws Exception {
        try {
            return transferenciaDao.buscarTransferenciaPorIdReferencia(referencia);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<Transferencia> listarTransferenciasEntrantesPorIdCliente(Integer idCliente) throws Exception {
        try{
            return transferenciaDao.listarTransferenciasEntrantesPorIdCliente(idCliente);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    @Override
    public List<Transferencia> listarTransferenciasSalientesPorIdCliente(Integer idCliente) throws Exception {
        try{
            return transferenciaDao.listarTransferenciasSalientesPorIdCliente(idCliente);
        }catch(Exception e){
            throw new Exception(e);
        }
    }
}
