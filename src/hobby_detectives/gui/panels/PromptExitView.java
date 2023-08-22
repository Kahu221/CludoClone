package hobby_detectives.gui.panels;

import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;

public class PromptExitView {
    public PromptExitView(GameView gv) {
        var frame = new JDialog(gv, "Confirm exit");
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

        frame.setLocationRelativeTo(gv);
        frame.pack();
        frame.setVisible(true);
    }
}
