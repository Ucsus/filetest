package ru.testmepls;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import ru.testmepls.model.Info;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTest {
    ClassLoader cl = FileTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/ziptest.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("ziptest.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains(".csv")) {
            try (InputStream inputStream = zf.getInputStream(entry)) {
                CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                List<String[]> content = reader.readAll();
                String[] row = content.get(0);
                assertThat(row[0]).isEqualTo("Period");
                assertThat(row[1]).isEqualTo("Sex");
                }
            }
        }
    }

    @Test
    void xlsTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/ziptest.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("ziptest.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains(".xls")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                    assertThat(xls.excel.getSheetAt(0)
                            .getRow(7)
                            .getCell(0)
                            .getStringCellValue())
                            .isEqualTo("Cигнализация");
                }
            }
        }
    }

    @Test
    void pdfTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/ziptest.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("ziptest.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains(".pdf")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                        PDF pdf = new PDF(inputStream);
                    System.out.println("");
                        assertThat(pdf.text).contains("КАССОВЫЙ ЧЕК");
                }
            }
        }
    }

    String jsonFile = "pavlik.json";

    @Test
    void jsonTest() throws Exception {
        File file = new File("src/test/resources/"+ jsonFile);
        ObjectMapper objectMapper = new ObjectMapper();
        Info info = objectMapper.readValue(file, Info.class);
        assertThat(info.name + " " + info.surname).isEqualTo("Pavlik Morozov");
        assertThat(info.age).isEqualTo(13);
        assertThat(info.father.isProsperous).isTrue();
    }
}


