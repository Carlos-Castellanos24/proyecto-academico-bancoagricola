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
@Table(name = "cuentas")
public class Cuenta implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Integer idCuenta;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_cuenta", referencedColumnName = "id_tipo_cuenta")
    private TipoCuenta tipoCuenta;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;
    
    @NotEmpty
    @Column(name = "numero")
    private String numeroCuenta;
    
    @NotEmpty
    @Column(name = "saldo")
    private double saldo;
    
    @NotEmpty
    @Column(name = "estado")
    private String estado;
    
}
