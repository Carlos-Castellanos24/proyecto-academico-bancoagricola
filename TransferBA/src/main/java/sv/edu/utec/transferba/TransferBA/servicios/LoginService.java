package sv.edu.utec.transferba.TransferBA.servicios;

import sv.edu.utec.transferba.TransferBA.entidades.Login;
import sv.edu.utec.transferba.TransferBA.seguridad.LoginNotFoundException;
/**
 *
 * @author Carlos
 */
public interface LoginService {
    
    public Login validarLogin(String correo) throws Exception;
    
    /*METODOS PARA TOKEN EMAIL*/
    
    public void restablecerPassword(String token, String correo) throws LoginNotFoundException;
    
    public Login get(String passwordToken);
    
    public void actualizarPassword(Login login, String nuevaPassword);
}
