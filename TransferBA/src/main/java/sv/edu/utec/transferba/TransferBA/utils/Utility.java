package sv.edu.utec.transferba.TransferBA.utils;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import sv.edu.utec.transferba.TransferBA.entidades.Transferencia;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.SimpleDateFormat;

/**
 *
 * @author Carlos
 */
public class Utility {
    
    public static String url(HttpServletRequest req){
        String url = req.getRequestURL().toString();
        return url.replace(req.getServletPath(), "");
    }
    
        
    public static void exportListaAExcel(Sheet sheet, List<? extends Transferencia> transferencias) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Referencia");
        headerRow.createCell(1).setCellValue("Monto");
        headerRow.createCell(2).setCellValue("Fecha");

        int rowNum = 1;
        for (Transferencia transferencia : transferencias) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(transferencia.getReferencia());
            row.createCell(1).setCellValue(transferencia.getMonto());
            row.createCell(2).setCellValue(transferencia.getFecha().toString());
        }
    }
    
    public static void addTableToPdf(Document document, String title, List<Transferencia> transferencias) throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 2});

        PdfPCell cell = new PdfPCell(new Phrase(title, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        if (transferencias.isEmpty()) {
            cell = new PdfPCell(new Phrase("Aun no tienes transacciones", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL)));
            cell.setColspan(3);
            table.addCell(cell);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMMM 'del' yyyy 'a las' HH:mm:ss");
            
            for (Transferencia transferencia : transferencias) {
                table.addCell("Referencia: " + transferencia.getReferencia());
                table.addCell("Monto: $" + transferencia.getMonto());
                table.addCell("Fecha: " + sdf.format(transferencia.getFecha()));
            }
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }
}