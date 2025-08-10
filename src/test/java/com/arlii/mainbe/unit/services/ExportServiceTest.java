package com.arlii.mainbe.unit.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportCustomerDto;
import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportOrderLineItemDto;
import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportRequestDto;
import com.arlii.mainbe.services.ExportService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ExportServiceTest {

  @Autowired
  private ExportService service;

  @Test
  void generateInvoiceShouldCreateDocumentWithCorrectData()
      throws InvalidFormatException, IOException {
    // arrange
    MSWordExportRequestDto request = createSampleRequest();

    // act
    XWPFDocument document = service.generateInvoice(request);

    // assert
    Assertions.assertThat(document).isNotNull();

    XWPFHeaderFooterPolicy documentHeaderFooterPolicy = document.getHeaderFooterPolicy();
    XWPFHeader documentHeader = documentHeaderFooterPolicy.getDefaultHeader();
    XWPFFooter documentFooter = documentHeaderFooterPolicy.getDefaultFooter();

    Assertions.assertThat(documentHeader).isNotNull();
    Assertions.assertThat(documentFooter).isNotNull();
    Assertions.assertThat(document.getTables()).hasSizeGreaterThanOrEqualTo(4);
    Assertions.assertThat(findTextInComponent(
        documentHeader.getParagraphs(),
        documentHeader.getTables(),
        "Test Inc.")).isTrue();
    // CHECK OTHER PARTS OF THE DOCUMENT...
    Assertions.assertThat(findTextInComponent(
        document.getParagraphs(),
        document.getTables(),
        "292.00")).isTrue();
  }

  /**
   * Helper to create sample request data for the test.
   */
  private MSWordExportRequestDto createSampleRequest() {
    MSWordExportCustomerDto customer = new MSWordExportCustomerDto();
    customer.setName("Test Customer");
    customer.setAddress("1 Main Street");

    MSWordExportOrderLineItemDto item1 = new MSWordExportOrderLineItemDto();
    item1.setQuantity(2);
    item1.setUnit("pcs");
    item1.setDescription("Product A");
    item1.setUnitPrice(new BigDecimal("150.00"));

    MSWordExportOrderLineItemDto item2 = new MSWordExportOrderLineItemDto();
    item2.setQuantity(1);
    item2.setUnit("box");
    item2.setDescription("Service B");
    item2.setUnitPrice(new BigDecimal("50.00"));

    MSWordExportRequestDto request = new MSWordExportRequestDto();
    request.setCustomer(customer);
    request.setOrders(Arrays.asList(item1, item2));
    request.setVat(new BigDecimal("42.00"));
    request.setDownpayment(new BigDecimal("100.00"));

    return request;
  }

  /**
   * A helper method to search for text within any document component
   * (body, header, footer) by checking its paragraphs and tables.
   */
  private boolean findTextInComponent(List<XWPFParagraph> paragraphs, List<XWPFTable> tables, String searchText) {
    for (XWPFParagraph p : paragraphs) {
      if (p.getText() != null && p.getText().contains(searchText)) {
        return true;
      }
    }
    if (tables != null) {
      for (XWPFTable tbl : tables) {
        for (XWPFTableRow row : tbl.getRows()) {
          for (XWPFTableCell cell : row.getTableCells()) {
            if (cell.getText() != null && cell.getText().contains(searchText)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
}
