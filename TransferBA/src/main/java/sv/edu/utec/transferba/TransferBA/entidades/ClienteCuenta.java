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
@Table(name = "clientes_cuentas")
public class ClienteCuenta implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente_cuenta")
    private Integer idClienteCuenta;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id_cuenta")
    private Cuenta cuenta;
}
