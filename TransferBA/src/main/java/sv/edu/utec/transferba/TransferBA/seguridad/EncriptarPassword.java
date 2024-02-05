package sv.edu.utec.transferba.TransferBA.seguridad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncriptarPassword {     
    
    /**
     * Este metodo se encarga de encriptar una contraseña.
     * @param password
     * @return Retorna una contraseña encriptada
     */
    public static String encriptarPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }   
}
