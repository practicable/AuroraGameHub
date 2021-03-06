/*
 * Copyright 2012 Sardonix Creative.
 *
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by-nc-nd/3.0/
 *
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aurora.V1.core;

import aurora.V1.core.screen_logic.LibraryLogic;
import aurora.V1.core.screen_logic.SettingsLogic;
import aurora.V1.core.screen_logic.WelcomeLogic;
import aurora.V1.core.screen_ui.DashboardUI;
import aurora.V1.core.screen_ui.LibraryUI;
import aurora.engine.V1.Logic.AFileManager;
import aurora.engine.V1.Logic.APostHandler;
import aurora.engine.V1.Logic.ASound;
import aurora.engine.V1.Logic.AThreadWorker;
import aurora.engine.V1.UI.AButton;
import aurora.engine.V1.UI.ADialog;
import aurora.engine.V1.UI.AImagePane;
import aurora.engine.V1.UI.AProgressWheel;
import aurora.engine.V1.UI.AScrollBar;
import aurora.engine.V1.UI.ASlickLabel;
import aurora.engine.V1.UI.ATextField;
import aurora.engine.V1.UI.ATimeLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 * .------------------------------------------------------------------------.
 * | Game
 * .------------------------------------------------------------------------.
 * |
 * |
 * | This Class is the Game Object that contains the UI and functionality
 * | For the Game Cover found in the Library along with the MetaData of
 * | The actual Game.
 * |
 * .........................................................................
 * <p/>
 * @author Sammy Guergachi <sguergachi at gmail.com>
 * @author Carlos Machado <camachado@gmail.com>
 */
