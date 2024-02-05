package sv.edu.utec.transferba.TransferBA.servicios;

import java.util.List;
import sv.edu.utec.transferba.TransferBA.entidades.Transferencia;
/**
 *
 * @author Carlos
 */
public interface TransferenciaService {
    
    public void insertarTransferencia(Transferencia transferencia) throws Exception;
    
    Transferencia buscarTransferenciaPorIdReferencia(String referencia) throws Exception;
    
    List<Transferencia> listarTransferenciasSalientesPorIdCliente(Integer idCliente) throws Exception;
    
    List<Transferencia> listarTransferenciasEntrantesPorIdCliente(Integer idCliente) throws Exception;
}
