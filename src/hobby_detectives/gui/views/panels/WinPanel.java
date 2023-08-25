package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;

import javax.swing.*;

public class WinPanel extends JPanel {
    public WinPanel(GameController controller) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Your guess was incorrect, you will no longer be able to make a solve attempt");
        JButton accept = new JButton("Ok");
        this.add(title);
        this.add(accept);
        this.setVisible(true);
    }
}
