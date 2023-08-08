import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class App {

    private static JLabel dirLabel;
    private static File directory;
    private static JTextField stringTF;
    private static JLabel resultLabel;

    public static void main(String[] args) throws Exception {

        JFrame frame = buildFrame();
        frame.setVisible(true);
    }

    private static JFrame buildFrame(){

        JFrame frame = new JFrame("String Finder");
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel stringLabel = new JLabel("String to Find");
        stringLabel.setBounds(10, 20, 100, 20);
        stringLabel.setFont(new Font("Times", Font.BOLD, 14));
        frame.add(stringLabel);

        stringTF = new JTextField(20);
        stringTF.setBounds(10, 50, 300, 20);
        frame.add(stringTF);

        JLabel folderHeader = new JLabel("Folder to Search");
        folderHeader.setBounds(10, 100, 150, 20);
        folderHeader.setFont(new Font("Times", Font.BOLD, 14));
        frame.add(folderHeader);

        dirLabel = new JLabel("No folder selected");
        dirLabel.setBounds(10, 130, 300, 20);
        frame.add(dirLabel);

        JButton folderButton = new JButton("Choose Folder");
        folderButton.setBounds(10, 160, 120, 30);
        folderButton.addActionListener(e -> chooseFolder());
        frame.add(folderButton);

        JLabel resultHeader = new JLabel("Result");
        resultHeader.setBounds(10, 220, 100, 20);
        resultHeader.setFont(new Font("Times", Font.BOLD, 14));
        frame.add(resultHeader);

        resultLabel = new JLabel("No result found");
        resultLabel.setBounds(10, 250, 300, 20);
        frame.add(resultLabel);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(200, 400, 75, 30);
        searchButton.addActionListener(e -> search());
        frame.add(searchButton);

        return frame;
    }

    private static void search(){

        if(stringTF.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "No string selected.");
            return;
        } else if(directory == null) {
            JOptionPane.showMessageDialog(null, "No directory selected.");
            return;
        }

        File[] files = directory.listFiles();

        String findStr = stringTF.getText().toUpperCase();

        for(File file : files){
            if(file.getAbsolutePath().endsWith(".txt")){

                try{

                    BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                    
                    String line = reader.readLine().toUpperCase();
                    if(line.contains(findStr)){
                        resultLabel.setText("Your string has been found in " + file.getName());
                        return;
                    }
                
                    reader.close();

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        // If no file has been found
        resultLabel.setText("No result found");
    }

    private static void chooseFolder(){

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showSaveDialog(null);

        directory = fileChooser.getSelectedFile();
        dirLabel.setText(directory.getAbsolutePath());
    }
}
