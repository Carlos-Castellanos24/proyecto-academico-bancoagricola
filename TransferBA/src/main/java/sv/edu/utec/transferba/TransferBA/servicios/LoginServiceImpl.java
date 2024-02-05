package sv.edu.utec.transferba.TransferBA.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.utec.transferba.TransferBA.dao.LoginDao;
import sv.edu.utec.transferba.TransferBA.entidades.Login;
import sv.edu.utec.transferba.TransferBA.seguridad.EncriptarPassword;
import sv.edu.utec.transferba.TransferBA.seguridad.LoginNotFoundException;

/**
 *
 * @author Carlos
 */
@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    LoginDao loginDao;
    
    @Override
    @Transactional(readOnly = true)
    public Login validarLogin(String username)throws Exception{
        try{
            return loginDao.validarLogin(username);
        }catch(Exception e){
            throw new Exception(e);
        }
    } 
    
    @Override
    @Transactional
    public void restablecerPassword(String token, String correo)throws LoginNotFoundException {
        Login login = loginDao.buscarLoginPorCorreo(correo);       
        if (login != null) {
            login.setPasswordToken(token);
            loginDao.save(login);
        } else {
            throw new LoginNotFoundException("No se encontro el correo: " + correo);
        }
    }

    @Override
    @Transactional
    public Login get(String passwordToken) {
        return loginDao.findByPasswordToken(passwordToken);
    }

    @Override
    @Transactional
    public void actualizarPassword(Login login, String nuevaPassword) {
        
        String nuevaPasswordEncriptada = EncriptarPassword.encriptarPassword(nuevaPassword);
        login.setClave(nuevaPasswordEncriptada);
        login.setPasswordToken(null);
        loginDao.save(login);
    }
}
