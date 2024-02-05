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
@Table(name = "clientes_transferencias")
public class ClienteTransferencia implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente_transfer")
    private Integer idClienteTransferencia;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_transfer", referencedColumnName = "id_transfer")
    private Transferencia transferencia;
}
