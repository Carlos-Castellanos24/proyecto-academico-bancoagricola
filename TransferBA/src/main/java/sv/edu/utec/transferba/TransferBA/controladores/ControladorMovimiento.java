package sv.edu.utec.transferba.TransferBA.controladores;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thymeleaf.TemplateEngine;
import sv.edu.utec.transferba.TransferBA.entidades.Transferencia;
import sv.edu.utec.transferba.TransferBA.servicios.TransferenciaService;
import sv.edu.utec.transferba.TransferBA.utils.Utility;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.ModelAndView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.time.LocalDate;
import sv.edu.utec.transferba.TransferBA.servicios.ClienteService;

/**
 *
 * @author Carlos
 */
@Data
@Controller
@SessionAttributes({"idCliente","nombre"})
public class ControladorMovimiento {

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private ClienteService clienteService;

    private final TemplateEngine templateEngine;

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response, Model model) throws Exception {
        List<Transferencia> transferenciasSalientes = transferenciaService.listarTransferenciasSalientesPorIdCliente((Integer) model.getAttribute("idCliente"));
        List<Transferencia> transferenciasEntrantes = transferenciaService.listarTransferenciasEntrantesPorIdCliente((Integer) model.getAttribute("idCliente"));

        Workbook workbook = new XSSFWorkbook();
        Utility.exportListaAExcel(workbook.createSheet("Transferencias Salientes"), transferenciasSalientes);
        Utility.exportListaAExcel(workbook.createSheet("Transferencias Entrantes"), transferenciasEntrantes);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=transacciones.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/export/pdf")
    public ModelAndView exportPdf(HttpServletResponse response, Model model) throws Exception {

        try {
            List<Transferencia> transferenciasSalientes = transferenciaService.listarTransferenciasSalientesPorIdCliente((Integer) model.getAttribute("idCliente"));

            List<Transferencia> transferenciasEntrantes = transferenciaService.listarTransferenciasEntrantesPorIdCliente((Integer) model.getAttribute("idCliente"));

            var clienteSesion = clienteService.buscarClientePorId((Integer) model.getAttribute("idCliente"));

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"movimientos.pdf\"");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Image img = Image.getInstance("src/main/resources/static/images/logo_ba.png");
            img.scaleToFit(140, 140);
            img.setAlignment(Element.ALIGN_LEFT);
            document.add(img);

            Paragraph header = new Paragraph("Listado de Transferencias", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(Chunk.NEWLINE);

            Paragraph clientInfo = new Paragraph("Nombre del Cliente: " + clienteSesion.getNombre() + ' ' + clienteSesion.getApellido() + "\nDUI: " + clienteSesion.getDui() + "\nFecha de generación: " + LocalDate.now(),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
            clientInfo.setAlignment(Element.ALIGN_LEFT);
            document.add(clientInfo);

            document.add(Chunk.NEWLINE);

            Utility.addTableToPdf(document, "ENTRANTES", transferenciasEntrantes);
            document.add(Chunk.NEWLINE);
            Utility.addTableToPdf(document, "SALIENTES", transferenciasSalientes);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
