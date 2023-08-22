package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;

/**
 * This view is responsible for rendering the confirm-exit dialog, which opens
 * when the user tries to exit the game, or clicks Game->Exit in the menu bar.
 */
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
