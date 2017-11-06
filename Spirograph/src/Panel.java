import javax.swing.*;

/**
 * Class Panel which is used for the the main window -JFrame
 */
public class Panel {

    //Our two private variables
    private JFrame frame;
    private JLabel label;

    public Panel() {

        //Instantiating the JFrame
        frame = new JFrame("Spirograph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null); //Position in on the centre of the screen
        frame.setContentPane(new Spirograph()); //Put the Spirograph Panel in to the frame

        //Adding a Label to the frame
        label = new JLabel("Spirograph");
        frame.getContentPane().add(label);


        //Display the window.
        frame.setSize(800, 800);
        frame.setVisible(true);

    }

}

