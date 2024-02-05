package sv.edu.utec.transferba.TransferBA.controladores;


import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sv.edu.utec.transferba.TransferBA.seguridad.LoginNotFoundException;
import sv.edu.utec.transferba.TransferBA.servicios.LoginService;
import sv.edu.utec.transferba.TransferBA.utils.Utility;
import sv.edu.utec.transferba.TransferBA.entidades.Login;

/**
 *
 * @author Carlos
 */
@Controller
public class ControladorPassword {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/olvidarPassword")
    public String olvidarPassword(Model model) {
        model.addAttribute("titulo", "Olvide mi contraseña");
        return "restablecerPassword";
    }

    @PostMapping("/enviarMensajePassword")
    public String enviarMensajePassword(HttpServletRequest req, Model model) {
        String correo = req.getParameter("correo");
        String token = RandomString.make(60);

        try {
            loginService.restablecerPassword(token, correo);
            String linkRestablecerPassword = Utility.url(req) + "/restablecerPassword?token=" + token;
            try {
                this.sendEmail(correo, linkRestablecerPassword);
                model.addAttribute("mensajeEnviado", "El mensaje se ha enviado.");
            } catch (MessagingException | UnsupportedEncodingException e) {
                model.addAttribute("errorMensaje", "Error al enviar el mensaje.");
            }
        } catch (LoginNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "restablecerPassword";
    }

    @GetMapping("/restablecerPassword")
    public String restablecerPassword(@Param(value = "token") String token, Model model) {
        Login login = loginService.get(token);
        if (login == null) {
            model.addAttribute("mensaje", "Codigo Invalido");
            return "restablecerPassword";
        }
        model.addAttribute("token", token);
        return "actualizarPassword";
    }

    @PostMapping("/actualizarNuevaPassword")
    public String actualizarNuevaPassword(HttpServletRequest req, Model model) {
        String token = req.getParameter("token");
        String clave = req.getParameter("clave");
        
        Login login = loginService.get(token);
        if (login == null) {
            model.addAttribute("mensaje", "Codigo Invalido");
        }else{
            loginService.actualizarPassword(login, clave);
            model.addAttribute("mensajePassword", "La contraseña ha sido actualizada.");
        }
        return "redirect:/";
    }

    private void sendEmail(String correo, String linkRestablecerPassword) throws MessagingException, UnsupportedEncodingException {
        
        FileSystemResource image = new FileSystemResource("C:/images/logo_ba.png");
        
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setFrom("transferba@bancoagricola.com", "Banco Agrícola");
        helper.setTo(correo);
        String asunto = "Este es tu link para restablecer tu contraseña";

        String contenido = "<p><img src='cid:imagenAdjunta' style='text-align:center;'></p>";
        
         contenido += "<p>Hola,</p>"
                + "<p>Has hecho una peticion para restablecer tu contraseña.</p>"
                + "<p>Has click en el link de abajo para poder cambiarla.</p>"
                + "<p><b><a href=\" " + linkRestablecerPassword + "\">Restablecer Contraseña</a></b></p>"
                + "<p>Si no has sido tu, ignora el mensaje.</p>";
        
        helper.setSubject(asunto);
        helper.setText(contenido, true);
        helper.addInline("imagenAdjunta", image);

        mailSender.send(mensaje);
    }
}