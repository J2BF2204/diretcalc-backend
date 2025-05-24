package com.projetointegrador.diretcalcbackend.utils;

import com.projetointegrador.diretcalcbackend.model.Indice;
import com.projetointegrador.diretcalcbackend.model.Moeda;
import com.projetointegrador.diretcalcbackend.model.Salario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExcelParser {

    public List<Indice> parseExcelIndice(InputStream inputStream) throws IOException {
        List<Indice> lista = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Indice vi = new Indice();
                LocalDate vigencia = parseData(getStringCell(row, 0));
                vi.setVigencia(vigencia);
                vi.setIndiceMonetario(parseBigDecimal(getStringCell(row, 1)));
                vi.setIndiceIpcae(parseBigDecimal(getStringCell(row, 2)));
                vi.setSelicMensal(parseBigDecimal(getStringCell(row, 3)));
                vi.setSelicAcumulada(parseBigDecimal(getStringCell(row, 4)));

                lista.add(vi);
            }
        }

        return lista;
    }

    public List<Moeda> parseExcelMoeda(InputStream inputStream) throws IOException {
        List<Moeda> lista = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Moeda vi = new Moeda();
                vi.setNome(getStringCell(row, 0));
                LocalDate vigencia = parseData(getStringCell(row, 1));
                vi.setVigencia(vigencia);
                vi.setSigla(getStringCell(row, 2));
                vi.setSimbolo(getStringCell(row, 3));

                lista.add(vi);
            }
        }

        return lista;
    }

    public List<Salario> parseExcelSalario(InputStream inputStream) throws IOException {
        List<Salario> lista = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Salario vi = new Salario();
                LocalDate vigencia = parseData(getStringCell(row, 0));
                vi.setVigencia(vigencia);
                vi.setLei(getStringCell(row, 1));
                vi.setValor(parseBigDecimal(getStringCell(row, 2)));

                lista.add(vi);
            }
        }

        return lista;
    }

    private String getStringCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Se você quiser tratar como texto de data, pode fazer algo aqui.
                    return new SimpleDateFormat("dd-MM-yyyy", Locale.forLanguageTag("pt")).format(cell.getDateCellValue());
                } else {
                    // Usa BigDecimal para manter precisão
                    return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                }

            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getStringCellValue().trim();
                    case NUMERIC:
                        return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    default:
                        return "";
                }

            case BLANK:
                return "";

            default:
                return cell.toString().trim();
        }
    }



    private LocalDate parseData(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        // Normalização da string
        text = text.trim()
                .replace("–", "-")
                .replace("−", "-")
                .replace(".", "-")
                .replace("/", "-")
                .replace(" ", "-")
                .toLowerCase();

        // Suporta formatos com nome de mês abreviado
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yy"),
                DateTimeFormatter.ofPattern("d-M-yy")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate date = LocalDate.parse(text, formatter);
                if (date.getYear() < 100) {
                    int year = date.getYear() >= 50 ? date.getYear() + 1900 : date.getYear() + 2000;
                    date = LocalDate.of(year, date.getMonth(), date.getDayOfMonth());
                }
                return date;
            } catch (DateTimeParseException e) {
                // tenta próximo formato
            }
        }

        throw new IllegalArgumentException("Formato de data inválido (esperado dd/MM/yyyy): " + text);
    }






    private BigDecimal parseBigDecimal(String valorStr) {
        if (valorStr == null || valorStr.isBlank()) {
            return BigDecimal.ZERO;
        }

        valorStr = valorStr.trim();

        // Trata casos como "90.212,45" (formato brasileiro)
        if (valorStr.contains(",") && valorStr.contains(".")) {
            // Remove pontos de milhar, troca vírgula decimal por ponto
            valorStr = valorStr.replace(".", "").replace(",", ".");
        }
        // Trata casos como "90212,45"
        else if (valorStr.contains(",")) {
            valorStr = valorStr.replace(",", ".");
        }
        // Se só tem ponto, assume que já está no formato correto

        try {
            return new BigDecimal(valorStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor numérico inválido: '" + valorStr + "'", e);
        }
    }
}


