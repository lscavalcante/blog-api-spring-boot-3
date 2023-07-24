package com.lscavalcante.blog.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ExcelService {

    public Resource createExcel() {
        try {
            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();

            // Create a new sheet
            Sheet sheet = workbook.createSheet("Sheet1");

            // Add a row
            Row row = sheet.createRow(0);

            // Add cells to the row
            Cell cell = row.createCell(0);
            cell.setCellValue("Hello, World!");

            // Save the workbook to a file
            String fileName = "excel-example.xlsx";
            Path filePath = Paths.get("uploads/", fileName);
            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
                workbook.write(fileOut);
                System.out.println("Arquivo Excel gerado em: " + filePath);
            }

            // Close the workbook
            workbook.close();

            // Create a FileSystemResource with the Excel file path
            return new FileSystemResource(filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
