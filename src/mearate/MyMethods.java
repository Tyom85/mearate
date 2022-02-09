package mearate;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MyMethods {


    public static final String incr = "increase";
    public static final String decr = "decrease";
    public static final String new_incr = "new increase";
    public static final String _new = "new";
    public static final String pend_incr = "pending increase";
    public static final String curr = "current";

    //    public static final String NEW_LINE_SEPARATO = "\n";
//    public static final String COMMA_DELIMITED = ";";
    public static final String sheetPath = "C:\\sheet\\sheet.xlsx";

    /**
     * @param old_new (input parametr) is status of destination.
     *                We split String array by , or ;   and each string become of
     *                compare.RateSheet object.
     *                After we fill map with created objects.
     *                We call this method twice for each .csv (new and old rates-sheet)
     * @return map for each .csv file
     */
    public static Map<String, RateSheet> HashMapFromTextFile(String old_new) {

        Map<String, RateSheet> map = new HashMap<>();
        BufferedReader br = null;

        String format = "dd.MM.yyyy";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);

        try {
            File file = new File(MyFrame.filePah);

            br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "There are Empty Rows in file");
                    System.exit(-1);
                }
                RateSheet rate_obj = new RateSheet();

                String[] parts = line.split("(,)|(;)");
                if (parts.length < 5) {
                    JOptionPane.showMessageDialog(null, "File has format issue");
                    System.exit(-1);
                }


                if (old_new.equals("old")) {
                    rate_obj.setCode(Integer.parseInt(parts[0].trim()));
                    rate_obj.setDest(parts[1].trim());
                    rate_obj.setRate(Double.parseDouble(parts[2].trim()));
                    rate_obj.setStatus(parts[3].trim());
                    rate_obj.setDate(LocalDate.parse(parts[4], dateTimeFormatter));
                    rate_obj.setOld_rate(Double.parseDouble(parts[2].trim()));
                    rate_obj.setPend_date(null);
                    rate_obj.setOld_new(old_new);

                } else {
                    rate_obj.setCode(Integer.parseInt(parts[0].trim()));
                    rate_obj.setDest(parts[1].trim());
                    rate_obj.setRate(Double.parseDouble(parts[2].trim()));
                    rate_obj.setStatus(old_new);
                    rate_obj.setDate(LocalDate.parse(sdf.format(date), dateTimeFormatter));
                    rate_obj.setOld_rate(Double.parseDouble(parts[2].trim()));
                    rate_obj.setPend_date(null);
                    rate_obj.setOld_new(old_new);
                }
                map.put(String.valueOf(rate_obj.getCode()), rate_obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * @param mapFirst  (this is new rate-sheet)
     * @param mapSecond (this is old rate-sheet)
     * @return map in which combined two rates with aproprate status, date, rate
     */
    public static Map<String, RateSheet> CombineMap(Map<String, RateSheet> mapFirst, Map<String, RateSheet> mapSecond) {

        if (!(mapFirst == null) && !(mapSecond == null)) {

            for (Map.Entry<String, RateSheet> entry1 : mapSecond.entrySet()) {
                if (!(mapFirst.containsKey(entry1.getKey()))) {

                    mapFirst.put(entry1.getKey(), entry1.getValue());
                } else {

                    if (mapFirst.get(entry1.getKey()).getRate() < mapSecond.get(entry1.getKey()).getRate() &&
                            !(mapFirst.get(entry1.getKey()).getStatus().equals(pend_incr))) {
                        mapFirst.get(entry1.getKey()).setOld_rate(mapFirst.get(entry1.getKey()).getRate());
                        mapFirst.get(entry1.getKey()).setRate(mapSecond.get(entry1.getKey()).getRate());
                        mapFirst.get(entry1.getKey()).setStatus(incr);
                        mapFirst.get(entry1.getKey()).setDate(LocalDate.now().plusDays(7));

                    }

                    if (mapFirst.get(entry1.getKey()).getRate() > mapSecond.get(entry1.getKey()).getRate() &&
                            !(mapFirst.get(entry1.getKey()).getStatus().equals(pend_incr))) {
                        mapFirst.get(entry1.getKey()).setOld_rate(mapFirst.get(entry1.getKey()).getRate());
                        mapFirst.get(entry1.getKey()).setRate(mapSecond.get(entry1.getKey()).getRate());
                        mapFirst.get(entry1.getKey()).setStatus(decr);
                        mapFirst.get(entry1.getKey()).setDate(LocalDate.now());
                    }


                }

            }

        }

        return final_returned_file(mapFirst);
    }

    /**
     * As it is TreeMap . no duplicated codes,
     * so we find only NEW and NEW Increase codes.
     *
     * @param map_combined this
     * @return already ready Map with all status, date, rate and date
     */

    public static Map<String, RateSheet> final_returned_file(Map<String, RateSheet> map_combined) {

        Map<String, RateSheet> map_New_Code = new TreeMap<>();
        String prefix_entry2;

        for (Map.Entry<String, RateSheet> entry3 : map_combined.entrySet()) {
            if (entry3.getValue().getStatus().equals(_new)) {
                map_New_Code.put(entry3.getKey(), entry3.getValue());
            }
        }

        for (Map.Entry<String, RateSheet> entry3 : map_New_Code.entrySet()) {

            map_combined.remove(entry3.getKey());
        }

        for (Map.Entry<String, RateSheet> entry2 : map_New_Code.entrySet()) {
            prefix_entry2 = entry2.getKey();

            Task:
            while (prefix_entry2.length() >= 2) {
                for (Map.Entry<String, RateSheet> entry1 : map_combined.entrySet()) {
                    if (entry1.getKey().equals(prefix_entry2.substring(0, prefix_entry2.length() - 1))) {
                        entry2.getValue().setStatus(new_incr);
                        entry2.getValue().setDate(LocalDate.now().plusDays(7));

                        map_combined.put(entry2.getKey(), entry2.getValue());

                        break Task;
                    }

                }

                prefix_entry2 = prefix_entry2.substring(0, prefix_entry2.length() - 1);
            }
        }
        map_combined.putAll(map_New_Code);

        return map_combined;
    }

    /**
     * We check in ready Map Pending codes.
     *
     * @param mapFirst_checked_date its ready file
     * @return map already with pending statuses
     */
    public static Map<String, RateSheet> checked_by_pending_date(Map<String, RateSheet> mapFirst_checked_date) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Set<String> set1 = mapFirst_checked_date.keySet();

        for (String key : set1) {

            if (mapFirst_checked_date.get(key).getDate().isBefore(LocalDate.parse(sdf.format(date), dateTimeFormatter)) ||
                    mapFirst_checked_date.get(key).getDate().isEqual(LocalDate.parse(sdf.format(date), dateTimeFormatter))) {
                mapFirst_checked_date.get(key).setStatus(curr);

            } else {
                mapFirst_checked_date.get(key).setStatus(pend_incr);
                mapFirst_checked_date.get(key).setPend_date(mapFirst_checked_date.get(key).getDate());
                mapFirst_checked_date.get(key).setOld_rate(mapFirst_checked_date.get(key).getRate());
            }
        }

        return mapFirst_checked_date;
    }

