package sv.edu.utec.transferba.TransferBA.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.edu.utec.transferba.TransferBA.entidades.Login;

/**
 *
 * @author Carlos
 */
public interface LoginDao extends JpaRepository<Login, Integer>{
    
    @Query("SELECT l FROM Login l WHERE l.username = :username AND l.estado = 'A'")
    Login validarLogin(String username)throws Exception;
       
    /*METODOS PARA RESTABLECER CONTRASEÑA*/
    @Query("SELECT l FROM Login l WHERE l.cliente.correo =:correo")
    public Login buscarLoginPorCorreo(String correo);
    
    public Login findByPasswordToken(String token);
}
