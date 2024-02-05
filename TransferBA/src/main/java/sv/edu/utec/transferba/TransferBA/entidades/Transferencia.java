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
@Table(name = "transferencias")
public class Transferencia implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transfer")
    private Integer idTransfer;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id_cuenta")
    private Cuenta cuenta;
    
    @NotEmpty
    @Column(name = "referencia")
    private String referencia;
    
    @NotEmpty
    @Column(name = "cuenta_receptor")
    private String cuentaBeneficiaria;
    
    @NotEmpty
    @Column(name = "monto")
    private double monto;
    
    @NotEmpty
    @Column(name = "fecha")
    private Date fecha;
    
    @NotEmpty
    @Column(name = "correo")
    private String correoBeneficiario;
    
    @NotEmpty
    @Column(name = "concepto")
    private String concepto;
    
    @NotEmpty
    @Column(name = "autorizacion")
    private String autorizacionKey;
}
