package sv.edu.utec.transferba.TransferBA.entidades;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

/**
 *
 * @author Carlos
 */
@Data
@Entity
@Table(name = "tipo_cuenta")
public class TipoCuenta implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cuenta")
    private Integer idTipoCuenta;
    
    @NotEmpty
    @Column(name = "nombre")
    private String nombre;
    
    @NotEmpty
    @Column(name = "descripcion")
    private String descripcion;
}
