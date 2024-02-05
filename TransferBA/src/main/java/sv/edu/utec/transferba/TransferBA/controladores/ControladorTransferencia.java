package sv.edu.utec.transferba.TransferBA.controladores;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import sv.edu.utec.transferba.TransferBA.entidades.*;
import sv.edu.utec.transferba.TransferBA.servicios.*;

/**
 *
 * @author Carlos
 */
@Controller
@SessionAttributes({"idCliente","nombre"})
public class ControladorTransferencia {

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteCuentaService clienteCuentaService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/realizarTransferencia")
    @Transactional
    public String realizarTransferencia(@Valid Transferencia transferencia, Errors errores, @RequestParam("referencia") String referencia, Model model,
            @RequestParam("cuentaBeneficiaria") String cuentaBeneficiaria, @RequestParam("monto") double monto, @RequestParam("correoBeneficiario") String correoBeneficiario,
            HttpServletRequest request) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "redirect:/transaccion";
        }

        var listaCuentas = (List<ClienteCuenta>) request.getSession().getAttribute("cuentas");
        if (!listaCuentas.isEmpty()) {
            model.addAttribute("cuentas", listaCuentas);
        }

        try {
            var clienteSesion = clienteService.buscarClientePorId((Integer) model.getAttribute("idCliente"));

            var cuentaCliente = clienteCuentaService.buscarClientePorNumeroCuenta(cuentaBeneficiaria);

            var cuentaClienteSesion = clienteCuentaService.buscarCuentaPorIdCliente((Integer) model.getAttribute("idCliente"));

            if (cuentaCliente == null) {
                System.out.println("ENTRO A CUENTACLIENTE");
                model.addAttribute("cuentaInexistente", "El numero de cuenta no existe");
                return "transaccion";
            } else {
                if (cuentaCliente.getCliente().getEstado().equals("I") && cuentaCliente.getCuenta().getEstado().equals("I")) {
                    System.out.println("ENTRO A CUENTA INACTIVA");
                    model.addAttribute("cuentaInactiva", "La cuenta se encuentra inactiva");
                    return "transaccion";
                } else {
                    if (cuentaClienteSesion.get(0).getCuenta().getNumeroCuenta().equals(cuentaBeneficiaria)) {
                        System.out.println("ENTRO A CUENTA PROPIA");
                        model.addAttribute("cuentaPropia", "La cuenta debe de ser diferente a la actual");
                        return "transaccion";
                    } else {
                        if (monto == 0) {
                            System.out.println("ENTRO A MONTO CERO");
                            model.addAttribute("montoCero", "El monto debe de ser mayor a cero");
                            return "transaccion";
                        } else {
                            if (monto > cuentaClienteSesion.get(0).getCuenta().getSaldo()) {
                                System.out.println("ENTRO A SALDO INSUFICIENTE");
                                model.addAttribute("saldoInsuficiente", "No posee saldo suficiente");
                                return "transaccion";
                            } else {
                                transferencia.setFecha(new Date());
                                transferenciaService.insertarTransferencia(transferencia);
                                if (referencia != null) {
                                    if (clienteSesion != null) {
                                        this.enviarCorreoEmisor(clienteSesion.getCorreo(), transferencia, clienteSesion);
                                    }
                                    var transferenciaInsertada = transferenciaService.buscarTransferenciaPorIdReferencia(referencia);
                                    if (!correoBeneficiario.equals("")) {
                                        if (transferenciaInsertada != null) {
                                            System.out.println(transferenciaInsertada);
                                            this.enviarCorreoReceptor(transferenciaInsertada.getCorreoBeneficiario(), transferencia);                                     
                                        }
                                    }
                                    model.addAttribute("detalleTransaccion", transferenciaInsertada);
                                    return "detalleTransaccion";
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/inicio";
    }

    private void enviarCorreoEmisor(String correo, Transferencia transferencia, Cliente cliente) throws MessagingException, UnsupportedEncodingException {

        FileSystemResource image = new FileSystemResource("C:/images/logo_ba.png");

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");

        helper.setFrom("transferba@bancoagricola.com", "Banco Agrícola");
        helper.setTo(correo);
        String asunto = "Transaccion Exitosa";

        String contenido = "<p><img src='cid:imagenAdjunta' style='text-align:center;'></p>";

        contenido += "<p>Estimado cliente,</p>"
                + "<p>Por este medio deseamos informarte que se ha</p>"
                + "<p>aplicado satisfactoriamente su transaccion: </p>"
                + "<ul>"
                + "<li>"
                + "<b style='font-weight: bold;'>Nombre Emisor: " + cliente.getNombre() + ' ' + cliente.getApellido() + "</b>"
                + "</li>"
                + "<li>"
                + "<b style='font-weight: bold;'>ID: " + transferencia.getReferencia() + "</b>"
                + "</li>"
                + "<li>"
                + "<p style='font-weight: bold;'>Monto: $" + transferencia.getMonto() + "</p>"
                + "</li>"
                + "<li>"
                + "<p style='font-weight: bold;'>Fecha: " + sdf.format(transferencia.getFecha()) + "</p>"
                + "</li>"
                + "</ul>"
                + "<p>Atentamente, </p>"
                + "<p>Banco Agrícola.</p>";

        helper.setSubject(asunto);
        helper.setText(contenido, true);
        helper.addInline("imagenAdjunta", image);

        mailSender.send(mensaje);
    }

    private void enviarCorreoReceptor(String correo, Transferencia transferencia) throws MessagingException, UnsupportedEncodingException {

        FileSystemResource image = new FileSystemResource("C:/images/logo_ba.png");

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");

        helper.setFrom("transferba@bancoagricola.com", "Banco Agrícola");
        helper.setTo(correo);
        String asunto = "Transaccion Exitosa";

        String contenido = "<p><img src='cid:imagenAdjunta' style='text-align:center;'></p>";

        contenido += "<p>Estimado cliente,</p>"
                + "<p>Por este medio deseamos informarte que se ha</p>"
                + "<p>aplicado satisfactoriamente su transaccion: </p>"
                + "<ul>"
                + "<li>"
                + "<b style='font-weight: bold;'>ID: " + transferencia.getReferencia() + "</b>"
                + "</li>"
                + "<li>"
                + "<p style='font-weight: bold;'>Cuenta Beneficiaria: " + transferencia.getCuentaBeneficiaria() + "</p>"
                + "</li>"
                + "<li>"
                + "<p style='font-weight: bold;'>Correo del Beneficiario: " + transferencia.getCorreoBeneficiario() + "</p>"
                + "</li>"
                + "<li>"
                + "<p style='font-weight: bold;'>Monto: $" + transferencia.getMonto() + "</p>"
                + "</li>"
                + "<li>"
                + "<p style='font-weight: bold;'>Fecha: " + sdf.format(transferencia.getFecha()) + "</p>"
                + "</li>"
                + "</ul>"
                + "<p>Atentamente, </p>"
                + "<p>Banco Agrícola.</p>";

        helper.setSubject(asunto);
        helper.setText(contenido, true);
        helper.addInline("imagenAdjunta", image);

        mailSender.send(mensaje);
    }
}