public class Game extends AImagePane implements Runnable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String coverURL;

    private String gamePath;

    private String timePlayed = null;

    private String lastPlayed;

    private String gameType;

    private int numberTimesPlayed;

    private int width;

    private int height;

    private Thread gameCoverThread;

    private boolean isFavorite;

    private boolean isLoaded = false;

    private boolean isSelected;

    private boolean isOverlayUIRemoved = false;

    private AProgressWheel progressWheel;

    private AImagePane coverImagePane;

    private AImagePane imgSelectedGlow;

    private AImagePane imgFavIcon;

    private AImagePane pnlOverlayBar;

    private AImagePane pnlConfirmRemove;

    private JPanel pnlInteractivePane;

    private JPanel pnlTop;

    private JPanel pnlBottom;

    private JPanel pnlOverlayButtonContainer;

    private AButton btnRemove;

    private AButton btnFavorite;

    private AButton btnFlip;

    private AButton btnPlay;

    private AButton confirmButton;

    private AButton denyButton;

    private GridManager gridManager;

    private AuroraCoreUI coreUI;

    private DashboardUI dashboardUI;

    private AuroraStorage storage;

    private final String rootCoverDBPath
                         = "https://s3.amazonaws.com/CoverArtDB/";

    private PlayButtonListener playButtonListener;

    private boolean isGameRemoveMode;

    private boolean isFliped;

    static final Logger logger = Logger.getLogger(Game.class);

    private AButton btnAward;

    private AButton btnSetting;

    private ASlickLabel lblHoursPlayed;

    private ASlickLabel lblLastPlayed;

    private ASlickLabel lblTimesPlayed;

    private ASlickLabel lblGameType;

    private ATextField txtHoursPlayed;

    private ATextField txtLastPlayed;

    private ATextField txtTimesPlayed;

    private ATextField txtGameType;

    private AImagePane pnlShortcut;

    private JScrollPane pnlFlipScrollPane;

    private JScrollBar flipScrollBar;

    private JPanel pnlFlipContentPane;

    private JPanel pnlRightPane;

    private JPanel pnlFlipContainer;

    private JPanel pnlTopImageContainer;

    private boolean isFlipUIReady;

    private ASlickLabel lblShortcut;

    private JPanel pnlShortcutLbl;

    private AButton btnWatch;

    private AButton btnFix;

    private AButton btnLearn;

    private JPanel pnlShortcutBtn;

    private int labelFontSize;

    private int flipPadding;

    private LibraryLogic libraryLogic;

    private ImageIcon localImage;

    private AButton btnAddCustomOverlay;

    private String localGameRootPath;

    private ActionListener settingsListener;

    private GridManager libraryManager;

    private boolean canShowGameInfoInLibraryStatusBar;

    private APostHandler postLoad;

    private boolean gameRemoved;

    private boolean isTransisioningBetweenGameInfo;

    private InteractiveListener gameClickListener;

    private MouseAdapter overlayMouseListener;

    private AFileManager fileIO;

    private int padding;

    private JPanel pnlTopContainer;

    private JPanel pnlConfirmRemoveContainer;

    private JPanel pnlOverlayBarContainer;

    private JPanel pnlFavContainer;

    private int OVERLAY_HEIGHT;

    private int OVERLAY_WIDTH;

    private JPanel pnlShortcutContainer;

    private JPanel pnlBottomContainer;

    public Game() {
    }

    public Game(final GridManager gridManager, final AuroraCoreUI auroraCoreUI,
                final DashboardUI dashboardUi) {

        this.dashboardUI = dashboardUi;
        this.coreUI = auroraCoreUI;
        this.gridManager = gridManager;
        this.setOpaque(false);
        this.setDoubleBuffered(true);

        //DEFAULT CASE
        this.setImage("Blank-Case.png", height, width);
        this.setPreferredSize(new Dimension(width, height));

    }

    public Game(final GridManager manager, final AuroraCoreUI ui,
                final DashboardUI dashboard, final AuroraStorage storage) {

        this.dashboardUI = dashboard;
        this.coreUI = ui;
        this.storage = storage;
        this.gridManager = manager;
        this.setOpaque(false);
        this.setDoubleBuffered(true);

        //DEFAULT CASE
        this.setImage("Blank-Case.png", height, width);
        this.setPreferredSize(new Dimension(width, height));

    }

    public Game(final GridManager manager, final AuroraCoreUI ui,
                final String CoverURL) {

        this.coreUI = ui;
        this.gridManager = manager;
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.coverURL = CoverURL;

        //DEFAULT CASE
        this.setImage("Blank-Case.png", height, width);
        this.setPreferredSize(new Dimension(width, height));

    }

    public Game(final String CoverURL, final DashboardUI dashboard) {

        this.setOpaque(false);
        this.coreUI = dashboard.getCoreUI();
        this.dashboardUI = dashboard;
        this.coverURL = CoverURL;
        this.storage = dashboardUI.getStorage();

        //DEFAULT CASE
        this.setImage("Blank-Case.png", height, width);
        this.setPreferredSize(new Dimension(width, height));

    }

    public Game(final DashboardUI dashboard) {

        this.setOpaque(false);
        this.dashboardUI = dashboard;
        this.coreUI = dashboard.getCoreUI();
        this.storage = dashboardUI.getStorage();

        //DEFAULT CASE
        this.setImage("Blank-Case.png", height, width);
        this.setPreferredSize(new Dimension(width, height));

    }

    public void setLibraryManager(final GridManager manager) {
        libraryManager = manager;
    }

    /**
     * .-----------------------------------------------------------------------.
     * | update()
     * .-----------------------------------------------------------------------.
     * |
     * | This method loads the Game Cover based on the coverURL. It saves the
     * | games cover locally by default and if it doesnt exist it downloads the
     * | image from servers.
     * |
     * | this also adds the Interactive Panel which is the overlayed UI on top
     * | of the Game to allow for manipulation.
     * |
     * .........................................................................
     * <p/>
     * @throws MalformedURLException Exception
     *
     */
    public final void update() throws MalformedURLException {

        // Set Up Interactive Overlay Panel
        // ----------------------------------------------------------------.
        pnlInteractivePane = new JPanel(new BorderLayout(0, 0));
        pnlInteractivePane.setBackground(new Color(10, 10, 79));
        pnlInteractivePane.setOpaque(false);
        pnlInteractivePane.setName("pnlInteractivePane");
        pnlInteractivePane.setPreferredSize(new Dimension(width, height));
        padding = width / 8;


        if (pnlInteractivePane.getKeyListeners().length == 0
            || gameClickListener == null) {
            gameClickListener = new InteractiveListener();
            pnlInteractivePane.addMouseListener(gameClickListener);
        }

        this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(
                "ENTER"), "EnterKeyHandler");
        this.getActionMap().put("EnterKeyHandler", new Game.EnterKeyHandler());



        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(width, height));

        // Create Overlay UI Components //
        coverImagePane = new AImagePane();
        coverImagePane.setName(name);

        if (coverURL.equals("library_noGameFound.png")) {
            coverImagePane.setImage("library_noGameFound.png");
            System.out.println("No Game Found");
        }

        imgSelectedGlow = new AImagePane("game_selectedGlow.png", width + 10,
                                         height + 10);


        // Set Up Bottom Bar Content
        // ----------------------------------------------------------------.
        OVERLAY_WIDTH = width - (2 * padding) - 5;
        OVERLAY_HEIGHT = (height / 9) + (OVERLAY_WIDTH / 20);


        pnlBottom = new JPanel(new BorderLayout(0, 0));
        pnlBottom.setOpaque(false);
        pnlBottom.setPreferredSize(new Dimension(OVERLAY_WIDTH + (2 * padding),
                                                 OVERLAY_HEIGHT));
        pnlBottom.setName("pnlBottom");

        // The Image Panel
        pnlOverlayBar = new AImagePane("game_overlay.png", OVERLAY_WIDTH,
                                       OVERLAY_HEIGHT);
        pnlOverlayBar.setPreferredSize(new Dimension(OVERLAY_WIDTH,
                                                     OVERLAY_HEIGHT));
        pnlOverlayBar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlOverlayBar.setBackground(Color.blue);
        pnlOverlayBar.setName("pnlOverlayBar");


        pnlOverlayBarContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,
                                                           0));
        pnlOverlayBarContainer.setOpaque(false);
        pnlOverlayBarContainer
                .setPreferredSize(pnlOverlayBar.getPreferredSize());


        // Play Game Button
        btnPlay = new AButton("game_btn_play_norm.png",
                              "game_btn_play_down.png",
                              "game_btn_play_over.png");
        playButtonListener = new Game.PlayButtonListener();
        btnPlay.addActionListener(playButtonListener);
        if (main.LAUNCHES < 5) {
            btnPlay.setToolTipText("Play");
        }
        btnPlay.setBorder(null);
        btnPlay.setPreferredSize(new Dimension(40, 40));

        // Favourite Buttom
        btnFavorite = new AButton("game_btn_star_norm.png",
                                  "game_btn_star_down.png",
                                  "game_btn_star_over.png");
        btnFavorite.addActionListener(new Game.FavoriteButtonListener());
        btnFavorite.setBorder(null);
        if (main.LAUNCHES < 10) {
            btnFavorite.setToolTipText("Favorite");
        }

        // Flip Button
        btnFlip = new AButton("game_btn_reverseRight_norm.png",
                              "game_btn_reverseRight_down.png",
                              "game_btn_reverseRight_over.png");
        btnFlip.addActionListener(new Game.FlipButtonListener());
        btnFlip.setBorder(null);
        if (main.LAUNCHES < 15) {
            btnFlip.setToolTipText("Flip");
        }

        // The Panel that Contains the overlay buttons
        pnlOverlayButtonContainer = new JPanel();
        pnlOverlayButtonContainer.setOpaque(false);
        pnlOverlayButtonContainer.setBackground(Color.red);
        pnlOverlayButtonContainer.setLayout(new FlowLayout(FlowLayout.CENTER,
                                                           OVERLAY_WIDTH / 13,
                                                           -(btnFlip
                                                           .getPreferredSize().height
                                                             - OVERLAY_HEIGHT)
                                                           + (btnFlip
                                                           .getPreferredSize().height
                                                              - OVERLAY_HEIGHT)
                                                             / 2
                                                           + btnFlip
                                                           .getPreferredSize().height
                                                             / 30));
        pnlOverlayButtonContainer.setName("pnlOverlayButtonContainer");


        // Custom Cover overlay button
        btnAddCustomOverlay = new AButton(
                "editUI_editGameOverlay_norm.png",
                "editUI_editGameOverlay_down.png",
                "editUI_editGameOverlay_norm.png");

        // Reverse Buttons
        // ----------------------------------------------------------------.
        // Awards Button
        btnAward = new AButton("game_btn_award_norm.png",
                               "game_btn_award_down.png",
                               "game_btn_award_over.png");
        btnAward.setBorder(null);
        if (main.LAUNCHES < 5) {
            btnAward.setToolTipText("Unavailable");
        }

        // Settings Button
        btnSetting = new AButton("game_btn_setting_norm.png",
                                 "game_btn_setting_down.png",
                                 "game_btn_setting_over.png");
        btnSetting.setPreferredSize(new Dimension(40, 40));
        btnSetting.setBorder(null);
        if (main.LAUNCHES < 5) {
            btnSetting.setToolTipText("Settings");
        }

        // Add Buttons to the Containers
        pnlOverlayButtonContainer.add(btnFavorite);
        pnlOverlayButtonContainer.add(btnPlay);
        pnlOverlayButtonContainer.add(btnFlip);
        pnlOverlayButtonContainer.setPreferredSize(
                new Dimension(btnFavorite.getPreferredSize().width
                              + btnPlay.getPreferredSize().width
                              + btnFlip.getPreferredSize().width
                              + 100,
                              pnlOverlayBar.getPreferredSize().height));

        // Add Overlay Image to the Container, which is not visible by default
        pnlOverlayBar.setVisible(false);
        pnlOverlayBar.add(pnlOverlayButtonContainer);
        pnlOverlayBar.validate();

        pnlOverlayBarContainer.add(pnlOverlayBar);



        // Add Bottom Pane to container
        int bottomPadding = height / 27 + OVERLAY_HEIGHT / 45;
        pnlBottomContainer = new JPanel(new BorderLayout());
        pnlBottomContainer.setOpaque(false);
        pnlBottomContainer.setPreferredSize(new Dimension(pnlBottom
                .getPreferredSize().width,
                                                          pnlBottom
                                                          .getPreferredSize().height
                                                          + bottomPadding));


        pnlBottom.add(Box.createHorizontalStrut(padding), BorderLayout.EAST);
        pnlBottom.add(pnlOverlayBarContainer, BorderLayout.CENTER);
        pnlBottom.add(Box.createHorizontalStrut(padding + 1), BorderLayout.WEST);

        pnlBottomContainer.add(pnlBottom, BorderLayout.CENTER);
        pnlBottomContainer.add(Box.createVerticalStrut(bottomPadding),
                               BorderLayout.SOUTH);


        // Set Up Top Bar Content
        // ----------------------------------------------------------------.

        imgFavIcon = new AImagePane("game_favouriteIcon.png");
        imgFavIcon.setPreferredSize(
                new Dimension(imgFavIcon.getRealImageWidth(),
                              imgFavIcon.getRealImageHeight()));

        pnlFavContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlFavContainer.setOpaque(false);
        pnlFavContainer.setPreferredSize(new Dimension((int) ((double) padding
                                                              / 1.4), 55));


        btnRemove = new AButton("game_btn_remove_norm.png",
                                "game_btn_remove_down.png",
                                "game_btn_remove_over.png");
        btnRemove.addActionListener(new RemoveButtonListener());

        pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setPreferredSize(new Dimension(width - (2 * padding), 55));
        pnlTop.setName("pnlTop");

        pnlTopContainer = new JPanel(new BorderLayout());
        pnlTopContainer.setOpaque(false);


        btnRemove.setVisible(false);
        imgFavIcon.setVisible(false);

        pnlFavContainer.add(imgFavIcon);

        pnlTopContainer.add(btnRemove, BorderLayout.EAST);
        pnlTopContainer.add(pnlFavContainer, BorderLayout.WEST);

        pnlTop.add(Box.createHorizontalStrut(padding - 5), BorderLayout.EAST);
        pnlTop.add(pnlTopContainer, BorderLayout.CENTER);
        pnlTop.add(Box.createHorizontalStrut(padding), BorderLayout.WEST);
        pnlTop.validate();


        // Finilize
        // ----------------------------------------------------------------.

        pnlInteractivePane.add(pnlTop, BorderLayout.NORTH);
        pnlInteractivePane.add(pnlBottomContainer, BorderLayout.SOUTH);
        pnlInteractivePane.validate();

        if (coverImagePane.getImageURL() == null
            || coverImagePane.getImageURL().equalsIgnoreCase(
                        "library_noGameFound.png")) {
            //Loading Thread
            gameCoverThread = null;

            if (gameCoverThread == null) {
                gameCoverThread = new Thread(this);
            }
            gameCoverThread.setName("Game Cover Thread");

            //Start Loader
            gameCoverThread.start();
        }

        this.removeAll();
        this.add(pnlInteractivePane, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

    }

    @Override
    public final void run() {

        if (Thread.currentThread() == gameCoverThread) {

            if (!java.util.Arrays.asList(this.getComponents())
                    .contains(progressWheel)) {
                progressWheel = new AProgressWheel("Aurora_Loader.png");
                progressWheel.setPreferredSize(this.getPreferredSize());
                this.add(progressWheel, BorderLayout.NORTH);

            }

            fileIO = dashboardUI.getStartUI().getFileIO();
            localGameRootPath = fileIO.getPath() + "Game Data//";

            // Try to Get Image Locally //
            Boolean loadedImage = true;
            try {
                localImage = fileIO.findImg("Game Data",
                                            coverURL);
            } catch (Exception ex) {
                loadedImage = false;
            }
            if (localImage != null && loadedImage) {

                coverImagePane.setImage(localImage,
                                        width, height);
                coverImagePane.setDoubleBuffered(true);

            } else if (coverImagePane.getImgIcon() == null) {

                // Load Image From S3 //
                try {

                    // Prevent too many games loading at once
                    int rand = (int) (Math.random() * 100) + (200 - 100);
                    try {
                        Thread.sleep(rand);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Game.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }

                    if (WelcomeLogic.checkOnline(rootCoverDBPath + coverURL)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(coverURL);
                        }

                        coverImagePane.setURL(rootCoverDBPath + coverURL);

                        //Set Background accordingly
                        if (coverImagePane.getImgIcon().getIconHeight() == -1) {

                            coverImagePane.setImage("library_noGameFound.png");

                        } else {
                            //Save image locally
                            fileIO.writeImage(
                                    coverImagePane, coverURL, "Game Data");

                        }
                    } else if (coverImagePane.checkImageExists(coverURL)) {

                        coverImagePane.setImageURL(coverURL);

                    } else {

                        coverImagePane.setImage("library_noGameFound.png");

                    }

                    //Set Background accordingly
                    coverImagePane.setImageSize(width, height);
                    coverImagePane.setPreferredSize(new Dimension(width,
                                                                  height));

                } catch (MalformedURLException ex) {
                    logger.error(ex);
                }
            }

            coverImagePane.setImageSize(width, height);
            coverImagePane.setPreferredSize(new Dimension(width,
                                                          height));

            this.setImage(coverImagePane);
            this.add(pnlInteractivePane);
            this.revalidate();
            this.repaint();
        }
        //End of Loading

        isLoaded = true;

        //Finalize
        afterLoad();

    }

    /**
     * .-----------------------------------------------------------------------.
     * | afterLoad();
     * .-----------------------------------------------------------------------.
     * |
     * | A method that is called after the Thread has completed its load.
     * | The method checks for what state it needs to be in terms of
     * | Being Selected, and Being Favorite.
     * |
     * .........................................................................
     * <p/>
     *
     */
    private void afterLoad() {
        AThreadWorker afterLoad = new AThreadWorker(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (isLoaded) {

                    if (isSelected) {
                        setSelected();
                    }

                    if (isFavorite) {
                        setFavorite();
                    }

                }

                try {
                    if (localImage == null) {
                        Thread.sleep(100);
                    }
                    if (progressWheel != null) {
                        progressWheel.stop();
                        remove(progressWheel);
                        progressWheel.setVisible(false);
                    }

                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Game.class.getName()).
                            log(Level.SEVERE, null, ex);
                }

                revalidate();
                repaint();
                if (postLoad != null) {
                    postLoad.doAction();
                }


            }

        });

        afterLoad.startOnce();

    }

    public void setPostLoad(APostHandler postLoad) {
        this.postLoad = postLoad;
    }

    /**
     * .-----------------------------------------------------------------------.
     * | reAddInteractive();
     * .-----------------------------------------------------------------------.
     * |
     * | This method is called when we want to re-add the
     * | Overlay UI on the Game Covers
     * |
     * .........................................................................
     * <p/>
     *
     */
    public final void reAddInteractive() {

        isOverlayUIRemoved = false;
        setSize();

        // Remove all and re-add
        pnlOverlayButtonContainer.removeAll();
        pnlOverlayButtonContainer.add(btnFavorite);
        pnlOverlayButtonContainer.add(btnPlay);
        pnlOverlayButtonContainer.add(btnFlip);
        pnlOverlayButtonContainer.validate();


        pnlOverlayBar.removeAll();
        pnlOverlayBar.setVisible(false);
        pnlOverlayBar.add(pnlOverlayButtonContainer);
        pnlOverlayBar.setOpaque(false);
        pnlOverlayBar.validate();

        pnlTopContainer.removeAll();
        pnlTopContainer.add(btnRemove, BorderLayout.EAST);
        pnlTopContainer.add(pnlFavContainer, BorderLayout.WEST);
        pnlTopContainer.validate();

        pnlTop.removeAll();
        pnlTop.setPreferredSize(new Dimension(width - (2 * padding), 55));
        pnlTop.add(Box.createHorizontalStrut(padding - 5), BorderLayout.EAST);
        pnlTop.add(pnlTopContainer, BorderLayout.CENTER);
        pnlTop.add(Box.createHorizontalStrut(padding), BorderLayout.WEST);
        pnlTop.validate();

        pnlInteractivePane.setVisible(true);
        pnlInteractivePane.removeAll();
        pnlInteractivePane.add(pnlTop, BorderLayout.NORTH);
        pnlInteractivePane.add(pnlBottomContainer, BorderLayout.SOUTH);

        if (pnlInteractivePane.getComponents().length > 2) {
            pnlInteractivePane.remove(1);
        }
        pnlInteractivePane.revalidate();


        // load selected and star
        afterLoad();

        this.repaint();
    }

    /**
     * .-----------------------------------------------------------------------.
     * | enableEditCoverOverlay();
     * .-----------------------------------------------------------------------.
     * |
     * | Enables the ability to add a custom cover art image to game by clicking
     * | on the game.
     * |
     * .........................................................................
     * <p/>
     *
     */
    public final void enableEditCoverOverlay() {

        this.removeAll();
        this.revalidate();
        if (isOverlayUIRemoved) {
            addOverlayUI();
        }

        if (pnlInteractivePane.getMouseListeners().length > 0) {
            pnlInteractivePane.removeMouseListener(
                    pnlInteractivePane.getMouseListeners()[0]);
        }

        if (this.getMouseListeners().length > 0) {
            this.removeMouseListener(this.getMouseListeners()[0]);
        }

        pnlInteractivePane.removeAll();
        pnlInteractivePane.setVisible(true);
        pnlInteractivePane
                .setPreferredSize(new Dimension(width, height));
        pnlInteractivePane.setLayout(new BorderLayout());
        pnlInteractivePane.revalidate();
        btnAddCustomOverlay.setButtonSize(width, height);
        btnAddCustomOverlay.setVisible(false);
        btnAddCustomOverlay.setMargin(new Insets(0, 0, 0, 0));

        if (overlayMouseListener == null) {
            overlayMouseListener = new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    btnAddCustomOverlay.setVisible(true);
                }

            };
        }

        pnlInteractivePane.addMouseListener(overlayMouseListener);



        btnAddCustomOverlay.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                btnAddCustomOverlay.setVisible(false);
            }
        });

        pnlInteractivePane.add(btnAddCustomOverlay);
    }

    /**
     * .-----------------------------------------------------------------------.
     * | disableEditCoverOverlay();
     * .-----------------------------------------------------------------------.
     * |
     * | disables the ability to add a custom cover art image to game by
     * clicking
     * | on the game.
     * |
     * .........................................................................
     * <p/>
     *
     */
    public final void disableEditCoverOverlay() {

        if (isOverlayUIRemoved) {
            addOverlayUI();
        }

        if (pnlInteractivePane.getMouseListeners()[0].equals(
                overlayMouseListener)) {
            pnlInteractivePane.removeMouseListener(overlayMouseListener);
        }

        pnlInteractivePane.addMouseListener(gameClickListener);

    }

    /**
     * .-----------------------------------------------------------------------.
     * | addTime(int minDiff, int hoursDiff)
     * .-----------------------------------------------------------------------.
     * |
     * | This method is the only way to increase the timePlayed value,
     * | you have to give the number of minutes and the number of hours.
     * | The method will accept 0 as a value and calculates everything in method
     * |
     * .........................................................................
     * <p/>
     * @param minDiff   Integer
     * @param hoursDiff Integer
     */
    public final void addTime(final int minDiff, final int hoursDiff) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        if (timePlayed == null || timePlayed.equals("null")) {
            timePlayed = "00:00";
        }
        try {
            d = df.parse(timePlayed);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, minDiff);
        cal.add(Calendar.HOUR, hoursDiff);

        timePlayed = df.format(cal.getTime());

    }

    /**
     * .-----------------------------------------------------------------------.
     * | unSelectPrevious()
     * .-----------------------------------------------------------------------.
     * |
     * | Uses the GridManager to un-select any previously setSelected game
     * |
     * .........................................................................
     * <p/>
     */
    public final void unSelectPrevious() {
        if (gridManager != null) {
            gridManager.unselectPrevious(null);
        }

    }

    /**
     * .-----------------------------------------------------------------------.
     * | setCoverSize(int width, int height)
     * .-----------------------------------------------------------------------.
     * |
     * | Method that sets the Size of the Game Cover
     * |
     * .........................................................................
     * <p/>
     * @param coverWidth  Integer
     * @param coverHeight Integer
     */
    public final void setCoverSize(final int coverWidth, final int coverHeight) {
        this.width = coverWidth;
        this.height = coverHeight;
        this.setImageSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        setSize();
    }

    /**
     * .-----------------------------------------------------------------------.
     * | setSelected()
     * .-----------------------------------------------------------------------.
     * |
     * | Method that selects the Game by showing the glow
     * |
     * .........................................................................
     * <p/>
     */
    public final void setSelected() {
        isSelected = true;
        if (isLoaded) {
            this.add(imgSelectedGlow);
            this.revalidate();
            this.repaint();

            libraryLogic.getLibraryUI().showEnterKeyIcon();
            coreUI.getInputController().setListener_A_Button(
                    new EnterKeyHandler());
            coreUI.getInputController().setListener_Y_Button(
                    new FlipButtonListener());

            canShowGameInfoInLibraryStatusBar = true;
            tranisionBetweenGameInfoInLibraryStatusBar();

        }

    }

    /**
     * .-----------------------------------------------------------------------.
     * | setUnselected()
     * .-----------------------------------------------------------------------.
     * |
     * | Method to de-select a game cover to show the overlay UI and glow
     * |
     * .........................................................................
     * <p/>
     */
    public final void setUnselected() {

        if (isSelected) {

            if (isGameRemoveMode) {
                new CancelRemoveGameHandler().actionPerformed(null);
                pnlOverlayBar.setVisible(false);
            }

            isSelected = false;
            btnRemove.setVisible(false);
            pnlInteractivePane.revalidate();
            this.remove(imgSelectedGlow);
            this.repaint();
            this.revalidate();

            libraryLogic.getLibraryUI().hideEnterKeyIcon();
            coreUI.getInputController().clearListener_A_Button();
            coreUI.getInputController().clearListener_Y_Button();

        }
        canShowGameInfoInLibraryStatusBar = false;
    }

    /**
     * .-----------------------------------------------------------------------.
     * | hideOverlayUI()
     * .-----------------------------------------------------------------------.
     * |
     * | hides the Overlay UI that shows up when you click on a game
     * | but does not hid the glow or star
     * |
     * .........................................................................
     * <p/>
     */
    public final void hideOverlayUI() {
        canShowGameInfoInLibraryStatusBar = false;
        if (isLoaded) {
            btnRemove.setVisible(false);
            pnlInteractivePane.revalidate();
        }
        pnlOverlayBar.setVisible(false);
        setUnselected();
    }

    /**
     * .-----------------------------------------------------------------------.
     * | showOverlayUI()
     * .-----------------------------------------------------------------------.
     * |
     * | shows the overlay/Interactive pane with the remove button and selects
     * | game.
     * |
     * .........................................................................
     * <p/>
     */
    public final void showOverlayUI() {

        if (!isSelected) {

            requestFocusInWindow();

            showRemoveBtn();
            pnlOverlayBar.setVisible(true);
            setSelected();

            LibraryUI.lblLibraryStatus.setFont(LibraryUI.lblLibraryStatus
                    .getFont()
                    .deriveFont(Font.PLAIN, LibraryUI.gameNameFontSize));
            LibraryUI.lblLibraryStatus.setForeground(Color.lightGray);
            LibraryUI.lblLibraryStatus.setText(getName());
        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | removeOverlayUI()
     * .-----------------------------------------------------------------------.
     * |
     * | Completely removes the Overlay UI from game cover. This is used if
     * | the game cover is only for displaying the game icon
     * |
     * .........................................................................
     * <p/>
     */
    public final void removeOverlayUI() {
        this.remove(pnlInteractivePane);
        this.isOverlayUIRemoved = true;
    }

    public final void addOverlayUI() {
        this.add(pnlInteractivePane);
        this.isOverlayUIRemoved = false;
    }

    public final void setFavorite() {

        isFavorite = true;
        if (isLoaded) {
            imgFavIcon.setVisible(true);
            pnlInteractivePane.revalidate();
        }

    }

    public final void setUnFavorite() {
        if (isFavorite) {
            isFavorite = false;
            imgFavIcon.setVisible(false);
            pnlInteractivePane.revalidate();
        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | animateFavouriteMove()
     * .-----------------------------------------------------------------------.
     * |
     * | Replaces the Game Cover and shows the Favorited Image cover for a
     * | second then re-shows the game cover.
     * |
     * | This is used as a visual cue to indicate that a game has been favorited
     * | and moved because of that.
     * |
     * .........................................................................
     * <p/>
     */
    private void animateFavouriteMove() {

        hideOverlayUI();
        revalidate();
        thisGame().setEnabled(false);
        Game temp = thisGame();

        AImagePane favouritedImg = new AImagePane("library_favourited_bg.png",
                                                  width, height);

        thisGame().clearImage();
        thisGame().setImage(favouritedImg);

        try {
            LibraryUI.lblLibraryStatus.setForeground(Color.yellow);
            LibraryUI.lblLibraryStatus.setText("Favorited a Game");

            Thread.sleep(900);
            thisGame().repaint();
            thisGame().revalidate();

            LibraryUI.lblLibraryStatus.setForeground(Color.lightGray);
            LibraryUI.lblLibraryStatus.setText(getName());

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        thisGame().setEnabled(true);
        thisGame().setVisible(false);

        thisGame().clearImage();
        thisGame().setImage(temp.getCoverImagePane().getImgIcon(),
                            height, width);
        select();
    }

    /**
     * .-----------------------------------------------------------------------.
     * | animateUnFavouriteMove()
     * .-----------------------------------------------------------------------.
     * |
     * | Replaces the Game Cover and shows the Un-Favorited Image cover for a
     * | second then re-shows the game cover.
     * |
     * | This is used as a visual cue to indicate that a game has been
     * | un-favorited
     * | and moved because of that.
     * |
     * .........................................................................
     * <p/>
     */
    private void animateUnFavouriteMove() {

        hideOverlayUI();
        revalidate();
        thisGame().setEnabled(false);
        Game temp = thisGame();

        AImagePane favouritedImg = new AImagePane("library_unfavourited_bg.png",
                                                  width, height);

        thisGame().clearImage();
        thisGame().setImage(favouritedImg);

        try {

            LibraryUI.lblLibraryStatus.setForeground(Color.yellow);
            LibraryUI.lblLibraryStatus.setText("Un-Favorited a Game");

            Thread.sleep(900);
            thisGame().repaint();
            thisGame().revalidate();

            LibraryUI.lblLibraryStatus.setForeground(Color.lightGray);
            LibraryUI.lblLibraryStatus.setText(getName());

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        thisGame().setEnabled(true);
        thisGame().setVisible(false);

        thisGame().clearImage();
        thisGame().setImage(temp.getCoverImagePane().getImgIcon(),
                            height, width);
        select();
    }

    public final void showRemoveBtn() {

        if (isLoaded) {
            btnRemove.setVisible(true);
            pnlInteractivePane.revalidate();
        }
    }

    /**
     * Pass the Library Logic Object to this game
     * <p/>
     * @param logic
     */
    public final void setLibraryLogic(LibraryLogic logic) {

        this.libraryLogic = logic;

    }

    public LibraryLogic getLibraryLogic() {
        return libraryLogic;
    }

    public void setSettingsListener(final ActionListener action) {
        this.settingsListener = action;
        if (btnSetting.getActionListeners().length == 0) {
            btnSetting.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    settingsListener.actionPerformed(new ActionEvent(thisGame(),
                                                                     0, ""));
                }
            });
        }
    }

    public ActionListener getSettingsListener() {
        return settingsListener;
    }

    /**
     * Saves the Metadata associated with this Game to the Stored Profile
     *
     */
    public final void saveMetadata() {
        storage.getStoredProfile().saveGameMetadata(this);

        if (storage.getStoredSettings().getSettingValue("organize")
                .equalsIgnoreCase("most played") && libraryLogic != null) {

            libraryLogic.addGamesToLibrary();
        }

    }

    public GridManager getManager() {
        return gridManager;
    }

    private void tranisionBetweenGameInfoInLibraryStatusBar() {
        if (!isTransisioningBetweenGameInfo) {
            AThreadWorker run = new AThreadWorker();
            run.setAction(new TransisionBetweenGameInfo(run));

            run.start();
        }
    }

    public void setCanShowGameInfoInLibraryStatusBar(
            boolean canShowGameInfoInLibraryStatusBar) {
        this.canShowGameInfoInLibraryStatusBar
        = canShowGameInfoInLibraryStatusBar;
    }

    public void setGameRemoved(boolean gameRemoved) {
        this.gameRemoved = gameRemoved;
    }

    public boolean isIsTransisioningBetweenGameInfo() {
        return isTransisioningBetweenGameInfo;
    }

    public void setIsTransisioningBetweenGameInfo(
            boolean isTransisioningBetweenGameInfo) {
        this.isTransisioningBetweenGameInfo = isTransisioningBetweenGameInfo;
    }

    public InteractiveListener getGameClickListener() {
        return gameClickListener;
    }

    private class TransisionBetweenGameInfo implements ActionListener {

        private final AThreadWorker run;

        public TransisionBetweenGameInfo(AThreadWorker run) {
            this.run = run;
        }

        int count = 0;

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                Thread.sleep(2700);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Game.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

            if (canShowGameInfoInLibraryStatusBar && !gameRemoved && isSelected) {

                isTransisioningBetweenGameInfo = true;

                count++;

                switch (count) {
                    case 1: // Times Played

                        LibraryUI.lblLibraryStatus.setFont(
                                LibraryUI.lblLibraryStatus
                                .getFont()
                                .deriveFont(Font.PLAIN,
                                            LibraryUI.gameNameFontSize));
                        LibraryUI.lblLibraryStatus.setForeground(
                                LibraryUI.DEFAULT_LIBRARY_COLOR);
                        String timesPlayed = parseTotalOccurence();
                        if (timesPlayed.equals("None")) {
                            LibraryUI.lblLibraryStatus.setText("Never Played");
                            count = 3;
                        } else {
                            LibraryUI.lblLibraryStatus.setText("Played - "
                                                               + timesPlayed);
                        }

                        break;
                    case 2: // Total Time Played

                        LibraryUI.lblLibraryStatus.setFont(
                                LibraryUI.lblLibraryStatus
                                .getFont()
                                .deriveFont(Font.PLAIN,
                                            LibraryUI.gameNameFontSize));
                        LibraryUI.lblLibraryStatus.setForeground(
                                LibraryUI.DEFAULT_LIBRARY_COLOR);
                        LibraryUI.lblLibraryStatus.setText("Played for - "
                                                           + parseTotalTimePlayed());

                        break;
                    case 3: // Last Played
                        LibraryUI.lblLibraryStatus.setFont(
                                LibraryUI.lblLibraryStatus
                                .getFont()
                                .deriveFont(Font.PLAIN,
                                            LibraryUI.gameNameFontSize));
                        LibraryUI.lblLibraryStatus.setForeground(
                                LibraryUI.DEFAULT_LIBRARY_COLOR);
                        LibraryUI.lblLibraryStatus.setText("Last Played - "
                                                           + parseLastTimePlayed());

                        break;
                    case 4:
                        LibraryUI.lblLibraryStatus.setFont(
                                LibraryUI.lblLibraryStatus
                                .getFont()
                                .deriveFont(Font.PLAIN,
                                            LibraryUI.gameNameFontSize));
                        LibraryUI.lblLibraryStatus.setForeground(
                                LibraryUI.DEFAULT_LIBRARY_COLOR);
                        LibraryUI.lblLibraryStatus.setText(getGameName());
                        // reset to first case
                        count = 0;
                        break;
                }



            } else {
                run.stop();
                isTransisioningBetweenGameInfo = false;
            }
        }
    }

    private class EnterGameTypeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            txtGameType.getTextBox().setFocusable(false);
            txtGameType.getTextBox().setFocusable(true);

        }
    }

    private class EnterKeyHandler extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isSelected) {
                getPlayHandler().actionPerformed(null);
            }
        }

    }

    private class GameTypeListener implements FocusListener {

        public GameTypeListener() {
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (txtGameType.getTextBox().getText().length() > 0) {
                setGameType(txtGameType.getText());
                saveMetadata();
            }
        }
    }

    public class FlipButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Flip button pressed");
            }

            if (isLoaded) {

                flip();

            }
        }
    }

    private Game tempGame;

    public void flip() {

        String soundEffectsSetting = storage.getStoredSettings()
                .getSettingValue("sound_effects");
        if (soundEffectsSetting == null) {
            soundEffectsSetting = SettingsLogic.DEFAULT_SFX_SETTING;
        }

        if (!isFliped) { // Flip Game

            // Sound FX
            if (soundEffectsSetting.equals("enabled")) {
                ASound flipSFX = new ASound("tick_3.wav", false);
                flipSFX.Play();
            }



            // Replace Game Cover art with Fliped image //
            tempGame = thisGame();

            thisGame().clearImage();
            thisGame().setImage("Reverse-Case.png", height, width);

            btnFlip.setButtonStates("game_btn_reverseLeft_norm.png",
                                    "game_btn_reverseLeft_down.png",
                                    "game_btn_reverseLeft_over.png");

            pnlOverlayButtonContainer.removeAll();
            pnlOverlayButtonContainer.validate();

            pnlOverlayButtonContainer.add(btnAward);
            pnlOverlayButtonContainer.add(btnSetting);
            pnlOverlayButtonContainer.add(btnFlip);
            pnlOverlayButtonContainer.revalidate();

            if (isFlipUIReady) {
                flipGame();
            } else {
                setUpFlipedUI();
            }

            thisGame().revalidate();
            isFliped = true;

        } else { // Un-flip game

            if (soundEffectsSetting.equals("enabled")) {
                ASound flipSFX = new ASound("tick_4.wav", false);
                flipSFX.Play();
            }

            // replace with
            thisGame().clearImage();
            thisGame().setImage(tempGame.getCoverImagePane()
                    .getImgIcon(),
                                height, width);
            btnFlip.setButtonStates("game_btn_reverseRight_norm.png",
                                    "game_btn_reverseRight_down.png",
                                    "game_btn_reverseRight_over.png");

            // reset to normal overlay UI //
            reAddInteractive();
            isSelected = false;
            showOverlayUI();

            thisGame().revalidate();
            isFliped = false;
        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | flipGame()
     * .-----------------------------------------------------------------------.
     * |
     * | Shows the flip UI which has been loaded using the setUpFlipedUI()
     * |
     * .........................................................................
     * <p/>
     */
    private void flipGame() {

        canShowGameInfoInLibraryStatusBar = false;
        LibraryUI.lblLibraryStatus.setForeground(
                LibraryUI.DEFAULT_LIBRARY_COLOR);
        LibraryUI.lblLibraryStatus.setText(getGameName());

        pnlTopContainer.removeAll();
        pnlTopContainer.revalidate();

        pnlTopContainer.add(Box.createVerticalStrut(OVERLAY_HEIGHT / 5),
                            BorderLayout.NORTH);
        pnlTopContainer.add(pnlShortcutContainer, BorderLayout.CENTER);
        pnlTopContainer.add(Box.createVerticalStrut(OVERLAY_HEIGHT / 10),
                            BorderLayout.SOUTH);
        pnlTop.setPreferredSize(pnlTopContainer.getPreferredSize());
        pnlTopContainer.revalidate();

        pnlInteractivePane.add(pnlFlipContainer, BorderLayout.CENTER, 1);
        pnlInteractivePane.revalidate();

        // Hours Played
        // ----------------------------------------------------------------.
        txtHoursPlayed.setText(parseTotalTimePlayed());
        txtHoursPlayed.getTextBox().setEnabled(false);
        txtHoursPlayed.revalidate();

        // Occurences Played
        // ----------------------------------------------------------------.
        txtTimesPlayed.getTextBox().setEnabled(false);
        txtTimesPlayed.setText(parseTotalOccurence());
        txtTimesPlayed.revalidate();

        // Last Time Played
        // ----------------------------------------------------------------.
        txtLastPlayed.getTextBox().setEnabled(false);

        // Calculate days past //
        txtLastPlayed.setText(parseLastTimePlayed());
        txtLastPlayed.revalidate();

        // Game Type
        // ----------------------------------------------------------------.
        txtGameType.setText(parseGameType());

        // analytics
        libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                .addProperty("Game Fliped", this.name);
        libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                .sendEventProperty("Fliped Game");

    }

    private int textBoxWidth;

    private int textBoxHeight;

    /**
     * .-----------------------------------------------------------------------.
     * | setUpFlipedUI()
     * .-----------------------------------------------------------------------.
     * |
     * | Sets up the UI that is shown when the Flip button on the game is
     * | selected.
     * |
     * .........................................................................
     * <p/>
     */
    private void setUpFlipedUI() {

        // Create main Panels
        // ----------------------------------------------------------------.

        // Shortcut Pane
        int flipShortcutWidth = pnlTopContainer.getSize().width - OVERLAY_WIDTH
                                                                  / 4;
        int flipShortcutHeight = (int) ((double) flipShortcutWidth / 3.2);
        int btnWidth = flipShortcutWidth / 4;
        int btnHeight = (int) ((double) btnWidth / 1.063);

        btnWatch = new AButton("game_btn_watch_norm.png",
                               "game_btn_watch_down.png",
                               "game_btn_watch_over.png", btnWidth, btnHeight);
        btnWatch.addActionListener(new WatchListener());
        btnWatch.setBorder(null);
        if (main.LAUNCHES < 5) {
            btnWatch.setToolTipText("Gameplay Videos");
        }

        btnFix = new AButton("game_btn_help_norm.png",
                             "game_btn_help_down.png",
                             "game_btn_help_over.png", btnWidth, btnHeight);
        btnFix.addActionListener(new FixListener());
        btnFix.setBorder(null);
        if (main.LAUNCHES < 5) {
            btnFix.setToolTipText("PC Gaming Wiki");
        }

        btnLearn = new AButton("game_btn_learn_norm.png",
                               "game_btn_learn_down.png",
                               "game_btn_learn_over.png", btnWidth, btnHeight);
        btnLearn.addActionListener(new LearnListener());
        btnLearn.setBorder(null);
        if (main.LAUNCHES < 5) {
            btnLearn.setToolTipText("Wikia");
        }

        pnlShortcutBtn = new JPanel(new FlowLayout(FlowLayout.CENTER,
                                                   flipShortcutWidth / 25, 0));
        pnlShortcutBtn.setOpaque(false);
        pnlShortcutBtn.setPreferredSize(new Dimension(flipShortcutWidth,
                                                      btnHeight));

        // Add Shortcut buttons to panel
        pnlShortcutBtn.add(btnWatch);
        pnlShortcutBtn.add(btnFix);
        pnlShortcutBtn.add(btnLearn);


        // Shortcut label to bottom
        lblShortcut = new ASlickLabel(" Shortcut");
        lblShortcut.setFont(this.coreUI.getRopaFont().deriveFont(Font.PLAIN,
                                                                 11));
        lblShortcut.setForeground(new Color(102, 102, 102));

        pnlShortcutLbl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -3));
        pnlShortcutLbl.setOpaque(false);
        pnlShortcutLbl.setPreferredSize(new Dimension(width - width / 4, 10));

        pnlShortcutLbl.add(lblShortcut);

        pnlShortcut = new AImagePane("game_flip_shortcutsBG.png",
                                     flipShortcutWidth,
                                     flipShortcutHeight);
        pnlShortcut.setPreferredSize(
                new Dimension(flipShortcutWidth, flipShortcutHeight));
        pnlShortcut.setLayout(new BoxLayout(pnlShortcut, BoxLayout.Y_AXIS));
        pnlShortcut.add(Box.createVerticalStrut(3));



        // Set up Shortcut panel
        pnlShortcut.add(pnlShortcutBtn);
        pnlShortcut.add(pnlShortcutLbl);

        pnlShortcutContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlShortcutContainer.setOpaque(false);

        pnlShortcutContainer.add(pnlShortcut);
        pnlShortcutContainer.add(Box.createHorizontalStrut(OVERLAY_WIDTH / 13));

        // Content Pane
        pnlFlipContentPane = new JPanel();
        pnlFlipContentPane.setOpaque(false);
        pnlFlipContentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlFlipContentPane.addMouseListener(gameClickListener);


        // Scroll Bar and Scroll Panes
        // ----------------------------------------------------------------.
        flipScrollBar = new JScrollBar();
        flipScrollBar.setUnitIncrement(20);
        flipScrollBar.setUI(new AScrollBar("game_scrollBar.png",
                                           "game_scrollBarBG.png"));
        flipScrollBar.setPreferredSize(new Dimension(6, flipScrollBar
                                                     .getPreferredSize().height));

        pnlFlipScrollPane = new JScrollPane(pnlFlipContentPane,
                                            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pnlFlipScrollPane.setOpaque(false);
        pnlFlipScrollPane.getViewport().setOpaque(false);
        pnlFlipScrollPane.setVerticalScrollBar(flipScrollBar);
        pnlFlipScrollPane.setBorder(null);
        pnlFlipScrollPane.validate();

        // Labels and Textboxes
        // ----------------------------------------------------------------.
        // Lables
        lblHoursPlayed = new ASlickLabel("Hours Played");
        lblHoursPlayed.setFont(this.coreUI.getRopaFont().deriveFont(Font.PLAIN,
                                                                    labelFontSize
                                                                    - 2));
        lblHoursPlayed.setForeground(new Color(202, 202, 217));

        lblLastPlayed = new ASlickLabel("Last Played");
        lblLastPlayed.setFont(this.coreUI.getRopaFont().deriveFont(Font.PLAIN,
                                                                   labelFontSize
                                                                   - 2));
        lblLastPlayed.setForeground(new Color(202, 202, 217));

        lblTimesPlayed = new ASlickLabel("Times Played");
        lblTimesPlayed.setFont(this.coreUI.getRopaFont().deriveFont(Font.PLAIN,
                                                                    labelFontSize
                                                                    - 2));
        lblTimesPlayed.setForeground(new Color(202, 202, 217));

        lblGameType = new ASlickLabel("Game Genre");
        lblGameType.setFont(this.coreUI.getRopaFont().deriveFont(Font.PLAIN,
                                                                 labelFontSize
                                                                 - 2));
        lblGameType.setForeground(new Color(202, 202, 217));

        // Text boxes
        textBoxHeight = height / 12;
        textBoxWidth = OVERLAY_WIDTH - OVERLAY_WIDTH / 5;
        txtHoursPlayed = new ATextField("game_textLabel_inactive.png",
                                        "game_textLabel_active.png");
        txtHoursPlayed.setTextboxSize(textBoxWidth, textBoxHeight);
        txtHoursPlayed.getTextBox().setFont(this.coreUI.getRopaFont()
                .deriveFont(Font.PLAIN,
                            labelFontSize));
        txtHoursPlayed.getTextBox().setDisabledTextColor(new Color(0, 255, 0));

        txtTimesPlayed = new ATextField("game_textLabel_inactive.png",
                                        "game_textLabel_active.png");
        txtTimesPlayed.setTextboxSize(textBoxWidth, textBoxHeight);
        txtTimesPlayed.getTextBox().setFont(this.coreUI.getRopaFont()
                .deriveFont(Font.PLAIN,
                            labelFontSize));
        txtTimesPlayed.getTextBox().setDisabledTextColor(new Color(0, 255, 0));

        txtLastPlayed = new ATextField("game_textLabel_inactive.png",
                                       "game_textLabel_active.png");
        txtLastPlayed.setTextboxSize(textBoxWidth, textBoxHeight);
        txtLastPlayed.getTextBox().setFont(this.coreUI.getRopaFont()
                .deriveFont(Font.PLAIN,
                            labelFontSize));
        txtLastPlayed.getTextBox().setDisabledTextColor(new Color(0, 255, 0));

        txtGameType = new ATextField("game_textLabel_inactive.png",
                                     "game_textLabel_active.png");
        txtGameType.setTextboxSize(textBoxWidth, textBoxHeight);
        txtGameType.getTextBox().setFont(this.coreUI.getRopaFont()
                .deriveFont(Font.PLAIN,
                            labelFontSize));
        txtGameType.getTextBox().setCaretColor(Color.CYAN);
        txtGameType.getTextBox().setForeground(new Color(0, 255, 0));

        txtGameType.getTextBox().addFocusListener(new GameTypeListener());
        txtGameType.getTextBox().addActionListener(new EnterGameTypeListener());

        // Add To Panels
        // ----------------------------------------------------------------.

        // Right Pane
        pnlRightPane = new JPanel();
        pnlRightPane.setLayout(new GridLayout(4, 0, 0, 13));
        pnlRightPane.setOpaque(false);

        // Add to Right Panel
        lblHoursPlayed.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        txtHoursPlayed.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        JPanel pnlHoursPlayedLbl = new JPanel();
        pnlHoursPlayedLbl.setOpaque(false);
        pnlHoursPlayedLbl.setLayout(new BoxLayout(pnlHoursPlayedLbl,
                                                  BoxLayout.Y_AXIS));
        pnlHoursPlayedLbl.add(lblHoursPlayed);
        pnlHoursPlayedLbl.add(txtHoursPlayed);
        pnlRightPane.add(pnlHoursPlayedLbl);

        lblTimesPlayed.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        txtTimesPlayed.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        JPanel pnlTimesPlayedLbl = new JPanel();
        pnlTimesPlayedLbl.setLayout(new BoxLayout(pnlTimesPlayedLbl,
                                                  BoxLayout.Y_AXIS));
        pnlTimesPlayedLbl.setOpaque(false);
        pnlTimesPlayedLbl.add(lblTimesPlayed);
        pnlTimesPlayedLbl.add(txtTimesPlayed);
        pnlRightPane.add(pnlTimesPlayedLbl);

        lblLastPlayed.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        txtLastPlayed.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        JPanel pnlLastPlayedLbl = new JPanel();
        pnlLastPlayedLbl.setLayout(new BoxLayout(pnlLastPlayedLbl,
                                                 BoxLayout.Y_AXIS));
        pnlLastPlayedLbl.setOpaque(false);
        pnlLastPlayedLbl.add(lblLastPlayed);
        pnlLastPlayedLbl.add(txtLastPlayed);
        pnlRightPane.add(pnlLastPlayedLbl);

        lblGameType.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        txtGameType.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        JPanel pnlGameTypeLbl = new JPanel();
        pnlGameTypeLbl.setLayout(new BoxLayout(pnlGameTypeLbl,
                                               BoxLayout.Y_AXIS));
        pnlGameTypeLbl.setOpaque(false);
        pnlGameTypeLbl.add(lblGameType);
        pnlGameTypeLbl.add(txtGameType);
        pnlRightPane.add(pnlGameTypeLbl);

        // Add to Content Pane
        pnlFlipContentPane.add(pnlRightPane);

        // Add scroll pane to container
        pnlFlipContainer = new JPanel();
        pnlFlipContainer.setLayout(new BorderLayout());
        pnlFlipContainer.setOpaque(false);
        pnlFlipContainer.setPreferredSize(pnlFlipContentPane.getPreferredSize());
        pnlFlipContainer.add(pnlFlipScrollPane, BorderLayout.CENTER);
        pnlFlipContainer.add(Box.createHorizontalStrut(padding + padding / 3),
                             BorderLayout.EAST);
        pnlFlipContainer.add(Box
                .createHorizontalStrut(padding + padding / 2 + 4),
                             BorderLayout.WEST);



        isFlipUIReady = true;
        flipGame();



    }

    /**
     * .-----------------------------------------------------------------------.
     * | WatchListener
     * .-----------------------------------------------------------------------.
     * |
     * | Listener for the Watch shortcut button to link to the Youtube search
     * | results for the game
     * |
     * .........................................................................
     * <p/>
     */
    public class WatchListener implements ActionListener {

        public WatchListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String url = "http://www.youtube.com/results?search_query=";
            String gameName = getName().replace(" ", "+").replace("'", "");
            url += gameName + "+gameplay";

            try {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    java.util.logging.Logger.getLogger(Game.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Game.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                    .addProperty("Watch Shortcut", name);
            libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                    .sendEventProperty("Game Shortcut Launched");

        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | FixListener
     * .-----------------------------------------------------------------------.
     * |
     * | Listener for the Watch shortcut button to link to the PCgamingWiki
     * | search results for the game
     * |
     * .........................................................................
     * <p/>
     */
    public class FixListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String url = "http://pcgamingwiki.com/wiki/";
            String gameName = getName().replace(" ", "_").replace("'", "");
            url += gameName;

            try {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    java.util.logging.Logger.getLogger(Game.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Game.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                    .addProperty("Fix Shortcut", name);
            libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                    .sendEventProperty("Game Shortcut Launched");

        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | LearnListener
     * .-----------------------------------------------------------------------.
     * |
     * | Listener for the Watch shortcut button to link to the Wikia
     * | search results for the game
     * |
     * .........................................................................
     * <p/>
     */
    public class LearnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String url = "http://gaming.wikia.com/wiki/";
            String gameName = getName().trim().replace(" ", "_");
            url = url + gameName;

            try {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    java.util.logging.Logger.getLogger(Game.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Game.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                    .addProperty("Learn Shortcut", name);
            libraryLogic.getLibraryUI().getDashboardUI().getDashboardLogic().getAnalytics()
                    .sendEventProperty("Game Shortcut Launched");

        }
    }

    protected String parseTotalTimePlayed() {
        if (this.timePlayed != null && !this.timePlayed.equals("null")) {

            // Parse time //
            String hoursPlayed = timePlayed
                    .substring(0, timePlayed.indexOf(":"));
            String minutesPlayed = timePlayed.substring(timePlayed.indexOf(':')
                                                        + 1,
                                                        timePlayed.length());

            if (!hoursPlayed.equals("0")) {
                hoursPlayed = hoursPlayed.replaceFirst("0", "");
            }
            if (!minutesPlayed.equals("0")) {
                minutesPlayed = minutesPlayed.replaceFirst("0", "");
            }

            // convert to ints, check for plurals
            int hours = Integer.parseInt(hoursPlayed);
            int mins = Integer.parseInt(minutesPlayed);

            String hourTxt = " hr";
            String minTxt = " min";

            if (hours > 1) {
                hourTxt = " hrs";
            }
            if (mins > 1) {
                minTxt = " mins";
            }

            // parse to textbox
            if ((minutesPlayed.equals("0")) && (hoursPlayed.equals("0"))) {

                return "Under a min";

            } else if (minutesPlayed.equals("0")) {

                return hoursPlayed + hourTxt;

            } else if (hoursPlayed.equals("0")) {

                return minutesPlayed + minTxt;

            } else {

                return hoursPlayed + hourTxt + " "
                       + minutesPlayed
                       + minTxt;
            }
        } else {
            return "None";
        }
    }

    protected String parseTotalOccurence() {
        String occurence = Integer.toString(this.getOccurencesPlayed());
        if (occurence.equals("0")) {
            return "None";
        } else {
            if (Integer.parseInt(occurence) > 1) {
                return occurence + " Times";
            } else {
                return occurence + " Time";
            }
        }
    }

    protected String parseLastTimePlayed() {
        SimpleDateFormat format = new SimpleDateFormat(ATimeLabel.DATE);
        Date past = null;

        String daysPast;
        if (this.lastPlayed != null && !this.lastPlayed.equals("null")) {
            try {
                past = format.parse(lastPlayed);


            } catch (ParseException ex) {
                java.util.logging.Logger.getLogger(Game.class
                        .getName()).
                        log(Level.SEVERE, null, ex);
            }
            Date now = new Date();

            daysPast = Long.toString(TimeUnit.MILLISECONDS.toDays(now
                    .getTime() - past.getTime()));
            if (daysPast.equals("0")) {

                return "Today";

            } else {

                if (Integer.parseInt(daysPast) > 30) {
                    return "Over a month ago";
                } else if (Integer.parseInt(daysPast) > 1) {
                    return daysPast + " days ago";
                } else {
                    return "Yesterday";
                }

            }
        } else {

            return "Not Played";
        }
    }

    protected String parseGameType() {
        if (this.getGameType() != null && !this.getGameType().equals("null")) {
            return this.getGameType();
        } else {
            return "";
        }
    }

    boolean isFavoriting;

    boolean isUnfavoriting;

    boolean prevState;

    class FavoriteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Favourite button pressed");
            }

            AThreadWorker favWorker = new AThreadWorker(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            if (storage.getStoredSettings().getSettingValue(
                                    "organize")
                            .equalsIgnoreCase("Favorite")) {

                                if (isFavorite) {
                                    // Unfavoriting

                                    if (isFavoriting == false) {
                                        prevState = isFavorite;
                                    }

                                    isUnfavoriting = true;

                                    setUnFavorite();

                                    // Give time to change decision
                                    try {
                                        Thread.sleep(850);
                                    } catch (InterruptedException ex) {
                                        java.util.logging.Logger.getLogger(
                                                Game.class
                                                .getName()).
                                        log(Level.SEVERE, null, ex);
                                    }

                                    // Check if still favourited
                                    if (!isFavorite && !isFavoriting
                                        && prevState
                                           != isFavorite) {
                                        storage.getStoredLibrary().SaveFavState(
                                                thisGame());
                                        animateUnFavouriteMove();
                                        gridManager.moveUnfavorite(Game.this);
                                        thisGame().setVisible(true);

                                    }
                                    isUnfavoriting = false;

                                } else {
                                    //Favoriting

                                    if (isUnfavoriting == false) {
                                        prevState = isFavorite;
                                    }

                                    isFavoriting = true;
                                    setFavorite();

                                    // Give time to change decision
                                    try {
                                        Thread.sleep(850);
                                    } catch (InterruptedException ex) {
                                        java.util.logging.Logger.getLogger(
                                                Game.class
                                                .getName()).
                                        log(Level.SEVERE, null, ex);
                                    }

                                    // Check if still favourited
                                    if (isFavorite && !isUnfavoriting
                                        && prevState
                                           != isFavorite) {
                                        storage.getStoredLibrary().SaveFavState(
                                                thisGame());
                                        setUnFavorite();
                                        animateFavouriteMove();
                                        setFavorite();
                                        gridManager.moveFavorite(Game.this);
                                        thisGame().setVisible(true);

                                    }
                                    isFavoriting = false;

                                }
                            } else {
                                if (isFavorite) {

                                    // Unfavoriting
                                    setUnFavorite();

                                } else {

                                    //Favoriting
                                    setFavorite();

                                }

                            }

                        }
                    });

            favWorker.startOnce();

        }
    }

    class PlayButtonListener implements ActionListener {

        private AuroraLauncher launcher;

        @Override
        public void actionPerformed(final ActionEvent e) {

            if (logger.isDebugEnabled()) {
                logger.debug("Play button pressed");
            }

            if (AFileManager.checkFile(getGamePath())) {

                LibraryUI.lblLibraryStatus.setForeground(Color.green);
                LibraryUI.lblLibraryStatus.setText("Launching Game");
                canShowGameInfoInLibraryStatusBar = false;
                launcher = new AuroraLauncher(coreUI);
                launcher.launchGame(thisGame());

            } else {

                final ADialog info = new ADialog(ADialog.aDIALOG_WARNING,
                                                 "Can't Find Game. Would You Like To EDIT the Game Location?      ",
                                                 coreUI.getRegularFont()
                                                 .deriveFont(Font.BOLD, 23));
                info.setOKButtonListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        settingsListener.actionPerformed(e);
                        info.setVisible(false);
                    }
                });
                info.showDialog();
                info.setVisible(true);

            }

            LibraryUI.lblLibraryStatus.setForeground(Color.lightGray);
            LibraryUI.lblLibraryStatus.setText(getName());

        }
    }

    class RemoveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {

            // Top Pane

            int confirmWidth = width / 2 + 10;
            int confirmHeight = (int) ((double) confirmWidth / 4.5);

            ASlickLabel lblConfirmRemove = new ASlickLabel("  Remove Game?");
            lblConfirmRemove.setFont(coreUI.getRopaFont().deriveFont(Font.PLAIN,
                                                                     20));

            pnlConfirmRemove = new AImagePane(
                    "game_img_removeWarning.png", confirmWidth, confirmHeight);
            pnlConfirmRemove.setPreferredSize(new Dimension(confirmWidth,
                                                            confirmHeight));
            pnlConfirmRemove.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
            pnlConfirmRemove.add(lblConfirmRemove);

            pnlConfirmRemoveContainer = new JPanel();
            pnlConfirmRemoveContainer.setName("pnlConfirmRemove");
            pnlConfirmRemoveContainer.setOpaque(false);
            pnlConfirmRemoveContainer.setPreferredSize(pnlConfirmRemove
                    .getPreferredSize());

            pnlConfirmRemoveContainer.setLayout(new FlowLayout(FlowLayout.LEFT));




            int centerPadding = (OVERLAY_WIDTH / 2)
                                - pnlConfirmRemove.getPreferredSize().width
                                  / 2
                                - pnlFavContainer.getPreferredSize().width;
            pnlConfirmRemoveContainer.add(Box.createHorizontalStrut(
                    centerPadding));
            pnlConfirmRemoveContainer.add(pnlConfirmRemove);


            pnlTopContainer.remove(btnRemove);
            // This is to calculate top of game cover padding
//            pnlTopButtonContainer.add(Box.createVerticalStrut(height / 85), BorderLayout.NORTH);
            pnlTopContainer.add(pnlConfirmRemoveContainer, BorderLayout.CENTER);
            pnlTop.revalidate();


            // Bottom Pane

            confirmButton = new AButton("game_btn_removeYes_norm.png",
                                        "game_btn_removeYes_down.png",
                                        "game_btn_removeYes_over.png",
                                        pnlOverlayBar.getPreferredSize().width
                                        / 2,
                                        pnlOverlayBar.getPreferredSize().height);
            confirmButton.addActionListener(new RemoveGameHandler());
            confirmButton.setBorder(null);

            denyButton = new AButton("game_btn_removeNo_norm.png",
                                     "game_btn_removeNo_down.png",
                                     "game_btn_removeNo_over.png",
                                     pnlOverlayBar.getPreferredSize().width / 2,
                                     pnlOverlayBar.getPreferredSize().height);
            denyButton.addActionListener(new CancelRemoveGameHandler());
            denyButton.setBorder(null);

            pnlOverlayBar.removeAll();
            pnlOverlayBar.add(denyButton);
            pnlOverlayBar.add(confirmButton);
            pnlOverlayBar.revalidate();

            isGameRemoveMode = true;
            setSelected();



            System.out.println("=> Is Selected? " + isSelected);

        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | CancelRemoveGameHandler
     * .-----------------------------------------------------------------------.
     * |
     * | Handler when No button setSelected remove the Confirm Removal overlay
     * | and re-add original Game Overlay.
     * |
     * |
     * .........................................................................
     *
     * @author
     * <p/>
     */
    class CancelRemoveGameHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            showRemoveBtn();

            reAddInteractive();
            pnlOverlayBar.setVisible(true);
            isGameRemoveMode = false;
            setSelected();
        }
    }

    class RemoveGameHandler implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Remove button pressed for " + Game.this.getName());
            }

            AThreadWorker removeGame = new AThreadWorker(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    StoredLibrary libraryStorage = storage.getStoredLibrary();
                    if (libraryStorage.search(name)) {

                        // Delete cached cover art
                        try {
                            fileIO.deleteFile(new File(fileIO.getPath()
                                                       + "Game Data\\"
                                                       + getBoxArtUrl()));
                        } catch (IOException ex) {
                            logger.error(ex);
                        }

                        libraryStorage.removeGame(Game.this);
                        if (storage.getStoredProfile().getGameNames().size() > 0) {
                            storage.getStoredProfile().removeGameMetadata(
                                    Game.this);
                        }


                        // Notify user game has been deleted
                        LibraryUI.lblLibraryStatus.setForeground(new Color(194,
                                                                           40,
                                                                           35));
                        LibraryUI.lblLibraryStatus.setText("Removed Game");

                        LibraryLogic.refreshAuto = true;

                        gameRemoved = true;

                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException ex) {
                            java.util.logging.Logger.getLogger(Game.class
                                    .getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LibraryUI.lblLibraryStatus.setForeground(Color.lightGray);
                    LibraryUI.lblLibraryStatus.setText("Select a Game");
                }
            });

            removeGame.startOnce();
            if (libraryManager != null) {
                libraryManager.removeGame(libraryManager.echoGame(Game.this));
                gridManager.removeGame(Game.this);
            } else {
                gridManager.removeGame(Game.this);
            }

        }
    }

    private void select() {

        unSelectPrevious();
        showOverlayUI();
        canShowGameInfoInLibraryStatusBar = true;
    }

    //Game Cover Selected Handler
    class InteractiveListener extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            if (!isOverlayUIRemoved) {
                requestFocus();
                unSelectPrevious();


                if (SwingUtilities.isRightMouseButton(e)) {
                    btnFlip.getActionListeners()[0].actionPerformed(null);
                    showOverlayUI();

                } else {
                    if (isSelected) {
                        hideOverlayUI();
                    } else {
                        showOverlayUI();
                        if (logger.isDebugEnabled()) {
                            logger.debug("SELECTED");
                        }
                    }
                }



            }
        }

        @Override
        public void mouseEntered(final MouseEvent e
        ) {
            // Mouse being dragged over game
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {

                if (!isOverlayUIRemoved) {
                    requestFocus();
                    if (!isSelected()) {
                        unSelectPrevious();
                        showOverlayUI();
                    }
                }
            }
        }

    }

    private void setSize() {

        if (coreUI.isLargeScreen()) {
            labelFontSize = 18;
        } else {
            labelFontSize = 17;
        }

    }

    public void refresh() {

        try {
            coverImagePane.setURL(new File(localGameRootPath + coverURL)
                    .getPath());

        } catch (Exception ex) {
            coverImagePane.setURL(rootCoverDBPath + coverURL);
        }
        coverImagePane.setImageFileName(localGameRootPath + coverURL);

        coverImagePane.setImageSize(width, height);
        this.setImage(coverImagePane);
        coverImagePane.repaint();
        this.revalidate();
        this.repaint();
    }

    // Getters & Setters
    // -----------------------------------------------------------------------.
    public final AuroraStorage getStorage() {
        return storage;
    }

    public final boolean isLoaded() {
        return isLoaded;
    }

    public final ActionListener getPlayHandler() {
        return playButtonListener;
    }

    public final void setStorage(final AuroraStorage storage) {
        this.storage = storage;
    }

    public final AButton getFavoriteButton() {
        return btnFavorite;
    }

    public final void setFavoriteButton(final AButton favoriteButton) {
        this.btnFavorite = favoriteButton;
    }

    public final String getBoxArtUrl() {
        if (coverURL == null || coverURL.equals("")) {
            return coverImagePane.getImageURL();
        } else {
            return coverURL;
        }
    }

    public final JPanel getInteractivePane() {
        return pnlInteractivePane;
    }

    public final AImagePane getGameBar() {
        return pnlOverlayBar;
    }

    public boolean isFliped() {
        return isFliped;
    }

    public final boolean isSelected() {
        return isSelected;
    }

    public final boolean isFavorite() {
        return isFavorite;

    }

    public final String getGameType() {
        return gameType;

    }

    public final String getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public final int getWidth() {
        return width;
    }

    @Override
    public final int getHeight() {
        return height;
    }

    @Override
    public final String getName() {
        return name;
    }

    public final int getOccurencesPlayed() {
        return numberTimesPlayed;
    }

    public final String getTotalTimePlayed() {
        return timePlayed;
    }

    public final DashboardUI getDashboardUI() {
        return this.dashboardUI;
    }

    public final void setCoverUrl(final String coverUrl) {
        this.coverURL = coverUrl;

    }

    public final void setCoverImage(final String coverUrl) throws
            MalformedURLException {
        this.coverImagePane.setImageURL(coverUrl);

    }

    public final void setFavorite(final boolean favorite) {
        this.isFavorite = favorite;

        if (favorite) {
            setFavorite();
        }
    }

    public AButton getBtnFlip() {
        return btnFlip;
    }

    public final void setGameType(final String gameType) {
        this.gameType = gameType;
    }

    public final void setLastPlayed(final String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public final void setDashboardUI(final DashboardUI dashboardUi) {
        this.dashboardUI = dashboardUi;
    }

    public final void setGameName(final String name) {
        this.name = name;
    }

    public final String getGameName() {
        return name;
    }

    public final void setOcurrencesPlayed(final int numberTimesPlayed) {
        this.numberTimesPlayed = numberTimesPlayed;
    }

    public final void setTotalTimePlayed(final String timePlayed) {
        this.timePlayed = timePlayed;
    }

    public synchronized final Game copy() {
        try {
            return (Game) super.clone();
        } catch (CloneNotSupportedException ex) {
            logger.error(ex);
            return null;
        }
    }

    public AImagePane getCoverImagePane() {
        return coverImagePane;
    }

    public final Game thisGame() {
        return this;
    }

    public final void setGamePath(final String path) {

        if (logger.isDebugEnabled()) {
            logger.debug("Game path = " + path);
        }

        this.gamePath = path;
    }

    public final String getGamePath() {
        return this.gamePath;
    }

    public AButton getBtnAddCustomOverlay() {
        return btnAddCustomOverlay;
    }

    public String getCoverURL() {
        return coverURL;
    }

}
