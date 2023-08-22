package hobby_detectives;

import hobby_detectives.controller.Controller;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;

public class Main {

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            var controller = new Controller();
            controller.start();
        });
    }
}
