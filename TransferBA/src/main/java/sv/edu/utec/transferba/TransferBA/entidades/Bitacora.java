package sv.edu.utec.transferba.TransferBA.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
/**
 *
 * @author Carlos
 */
@Data
@Entity
@Table(name = "bitacora")
public class Bitacora implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Integer idBitacora;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;
    
    @NotEmpty
    @Column(name = "fecha")
    private Date fecha;
    
    @NotEmpty
    @Column(name = "descripcion")
    private String descripcion;
}
