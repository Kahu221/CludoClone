package hobby_detectives.gui.views;
import hobby_detectives.data.CharacterType;
import hobby_detectives.game.Card;
import hobby_detectives.game.PlayerCard;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.panels.*;
import hobby_detectives.gui.views.transitive.WaitingForPlayerView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class GameView extends JFrame implements PropertyChangeListener {
    public final int GAME_FRAME_WIDTH = 1200;
    public final int GAME_FRAME_HEIGHT = 800;

    private final GameController controller;
    private final GameModel model;
    
    public final StatusPanelView statusPanel;
    public final MapPanelView mapView;

    private final JMenuBar menuBar;

    private WaitingForPlayerView wfpView;
    private GuessNotificationView gnView;
    private RefutationView rfView;
    private SolvePanelView spView;
    private JPanel wlPanel;
    private final SetupView setupView;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        statusPanel = new StatusPanelView(model, controller, this);
        mapView = new MapPanelView(model, controller);
        this.model.addPropertyChangeListener(this);

        var gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
        gridBagLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
        this.setLayout(gridBagLayout);

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                promptForGameExit();
            }
        });
        this.setSize(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);

        this.setLocationRelativeTo(null); // centers the window in the center of the screen

        menuBar = new JMenuBar();

        var game = new JMenu("Game");
        var exit = new JMenuItem("Exit");
        exit.addActionListener(e -> { this.promptForGameExit(); });
        game.add(exit);
        menuBar.add(game);

        setupView = new SetupView(this, 4, this.controller);
        this.add(setupView);
        this.setJMenuBar(menuBar);
    }

    public void finishSetup() {
        this.remove(this.setupView);
        this.revalidate();
        this.repaint();
    }

    /**
     * Prompts the game to exit by creating a new PromptExitView.
     */
    public void promptForGameExit() {
        var view = new PromptExitView(this);
    }

    public void propertyChange(PropertyChangeEvent event) {
        System.out.println(this.model.correctTriplet);
        String propName = event.getPropertyName();
        switch (propName){
            case "hasMovedIntoEstate" -> {
                if (event.getNewValue().equals(true)) {
                    this.remove(this.mapView);
                    this.remove(this.statusPanel);
                    this.gnView = new GuessNotificationView(this.model, this.controller);
                    this.add(this.gnView);

                } else {
                    this.remove(this.wfpView);
                    addGridComponent(statusPanel, 0, 0, 1, 4);
                    addGridComponent(mapView, 1, 0, 3, 4);
                }
                this.revalidate();
                this.repaint();
            }

            case "waitingForPlayer" -> {
                if ((Boolean) event.getNewValue()) {
                    if (gnView != null) this.remove(gnView);
                    this.remove(this.mapView);
                    this.remove(this.statusPanel);
                    this.wfpView = new WaitingForPlayerView(this.model, this.controller);
                    this.add(this.wfpView);
                    if (this.rfView != null) { this.remove(this.rfView); }
                } else {
                    if(this.model.getPlayersToRefute().isEmpty()) {
                        this.remove(this.wfpView);
                        addGridComponent(statusPanel, 0, 0, 1, 4);
                        addGridComponent(mapView, 1, 0, 3, 4);
                    }
                    else {
                        this.remove(this.wfpView);
                        this.rfView = new RefutationView(this.model, this.controller);
                        this.add(rfView);
                    }

                }
                this.revalidate();
                this.repaint();
            }

            case "attemptingToSolve" -> {
                if (event.getNewValue().equals(true)) {
                    if (gnView != null) this.remove(gnView);
                    this.remove(this.mapView);
                    this.remove(this.statusPanel);
                    spView = new SolvePanelView(this, model, controller);
                    this.add(spView);
                } else {
                    if (gnView != null) this.remove(gnView);
                    addGridComponent(statusPanel, 0, 0, 1, 4);
                    addGridComponent(mapView, 1, 0, 3, 4);
                }
                this.revalidate();
                this.repaint();
            }

            case "playerWin" -> {
                if (event.getNewValue().equals(true)) {
                    if (gnView != null) this.remove(gnView);
                    if (spView != null) this.remove(spView);
                    this.remove(this.mapView);
                    this.remove(this.statusPanel);
                    wlPanel = new WinPanel(this.model);
                    this.add(wlPanel);
                } else {
                    if (gnView != null) this.remove(gnView);
                    addGridComponent(statusPanel, 0, 0, 1, 4);
                    addGridComponent(mapView, 1, 0, 3, 4);
                }
                this.revalidate();
                this.repaint();
            }
        }
    }

    private void addGridComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        var constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(component, constraints);
    }
    //TODO fix the center alignment of the contiue button i have legot no idea why this does not work but this is functional for now
    public void refutedCardsPopUpSetup(ArrayList<Card> refutedCards){
        JDialog refutedPopUp = new JDialog(this, "Refuted cards!");
        refutedPopUp.getContentPane().setLayout(new BoxLayout(refutedPopUp.getContentPane(), BoxLayout.Y_AXIS));
        //No refuted cards
        if(refutedCards.isEmpty()){
            JLabel noRefute = new JLabel("No one was able to refute your guess!");
            noRefute.setFont(new Font("Arial", Font.PLAIN, 30));
            refutedPopUp.add(noRefute);
        } else {
            //Refuted cards
            JLabel refuteTitle = new JLabel("Here are your refuted Cards:");
            refuteTitle.setFont(new Font("Arial", Font.PLAIN, 30));
            refutedPopUp.add(refuteTitle);
            JPanel cardContainer = new JPanel();
            for(Card c : refutedCards){
                JPanel refutedCard = new JPanel();
                refutedCard.setPreferredSize(new Dimension(100,200));
                refutedCard.setBackground(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                refutedCard.add(new JLabel(c.toString()));
                cardContainer.add(refutedCard);
            }
            refutedPopUp.getContentPane().add(cardContainer);
        }

        JButton continueButton = new JButton("Continue");
        refutedPopUp.getContentPane().add(continueButton);
        //continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(onClick -> {
            refutedPopUp.dispose();
        });


        refutedPopUp.setLocationRelativeTo(this);
        refutedPopUp.pack();
        refutedPopUp.setVisible(true);
    }

    public GameModel getModel() {
        return model;
    }

}
