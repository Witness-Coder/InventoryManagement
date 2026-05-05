package com.example.InvetoryManagement.utils;

import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EquipmentPDFGenerator {

    public static ByteArrayInputStream allDepartmentsReport(List<Departments> departments) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("All Departments Equipment Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            for (Departments dept : departments) {
                document.add(new Paragraph("Department: " + dept.getDeptName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph("HOD: " + (dept.getHod() != null ? dept.getHod().getName() : "N/A")));
                document.add(new Paragraph(" "));

                List<OfficeEquipment> equipmentList = dept.getOfficeEquipment();
                if (equipmentList == null || equipmentList.isEmpty()) {
                    document.add(new Paragraph("No equipment available.\n\n"));
                    continue;
                }

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{4, 3, 4});

                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                PdfPCell h1 = new PdfPCell(new Phrase("Equipment Name", headFont));
                PdfPCell h2 = new PdfPCell(new Phrase("Code", headFont));
                PdfPCell h3 = new PdfPCell(new Phrase("Added Date", headFont));
                h1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                h2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                h3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(h1);
                table.addCell(h2);
                table.addCell(h3);

                for (OfficeEquipment eq : equipmentList) {
                    table.addCell(eq.getName());
                    table.addCell(eq.getCodeNo());
                    String formattedDate = eq.getAddedDate() != null ? eq.getAddedDate().format(formatter) : "N/A";
                    table.addCell(formattedDate);
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
