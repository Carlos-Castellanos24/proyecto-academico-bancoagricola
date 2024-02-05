package sv.edu.utec.transferba.TransferBA.controladores;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import sv.edu.utec.transferba.TransferBA.servicios.ClienteCuentaService;
import sv.edu.utec.transferba.TransferBA.servicios.TransferenciaService;

/**
 * @author Carlos
 */
@Controller
@SessionAttributes({"idCliente","nombre"})
public class ControladorHome {

    @Autowired
    ClienteCuentaService clienteCuentaService;

    @Autowired
    TransferenciaService transferenciaService;

    @GetMapping("/inicio")
    public String paginaPrincipal(Model model, HttpServletRequest request) {
        if (model.getAttribute("idCliente") == null) {
            return "redirect:/";
        }
        try {
            var listaCuentasCliente = clienteCuentaService.buscarCuentasDeCliente((Integer) model.getAttribute("idCliente"));
            
            if (!listaCuentasCliente.isEmpty()) {
                model.addAttribute("listaCuentasCliente", listaCuentasCliente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "inicio";
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        if (model.getAttribute("idCliente") == null) {
            return "redirect:/";
        }
        try {
            var clienteSesion = clienteCuentaService.buscarCuentaPorIdCliente((Integer) model.getAttribute("idCliente"));
            
            var listaCuentasCliente = clienteCuentaService.buscarCuentasDeCliente((Integer) model.getAttribute("idCliente"));

            System.out.println(listaCuentasCliente);

            if (!listaCuentasCliente.isEmpty()) {
                model.addAttribute("listaCuentasCliente", listaCuentasCliente);
            }
            
            if (clienteSesion != null) {
                model.addAttribute("nombreCliente", clienteSesion.get(0).getCliente().getNombre() + " " + clienteSesion.get(0).getCliente().getApellido());            
                model.addAttribute("correoCliente", clienteSesion.get(0).getCliente().getCorreo());
                model.addAttribute("numeroCliente", clienteSesion.get(0).getCliente().getTelefono());
                model.addAttribute("duiCliente", clienteSesion.get(0).getCliente().getDui());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "perfil";
    }

    @GetMapping("/movimientos")
    public String movimientos(Model model) {
        if (model.getAttribute("idCliente") == null) {
            return "redirect:/";
        }
        try {
            var transferenciasEntrantes = transferenciaService.listarTransferenciasEntrantesPorIdCliente((Integer) model.getAttribute("idCliente"));
            var transferenciasSalientes = transferenciaService.listarTransferenciasSalientesPorIdCliente((Integer) model.getAttribute("idCliente"));

            if (!transferenciasEntrantes.isEmpty()) {
                model.addAttribute("transferenciasEntrantes", transferenciasEntrantes);
            }
            if (!transferenciasSalientes.isEmpty()) {
                model.addAttribute("transferenciasSalientes", transferenciasSalientes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "movimientos";
    }

    @GetMapping("/transaccion")
    public String transaccion(Model model, HttpServletRequest request) {
        if (model.getAttribute("idCliente") == null) {
            return "redirect:/";
        }
        try {
            var listaCuentas = clienteCuentaService.buscarCuentasDeCliente((Integer) model.getAttribute("idCliente"));

            String referencia = ("TRX" + RandomString.make(22)).toUpperCase();

            if (!listaCuentas.isEmpty()) {

                model.addAttribute("cuentas", listaCuentas);
                model.addAttribute("referencia", referencia);
                model.addAttribute("autorizacion", referencia);
                request.getSession().setAttribute("cuentas", listaCuentas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "transaccion";
    }
}
