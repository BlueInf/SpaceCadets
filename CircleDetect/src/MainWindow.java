

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class MainWindow extends JFrame {

    private static int WIDTH_WINDOW=800;
    private static int HEIGHT_WINDOW=400;
    private JLabel appTitle=new JLabel("Circle Detection Program",JLabel.CENTER);
    private JButton button=new JButton("Select Image");


    public MainWindow(String nameWindow){
        super(nameWindow);
    }



    public void init(){



        /** Layout of our MainWindow and its child Panels */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(new Dimension(WIDTH_WINDOW,HEIGHT_WINDOW));
        this.setLayout(new GridLayout(2,1));
        this.getContentPane().setBackground(Color.GRAY);


        addComponents(this);

        this.setVisible(true);

    }

    private void addComponents(JFrame root){
        appTitle.setFont(new Font("Times New Roman",Font.BOLD,60));
        button.setPreferredSize(new Dimension(200,100));

        JPanel buttonPanel=new JPanel();
        buttonPanel.setBackground(Color.GRAY);


        JFileChooser fileChooser=new JFileChooser();
        fileChooser.addChoosableFileFilter(new ImageFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        this.add(appTitle);
        this.add(buttonPanel);


        buttonPanel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        root.setVisible(false);
                        ImageWindow imageWindow=new ImageWindow(selectedFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println(selectedFile.getName());


                }
            }
        });
    }





}
