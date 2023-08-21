package hobby_detectives;

import hobby_detectives.game.Game;
import hobby_detectives.gui.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new GameFrame();
            frame.start();
        });
    }
}
