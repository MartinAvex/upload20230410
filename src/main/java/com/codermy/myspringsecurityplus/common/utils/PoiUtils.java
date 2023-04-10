package com.codermy.myspringsecurityplus.common.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * excel poi 通用工具类
 */
public class PoiUtils {

    private final static String xls = ".xls";
    private final static String xlsx = ".xlsx";

    /**
     * 读入excel文件，解析内容后返回结果
     *
     * @param file
     * @throws IOException
     */
    public static <T> List<T> readExcel(MultipartFile file, ExcelRowsHandler<T> handler) throws IOException {

        String filename = file.getOriginalFilename();

        // 检查文件
        checkFile(filename);
        // 获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file, filename);
        // 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<T> list = new ArrayList<T>();
        if (workbook != null) {
            if (workbook.getNumberOfSheets() < 1 || workbook.getSheetAt(0).getLastRowNum() == 0) {
                throw new FileNotFoundException("文件不存在!");
            }

//            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {// 循环导入各个Sheet
            // 获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            // 获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            // 根据行头确定每一行的列数，这里规定了行头的列数 = 数据的列数
            int lastCellNum = sheet.getRow(0).getLastCellNum();
            // 循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                // 获得当前行
                Row row = sheet.getRow(rowNum);
                if (isRowEmpty(row)) { //空白行
                    continue;
                }
                // 获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                String[] cellDataArr = new String[lastCellNum];
                // 循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    cellDataArr[cellNum] = getCellValue(cell);
                }

                // 结果添加到list
                T value = handler.execute(rowNum + 1, cellDataArr);
                if (value != null) {
                    list.add(value);
                }
            }
        }
//        }
        return list;
    }


    /**
     *
     * @param response
     * @param filename 文件名称
     * @param header 导出的标题数据 key为listMap的key值  value为标题
     * @param dataset 数据
     * @throws IOException
     */
    public static void writeExcel(HttpServletResponse response, String filename, Map<String, String> header,
                                  List<Map<String, Object>> dataset) throws IOException {

        writeExcel(response, filename, header, null, Collections.singletonList(dataset));
    }

    /**
     * 批量导出多个sheet
     * @param response
     * @param filename
     * @param header
     * @param sheetNames sheet名称
     * @param sheetListData sheet数据
     * @throws IOException
     */
    public static void writeExcel(HttpServletResponse response, String filename, Map<String, String> header, List<String> sheetNames,
                                  List<List<Map<String, Object>>> sheetListData) throws IOException {

        // 获得Workbook工作薄对象
        Workbook workbook = getWorkBook(filename);
        CellStyle defaultTitleStyle = getDefaultTitleStyle(workbook);

        for (int sheetIndex = 0; sheetIndex < sheetListData.size(); sheetIndex++) {

            List<Map<String, Object>> dataset = sheetListData.get(sheetIndex);  // 需要导出的数据
            Sheet sheet = workbook.createSheet();
            sheet.setColumnWidth(1, 256*35+184);
            if (sheetNames != null) {
                workbook.setSheetName(sheetIndex, sheetNames.get(sheetIndex));
            }

            // 设置标题
            Row headRow = sheet.createRow(0);
            List<String> values = new ArrayList<String>(header.values());
            for (int i = 0; i < values.size(); i++) {
                Cell cell = headRow.createCell(i);
                cell.setCellValue(values.get(i));
                cell.setCellStyle(defaultTitleStyle);
            }

            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            cellStyle.setFont(font);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

            Font redFont = workbook.createFont();
            redFont.setColor(IndexedColors.RED.getIndex());

            List<String> headKeys = new ArrayList<String>(header.keySet());

            for (int i = 0; i < dataset.size(); i++) {
                Map<String, Object> data = dataset.get(i);
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < headKeys.size(); j++) {

                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    String headKey = headKeys.get(j);
                    Object value = data.get(headKey);
                    String textVal = null;
                    if (value != null) {
                        textVal = value.toString();
                    }
                    cell.setCellValue(textVal);
                    if (headKey.equals("gene")) {
                        cell.getRichStringCellValue().applyFont(10, 11, redFont);
                        cell.getRichStringCellValue().applyFont(11, 21, font);
                    }
                }
            }
        }

        ServletOutputStream out = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/force-download");
        workbook.write(out);
    }









    /**
     * 设置标题样式
     */
    private static CellStyle getDefaultTitleStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
        return cellStyle;
    }



    private static void checkFile(String fileName) throws IOException {
        // 判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    /**
     * 获取工作区间
     *
     * @param file
     * @return
     */
    private static Workbook getWorkBook(MultipartFile file, String fileName) {
        // 创建Workbook工作薄对象

        Workbook workbook = null;
        try {
            // 获取文件io流
            InputStream is = file.getInputStream();
            // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                // 2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                // 2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return workbook;
    }


    private static Workbook getWorkBook(String fileName) throws IOException {
        // 创建Workbook工作薄对象
        // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
        if (fileName.endsWith(xls)) {
            // 2003
            return new HSSFWorkbook();
        } else if (fileName.endsWith(xlsx)) {
            // 2007
            return new XSSFWorkbook();
        } else {
            throw new IOException("文件名后缀错误");
        }
    }


    /**
     * 获取各个单元格的内容
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数值:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
            default:
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
        }


    }

    /**
     * 每读取一行，就调用execute方法
     */
    public interface ExcelRowsHandler<T> {

        /**
         * 可以再此方法校验数据，校验通过后封装 T，再返回，读入完后，再批量insert/update
         *
         * @param lineNum 行号
         * @param rows 读取的当前这一行的数据
         * @return 自定义封装成 T,返回null则不记录到list
         */
        T execute(int lineNum, String[] rows);

    }

    private static boolean isRowEmpty(Row row) {
        if (Objects.isNull(row)) {
            return true;
        }
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
    }


}