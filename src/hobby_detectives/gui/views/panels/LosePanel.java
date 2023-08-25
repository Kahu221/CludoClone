package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;

public class LosePanel extends JPanel {

    public LosePanel(GameController controller) {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel labelOne = new JLabel("Your guess was incorrect :(\n");
        JLabel labelTwo = new JLabel("you will no longer be able to make a solve attempt");
        JButton accept = new JButton("Ok");
        labelOne.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelOne.setFont(new Font("Arial", Font.PLAIN, 40));
        labelTwo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTwo.setFont(new Font("Arial", Font.PLAIN, 40));
        accept.setAlignmentX(CENTER_ALIGNMENT);
        this.add(labelOne);
        this.add(labelTwo);
        this.add(accept);
        this.setVisible(true);

        accept.addActionListener(clicked -> controller.endTurn());
    }
}
