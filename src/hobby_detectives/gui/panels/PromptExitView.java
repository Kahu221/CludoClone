package hobby_detectives.gui.panels;

import javax.swing.*;

public class PromptExitView {
    public PromptExitView() {
        var frame = new JFrame();
        frame.add(new JLabel("Are you sure you want to exit Hobby Detectives?"));
        var confirm = new JButton();
    }
}
