package com.mortgage.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mortgage.exception.BusinessException;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.dto.PlanDTO;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {
    private final PlanService planService;

    public ExportService(PlanService planService) {
        this.planService = planService;
    }

    public byte[] exportPlanToPdf(Long planId) {
        PlanDTO plan = planService.getPlanById(planId);
        List<DetailDTO> details = planService.getDetailsByPlanId(planId);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(bfChinese, 18, Font.BOLD);
            Font normalFont = new Font(bfChinese, 12, Font.NORMAL);
            Paragraph title = new Paragraph("房贷还款计划表", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));
            addPlanSummary(document, plan, normalFont);
            document.add(new Paragraph("\n"));
            addDetailTable(document, details, bfChinese);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new BusinessException("PDF导出失败：" + e.getMessage());
        }
    }

    private void addPlanSummary(Document document, PlanDTO plan, Font font) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        document.add(new Paragraph("计划编号：" + plan.getPlanNo(), font));
        document.add(new Paragraph("贷款金额：" + formatAmount(plan.getLoanAmount()) + "元", font));
        document.add(new Paragraph("贷款期限：" + plan.getLoanTerm() + "个月", font));
        document.add(new Paragraph("年利率：" + formatRate(plan.getAnnualRate()), font));
        document.add(new Paragraph("还款方式：" + plan.getRepaymentMethodName(), font));
        document.add(new Paragraph("首月还款：" + formatAmount(plan.getFirstPayment()) + "元", font));
        document.add(new Paragraph("末月还款：" + formatAmount(plan.getLastPayment()) + "元", font));
        document.add(new Paragraph("总还款额：" + formatAmount(plan.getTotalPayment()) + "元", font));
        document.add(new Paragraph("总利息：" + formatAmount(plan.getTotalInterest()) + "元", font));
        document.add(new Paragraph("利息占比：" + plan.getInterestRatio() + "%", font));
        document.add(new Paragraph("首次还款日：" + plan.getFirstPaymentDate().format(formatter), font));
        document.add(new Paragraph("最后还款日：" + plan.getLastPaymentDate().format(formatter), font));
    }

    private void addDetailTable(Document document, List<DetailDTO> details, BaseFont bfChinese) throws Exception {
        Font font = new Font(bfChinese, 10, Font.NORMAL);
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        String[] headers = {"期数", "还款日期", "月供", "本金", "利息", "剩余本金", "累计还款", "累计利息"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, font));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            table.addCell(cell);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (DetailDTO detail : details) {
            table.addCell(new Paragraph(String.valueOf(detail.getPeriod()), font));
            table.addCell(new Paragraph(detail.getPaymentDate().format(formatter), font));
            table.addCell(new Paragraph(formatAmount(detail.getMonthlyPayment()), font));
            table.addCell(new Paragraph(formatAmount(detail.getPrincipal()), font));
            table.addCell(new Paragraph(formatAmount(detail.getInterest()), font));
            table.addCell(new Paragraph(formatAmount(detail.getRemainingPrincipal()), font));
            table.addCell(new Paragraph(formatAmount(detail.getCumulativePayment()), font));
            table.addCell(new Paragraph(formatAmount(detail.getCumulativeInterest()), font));
        }
        document.add(table);
    }

    private String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toString();
    }

    private String formatRate(BigDecimal rate) {
        return rate.multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP) + "%";
    }
}