//    /**
//     * We separate data in Map and return .CSV file
//     *
//     * @param filepath   show location where we want to save .CSV file
//     * @param mapCombain this is Map which we split by separator
//     */
//    public static void write_csv(String filepath, Map<String, RateSheet> mapCombain) {
//
//        String[] headers = {"Prefix", "Destination", "Rate", "Status", "Effective Date", "Pending Date", "Old Rate"};
//
//
//        try {
//
//            FileWriter fw = new FileWriter(filepath, true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            PrintWriter pw = new PrintWriter(bw, true);
//            // importing headers
//            for (int i = 0; i < headers.length; i++) {
//                pw.append(headers[i]);
//                pw.append(COMMA_DELIMITED);
//            }
//            pw.append(NEW_LINE_SEPARATO);
//
//            for (Map.Entry<String, RateSheet> entry1 : mapCombain.entrySet()) {
//                String[] maplines = entry1.getValue().toString().split(",");
//
//                for (int i = 0; i < maplines.length; i++) {
//                    pw.append(maplines[i]);
//                    pw.append(COMMA_DELIMITED);
//
//                }
//                pw.append(NEW_LINE_SEPARATO);
//            }
//            pw.flush();
//            pw.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void write_xlsx(Map<String, RateSheet> mapCombain) throws NullPointerException {

        try {
            File filepathh = new File(sheetPath);
         //   FileInputStream finput = new FileInputStream(filepathh);

            XSSFWorkbook wb = new XSSFWorkbook(filepathh);
            XSSFSheet sheet = wb.getSheetAt(0);
            // int lastRowNumber = sheet.getLastRowNum();
            int lastRowNumber = 9;

            for (Map.Entry<String, RateSheet> entry1 : mapCombain.entrySet()) {

                Row dataRow = sheet.createRow(++lastRowNumber);
                dataRow.createCell(0).setCellValue(entry1.getValue().getCode());
                dataRow.createCell(1).setCellValue(entry1.getValue().getDest());
                dataRow.createCell(2).setCellValue(entry1.getValue().getRate());
                dataRow.createCell(3).setCellValue(entry1.getValue().getStatus());
                dataRow.createCell(4).setCellValue(entry1.getValue().getDate().toString());
                if (entry1.getValue().getPend_date() == null) {
                    dataRow.createCell(5).setBlank();
                } else {
                    dataRow.createCell(5).setCellValue(entry1.getValue().getPend_date().toString());
                }
                dataRow.createCell(6).setCellValue(entry1.getValue().getOld_rate());

            }
            String filepath = "";
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(MyFrame.filePah));
            int resp = fileChooser.showSaveDialog(null);
            if (resp == JFileChooser.APPROVE_OPTION) {
                filepath = fileChooser.getSelectedFile().getAbsolutePath() + ".xlsx";
            }

            FileOutputStream fiout = new FileOutputStream(filepath);

            wb.write(fiout);
         //   finput.close();
            wb.close();
            fiout.close();


        } catch (Exception e) {
            System.out.println("catch");
            e.printStackTrace();
        }


    }

//    public static void cleanXlsxSheet() throws IOException, InvalidFormatException {
//
//        File filepath = new File(sheetPath);
//        XSSFWorkbook wb = new XSSFWorkbook(filepath);
//        XSSFSheet sheet = wb.getSheetAt(0);
//        int lastRowNumber = sheet.getLastRowNum();
//
//        List<Row> rowList = new ArrayList<>();
//
//
//      //  Row dataRow = sheet.createRow(lastRowNumber);
//        for(int i = lastRowNumber; i >= 10 ; i--){
//            rowList.add(sheet.getRow(i));
//        }
//
//        for (Row row:rowList) {
//            sheet.removeRow(row);
//        }
//          wb.close();
//    }

}
