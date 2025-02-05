package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static Object[][] getExcelData(String excelFileName, String sheetName) {
        List<Object[]> data = new ArrayList<>();
        try {
            InputStream inputStream = ExcelUtils.class.getClassLoader().getResourceAsStream(excelFileName);
            if (inputStream == null) {
                throw new RuntimeException("Excel file " + excelFileName + " not found in classpath");
            }
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            DataFormatter formatter = new DataFormatter();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int lastCellNum = row.getLastCellNum();
                String[] cellValues = new String[lastCellNum];
                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = row.getCell(i);
                    cellValues[i] = formatter.formatCellValue(cell);
                }
                data.add(cellValues);
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading excel file: " + e.getMessage());
        }
        return data.toArray(new Object[0][]);
    }
}
