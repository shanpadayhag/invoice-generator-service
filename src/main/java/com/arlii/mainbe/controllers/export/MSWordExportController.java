package com.arlii.mainbe.controllers.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportRequestDto;
import com.arlii.mainbe.services.ExportService;

@RestController
public class MSWordExportController {
  private ExportService service;

  public MSWordExportController(ExportService service) {
    this.service = service;
  }

  @PostMapping("/export/msword")
  public ResponseEntity<byte[]> invoke(@RequestBody @Validated MSWordExportRequestDto request) throws IOException, InvalidFormatException {
    try (XWPFDocument document = this.service.generateInvoice(request)) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      document.write(out);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.docx");

      return ResponseEntity.ok()
          .headers(headers)
          .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
          .body(out.toByteArray());
    }
  }
}
