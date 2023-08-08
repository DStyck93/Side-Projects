import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class App {

    private static File inputDir;
    private static File outputDir;
    private static boolean isJpg;
    private static boolean isPng;
    private static String[] fileTypes;
    private static JLabel inputLabel;
    private static JLabel outputLabel;
    private static JCheckBox jpgBox;
    private static JCheckBox pngBox;
    private static boolean isValidInput;

    public static void main(String[] args) {

        // Frame
        int frameWidth = 600;
        int frameHeight = 350;
        JFrame frame = new JFrame("Styx Image Metadata Remover");
        frame.setSize(frameWidth, frameHeight); // W x H
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Header
        int padding = 20;
        JLabel header = new JLabel("Selecting the same input/output directories will overwrite your images.");
        header.setBounds(padding, 20, 500, 20);
        frame.add(header);

        // Input Button
        int inputButtonYPos  = 70;
        JButton inputDirButton = new JButton("Select Input Folder");
        inputDirButton.setBounds(padding, inputButtonYPos, 150, 20);
        inputDirButton.addActionListener(e -> setInputDirectory());
        frame.add(inputDirButton);

        // Input Label
        int labelMaxWidth = 400;
        inputLabel = new JLabel("Input Directory:");
        inputLabel.setBounds(padding, inputButtonYPos + 30, labelMaxWidth, 20);
        frame.add(inputLabel);

        // Output Button
        JButton outputDirButton = new JButton("Select Output Folder");
        outputDirButton.setBounds(padding, 150, 150, 20);
        outputDirButton.addActionListener(e -> setOutputDirectory());
        frame.add(outputDirButton);

        // Output Label
        outputLabel = new JLabel("Output Directory:");
        outputLabel.setBounds(padding, 180, labelMaxWidth, 20);
        frame.add(outputLabel);

        // Check Box Label
        int minCenterGap = 20;
        int boxXPos = padding + labelMaxWidth + minCenterGap;
        JLabel boxLabel = new JLabel("Select the file types:");
        boxLabel.setBounds(boxXPos - 20, inputButtonYPos, 150, 20); 
        frame.add(boxLabel);

        // jpg check box
        int boxSize = 50;
        jpgBox = new JCheckBox("jpg");
        jpgBox.setBounds(boxXPos, inputButtonYPos + 30, boxSize, boxSize);
        frame.add(jpgBox);

        // png check box
        pngBox = new JCheckBox("png");
        pngBox.setBounds(boxXPos, inputButtonYPos + 70, boxSize, boxSize);
        frame.add(pngBox);

        // Delete Button
        int width = 150;
        int xPos = (frameWidth / 2) - (width / 2); // Center the button
        JButton deleteButton = new JButton("Delete Metadata");
        deleteButton.setBounds(xPos, frameHeight - 100, width, 20);
        deleteButton.addActionListener(e -> deleteButtonHandler());
        frame.add(deleteButton);

        frame.setVisible(true);
    }

    private static void deleteButtonHandler() {
        setFileTypes();
        try {
            if(isValidInput){
                deleteMetadata(inputDir, outputDir, fileTypes);
                JOptionPane.showMessageDialog(null, "Process completed");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Error deleting metadata");
        }
    }

    private static void setFileTypes(){

        isJpg = jpgBox.isSelected();
        isPng = pngBox.isSelected();

        // Nothing selected
        if(!isJpg && !isPng) {
            isValidInput = false;
            fileTypes = new String[] {"NA"};
            JOptionPane.showMessageDialog(null, "No file type selected.");
        }

        // jpg
        else if(isJpg && !isPng) {
            isValidInput = true;
                fileTypes = new String[] {"jpg"};
                try {
                    deleteMetadata(inputDir, outputDir, fileTypes);
                } catch (IOException e1) {
                    System.out.println("Error deleting jpg files.");
                }
        }

        // jpg/png
        else if(isJpg && isPng) {
            isValidInput = true;
            fileTypes = new String[] {"jpg", "png"};
            try {
                deleteMetadata(inputDir, outputDir, fileTypes);
            } catch (IOException e1) {
                System.out.println("Error deleting jpg & png files.");
            }
        }

        // png
        else {
            isValidInput = true;
            fileTypes = new String[] {"png"};
            try {
                deleteMetadata(inputDir, outputDir, fileTypes);
            } catch (IOException e1) {
                System.out.println("Error deleting png files.");
            }
        }           
    }

    private static void setInputDirectory() {
        
        JFileChooser inputJFC = new JFileChooser("C:/");
        inputJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        inputJFC.showSaveDialog(null);

        inputDir = inputJFC.getSelectedFile();
        inputLabel.setText("Input Directory: " + inputDir.getAbsolutePath());
    }

    private static void setOutputDirectory(){

        JFileChooser inputJFC = new JFileChooser("C:/");
        inputJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        inputJFC.showSaveDialog(null);

        outputDir = inputJFC.getSelectedFile();
        outputLabel.setText("Output Directory: " + outputDir.getAbsolutePath());
    }

    private static void deleteMetadata(File inputDir, File outputDir, String[] fileTypes) throws IOException {
        if(inputDir == null || outputDir == null) {
            JOptionPane.showMessageDialog(null, "Please select both directories.");
            isValidInput = false;
            return;
        }

        File[] files = inputDir.listFiles();
        for (String type : fileTypes) {
            for (File file : files) {
                if(file.getAbsolutePath().endsWith("." + type)) {
                    BufferedImage image = ImageIO.read(file);
                    ImageIO.write(image, type, new File(outputDir.getAbsolutePath() + "\\" + file.getName()));
                }
            }
        }
    }
}
