package ml.attendee.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ml.attendee.domain.User;

public class CsvUtils {

    public static XSSFWorkbook downloadXlsx(List<User> users) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	XSSFWorkbook objWorkBook = new XSSFWorkbook();
        XSSFSheet objSheet = null;
        XSSFRow objRow = null;
        XSSFCell objCell = null;
        
        XSSFFont font = objWorkBook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("맑은고딕");
        
        XSSFCellStyle styleHd = objWorkBook.createCellStyle(); // 제목 스타일
        styleHd.setFont(font);

        objSheet = objWorkBook.createSheet("첫번째 시트"); // 워크시트 생성
        
        objRow = objSheet.createRow(0);
        objRow.setHeight((short) 0x180);
    
        objCell = objRow.createCell(0);
        objCell.setCellValue("이름");
        objCell.setCellStyle(styleHd);

        objCell = objRow.createCell(1);
        objCell.setCellValue("출석시간");
        objCell.setCellStyle(styleHd);
        
        int index = 1;
        for(User u : users) {
        	LocalDateTime date = u.getRegdate();
        	String formatDate = date.format(formatter).toString();
        	
        	objRow = objSheet.createRow(index);
            objRow.setHeight((short) 0x180);
            
            objCell = objRow.createCell(0);
            objCell.setCellValue(u.getUsername());
            objCell.setCellStyle(styleHd);

            objCell = objRow.createCell(1);
            objCell.setCellValue(formatDate);
            objCell.setCellStyle(styleHd);
            index++;
        }
        return objWorkBook;
    }

}