package sv.edu.utec.transferba.TransferBA.entidades;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 *
 * @author Carlos
 */
@Data
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;
    
    @NotEmpty
    @Column(name = "nombres")
    private String nombre;
    
    @NotEmpty
    @Column(name = "apellidos")
    private String apellido;
    
    @NotEmpty
    @Column(name = "dui")
    private String dui;
    
    @NotEmpty
    @Column(name = "telefono")
    private String telefono;
    
    @NotEmpty
    @Column(name = "direccion")
    private String direccion;
    
    @NotEmpty
    @Column(name = "fecha_nac")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaNacimiento;
    
    @NotEmpty
    @Column(name = "correo")
    private String correo;
    
    @NotEmpty
    @Column(name = "estado")
    private String estado;
}
