package mearate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MyFrame extends JFrame implements ActionListener {
    JButton open = new JButton();
    JFileChooser fc = new JFileChooser();
    static String filePah;


    @Override
    public void actionPerformed(ActionEvent e) {
        fc.setCurrentDirectory(new File("C:\\Users\\Artyom\\Desktop"));

        fc.setDialogTitle("Choose file");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            filePah = fc.getSelectedFile().getAbsolutePath();


        }

    }
}
