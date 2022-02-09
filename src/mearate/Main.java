package mearate;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Main {

    public static void main(String[] args) throws IOException, InvalidFormatException {

        String old_new = "old";

        new MyFrame().actionPerformed(null);

        Map<String, RateSheet> mapFromFileFirst = MyMethods.HashMapFromTextFile(old_new);
        Map<String, RateSheet> mapFirst = new TreeMap<>(MyMethods.checked_by_pending_date(mapFromFileFirst));

        old_new = MyMethods._new;

        new MyFrame().actionPerformed(null);

        Map<String, RateSheet> mapFromFileSecond = MyMethods.HashMapFromTextFile(old_new);

        Map<String, RateSheet> mapSecond = new TreeMap<>(mapFromFileSecond);

        Map<String, RateSheet> mapCombain = MyMethods.CombineMap(mapFirst, mapSecond);
        /*

            String filepath = "";
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory((new File("C:\\Users\\Artyom\\Desktop")));
            int resp = fileChooser.showSaveDialog(null);
            if (resp == JFileChooser.APPROVE_OPTION) {
                filepath = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";
            }

            myMethods.write_csv(filepath, mapCombain);
            */


        MyMethods.write_xlsx(mapCombain);
      //  MyMethods.cleanXlsxSheet();

    }
}
