package sv.edu.utec.transferba.TransferBA.entidades;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @author Carlos
 */
@Data
@Entity
@Table(name = "login")
public class Login implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    private Integer idLogin;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;
    
    @NotEmpty
    @Column(name = "username")
    private String username;
    
    @NotEmpty
    @Column(name = "clave")
    private String clave;
    
    @NotEmpty
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "password_token")
    private String passwordToken;
}
