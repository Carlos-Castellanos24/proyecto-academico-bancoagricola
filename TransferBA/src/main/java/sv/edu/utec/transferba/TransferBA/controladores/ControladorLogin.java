package sv.edu.utec.transferba.TransferBA.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.context.request.WebRequest;
import sv.edu.utec.transferba.TransferBA.servicios.LoginService;
import sv.edu.utec.transferba.TransferBA.servicios.ClienteService;
import sv.edu.utec.transferba.TransferBA.servicios.ClienteCuentaService;

/**
 *
 * @author Carlos
 */
@Controller
@SessionAttributes({"idCliente", "nombre"})
public class ControladorLogin {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteCuentaService clienteCuentaService;

    @PostMapping("/validarLogin")
    public String validarLogin(String username, String clave, Model model) {

        try {
            var login = loginService.validarLogin(username);
            System.out.println(login);
            //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            //if (login != null && encoder.matches(clave, login.getClave())) {
            if (login != null) {
                if (clave.equals(login.getClave())) {
                    var cliente = clienteService.buscarClientePorId(login.getCliente().getIdCliente());

                    if (cliente != null && cliente.getEstado().equals("A")) {

                        var clienteCuenta = clienteCuentaService.buscarCuentaPorIdCliente(cliente.getIdCliente());

                        if (clienteCuenta.get(0).getCuenta().getCliente().getIdCliente().equals(clienteCuenta.get(0).getCliente().getIdCliente())
                                && clienteCuenta.get(0).getCuenta().getEstado().equals("A")) {

                            model.addAttribute("idCliente", cliente.getIdCliente());
                            model.addAttribute("nombre", cliente.getNombre() + ' ' + cliente.getApellido());
                            return "redirect:/inicio";
                        } else {
                            model.addAttribute("cuentaInactiva", "Su cuenta ha sido desactivada, contactar con servicio al cliente");
                            return "login";
                        }
                    } else {
                        model.addAttribute("userInactivo", "Su usuario ha sido desactivado, contactar con servicio al cliente");
                        return "login";
                    }
                }else{
                    model.addAttribute("credencialesInvalidas", "Usuario o contraseña invalidos");
                    return "login";
                }
            } else {
                model.addAttribute("usuarioNotFound", "El nombre de usuario no existe");
                return "login";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/login";
    }

    @GetMapping("/cerrarSesion")
    public String cerrarSesion(WebRequest request, DefaultSessionAttributeStore status, ModelMap model) {
        if (model.getAttribute("idCliente") != null) {
            model.remove("idCliente");
            status.cleanupAttribute(request, "idCliente");

            return "redirect:/login";
        }
        return null;
    }
}
