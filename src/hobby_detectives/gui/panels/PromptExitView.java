package hobby_detectives.gui.panels;

import javax.swing.*;
import java.awt.*;

public class PromptExitView {
    public PromptExitView() {
        var frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel("Are you sure you want to exit Hobby Detectives?"));
        var confirm = new JButton("Confirm");

        confirm.addActionListener(e -> {
            System.exit(0);
        });

        var cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            frame.dispose();
        });

        frame.getContentPane().add(confirm);
        frame.getContentPane().add(cancel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
