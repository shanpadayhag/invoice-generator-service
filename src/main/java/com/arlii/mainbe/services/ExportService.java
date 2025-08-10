package com.arlii.mainbe.services;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJcTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportCustomerDto;
import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportOrderLineItemDto;
import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportRequestDto;

@Service
public class ExportService {

  @Value("${company.name}")
  private String companyName;

  @Value("${company.address}")
  private String companyAddress;

  @Value("${company.email}")
  private String companyEmail;

  @Value("${company.phone-number}")
  private String companyPhoneNumber;

  /**
   * The main method to generate the invoice document. It orchestrates the
   * creation of all document sections.
   *
   * @param params The request DTO containing all necessary data for the invoice.
   * @return The fully generated XWPFDocument.
   * @throws IOException            If an I/O error occurs (e.g., reading image
   *                                files).
   * @throws InvalidFormatException If the image file format is invalid.
   */
  public XWPFDocument generateInvoice(MSWordExportRequestDto params) throws IOException, InvalidFormatException {
    XWPFDocument document = new XWPFDocument();
    XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);
    XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);

    MSWordExportCustomerDto customer = params.getCustomer();
    List<MSWordExportOrderLineItemDto> orders = params.getOrders();
    LocalDate invoiceDate = LocalDate.now();

    createHeaderContent(header);
    createInvoiceTitleBar(document, invoiceDate);
    createRecipientInfoBlock(document, customer);

    BigDecimal subtotal = createOrdersTable(document, orders);

    createSummaryTotalsTable(document, params, subtotal);
    createPaymentDetailsBlock(document);
    createFooterContent(footer);

    return document;
  }

  /**
   * Creates and populates the document header with the company logo and contact
   * information.
   *
   * @param header The XWPFHeader object to populate.
   * @throws IOException
   * @throws InvalidFormatException
   */
  private void createHeaderContent(XWPFHeader header) throws IOException, InvalidFormatException {
    header.createParagraph().createRun().addBreak();

    XWPFTable companyInfoTable = header.createTable(1, 2);
    companyInfoTable.getCTTbl().getTblPr().unsetTblBorders();

    XWPFTableCell logoCell = companyInfoTable.getRow(0).getCell(0);
    XWPFParagraph logoParagraph = logoCell.getParagraphs().get(0);
    logoParagraph.setAlignment(ParagraphAlignment.LEFT);
    XWPFRun logoRun = logoParagraph.createRun();

    try (InputStream logoStream = this.getClass().getResourceAsStream("/static/images/logo.png")) {
      if (logoStream == null)
        logoRun.setText("Logo Not Found");
      else
        logoRun.addPicture(logoStream, XWPFDocument.PICTURE_TYPE_PNG, "logo.png", 756000, 756000);
    }

    XWPFTableCell companyDetailsCell = companyInfoTable.getRow(0).getCell(1);
    companyDetailsCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

    String[] companyDetails = {
        companyName,
        companyAddress,
        companyEmail,
        companyPhoneNumber
    };

    for (int index = 0; index < companyDetails.length; index++) {
      XWPFParagraph detailParagraph;
      if (index == 0)
        detailParagraph = companyDetailsCell.getParagraphs().get(0);
      else
        detailParagraph = companyDetailsCell.addParagraph();

      detailParagraph.setAlignment(ParagraphAlignment.LEFT);
      detailParagraph.setSpacingBetween(1.15);

      XWPFRun detailRun = detailParagraph.createRun();
      detailRun.setText(companyDetails[index]);
      detailRun.setFontSize(10);
      detailRun.setFontFamily("Cambria");
      detailRun.setColor("595959");
    }

    header.createParagraph().createRun().addBreak();
  }

  /**
   * Creates the styled title bar containing the "Invoice" label and the issue
   * date.
   *
   * @param document    The document to add the title bar to.
   * @param invoiceDate The date to display on the invoice.
   */
  private void createInvoiceTitleBar(XWPFDocument document, LocalDate invoiceDate) {
    document.createParagraph();

    XWPFTable titleBarTable = document.createTable(1, 2);
    titleBarTable.getCTTbl().getTblPr().unsetTblBorders();

    CTTblWidth tableWidth = titleBarTable.getCTTbl().getTblPr().addNewTblW();
    tableWidth.setType(STTblWidth.PCT);
    tableWidth.setW(new BigInteger("5000"));

    XWPFTableCell titleCell = titleBarTable.getRow(0).getCell(0);
    styleTitleBarCell(titleCell, "Invoice", ParagraphAlignment.LEFT);

    XWPFTableCell dateCell = titleBarTable.getRow(0).getCell(1);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH);
    String formattedDate = invoiceDate.format(dateFormatter);
    styleTitleBarCell(dateCell, formattedDate, ParagraphAlignment.RIGHT);
  }

  /**
   * Helper method to apply consistent styling to the title bar cells.
   *
   * @param cell      The cell to style.
   * @param text      The text content for the cell.
   * @param alignment The paragraph alignment for the text.
   */
  private void styleTitleBarCell(XWPFTableCell cell, String text, ParagraphAlignment alignment) {
    cell.setColor("C0C0C0");

    final long TWIPS_PER_INCH = 1440;
    long paddingInTwips = (long) (0.1 * TWIPS_PER_INCH);

    XWPFParagraph paragraph = cell.getParagraphs().get(0);
    paragraph.setAlignment(alignment);
    paragraph.setIndentationLeft((int) paddingInTwips);
    paragraph.setIndentationRight((int) paddingInTwips);
    paragraph.setSpacingBefore((int) paddingInTwips);
    paragraph.setSpacingAfter((int) paddingInTwips);

    XWPFRun run = paragraph.createRun();
    run.setText(text);
    run.setFontFamily("Calibri");
    run.setFontSize(14);
    run.setColor("FFFFFF");
    run.setSmallCaps(true);
  }

  /**
   * Creates the block displaying the recipient's information.
   *
   * @param document The document to add the recipient block to.
   * @param customer The customer data object.
   */
  private void createRecipientInfoBlock(XWPFDocument document, MSWordExportCustomerDto customer) {
    document.createParagraph();

    XWPFTable recipientTable = document.createTable(3, 1);
    recipientTable.getCTTbl().getTblPr().unsetTblBorders();

    CTTblWidth tableWidth = recipientTable.getCTTbl().getTblPr().addNewTblW();
    tableWidth.setType(STTblWidth.PCT);
    tableWidth.setW(new BigInteger("5000"));

    XWPFTableCell headerCell = recipientTable.getRow(0).getCell(0);
    setCellText(headerCell, "RECIPIENT", "Calibri", 10, "577188");
    setCellBottomBorder(headerCell, STBorder.SINGLE, 4, "587188");

    XWPFTableCell nameCell = recipientTable.getRow(1).getCell(0);
    setCellText(nameCell, customer.getName(), "Cambria", 10, "595959");

    XWPFTableCell addressCell = recipientTable.getRow(2).getCell(0);
    setCellText(addressCell, customer.getAddress(), "Cambria", 10, "595959");
  }

  /**
   * Helper method to set the text and basic styling for a cell's first paragraph.
   *
   * @param cell       The cell to modify.
   * @param text       The text to insert.
   * @param fontFamily The font family.
   * @param fontSize   The font size.
   * @param color      The hex color code for the font.
   */
  private void setCellText(XWPFTableCell cell, String text, String fontFamily, int fontSize, String color) {
    XWPFParagraph paragraph = cell.getParagraphs().get(0);
    XWPFRun run = paragraph.createRun();
    run.setText(text);
    run.setFontFamily(fontFamily);
    run.setFontSize(fontSize);
    run.setColor(color);
  }

  /**
   * Helper method to apply a bottom border to a table cell.
   *
   * @param cell       The cell to apply the border to.
   * @param borderType The style of the border (e.g., STBorder.SINGLE).
   * @param size       The thickness of the border (in 1/8ths of a point).
   * @param color      The hex color code for the border.
   */
  private void setCellBottomBorder(XWPFTableCell cell, STBorder.Enum borderType, int size, String color) {
    CTTcPr cellProperties = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
    CTTcBorders borders = cellProperties.isSetTcBorders() ? cellProperties.getTcBorders()
        : cellProperties.addNewTcBorders();
    CTBorder bottomBorder = borders.isSetBottom() ? borders.getBottom() : borders.addNewBottom();

    bottomBorder.setVal(borderType);
    bottomBorder.setSz(BigInteger.valueOf(size));
    bottomBorder.setColor(color);
  }

  /**
   * Creates the main table of order line items, including the header, data rows,
   * and a final blank row.
   *
   * @param document The document to add the orders table to.
   * @param orders   A list of order line items.
   * @return The calculated subtotal of all line items.
   */
  private BigDecimal createOrdersTable(XWPFDocument document, List<MSWordExportOrderLineItemDto> orders) {
    document.createParagraph();

    int numRows = orders.size() + 2;
    XWPFTable ordersTable = document.createTable(numRows, 5);
    ordersTable.getCTTbl().getTblPr().unsetTblBorders();

    CTTblWidth tableWidth = ordersTable.getCTTbl().getTblPr().addNewTblW();
    tableWidth.setType(STTblWidth.PCT);
    tableWidth.setW(new BigInteger("5000"));

    createOrdersTableHeader(ordersTable.getRow(0));

    BigDecimal subtotal = BigDecimal.ZERO;
    for (int index = 0; index < orders.size(); index++) {
      MSWordExportOrderLineItemDto order = orders.get(index);
      XWPFTableRow dataRow = ordersTable.getRow(index + 1);

      BigDecimal lineTotal = BigDecimal.ZERO;
      BigDecimal orderUnitPrice = order.getUnitPrice();
      Integer orderQuantity = order.getQuantity();
      if (orderQuantity == null && orderUnitPrice != null) {
        subtotal = subtotal.add(orderUnitPrice);
      } else {
        lineTotal = orderUnitPrice.multiply(new BigDecimal(orderQuantity));
        subtotal = subtotal.add(lineTotal);
      }

      populateOrderDataRow(dataRow, order, lineTotal);
    }

    XWPFTableRow finalBlankRow = ordersTable.getRow(orders.size() + 1);
    for (XWPFTableCell cell : finalBlankRow.getTableCells()) {
      setCellBottomBorder(cell, STBorder.SINGLE, 4, "D9D9D9");
    }

    return subtotal;
  }

  /**
   * Helper method to create and style the header row of the orders table.
   *
   * @param headerRow The XWPFTableRow to populate as the header.
   */
  private void createOrdersTableHeader(XWPFTableRow headerRow) {
    String[] headers = { "Quantity", "Unit", "Description", "Unit Price (PHP)", "Total (PHP)" };
    String headerBackgroundColor = "577188";
    String headerFontColor = "FFFFFF";

    for (int i = 0; i < headers.length; i++) {
      XWPFTableCell cell = headerRow.getCell(i);
      cell.setColor(headerBackgroundColor);

      XWPFParagraph paragraph = cell.getParagraphs().get(0);
      XWPFRun run = paragraph.createRun();
      run.setText(headers[i]);
      run.setFontFamily("Calibri");
      run.setFontSize(10);
      run.setColor(headerFontColor);
    }
  }

  /**
   * Helper method to populate a single data row in the orders table.
   *
   * @param dataRow   The XWPFTableRow to populate.
   * @param order     The order data for the row.
   * @param lineTotal The pre-calculated total for the line item.
   */
  private void populateOrderDataRow(XWPFTableRow dataRow, MSWordExportOrderLineItemDto order, BigDecimal lineTotal) {
    String quantityStr = "";
    String unitPriceStr = "";
    String lineTotalStr;

    Integer orderQuantity = order.getQuantity();
    BigDecimal orderUnitPrice = order.getUnitPrice();

    if (orderQuantity != null) {
      quantityStr = orderQuantity.toString();
      unitPriceStr = formatDecimalForDisplay(orderUnitPrice);
      lineTotalStr = formatDecimalForDisplay(lineTotal);
    } else {
      lineTotalStr = formatDecimalForDisplay(orderUnitPrice);
    }

    String[] cellData = {
        quantityStr,
        order.getUnit(),
        order.getDescription(),
        unitPriceStr,
        lineTotalStr
    };

    for (int i = 0; i < cellData.length; i++) {
      XWPFTableCell cell = dataRow.getCell(i);
      while (cell.getParagraphs().size() > 0) {
        cell.removeParagraph(0);
      }
      cell.addParagraph();

      setCellText(cell, cellData[i], "Cambria", 10, "595959");
      setCellBottomBorder(cell, STBorder.SINGLE, 4, "595959");
    }
  }

  /**
   * Formats a BigDecimal for display with thousand separators and two decimal
   * places.
   * Returns "" for null, "(1,200.00)" for negative, and "1,200.00" for
   * non-negative.
   *
   * @param number The BigDecimal to format.
   * @return The formatted string for display.
   */
  private String formatDecimalForDisplay(BigDecimal number) {
    if (number == null)
      return "";

    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
    formatter.setMinimumFractionDigits(2);
    formatter.setMaximumFractionDigits(2);

    if (number.compareTo(BigDecimal.ZERO) < 0)
      return "(" + formatter.format(number.abs()) + ")";

    return formatter.format(number);
  }

  /**
   * Creates the right-aligned table for financial summaries (Subtotal, VAT,
   * Downpayment, Total Due).
   *
   * @param document    The document to add the summary table to.
   * @param subtotal    The calculated subtotal from the orders table.
   * @param vat         The VAT amount (can be null).
   * @param downpayment The downpayment amount (can be null).
   */
  private void createSummaryTotalsTable(XWPFDocument document, MSWordExportRequestDto params, BigDecimal subtotal) {
    document.createParagraph();

    BigDecimal vatAmount = (params.getVat() != null) ? params.getVat() : BigDecimal.ZERO;
    BigDecimal downpaymentAmount = (params.getDownpayment() != null) ? params.getDownpayment() : BigDecimal.ZERO;
    BigDecimal totalDue = subtotal.add(vatAmount).subtract(downpaymentAmount);

    XWPFTable summaryTable = document.createTable(4, 2);
    summaryTable.getCTTbl().getTblPr().unsetTblBorders();

    CTTblWidth tableWidth = summaryTable.getCTTbl().getTblPr().addNewTblW();
    tableWidth.setType(STTblWidth.PCT);
    tableWidth.setW(new BigInteger("2500"));

    CTJcTable tableAlignment = summaryTable.getCTTbl().getTblPr().addNewJc();
    tableAlignment.setVal(STJcTable.RIGHT);

    createSummaryRow(summaryTable.getRow(0), "SUBTOTAL", formatDecimalForDisplay(subtotal));
    createSummaryRow(summaryTable.getRow(1), "VAT", formatDecimalForDisplay(vatAmount));
    createSummaryRow(summaryTable.getRow(2), "DOWNPAYMENT", formatDecimalForDisplay(downpaymentAmount));
    createSummaryRow(summaryTable.getRow(3), "TOTAL DUE", formatDecimalForDisplay(totalDue));
  }

  /**
   * Helper method to create a styled row in the summary table.
   *
   * @param row   The XWPFTableRow to populate.
   * @param label The text for the left cell.
   * @param value The text for the right cell.
   */
  private void createSummaryRow(XWPFTableRow row, String label, String value) {
    XWPFTableCell labelCell = row.getCell(0);
    XWPFTableCell valueCell = row.getCell(1);

    // Style the left-aligned label cell
    labelCell.removeParagraph(0);
    labelCell.addParagraph();
    setCellText(labelCell, label, "Calibri", 10, "577188");
    setCellBottomBorder(labelCell, STBorder.SINGLE, 4, "595959");

    // Style the right-aligned value cell
    valueCell.removeParagraph(0);
    XWPFParagraph valueParagraph = valueCell.addParagraph();
    valueParagraph.setAlignment(ParagraphAlignment.RIGHT);

    XWPFRun valueRun = valueParagraph.createRun();
    valueRun.setText(value);
    valueRun.setFontFamily("Calibri");
    valueRun.setFontSize(10);
    valueRun.setColor("577188");
    setCellBottomBorder(valueCell, STBorder.SINGLE, 4, "595959");
  }

  /**
   * Creates the section displaying payment instructions and QR codes.
   *
   * @param document The document to add the payment details to.
   */
  private void createPaymentDetailsBlock(XWPFDocument document) throws IOException, InvalidFormatException {
    document.createParagraph();

    XWPFParagraph instructionParagraph = document.createParagraph();
    XWPFRun instructionRun = instructionParagraph.createRun();
    instructionRun.setText("Please send your payment to any of the following:");

    document.createParagraph();

    String[][] paymentOptions = {
        { "/static/images/qr_bpi.png", "BPI" },
        { "/static/images/qr_cimb.png", "CIMB" },
        { "/static/images/qr_gcash.png", "GCASH" }
    };

    XWPFTable paymentOptionsTable = document.createTable(1, paymentOptions.length);
    paymentOptionsTable.getCTTbl().getTblPr().unsetTblBorders();

    CTTblWidth tableWidth = paymentOptionsTable.getCTTbl().getTblPr().addNewTblW();
    tableWidth.setType(STTblWidth.PCT);
    tableWidth.setW(new BigInteger("5000"));

    XWPFTableRow row = paymentOptionsTable.getRow(0);
    for (int i = 0; i < paymentOptions.length; i++) {
      XWPFTableCell cell = row.getCell(i);
      String imagePath = paymentOptions[i][0];
      String label = paymentOptions[i][1];
      populatePaymentCell(cell, imagePath, label);
    }
  }

  /**
   * Helper method to populate a single cell with a QR code image and a centered
   * label.
   *
   * @param cell      The table cell to populate.
   * @param imagePath The classpath resource path to the QR code image.
   * @param label     The text label to display below the image.
   * @throws IOException            If the image file cannot be read.
   * @throws InvalidFormatException If the image format is invalid.
   */
  private void populatePaymentCell(XWPFTableCell cell, String imagePath, String label)
      throws IOException, InvalidFormatException {
    cell.removeParagraph(0);
    XWPFParagraph imageParagraph = cell.addParagraph();
    imageParagraph.setAlignment(ParagraphAlignment.CENTER);

    try (InputStream imageStream = this.getClass().getResourceAsStream(imagePath)) {
      XWPFRun imageRun = imageParagraph.createRun();
      if (imageStream != null) {
        final int imageSizeEmu = 1680000;
        imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_PNG, imagePath, imageSizeEmu, imageSizeEmu);
      } else {
        imageRun.setText("[QR Not Found]");
        imageRun.setColor("FF0000");
      }
    }

    XWPFParagraph labelParagraph = cell.addParagraph();
    labelParagraph.setAlignment(ParagraphAlignment.CENTER);

    XWPFRun labelRun = labelParagraph.createRun();
    labelRun.setText(label);
    labelRun.setFontSize(10);
    labelRun.setFontFamily("Cambria");
    labelRun.setColor("595959");
  }

  /**
   * Populates the document footer with a horizontal line and a closing message.
   *
   * @param footer The XWPFFooter object to populate.
   */
  private void createFooterContent(XWPFFooter footer) {
    XWPFParagraph lineParagraph = footer.createParagraph();

    CTPPr paragraphProperties = lineParagraph.getCTP().isSetPPr() ? lineParagraph.getCTP().getPPr()
        : lineParagraph.getCTP().addNewPPr();
    CTPBdr paragraphBorders = paragraphProperties.isSetPBdr() ? paragraphProperties.getPBdr()
        : paragraphProperties.addNewPBdr();

    CTBorder bottomBorder = paragraphBorders.isSetBottom() ? paragraphBorders.getBottom()
        : paragraphBorders.addNewBottom();
    bottomBorder.setVal(STBorder.SINGLE);
    bottomBorder.setSz(BigInteger.valueOf(4));
    bottomBorder.setColor("B1BFCD");

    XWPFParagraph messageParagraph = footer.createParagraph();
    messageParagraph.setAlignment(ParagraphAlignment.CENTER);

    XWPFRun messageRun = messageParagraph.createRun();
    messageRun.setText("Thank you for your order!");
    messageRun.setFontSize(10);
    messageRun.setFontFamily("Cambria");
    messageRun.setColor("595959");
  }
}
