package hobby_detectives;

import hobby_detectives.controller.GameController;

import javax.swing.*;

public class Main {

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            var controller = new GameController();
            controller.start();
        });
    }
}
