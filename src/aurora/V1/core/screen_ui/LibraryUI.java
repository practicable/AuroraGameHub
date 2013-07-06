/*
 *  Made By Sardonix Creative.
 *
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by-nc-nd/3.0/
 *
 * or send a letter to Creative Commons, 444 Castro Street, ScoreUIte 900,
 * Mountain View, California, 94041, USA.
 * Unless reqcoreUIred by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aurora.V1.core.screen_ui;

import aurora.V1.core.*;
import aurora.V1.core.screen_handler.LibraryHandler;
import aurora.V1.core.screen_handler.LibraryHandler.GameLibraryKeyListener;
import aurora.V1.core.screen_handler.LibraryHandler.HoverButtonLeft;
import aurora.V1.core.screen_handler.LibraryHandler.HoverButtonRight;
import aurora.V1.core.screen_handler.LibraryHandler.ShowAddGameUiHandler;
import aurora.V1.core.screen_handler.LibraryHandler.SearchBoxHandler;
import aurora.V1.core.screen_handler.LibraryHandler.SearchFocusHandler;
import aurora.V1.core.screen_logic.LibraryLogic;
import aurora.engine.V1.Logic.*;
import aurora.engine.V1.UI.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

/**
 * .---------------------------------------------------------------------------.
 * | LibraryUI :: Aurora App Class
 * .---------------------------------------------------------------------------.
 * |
 * | This class contains the UI for the GameLibrary Screen which has a Handler
 * | and a Logic class associated to it.
 * |
 * | This is an AuroraApp meaning it also implements the AuroraScreenUI
 * | interface.
 * |
 * .............................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 * @author Carlos Machado <camachado@gmail.com>
 * <p/>
 */
public class LibraryUI extends AuroraApp {

    /**
     * Zoom In Button.
     */
    private AButton btnZoomPlus;

    /**
     * Zoom Out Button.
     */
    private AButton btnZoomLess;

    /**
     * Search Button to activate focus on Library Search Bar.
     */
    private AButton btnSearch;

    /**
     * Button to show add game UI.
     */
    private AButton btnShowAddGameUI;

    /**
     * Button to Exit out of Library Search.
     */
    private AButton removeSearchButton;

    /**
     * Button to Add Searched Game to Library.
     */
    private AButton btnGameToLib_addUI;

    /**
     * Button to Close Add Game UI.
     */
    private AButton btnClose_addUI;

    /**
     * Hover Button to navigate Right in Library.
     */
    private AHoverButton btnGameRight;

    /**
     * Hover Button to navigate Left in Library.
     */
    private AHoverButton btnGameLeft;

    /**
     * Panel Containing Hover Buttons and the Library Grids.
     */
    private JPanel paneLibraryContainer;

    /**
     * Panel Containing imgSelectedGamePane.
     */
    private JPanel pnlBottomCenterContainer;

    /**
     * Panel Containing SearchBarBG.
     */
    private JPanel pnlSearchBar;

    /**
     * Panel Containing Text box for Search Bar.
     */
    private JPanel pnlSearchText;

    /**
     * Panel Containing Button Panel and Search Text box Pane for Search Box.
     */
    private JPanel pnlSearchContainer;

    /**
     * Panel Containing Search Button for Search Box.
     */
    private JPanel pnlSearchButton;

    /**
     * AddGameUI Panel Containing Search box.
     */
    private JPanel pnlAddGameSearchContainer;

    /**
     * AddGameUI Panel Containing Center Content for picking game to add.
     */
    private JPanel pnlCenter_addUI;

    /**
     * AddGameUI Panel Containing Top part of Bottom Panel.
     */
    private JPanel pnTopOfBottom;

    /**
     * AddGameUI Panel Containing Top part of Center Panel.
     */
    private JPanel pnlTopOfCenter;

    /**
     * AddGameUI ImagePane Containing Left part of Center Panel.
     */
    private AImagePane pnlLeftOfTopCenter;

    /**
     * AddGameUI ImagePane Containing Right part of Top Part of Center Panel.
     */
    private AImagePane pnlRightOfTop;

    /**
     * AddGameUI Panel Containing Top part of Panel.
     */
    private JPanel pnlTopPane_addUI;

    /**
     * AddGameUI Panel Containing Left part of Bottom Part of Center Panel.
     */
    private JPanel pnlLeftOfBottom;

    /**
     * AddGameUI Panel Containing Right part of Bottom Part of Center Panel.
     */
    private JPanel pnlRightOfBottom;

    /**
     * AddGameUI Panel Containing Bottom part of Center Panel.
     */
    private JPanel pnlBottomOfCenter;

    /**
     * AddGameUI Panel Containing Right part of Bottom Panel.
     */
    private JPanel pnlRightOfBottomContainer;

    /**
     * AddGameUI Main Panel Containing All Other Panels.
     */
    private JPanel pnlAddGameContainer;

    /**
     * AddGameUI Glass Panel from current JFrame.
     */
    private JPanel pnlGlass;

    /**
     * AddGameUI Bottom panel.
     */
    private JPanel pnlBottomPane;

    /**
     * Panel Containing background image of Currently Selected game label.
     */
    private AImagePane imgLibraryStatusPane;

    /**
     * Background image for Search Bar.
     */
    private AImagePane pnlSearchBarBG;

    /**
     * AddGameUI Image Panel representing AddGameUI.
     */
    private AImagePane pnlAddGamePane;

    /**
     * Image Panel Containing Search Text Box for Search Bar.
     */
    private AImagePane pnlSearchBG;

    /**
     * AddGameUI Panel that contains the Game Cover.
     */
    private AImagePane pnlCoverPane_addUI;

    /**
     * AddGameUI Image that is a Blank Case as a place holder Game Cover.
     */
    private AImagePane pnlBlankCoverGame_addUI;

    /**
     * AddGameUI Background Image of Button Panel.
     */
    private AImagePane pnlSearchButtonBG;

    /**
     * AddGameUI Shortcut button to Steam games dir.
     */
    private AButton btnGoToSteam;

    /**
     * AddGameUI Shortcut button to Programs dir.
     */
    private AButton btnGoToProgram;

    /**
     * AddGameUI Pane containing Shortcut buttons.
     */
    private JPanel pnlRightOfTopEast;

    /**
     * Image for keyboard icon.
     */
    private AImage imgKeyIco;

    /**
     * Image for Favorite logo on side of library.
     */
    private AImage imgOrganizeTypeSideBar;

    /**
     * Image Step One badge.
     */
    private AImage statusBadge1;

    /**
     * Image Step Two badge.
     */
    private AImage statusBadge2;

    /**
     * AddGameUI Label explaining left side of panel.
     */
    private JLabel lblLeftTitle;

    /**
     * AddGameUI Label explaining right side of panel.
     */
    private JLabel lblRightTitle;

    /**
     * AddGameUI Label explaining Keyboard action.
     */
    private JLabel lblKeyAction;

    /**
     * AddGameUI Label with Title of Selected Game.
     */
    public static AFadeLabel lblLibraryStatus;

    private ArrayList<AImagePane> gameCover;

    private int zoom;

    private GridManager GridSplit;

    private int currentIndex;

    private ArrayList<Boolean> loadedPanels;

    private GridAnimation GridAnimate;

    private JTextField searchTextField;

    private LibraryHandler handler;

    private ASimpleDB CoverDB;

    private AAnimate addGameAnimator;

    private JTextField txtSearchField_addUI;

    private SearchBoxHandler searchBoxHandler;

    private SearchFocusHandler searchFocusHandler;

    private boolean addGameUI_Visible = false;

    private JList gamesList_addUI;

    private JScrollPane gameScrollPane;

    private JFileChooser gameFileChooser_addUI;

    private DefaultListModel listModel_addUI;

    private String currentPath;

    private AAnimate addGameToLibButtonAnimator;

    private AuroraStorage storage;

    private HoverButtonLeft moveLibraryLeftHandler;

    private HoverButtonRight moveLibraryRightHandler;

    private boolean isAddGameUILoaded = false;

    private boolean isGameLibraryKeyListenerAdded = false;

    private int SearchBarWidth;

    public static int gameCoverWidth;

    public static int zoomButtonHeight;

    public static int selectedGameBarHeight;

    public static int selectedGameBarWidth;

    public static int addGameWidth;

    public static int addGameHeight;

    public static int gameNameFontSize;

    public static int gameCoverHeight;

    private boolean isScreenLoaded = false;

    private final DashboardUI dashboardUI;

    private final AuroraCoreUI coreUI;

    private final LibraryLogic logic;

    private AButton btnOrganizeGames;

    private int listFontSize;

    private int gridSearchFontSize;

    private int addGameFontSize;

    private int bottomTopPadding;

    static final Logger logger = Logger.getLogger(LibraryUI.class);

    private JPanel pnlRightOfTopEastContainer;

    private APopupMenu organizeMenu;

    private ARadioButtonManager organizeBtnManager;

    private ARadioButton btnBottom;

    private ARadioButton btnMiddle;

    private ARadioButton btnTop;

    private ASlickLabel lblFavorite;

    private ASlickLabel lblAlphabetic;

    private ASlickLabel lblMostPlayed;

    private AImage icoFavorite;

    private AImage icoAlphabetic;

    private AImage icoMostPlayed;

    private JPanel favoritePane;

    private JPanel alphabeticPane;

    private JPanel mostplayedPane;

    private AImagePane pnlEditGamePane;

    private AButton btnClose_editUI;

    private boolean isEditUILoaded = false;

    private JPanel pnlTopPane_editUI;

    private boolean editGameUI_Visible = false;

    private AAnimate editGameAnimator;

    private JPanel pnlAddGameType;

    private ARadioButton btnManual;

    private ARadioButton btnAuto;

    private AImagePane pnlRightPane_editUI;

    private JPanel pnlCenter_editUI;

    private ARadioButton btnGameLocation_editUI;

    private ARadioButton btnGameCover_editUI;

    private ARadioButton btnOther_editUI;

    private JPanel pnlTopRightPane_editUI;

    private JPanel pnlLeftPane_editUI;

    private ASlickLabel lblCurrentName_editUI;

    private ASlickLabel lblGameLocation_editUI;

    private ASlickLabel lblGameCover_editUI;

    private AButton btnDone_editUI;

    private ASlickLabel lblOther_editUI;

    private AImagePane imgCurrentGame_editUI;

    private JPanel pnlCenterRight_editUI;

    private JPanel pnlCurrentImage_editUI;

    private JPanel pnlCurrentName_editUI;

    private Game currentGame_editUI;

    private JPanel pnlGameLocation_editUI;

    private ASlickLabel lblCurrentLocation_editUI;

    private ASlickLabel lblNewLocation_editUI;

    private ATextField txtCurrentLocation_editUI;

    private ATextField txtNewLocation_editUI;

    private JFileChooser gameFileChooser_editUI;

    private JPanel pnlGameFileChooser_editUI;

    private JPanel pnlGameLocationTop;

    private JPanel pnlGameLocationBottom;

    private AImage imgGameLocationStatus;

    private JPanel pnlGameLocationCenter;

    private boolean isGameLocation = false;

    private boolean isGameCover = false;

    private boolean isOther = false;

    private AButton btnClearSearch_addUI;

    private JPanel pnlGameCover_editUI;

    private JPanel pnlGameCoverCenter;

    private AImage imgGameCoverStatus;

    private AImagePane pnlCoverPane_editUI;

    private AImagePane pnlBlankCoverGame_editUI;

    private JPanel pnlGameCoverContainer;

    private DefaultListModel listModel_editUI;

    private JList gamesList_editUI;

    private JPanel pnlGameCoverBottom;

    private ASlickLabel lblGameCoverSearch;

    private ATextField txtGameCoverSearch_editUI;

    private AButton btnClearSearch_editUI;

    /**
     * .-----------------------------------------------------------------------.
     * | LibraryUI(AuroraStorage, DashboardUI, AuroraCoreUI)
     * .-----------------------------------------------------------------------.
     * |
     * | This is the Constructor of the LibraryUI UI class.
     * |
     * | The Constructor of Screen Classes must initialize/create both a
     * | Handler and a Logic object which should contain the UI as a parameter
     * |
     * .........................................................................
     *
     * @param auroraStorage AuroraStorage
     * @param dashboardUi   DashboardUI
     * @param auroraCoreUI  AuroraCoreUI
     *
     */
    public LibraryUI(final AuroraStorage auroraStorage,
                     final DashboardUI dashboardUi,
                     final AuroraCoreUI auroraCoreUI) {
        this.appName = "Game Library";
        this.coreUI = auroraCoreUI;
        this.storage = auroraStorage;
        this.dashboardUI = dashboardUi;

        this.logic = new LibraryLogic(this);
        this.handler = new LibraryHandler(this);

        handler.setLogic(logic);
        logic.setHandler(handler);
    }

    @Override
    public final void loadUI() {

        setSize();

        // Load All UI Components
        // --------------------------------------------------------------------.

        //* Create Components for Library *//

        paneLibraryContainer = new JPanel(true);
        paneLibraryContainer.setBorder(BorderFactory.createEmptyBorder(4,
                0, 0, 0));

        imgOrganizeTypeSideBar = new AImage("library_favourites.png");

        btnGameRight = new AHoverButton(3,
                "library_navRight_norm.png", "library_navRight_over.png");
        btnGameLeft = new AHoverButton(3,
                "library_navLeft_norm.png", "library_navLeft_over.png");

        //* Key Board Naviagtion Icon *//
        imgKeyIco = new AImage("KeyboardKeys/arrows.png", coreUI
                .getKeyIconWidth(), coreUI.getKeyIconHeight());
        lblKeyAction = new JLabel(" Move ");


        //* Selected Game Name Bar *//
        pnlBottomCenterContainer = new JPanel(new FlowLayout(FlowLayout.CENTER,
                0, bottomTopPadding));

        imgLibraryStatusPane = new AImagePane("library_selectedGameBar_bg.png",
                selectedGameBarWidth, selectedGameBarHeight,
                new FlowLayout(FlowLayout.CENTER, 0, 5));
        imgLibraryStatusPane.setLayout(new BorderLayout());

        lblLibraryStatus = new AFadeLabel("Select a Game");
        lblLibraryStatus.setForeground(Color.lightGray);
        lblLibraryStatus.setFont(coreUI
                .getDefaultFont().deriveFont(Font.PLAIN,
                gameNameFontSize));




        //* Add Game Button *//

        btnShowAddGameUI = new AButton("library_btn_addGame_norm.png",
                "library_btn_addGame_down.png",
                "library_btn_addGame_over.png");

        btnOrganizeGames = new AButton("library_btn_organizeGame_norm.png",
                "library_btn_organizeGame_down.png",
                "library_btn_organizeGame_over.png");


        //* Search Bar *//
        pnlSearchBarBG = new AImagePane("library_searchBar_inactive.png",
                new BorderLayout());
        removeSearchButton = new AButton("library_btnCancelSearch_norm.png",
                "library_btnCancelSearch_down.png",
                "library_btnCancelSearch_over.png");
        searchTextField = new JTextField("Start Typing To Search...");
        btnSearch = new AButton("library_btnSearch_norm.png",
                "library_btnSearch_down.png", "library_btnSearch_over.png");
        pnlSearchButtonBG = new AImagePane("library_searchButton_bg.png",
                new BorderLayout());
        pnlSearchText = new JPanel(new BorderLayout());
        pnlSearchButton = new JPanel(new BorderLayout());
        pnlSearchContainer = new JPanel(new BorderLayout());
        pnlSearchBar = new JPanel(new BorderLayout());

        //* Create Grid Manager *//
        GridSplit = new GridManager(2, 4, coreUI);

        //* Grid Animator *//
        this.GridAnimate = new GridAnimation(GridSplit, paneLibraryContainer);


        //* Load Organize UI *//
        loadOrganizeUI();

        //* Add Game UI *//
        loadAddGameUI();

        //* Edit Game UI *//
        loadEditGameUI();

    }

    @Override
    public final void buildUI() {
        if (!isScreenLoaded) {
            setSize();

            paneLibraryContainer.setOpaque(false);
            paneLibraryContainer.setBackground(Color.red);
            paneLibraryContainer.setLayout(new BorderLayout(0, 140));

            lblKeyAction.setFont(coreUI.getDefaultFont().deriveFont(Font.PLAIN,
                    coreUI.getKeysFontSize()));
            lblKeyAction.setForeground(new Color(0, 178, 178));


            //* Bottom Center Bar *//
            pnlBottomCenterContainer.setOpaque(false);



            imgLibraryStatusPane.setPreferredSize(new Dimension(
                    selectedGameBarWidth, selectedGameBarHeight));
            imgLibraryStatusPane.add(lblLibraryStatus);

            //* Organize Games Button *//
            pnlBottomCenterContainer.add(btnOrganizeGames);

            //* Selected Game Bar *//
            pnlBottomCenterContainer.add(imgLibraryStatusPane);

            //* Add Game Button *//
            pnlBottomCenterContainer.add(btnShowAddGameUI);




            // Search Bar
            // ----------------------------------------------------------------.

            pnlSearchBarBG.setPreferredSize(new Dimension(SearchBarWidth,
                    50));
            removeSearchButton.setPreferredSize(new Dimension(70, 51));

            searchTextField.setOpaque(false);
            searchTextField.setBorder(null);
            searchTextField.setColumns(20);
            searchTextField.setForeground(Color.darkGray);
            searchTextField.setFont(coreUI.getDefaultFont()
                    .deriveFont(Font.BOLD,
                    gridSearchFontSize));
            searchTextField.setPreferredSize(new Dimension(880, 50));

            btnSearch.setPreferredSize(new Dimension(70, 51));
            btnSearch
                    .addActionListener(handler.new SearchButtonHandler());

            pnlSearchButtonBG.setPreferredSize(new Dimension(70, 51));
            pnlSearchButtonBG.add(btnSearch, BorderLayout.NORTH);


            pnlSearchText.setOpaque(false);
            pnlSearchText.add(searchTextField, BorderLayout.CENTER);

            pnlSearchButton.setOpaque(false);
            pnlSearchButton.add(pnlSearchButtonBG, BorderLayout.NORTH);


            pnlSearchContainer.setOpaque(false);
            pnlSearchContainer.add(pnlSearchButton, BorderLayout.WEST);
            pnlSearchContainer.add(pnlSearchText, BorderLayout.CENTER);


            pnlSearchBarBG.add(pnlSearchContainer, BorderLayout.WEST);
            pnlSearchBarBG.validate();

            pnlSearchBar.setOpaque(false);
            pnlSearchBar.add(pnlSearchBarBG, BorderLayout.EAST);
            pnlSearchBar.setPreferredSize(new Dimension(
                    pnlSearchBar.getBounds().width,
                    75));
            pnlSearchBar.validate();

            //* Initiate Library Grid *//
            GridSplit.initiateGrid(0);


            //* Add Components to Central Container *//
            paneLibraryContainer.add(BorderLayout.WEST, imgOrganizeTypeSideBar);
            paneLibraryContainer.add(BorderLayout.CENTER, GridSplit.getGrid(0));
            paneLibraryContainer.add(BorderLayout.EAST, btnGameRight);


            //* add game to library *//
            logic.addGamesToLibrary();

            isScreenLoaded = true;
            addToCanvas();


        } else {
            addToCanvas();
        }


    }

    @Override
    public final void addToCanvas() {
        coreUI.getTitleLabel().setText("   Loading...   ");

        coreUI.getLblKeyActionEnter().setText(" Play ");
        dashboardUI.getLblKeyActionArrow().setText(" Nav ");



        // Add Components with listeners to volatile listener bank
        // ----------------------------------------------------------------.

        addToVolatileListenerBank(searchTextField);
        addToVolatileListenerBank(coreUI.getBackgroundImagePane());
        addToVolatileListenerBank(coreUI.getBottomPane());
        addToVolatileListenerBank(coreUI.getCenterPanel());
        addToVolatileListenerBank(coreUI.getSouthFromTopPanel());
        addToVolatileListenerBank(coreUI.getFrameControlImagePane());
        addToVolatileListenerBank(coreUI.getTopPane());
        addToVolatileListenerBank(this.paneLibraryContainer);
        addToVolatileListenerBank(this.imgLibraryStatusPane);
        addToVolatileListenerBank(this.btnGameLeft);
        addToVolatileListenerBank(this.btnGameRight);

        if (this.btnShowAddGameUI.getActionListeners().length > 0) {
            this.btnShowAddGameUI.removeActionListener(btnShowAddGameUI
                    .getActionListeners()[0]);
        }
        attactchHandlers();

        //* Add Search Bar to Top Bar *//
        coreUI.getSouthFromTopPanel().add(BorderLayout.CENTER, pnlSearchBar);
        coreUI.getSouthFromTopPanel().setPreferredSize(
                new Dimension(coreUI.getSouthFromTopPanel().getWidth(), coreUI
                .getFrameControlImagePane().getHeight()));
        coreUI.getSouthFromTopPanel().revalidate();

        //* Add AddGameButton to Bottom Bar *//
        coreUI.getBottomContentPane().setLayout(new BorderLayout());
        coreUI.getBottomContentPane().setVisible(true);

        // Add InfoFeed to bottom //
        getDashboardUI().getInfoFeed().setImageSize(getCoreUI()
                .getScreenWidth() - 20, getDashboardUI().getInfoFeed()
                .getImageHeight() - 5);
        getDashboardUI().getInfoFeed()
                .setPreferredSize(new Dimension(getDashboardUI().getInfoFeed()
                .getPreferredSize().width,
                getDashboardUI().getInfoFeed().getImageHeight()));

        coreUI.getBottomContentPane().add(Box.createVerticalStrut(4),
                BorderLayout.NORTH);
        coreUI.getBottomContentPane().add(Box.createHorizontalStrut(10),
                BorderLayout.EAST);
        coreUI.getBottomContentPane().add(dashboardUI.getInfoFeedContainer(),
                BorderLayout.CENTER);
        coreUI.getBottomContentPane().add(Box.createHorizontalStrut(10),
                BorderLayout.WEST);
        coreUI.getBottomContentPane().setPreferredSize(new Dimension(dashboardUI
                .getInfoFeed().getImageWidth(), dashboardUI.getInfoFeed()
                .getImageHeight()));
        coreUI.getBottomContentPane().revalidate();

        //* Set up Bottom Bar *//
        coreUI.getCenterFromBottomPanel().setLayout(new BorderLayout());
        coreUI.getCenterFromBottomPanel().add(BorderLayout.NORTH,
                pnlBottomCenterContainer);
        coreUI.getCenterFromBottomPanel().add(BorderLayout.CENTER, coreUI
                .getBottomContentPane());



        //* Add To Key Action Panel *//
        coreUI.getKeyToPressPanel().add(coreUI.getKeyIconImage());
        coreUI.getKeyToPressPanel().add(coreUI.getKeyActionLabel());
        coreUI.getKeyToPressPanel().add(imgKeyIco);
        coreUI.getKeyToPressPanel().add(lblKeyAction);
        coreUI.getHeaderOfCenterFromBottomPanel()
                .setPreferredSize(new Dimension(coreUI.getFrame().getWidth(), 5));
        coreUI.getHeaderOfCenterFromBottomPanel().revalidate();


        //* Add Library Container to Center Panel *//
        coreUI.getCenterPanel().add(BorderLayout.CENTER, paneLibraryContainer);
        coreUI.getCenterPanel().repaint();



        //* Finalize *//
        coreUI.getTitleLabel().setText("     Game Library   ");
        btnGameRight.requestFocusInWindow();
        coreUI.getFrame().requestFocus();

    }

    /**
     * .-----------------------------------------------------------------------.
     * | attactchHandlers()
     * .-----------------------------------------------------------------------.
     * |
     * | Attaches Handlers/Listeners to UI components that have previously
     * | never had them attached or been scrubbed of them through being an
     * | AuroraApp
     * |
     * .........................................................................
     *
     */
    public final void attactchHandlers() {

        searchBoxHandler = handler.new SearchBoxHandler();
        searchFocusHandler = handler.new SearchFocusHandler();
        searchTextField.addKeyListener(searchBoxHandler);
        searchTextField.addFocusListener(searchFocusHandler);
        searchTextField.addMouseListener(handler.new SearchSelectHandler());

        moveLibraryLeftHandler = handler.new HoverButtonLeft();
        moveLibraryRightHandler = handler.new HoverButtonRight();

        btnShowAddGameUI.addActionListener(handler.new ShowAddGameUiHandler());
        btnOrganizeGames
                .addActionListener(handler.new ShowOrganizeGameHandler());

        btnGameRight.setMouseListener(moveLibraryRightHandler);
        btnGameLeft.setMouseListener(moveLibraryLeftHandler);

        coreUI.getFrame()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getFrame()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getFrame()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        coreUI.getBackgroundImagePane()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getBackgroundImagePane()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getBackgroundImagePane()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        coreUI.getBottomPane()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getBottomPane()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getBottomPane()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        coreUI.getCenterPanel()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getCenterPanel()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getCenterPanel()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        coreUI.getSouthFromTopPanel()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getSouthFromTopPanel()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getSouthFromTopPanel()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        coreUI.getFrameControlImagePane()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getFrameControlImagePane()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getFrameControlImagePane()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        coreUI.getTopPane()
                .addKeyListener(handler.new SearchRefocusListener());
        coreUI.getTopPane()
                .addKeyListener(handler.new GameLibraryKeyListener());
        coreUI.getTopPane()
                .addMouseWheelListener(handler.new GridMouseWheelListener());

        this.btnShowAddGameUI
                .addKeyListener(handler.new SearchRefocusListener());
        this.btnShowAddGameUI.addKeyListener(
                handler.new GameLibraryKeyListener());

        this.paneLibraryContainer.addKeyListener(
                handler.new SearchRefocusListener());
        this.paneLibraryContainer
                .addKeyListener(handler.new GameLibraryKeyListener());

        this.imgLibraryStatusPane
                .addKeyListener(handler.new SearchRefocusListener());
        this.imgLibraryStatusPane
                .addKeyListener(handler.new GameLibraryKeyListener());

        this.btnGameLeft.addKeyListener(handler.new SearchRefocusListener());
        this.btnGameLeft.addKeyListener(handler.new GameLibraryKeyListener());

        this.btnGameRight
                .addKeyListener(handler.new SearchRefocusListener());
        this.btnGameRight
                .addKeyListener(handler.new GameLibraryKeyListener());

    }

    /**
     * .-----------------------------------------------------------------------.
     * | loadAddGameUI()
     * .-----------------------------------------------------------------------.
     * |
     * | load the components to the Add Game UI
     * | this method is called in the loadUI method of the LibraryUI class
     * |
     * .........................................................................
     *
     */
    public final void loadAddGameUI() {

        // Create Components
        // ----------------------------------------------------------------.

        //* Get Glass Pane to Put UI On *//
        pnlGlass = (JPanel) coreUI.getFrame().getGlassPane();
        pnlAddGamePane = new AImagePane("addUI_bg.png",
                new BorderLayout());

        //* TOP PANEL COMPONENTS *//
        pnlTopPane_addUI = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        pnlTopPane_addUI.setLayout(new BorderLayout(10, 4));
        pnlTopPane_addUI.setOpaque(false);

        btnClose_addUI = new AButton("addUI_btnClose_norm.png",
                "addUI_btnClose_down.png", "addUI_btnClose_over.png");

        pnlAddGameType = new JPanel(new FlowLayout(FlowLayout.CENTER, -18, 0));
        pnlAddGameType.setOpaque(false);


        // Manual Mode
        // ----------------------------------------------------------------.

        btnManual = new ARadioButton("addUI_btnManual_norm.png",
                "addUI_btnManual_down.png");
        btnManual.setBorder(null);

        btnGoToSteam = new AButton("addUI_btnGoToSteam_norm.png",
                "addUI_btnGoToSteam_down.png", "addUI_btnGoToSteam_over.png");
        btnGoToSteam.setBorder(null);
        btnGoToSteam.setMargin(new Insets(0, 0, 0, 0));
        btnGoToSteam.addActionListener(new GoToSteamListener());


        if (coreUI.getOS().contains("Mac")) {
            btnGoToProgram = new AButton("addUI_btnGoToApps_norm.png",
                    "addUI_btnGoToApps_down.png",
                    "addUI_btnGoToApps_over.png");
        } else {
            btnGoToProgram = new AButton("addUI_btnGoToPrograms_norm.png",
                    "addUI_btnGoToPrograms_down.png",
                    "addUI_btnGoToPrograms_over.png");
        }

        btnGoToProgram.setBorder(null);
        btnGoToProgram.setMargin(new Insets(0, 0, 0, 0));

        btnGoToProgram.addActionListener(new GoToProgramsListener());



        //* CENTRAL PANEL COMPONENTS *//
        pnlCenter_addUI = new JPanel(new BorderLayout());
        pnlCenter_addUI.setOpaque(false);

        pnlTopOfCenter = new JPanel(new BorderLayout());
        pnlTopOfCenter.setOpaque(false);

        pnlLeftOfTopCenter = new AImagePane("addUI_status_container.png",
                new FlowLayout(FlowLayout.LEFT));
        pnlRightOfTop = new AImagePane("addUI_status_container.png",
                new FlowLayout(FlowLayout.LEFT, 0, 5));

        pnlRightOfTopEast = new JPanel(new BorderLayout(0, 0));
        pnlRightOfTopEast.setOpaque(false);


        pnlRightOfTopEastContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT,
                0, 0));
        pnlRightOfTopEastContainer.setOpaque(false);



        pnlLeftOfBottom = new JPanel(new FlowLayout(FlowLayout.LEFT,
                0, 0));
        pnlLeftOfBottom.setOpaque(false);
        pnlRightOfBottom = new JPanel(new FlowLayout(FlowLayout.CENTER,
                0, 0));
        pnlRightOfBottom.setOpaque(true);
        pnlRightOfBottomContainer = new JPanel(new FlowLayout(FlowLayout.LEFT,
                0, 0));
        pnlRightOfBottomContainer.setOpaque(false);
        pnlAddGameContainer = new JPanel(new BorderLayout(0, 0));
        pnlAddGameContainer.setOpaque(false);


        lblLeftTitle = new JLabel("Name");
        lblRightTitle = new JLabel("Location");

        statusBadge1 = new AImage("addUI_badge_idle.png");
        statusBadge2 = new AImage("addUI_badge_idle.png");

        pnlCoverPane_addUI = new AImagePane("addUI_game_bg.png",
                new FlowLayout(FlowLayout.RIGHT, -10, 10));
        pnlBlankCoverGame_addUI = new AImagePane("Blank-Case.png", 240, 260);
        gamesList_addUI = new JList();


        gameFileChooser_addUI = new JFileChooser(System.getProperty("user.home"));

        // Set up File Chooser UI //

        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        //* Set up File Chooser *//
        SwingUtilities.updateComponentTreeUI(gameFileChooser_addUI);


        gameFileChooser_addUI.setApproveButtonText("Select");
        gameFileChooser_addUI.setDragEnabled(false);
        gameFileChooser_addUI.setDialogType(JFileChooser.OPEN_DIALOG);
        gameFileChooser_addUI.setMultiSelectionEnabled(false);
        gameFileChooser_addUI.setAcceptAllFileFilterUsed(true);
        gameFileChooser_addUI.setEnabled(true);
        gameFileChooser_addUI.revalidate();

        try {
            UIManager.setLookAndFeel(UIManager
                    .getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }


        //* BOTTOM PANEL COMPONENTS *//
        pnlBottomPane = new JPanel(new BorderLayout(0, 20));
        pnlBottomPane.setOpaque(false);
        pnTopOfBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnTopOfBottom.setOpaque(false);
        pnlBottomOfCenter = new JPanel(new BorderLayout());
        pnlBottomOfCenter.setOpaque(false);

        pnlSearchBG = new AImagePane("addUI_text_inactive.png",
                new FlowLayout(FlowLayout.RIGHT, 5, -1));
        pnlSearchBG.setLayout(new BorderLayout(0, -1));

        txtSearchField_addUI = new JTextField("Search For Game...");
        pnlAddGameSearchContainer = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 80, 5));
        pnlAddGameSearchContainer.setOpaque(false);

        btnClearSearch_addUI = new AButton("addUI_btnClearText_norm.png",
                "addUI_btnClearText_down.png", "addUI_btnClearText_over.png");

        btnGameToLib_addUI = new AButton("addUI_btnAdd_norm.png",
                "addUI_btnAdd_down.png", "addUI_btnAdd_over.png");
        btnGameToLib_addUI.setVisible(false);



        // Auto Mode
        // ----------------------------------------------------------------.

        btnAuto = new ARadioButton("addUI_btnAuto_norm.png",
                "addUI_btnAuto_down.png");


    }

    /**
     * .-----------------------------------------------------------------------.
     * | buildAddGameUI()
     * .-----------------------------------------------------------------------.
     * |
     * | This method builds the entire AddGameUI overlay that pops up when
     * | a new game is to be added.
     * |
     * | This UI is loaded the first time a new game is to be added, but never
     * | reloaded again.
     * |
     * .........................................................................
     *
     */
    public final void buildAddGameUI() {

        if (!isAddGameUILoaded) {

            // Set Up Components
            // ----------------------------------------------------------------.


            //* CENTRAL PANEL COMPONENTS *//

            //*
            // Set Up Title labels for both Left
            // and Right side of the Central Panel
            //*
            lblLeftTitle.setFont(coreUI.getDefaultFont().deriveFont(Font.BOLD,
                    33));
            lblLeftTitle.setForeground(Color.lightGray);
            lblRightTitle.setFont(coreUI.getDefaultFont().deriveFont(Font.BOLD,
                    33));
            lblRightTitle.setForeground(Color.lightGray);


            //* Set Up Panels containing the Game Cover Art *//
            pnlCoverPane_addUI.setPreferredSize(new Dimension(pnlCoverPane_addUI
                    .getImgIcon()
                    .getIconWidth(), pnlCoverPane_addUI.getImgIcon()
                    .getIconHeight()));

            //*
            // Set Up 2 Panels containing the Left and
            // Right titles at the top of the Content panel
            //*
            pnlLeftOfTopCenter.setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon().getIconWidth() / 2, 75));
            pnlRightOfTop.setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon().getIconWidth() / 2, 75));


            pnlLeftOfBottom
                    .setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon().getIconWidth() / 2 - 10,
                    pnlCoverPane_addUI
                    .getImgIcon().getIconHeight()));
            pnlRightOfBottom
                    .setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon().getIconWidth() / 2 - 10,
                    pnlCoverPane_addUI
                    .getImgIcon().getIconHeight()));
            pnlRightOfBottom.setBackground(new Color(38, 46, 60));

            pnlRightOfBottomContainer
                    .setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon().getIconWidth() / 2,
                    pnlCoverPane_addUI
                    .getImgIcon().getIconHeight()));

            pnlBlankCoverGame_addUI.setPreferredSize(new Dimension(240, 260));



            // Set up Go To Shortcuts //

            int rightOfTopEastWidth;
            // Check that steam exists before showing the
            if (logic.fetchSteamDirOnWindows() != null) {
                pnlRightOfTopEastContainer.add(btnGoToSteam);
                rightOfTopEastWidth = 4;
            } else {
                rightOfTopEastWidth = 3;
            }

            // Add Go To Program

            pnlRightOfTopEastContainer.add(btnGoToProgram);

            pnlRightOfTopEast.add(Box.createHorizontalStrut(pnlRightOfTop
                    .getPreferredSize().width / rightOfTopEastWidth));
            pnlRightOfTopEast.add(pnlRightOfTopEastContainer, BorderLayout.EAST);


            // Set up Game List //


            gamesList_addUI.setPreferredSize(
                    new Dimension(pnlCoverPane_addUI.getImgIcon().getIconWidth()
                                  + 85,
                    pnlCoverPane_addUI.getImgIcon().getIconHeight()));
            gamesList_addUI.setBackground(new Color(38, 46, 60));
            gamesList_addUI.setForeground(Color.lightGray);
            gamesList_addUI.setFont(coreUI.getDefaultFont()
                    .deriveFont(Font.BOLD,
                    listFontSize));
            gamesList_addUI
                    .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            gamesList_addUI.setSelectionBackground(new Color(54,95,143));
            gamesList_addUI.setSelectionForeground(new Color(238,243,249));
            gamesList_addUI.setBorder(null);

            gamesList_addUI.setLayoutOrientation(JList.VERTICAL);
            gamesList_addUI.setVisibleRowCount(10);

            //* List model for JList Containing Game Names *//
            listModel_addUI = new DefaultListModel();
            gamesList_addUI.setModel(listModel_addUI);
            gamesList_addUI.setCellRenderer(handler.new listRender());

            try {
                Field field = PopupFactory.class.getDeclaredField(
                        "forceHeavyWeightPopupKey");
                field.setAccessible(true);
                gameFileChooser_addUI.putClientProperty(field.get(null), true);
            } catch (Exception e) {
                logger.error(e);
            }



            gameFileChooser_addUI.setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon().getIconWidth() / 2 - 10, pnlCoverPane_addUI
                    .getImgIcon().getIconHeight()));


            //* BOTTOM PANEL COMPONENTS *//


            //* Set Up Textfield where user will search for game to add *//
            txtSearchField_addUI.setFont(coreUI.getRopaFont().deriveFont(
                    Font.PLAIN,
                    addGameFontSize - 3));
            txtSearchField_addUI.setForeground(Color.gray);
            txtSearchField_addUI.setOpaque(false);
            txtSearchField_addUI.setBorder(BorderFactory.createEmptyBorder());
            txtSearchField_addUI.setBorder(null);

            txtSearchField_addUI.setPreferredSize(new Dimension(375, 50));

            //* Set up image sizes for the Search box *//
            pnTopOfBottom.setPreferredSize(new Dimension(pnlAddGamePane
                    .getImgIcon()
                    .getIconWidth(), 20));
            pnlSearchBG.setPreferredSize(new Dimension(pnlSearchBG.getImgIcon()
                    .getIconWidth(), pnlSearchBG.getImgIcon().getIconHeight()));


            //* Set up glass panel *//
            pnlGlass.setVisible(true);
            pnlGlass.setLayout(null);
            //* Set Location for Add Game UI panels *//
            pnlAddGamePane.setLocation((coreUI.getFrame().getWidth() / 2)
                                       - (pnlAddGamePane.getImgIcon()
                    .getIconWidth()
                                          / 2), -380);
            pnlAddGamePane
                    .setSize(
                    new Dimension(pnlAddGamePane.getImgIcon()
                    .getIconWidth(), pnlAddGamePane.getImgIcon().getIconHeight()));
            pnlAddGamePane.revalidate();

            btnGameToLib_addUI.setLocation((coreUI.getFrame().getWidth() / 2)
                                           - btnGameToLib_addUI.getWidth() / 2,
                    pnlAddGamePane
                    .getImgIcon()
                    .getIconHeight() - 90);
            btnGameToLib_addUI.setSize(new Dimension(340, 140));


            // Add to Components
            // ----------------------------------------------------------------.

            //* TOP PANEL COMPONENTS *//

            //* Add the Close button to the Top most Panel *//
            pnlTopPane_addUI.add(btnClose_addUI, BorderLayout.EAST);

            //* Type Panel *//
            ARadioButtonManager rdbManager = new ARadioButtonManager();
            rdbManager.addButton(btnManual);
            rdbManager.addButton(btnAuto);
            rdbManager.setRadioButton();

            btnManual.setSelected();

            pnlAddGameType.add(btnManual);
            pnlAddGameType.add(btnAuto);

            pnlTopPane_addUI.add(pnlAddGameType, BorderLayout.CENTER);

            pnlTopPane_addUI.add(Box.createHorizontalStrut(82),
                    BorderLayout.WEST);

            //* BOTTOM PANEL COMPONENTS *//

            btnClearSearch_addUI.addActionListener(
                    handler.new AddGameSearchClear(txtSearchField_addUI));
            btnClearSearch_addUI.setMargin(new Insets(0, 0, 0, 0));

            //* Add components to form the Search Box *//
            pnlSearchBG.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
            pnlSearchBG.add(txtSearchField_addUI, BorderLayout.CENTER);
            pnlSearchBG.add(btnClearSearch_addUI, BorderLayout.EAST);
            pnlAddGameSearchContainer.add(pnlSearchBG);

            //* Add UI elements to the Bottom Panel in the Add Game UI *//
            pnlBottomPane
                    .add(pnlAddGameSearchContainer, BorderLayout.PAGE_START);

            //* CENTRAL PANEL COMPONENTS *//

            //* Add the Titles and Badges at the Top of the CENTRAL panel *//
            pnlLeftOfTopCenter.add(statusBadge1);
            pnlLeftOfTopCenter.add(lblLeftTitle);
            pnlRightOfTop.add(statusBadge2);
            pnlRightOfTop.add(lblRightTitle);
            pnlRightOfTop.add(pnlRightOfTopEast);

            pnlCoverPane_addUI.add(pnlBlankCoverGame_addUI, BorderLayout.SOUTH);
            pnlLeftOfBottom.add(Box.createHorizontalStrut(20));
            pnlLeftOfBottom.add(pnlCoverPane_addUI);
            pnlLeftOfBottom.add(gamesList_addUI);

            //* Add the main Content to the Central Panel *//

            pnlRightOfBottom.add(gameFileChooser_addUI);
            pnlRightOfBottomContainer
                    .add(pnlRightOfBottom);

            pnlAddGameContainer.add(pnlLeftOfBottom, BorderLayout.WEST);
            pnlAddGameContainer
                    .add(pnlRightOfBottomContainer, BorderLayout.EAST);

            pnlTopOfCenter.add(pnlLeftOfTopCenter, BorderLayout.WEST);
            pnlTopOfCenter.add(pnlRightOfTop, BorderLayout.EAST);

            pnlCenter_addUI.add(pnlTopOfCenter, BorderLayout.NORTH);
            pnlCenter_addUI.add(pnlAddGameContainer, BorderLayout.CENTER);
            pnlCenter_addUI.add(pnlBottomPane, BorderLayout.SOUTH);

            //*
            // Add the TOP the CENTER and the BOTTOM
            // panels to the Add Game UI
            //*
            pnlAddGamePane.add(pnlTopPane_addUI, BorderLayout.PAGE_START);
            pnlAddGamePane.add(pnlCenter_addUI, BorderLayout.CENTER);

            pnlGlass.add(pnlAddGamePane);
            pnlGlass.add(btnGameToLib_addUI);



            // Handlers
            // ----------------------------------------------------------------.

            btnClose_addUI.addActionListener(handler.new HideGameAddUIHandler(
                    this));

            pnlAddGamePane.addMouseListener(handler.new EmptyMouseHandler());

            txtSearchField_addUI
                    .addFocusListener(handler.new AddGameFocusHandler());
            txtSearchField_addUI
                    .addMouseListener(handler.new AddGameMouseHandler());
            txtSearchField_addUI
                    .addKeyListener(handler.new AddGameSearchBoxHandler());
            gamesList_addUI.addListSelectionListener(
                    handler.new SelectListHandler());
            gameFileChooser_addUI.setFileFilter(
                    handler.new ExecutableFilterHandler());
            gameFileChooser_addUI
                    .addActionListener(handler.new ExecutableChooserHandler(
                    gameFileChooser_addUI));
            btnGameToLib_addUI
                    .addActionListener(handler.new AddToLibraryHandler());


            // Finalize
            // ----------------------------------------------------------------.

            pnlAddGamePane.setVisible(false);

            btnGameToLib_addUI.revalidate();
            pnlAddGamePane.revalidate();

            isAddGameUILoaded = true;
        }
    }

    public void loadEditGameUI() {


        // Create Components
        // ----------------------------------------------------------------.

        //* Get Glass Pane to Put UI On *//
        pnlGlass = (JPanel) coreUI.getFrame().getGlassPane();
        pnlEditGamePane = new AImagePane("editUI_bg.png",
                new BorderLayout());

        //* Top Panel Components *//
        pnlTopPane_editUI = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        pnlTopPane_editUI.setOpaque(false);
        btnClose_editUI = new AButton("addUI_btnClose_norm.png",
                "addUI_btnClose_down.png", "addUI_btnClose_over.png");

        //* Center Panel *//

        pnlCenter_editUI = new JPanel(new BorderLayout());
        pnlCenter_editUI.setOpaque(false);



        //* Right Menu Pane *//

        pnlRightPane_editUI = new AImagePane("editUI_right.png");
        pnlRightPane_editUI.setPreferredSize(new Dimension(pnlRightPane_editUI
                .getRealImageWidth() + 5, pnlRightPane_editUI
                .getRealImageHeight()));
        pnlRightPane_editUI.setLayout(new BoxLayout(pnlRightPane_editUI,
                BoxLayout.Y_AXIS));

        // Panel containing current Game cover with game name
        pnlTopRightPane_editUI = new JPanel();
        pnlTopRightPane_editUI.setLayout(new BoxLayout(pnlTopRightPane_editUI,
                BoxLayout.Y_AXIS));
        pnlTopRightPane_editUI.setOpaque(false);



        lblCurrentName_editUI = new ASlickLabel("Game Name");

        imgCurrentGame_editUI = new AImagePane("Blank-Case.png");
        imgCurrentGame_editUI.setImageSize((imgCurrentGame_editUI
                .getRealImageWidth() / 4),
                (imgCurrentGame_editUI.getRealImageHeight() / 4));
        imgCurrentGame_editUI.setPreferredSize(new Dimension(
                imgCurrentGame_editUI.getImageWidth(), imgCurrentGame_editUI
                .getImageHeight()));


        pnlCurrentName_editUI = new JPanel();
        pnlCurrentName_editUI.setOpaque(false);
        pnlCurrentName_editUI.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        pnlCurrentImage_editUI = new JPanel();
        pnlCurrentImage_editUI.setOpaque(false);
        pnlCurrentImage_editUI
                .setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlCurrentImage_editUI.setPreferredSize(new Dimension(
                pnlRightPane_editUI.getRealImageWidth(), imgCurrentGame_editUI
                .getImageHeight() - 10));


        // Panel containing Buttons to go between diffrent settings
        pnlCenterRight_editUI = new JPanel();
        pnlCenterRight_editUI.setOpaque(false);
        pnlCenterRight_editUI.setLayout(new GridLayout(3, 1, 0, -6));

        btnGameLocation_editUI = new ARadioButton("editUI_btnSetting_norm.png",
                "editUI_btnSetting_down.png");
        btnGameLocation_editUI.setLayout(new BoxLayout(btnGameLocation_editUI,
                BoxLayout.Y_AXIS));

        btnGameCover_editUI = new ARadioButton("editUI_btnSetting_norm.png",
                "editUI_btnSetting_down.png");
        btnGameCover_editUI.setLayout(new BoxLayout(btnGameCover_editUI,
                BoxLayout.Y_AXIS));

        btnOther_editUI = new ARadioButton("editUI_btnSetting_norm.png",
                "editUI_btnSetting_down.png");
        btnOther_editUI.setLayout(new BoxLayout(btnOther_editUI,
                BoxLayout.Y_AXIS));

        lblGameLocation_editUI = new ASlickLabel(" Game Location ");
        lblGameCover_editUI = new ASlickLabel(" Box Art ");
        lblOther_editUI = new ASlickLabel(" Other ");

        // Button when setting is done, to save.
        btnDone_editUI = new AButton("editUI_btnDone_norm.png",
                "editUI_btnDone_down.png", "editUI_btnDone_over.png");




        //* Left Content Pane *//

        pnlLeftPane_editUI = new JPanel();
        pnlLeftPane_editUI.setOpaque(false);


        //*  Game location

        pnlGameLocation_editUI = new JPanel();
        pnlGameLocation_editUI.setOpaque(false);
        pnlGameLocation_editUI.setLayout(new BoxLayout(pnlGameLocation_editUI,
                BoxLayout.Y_AXIS));


        //- Top
        pnlGameLocationTop = new JPanel(new FlowLayout(FlowLayout.RIGHT,
                0, 0));
        pnlGameLocationTop.setOpaque(false);

        lblCurrentLocation_editUI = new ASlickLabel("Current Location");
        lblCurrentLocation_editUI.setForeground(Color.lightGray);
        lblCurrentLocation_editUI.setFont(getCoreUI().getRopaFont().deriveFont(
                Font.PLAIN, 21));

        txtCurrentLocation_editUI = new ATextField("editUI_textbar.png");
        txtCurrentLocation_editUI.setTextboxSize(0, 0);
        txtCurrentLocation_editUI.getTextBox().setEditable(false);
        txtCurrentLocation_editUI.getTextBox().setFont(getCoreUI()
                .getRegularFont().deriveFont(
                Font.PLAIN, 18));
        txtCurrentLocation_editUI.getTextBox().setForeground(new Color(
                0x46B8B8));




        //- Center

        pnlGameLocationCenter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,
                10));
        pnlGameLocationCenter.setOpaque(false);

        pnlGameFileChooser_editUI = new JPanel(new FlowLayout(FlowLayout.CENTER,
                0, 0));
        pnlGameFileChooser_editUI.setOpaque(true);

        gameFileChooser_editUI = new JFileChooser(System
                .getProperty("user.home"));

        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        //* Set up File Chooser *//
        SwingUtilities.updateComponentTreeUI(gameFileChooser_editUI);

        gameFileChooser_editUI.setApproveButtonText("Select");
        gameFileChooser_editUI.setDragEnabled(false);
        gameFileChooser_editUI.setDialogType(JFileChooser.OPEN_DIALOG);
        gameFileChooser_editUI.setMultiSelectionEnabled(false);
        gameFileChooser_editUI.setAcceptAllFileFilterUsed(true);
        gameFileChooser_editUI.setEnabled(true);

        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger
                    .getLogger(LibraryUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }


        imgGameLocationStatus = new AImage("addUI_badge_idle.png");


        //- Bottom
        pnlGameLocationBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT,
                0, 0));
        pnlGameLocationBottom.setOpaque(false);

        lblNewLocation_editUI = new ASlickLabel("New Location");
        lblNewLocation_editUI.setForeground(Color.lightGray);
        lblNewLocation_editUI.setFont(getCoreUI().getRopaFont().deriveFont(
                Font.PLAIN, 21));

        txtNewLocation_editUI = new ATextField("editUI_textbar.png");
        txtNewLocation_editUI.setTextboxSize(0, 0);
        txtNewLocation_editUI.getTextBox().setEditable(false);
        txtNewLocation_editUI.getTextBox().setFont(getCoreUI().getRegularFont()
                .deriveFont(
                Font.PLAIN, 18));
        txtNewLocation_editUI.getTextBox().setForeground(new Color(70, 184, 70));



        //*  Game Box Art


        pnlGameCover_editUI = new JPanel();
        pnlGameCover_editUI.setOpaque(false);
        pnlGameCover_editUI.setLayout(new BoxLayout(pnlGameCover_editUI,
                BoxLayout.Y_AXIS));


        //- Center
        pnlGameCoverCenter = new JPanel(new FlowLayout(FlowLayout.CENTER,
                25, 20));
        pnlGameCoverCenter.setOpaque(false);

        pnlGameCoverContainer = new JPanel(new FlowLayout(FlowLayout.LEFT,
                0, 0));
        pnlGameCoverContainer.setOpaque(false);

        pnlGameCoverCenter.setOpaque(false);



        pnlCoverPane_editUI = new AImagePane("addUI_game_bg.png",
                new FlowLayout(FlowLayout.RIGHT, -10, 10));

        pnlBlankCoverGame_editUI = new AImagePane("Blank-Case.png", 240, 260);
        gamesList_editUI = new JList();
        listModel_editUI = new DefaultListModel();

        imgGameCoverStatus = new AImage("addUI_badge_idle.png");



        //- Bottom
        pnlGameCoverBottom = new JPanel(new FlowLayout(FlowLayout.CENTER,
                14, 0));
        pnlGameCoverBottom.setOpaque(false);

        lblGameCoverSearch = new ASlickLabel("Game Name");
        txtGameCoverSearch_editUI = new ATextField("editUI_text_inactive.png",
                "editUI_text_active.png");
        btnClearSearch_editUI = new AButton("addUI_btnClearText_norm.png",
                "addUI_btnClearText_down.png", "addUI_btnClearText_over.png");
        btnClearSearch_editUI.setMargin(new Insets(0, 0, 0, 3));
    }

    public void buildEditGameUI(Game game) {



        if (!isEditUILoaded) {

            // Set Up Components
            // ----------------------------------------------------------------.


            currentGame_editUI = game;

            //* Set up glass panel *//
            pnlGlass.setVisible(true);
            pnlGlass.setLayout(null);

            //* Set Location for Edit Game UI panels *//
            pnlEditGamePane.setLocation((coreUI.getFrame().getWidth() / 2)
                                        - (pnlAddGamePane.getImgIcon()
                    .getIconWidth() / 2), -380);
            pnlEditGamePane
                    .setSize(
                    new Dimension(pnlAddGamePane.getImgIcon()
                    .getIconWidth(), pnlAddGamePane.getImgIcon().getIconHeight()));
            pnlEditGamePane.revalidate();


            // Add to Components
            // ----------------------------------------------------------------.

            //* TOP PANEL COMPONENTS *//

            //* Add the Close button to the Top most Panel *//
            pnlTopPane_editUI.add(btnClose_editUI);


            //* Right Menu Pane *//

            lblCurrentName_editUI.setFont(getCoreUI().getRopaFont().deriveFont(
                    Font.PLAIN, 13));
            lblCurrentName_editUI.setForeground(Color.LIGHT_GRAY);
            lblCurrentName_editUI.setText("  " + currentGame_editUI.getName());
            imgCurrentGame_editUI
                    .setImage(currentGame_editUI.getCoverImagePane()
                    .getImgIcon(),
                    imgCurrentGame_editUI.getImageWidth(), imgCurrentGame_editUI
                    .getImageHeight());

            pnlCurrentImage_editUI.add(imgCurrentGame_editUI);
            pnlCurrentName_editUI.add(lblCurrentName_editUI);

            pnlCurrentImage_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            pnlCurrentName_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            pnlTopRightPane_editUI.add(Box.createVerticalGlue());
            pnlTopRightPane_editUI.add(pnlCurrentImage_editUI);
            pnlTopRightPane_editUI.add(pnlCurrentName_editUI);



            lblGameLocation_editUI.setFont(getCoreUI().getRopaFont().deriveFont(
                    Font.PLAIN, 25));
            lblGameLocation_editUI.setForeground(Color.lightGray);

            lblGameCover_editUI.setFont(getCoreUI().getRopaFont().deriveFont(
                    Font.PLAIN, 25));
            lblGameCover_editUI.setForeground(Color.lightGray);

            lblOther_editUI.setFont(getCoreUI().getRopaFont().deriveFont(
                    Font.PLAIN, 25));
            lblOther_editUI.setForeground(Color.lightGray);

            lblGameLocation_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            btnGameLocation_editUI.add(Box.createVerticalStrut(btnOther_editUI
                    .getRealImageHeight() / 4));
            btnGameLocation_editUI.add(lblGameLocation_editUI);

            lblGameCover_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            btnGameCover_editUI.add(Box.createVerticalStrut(btnOther_editUI
                    .getRealImageHeight() / 4));
            btnGameCover_editUI.add(lblGameCover_editUI);

            lblOther_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            btnOther_editUI.add(Box.createVerticalStrut(btnOther_editUI
                    .getRealImageHeight() / 4));
            btnOther_editUI.add(lblOther_editUI);

            ARadioButtonManager rdbManager = new ARadioButtonManager();
            rdbManager.addButton(btnGameLocation_editUI);
            rdbManager.addButton(btnGameCover_editUI);
            rdbManager.addButton(btnOther_editUI);
            rdbManager.setRadioButton();



            btnGameLocation_editUI.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
            btnGameCover_editUI.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
            btnOther_editUI.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
            pnlCenterRight_editUI.add(btnGameLocation_editUI);
            pnlCenterRight_editUI.add(btnGameCover_editUI);
            pnlCenterRight_editUI.add(btnOther_editUI);


            pnlTopRightPane_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            pnlCenterRight_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            btnDone_editUI.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            pnlRightPane_editUI.add(pnlTopRightPane_editUI);
            pnlRightPane_editUI.add(pnlCenterRight_editUI);
            pnlRightPane_editUI.add(Box.createVerticalStrut(10));
            pnlRightPane_editUI.add(btnDone_editUI);
            pnlRightPane_editUI.add(Box.createVerticalStrut(25));

            //* Left Content Panel *//



            // Game Location
            // ----------------------------------------------------------------.

            // Top
            pnlGameLocationTop.add(lblCurrentLocation_editUI);
            pnlGameLocationTop.add(txtCurrentLocation_editUI);

            // Center
            pnlGameFileChooser_editUI
                    .setPreferredSize(new Dimension(txtCurrentLocation_editUI
                    .getRealImageWidth(),
                    pnlEditGamePane
                    .getRealImageHeight() / 2 + 10));
            pnlGameFileChooser_editUI.setBackground(new Color(38, 46, 60));


            try {
                Field field = PopupFactory.class.getDeclaredField(
                        "forceHeavyWeightPopupKey");
                field.setAccessible(true);
                gameFileChooser_editUI.putClientProperty(field.get(null), true);
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
            }

            // Set up File Chooser UI //


            gameFileChooser_editUI.setPreferredSize(new Dimension(
                    pnlGameFileChooser_editUI.getPreferredSize().width,
                    pnlGameFileChooser_editUI.getPreferredSize().height));

            pnlGameFileChooser_editUI.add(gameFileChooser_editUI);


            pnlGameLocationCenter.add(imgGameLocationStatus);
            pnlGameLocationCenter.add(Box.createHorizontalStrut(40));
            pnlGameLocationCenter.add(pnlGameFileChooser_editUI);

            // Bottom
            pnlGameLocationBottom.add(lblNewLocation_editUI);
            pnlGameLocationBottom.add(txtNewLocation_editUI);





            pnlGameLocation_editUI.add(Box.createVerticalStrut(25));
            pnlGameLocation_editUI.add(pnlGameLocationTop);
            pnlGameLocation_editUI.add(pnlGameLocationCenter);
            pnlGameLocation_editUI.add(pnlGameLocationBottom);



            //-
            // Add major panels to component
            //-

            pnlCenter_editUI.add(pnlRightPane_editUI, BorderLayout.EAST);
            pnlCenter_editUI.add(pnlLeftPane_editUI, BorderLayout.CENTER);

            pnlEditGamePane.add(pnlTopPane_editUI, BorderLayout.PAGE_START);
            pnlEditGamePane.add(pnlCenter_editUI, BorderLayout.CENTER);

            pnlGlass.add(pnlEditGamePane);




            // Game Cover
            // ----------------------------------------------------------------.

            // Set up Game List //

            gamesList_editUI.setPreferredSize(
                    new Dimension(pnlCoverPane_editUI.getImgIcon()
                    .getIconWidth()
                                  + 110,
                    pnlCoverPane_editUI.getImgIcon().getIconHeight()));
            gamesList_editUI.setForeground(Color.lightGray);
            gamesList_editUI.setBackground(new Color(38, 46, 60));
            gamesList_addUI.setSelectionBackground(new Color(54,95,143));
            gamesList_addUI.setSelectionForeground(new Color(238,243,249));
            gamesList_editUI.setFont(coreUI.getDefaultFont().deriveFont(
                    Font.BOLD,
                    listFontSize));
            gamesList_editUI.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION);
            gamesList_editUI.setLayoutOrientation(JList.VERTICAL);
            gamesList_editUI.setVisibleRowCount(10);

            gamesList_addUI.setCellRenderer(handler.new listRender());
            gamesList_editUI.setModel(listModel_editUI);


            pnlGameCoverContainer
                    .setPreferredSize(new Dimension(pnlEditGamePane
                    .getImgIcon().getIconWidth() / 2,
                    pnlCoverPane_editUI
                    .getImgIcon().getIconHeight()));

            pnlCoverPane_addUI.setPreferredSize(new Dimension(
                    pnlCoverPane_editUI
                    .getImgIcon()
                    .getIconWidth(), pnlCoverPane_editUI.getImgIcon()
                    .getIconHeight()));

            pnlBlankCoverGame_editUI.setPreferredSize(new Dimension(240, 260));

            pnlCoverPane_editUI.add(pnlBlankCoverGame_editUI);

            pnlGameCoverContainer.add(pnlCoverPane_editUI);
            pnlGameCoverContainer.add(gamesList_editUI);

            pnlGameCoverCenter.add(imgGameCoverStatus);
            pnlGameCoverCenter.add(Box.createHorizontalStrut(19));
            pnlGameCoverCenter.add(pnlGameCoverContainer);


            // Bottom

            lblGameCoverSearch.setForeground(Color.LIGHT_GRAY);
            lblGameCoverSearch.setFont(getCoreUI().getRopaFont().deriveFont(
                    Font.PLAIN, 22));

            txtGameCoverSearch_editUI.getTextBox().setForeground(Color.gray);
            txtGameCoverSearch_editUI.getTextBox().setFont(getCoreUI()
                    .getRopaFont()
                    .deriveFont(
                    Font.PLAIN, 28));
            txtGameCoverSearch_editUI.setText("Search Here...");
            txtGameCoverSearch_editUI.setTextboxSize(0, 0);

            txtGameCoverSearch_editUI.add(Box.createHorizontalStrut(10),
                    BorderLayout.WEST);
            txtGameCoverSearch_editUI.add(btnClearSearch_editUI,
                    BorderLayout.EAST);

            pnlGameCoverBottom.add(lblGameCoverSearch);
            pnlGameCoverBottom.add(txtGameCoverSearch_editUI);


            // Add to Game Cover Edit main Panel
            pnlGameCover_editUI.add(Box.createVerticalStrut(25));
            pnlGameCover_editUI.add(pnlGameCoverCenter);
            pnlGameCover_editUI.add(pnlGameCoverBottom);

            isEditUILoaded = true;





            // Handlers
            // ----------------------------------------------------------------.

            btnClose_editUI.addActionListener(handler.new HideEditAddUIHandler(
                    this));
            pnlEditGamePane.addMouseListener(handler.new EmptyMouseHandler());

            btnGameLocation_editUI.setSelectedHandler(
                    handler.new GameLocationSettingListener(
                    lblGameLocation_editUI));
            btnGameLocation_editUI.setUnSelectedHandler(
                    handler.new UnselectSettingListener(lblGameLocation_editUI));

            btnGameCover_editUI.setSelectedHandler(
                    handler.new GameCoverSettingListener(lblGameCover_editUI));
            btnGameCover_editUI.setUnSelectedHandler(
                    handler.new UnselectSettingListener(lblGameCover_editUI));

            btnOther_editUI.setSelectedHandler(
                    handler.new OtherSettingListener(lblOther_editUI));
            btnOther_editUI.setUnSelectedHandler(
                    handler.new UnselectSettingListener(lblOther_editUI));

            gameFileChooser_editUI.setFileFilter(
                    handler.new ExecutableFilterHandler());
            gameFileChooser_editUI
                    .addActionListener(handler.new GameEditFileChooserHandler(
                    gameFileChooser_editUI));

            btnDone_editUI.addActionListener(
                    handler.new GameSettingDoneHandler());



        } else if (game != currentGame_editUI) {

            currentGame_editUI = game;
            lblCurrentName_editUI.setText("  " + currentGame_editUI.getName());
            imgCurrentGame_editUI
                    .setImage(currentGame_editUI.getCoverImagePane()
                    .getImgIcon(),
                    imgCurrentGame_editUI.getImageWidth(), imgCurrentGame_editUI
                    .getImageHeight());

        }

        if (!btnGameLocation_editUI.isSelected
            && !btnGameCover_editUI.isSelected && !btnOther_editUI.isSelected) {
            btnGameLocation_editUI.setSelected();
        }


        txtNewLocation_editUI.setText("");
        imgGameLocationStatus.setImgURl("addUI_badge_idle.png");

        try {
            gameFileChooser_editUI.setCurrentDirectory(null);
        } catch (Exception e) {
        }
    }

    private void loadOrganizeUI() {

        organizeMenu = new APopupMenu();
        organizeMenu.setOpaque(false);

        // Background Panes //

        btnTop = new ARadioButton("library_organize_top.png",
                "library_organize_top_selected.png");
        btnTop.setLayout(
                new FlowLayout(FlowLayout.CENTER));
        btnTop.setPreferredSize(new Dimension(btnTop.getRealImageWidth(), btnTop
                .getRealImageHeight()));




        btnMiddle = new ARadioButton("library_organize_middle.png",
                "library_organize_middle_selected.png");
        btnMiddle.setLayout(
                new FlowLayout(FlowLayout.CENTER));
        btnMiddle.setPreferredSize(new Dimension(btnMiddle.getRealImageWidth(),
                btnMiddle.getRealImageHeight()));




        btnBottom = new ARadioButton("library_organize_bottom.png",
                "library_organize_bottom_selected.png");
        btnBottom.setLayout(
                new FlowLayout(FlowLayout.CENTER));
        btnBottom.setPreferredSize(new Dimension(btnBottom.getRealImageWidth(),
                btnBottom.getRealImageHeight()));


        organizeBtnManager = new ARadioButtonManager();
        organizeBtnManager.addButton(btnBottom);
        organizeBtnManager.addButton(btnMiddle);
        organizeBtnManager.addButton(btnTop);
        organizeBtnManager.setRadioButton();


        // Labels //

        lblFavorite = new ASlickLabel("Favorite");
        lblFavorite.setFont(getCoreUI().getRopaFont().deriveFont(
                Font.PLAIN, 19));
        lblFavorite.setForeground(new Color(173, 173, 173));

        lblAlphabetic = new ASlickLabel("Alphabetic");
        lblAlphabetic.setFont(getCoreUI().getRopaFont()
                .deriveFont(
                Font.PLAIN, 19));
        lblAlphabetic.setForeground(new Color(173, 173, 173));

        lblMostPlayed = new ASlickLabel("Most Played");
        lblMostPlayed.setFont(getCoreUI().getRopaFont()
                .deriveFont(
                Font.PLAIN, 19));
        lblMostPlayed.setForeground(new Color(173, 173, 173));

        // Icons //
        icoFavorite = new AImage("library_organize_favorite.png");

        icoAlphabetic = new AImage("library_organize_alphabetic.png");

        icoMostPlayed = new AImage("library_organize_mostPlayed.png");


        // Containers //
        favoritePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,
                2));
        favoritePane.setPreferredSize(new Dimension(btnBottom
                .getRealImageWidth(),
                btnBottom.getRealImageHeight()));
        favoritePane.setOpaque(false);

        alphabeticPane = new JPanel(new FlowLayout(FlowLayout.LEFT,
                10, 2));
        alphabeticPane.setPreferredSize(new Dimension(btnBottom
                .getRealImageWidth(),
                btnBottom.getRealImageHeight()));
        alphabeticPane.setOpaque(false);

        mostplayedPane = new JPanel(new FlowLayout(FlowLayout.LEFT,
                10, 2));
        mostplayedPane.setPreferredSize(new Dimension(btnBottom
                .getRealImageWidth(),
                btnBottom.getRealImageHeight()));
        mostplayedPane.setOpaque(false);


        // Handlers //
        btnTop.addMouseListener(handler.new OrganizeMouseListener(
                lblFavorite));
        btnTop.setSelectedHandler(handler.new SelectedOrganizeListener(
                lblFavorite,
                storage.getStoredSettings(),
                "Favorite"));
        btnTop.setUnSelectedHandler(handler.new UnSelectedOrganizeListener(
                lblFavorite, organizeMenu));


        btnMiddle.addMouseListener(
                handler.new OrganizeMouseListener(lblAlphabetic));
        btnMiddle.setSelectedHandler(
                handler.new SelectedOrganizeListener(lblAlphabetic, storage
                .getStoredSettings(), "Alphabetic"));
        btnMiddle.setUnSelectedHandler(handler.new UnSelectedOrganizeListener(
                lblAlphabetic, organizeMenu));

        btnBottom.addMouseListener(handler.new OrganizeMouseListener(
                lblMostPlayed));
        btnBottom.setSelectedHandler(
                handler.new SelectedOrganizeListener(lblMostPlayed, storage
                .getStoredSettings(), "Most Played"));
        btnBottom.setUnSelectedHandler(handler.new UnSelectedOrganizeListener(
                lblMostPlayed, organizeMenu));

        // Add to panels //

        favoritePane.add(icoFavorite);
        favoritePane.add(lblFavorite);

        btnTop.add(favoritePane);


        alphabeticPane.add(icoAlphabetic);
        alphabeticPane.add(lblAlphabetic);

        btnMiddle.add(alphabeticPane);


        mostplayedPane.add(icoMostPlayed);
        mostplayedPane.add(lblMostPlayed);

        btnBottom.add(mostplayedPane);


        organizeMenu.add(btnTop);
        organizeMenu.add(btnMiddle);
        organizeMenu.add(btnBottom);
    }

    public void showOrganizeUI() {
        ASound organizeSFX = new ASound("tick_2.wav", false);
        organizeSFX.Play();

        // States //

        if (!btnTop.isSelected && !btnMiddle.isSelected && !btnBottom.isSelected) {
            String value = storage.getStoredSettings().getSettingValue(
                    "organize");
            if (value != null) {
                if (value.equals("Favorite")) {
                    btnTop.setSelected();

                } else if (value.equals("Alphabetic")) {
                    btnMiddle.setSelected();

                } else if (value.equals("Most Played")) {
                    btnBottom.setSelected();
                }
            } else {

                storage.getStoredSettings().saveSetting("organize", "favorite");
                value = "Favorite";
                btnTop.setSelected();
            }
        }



        organizeMenu
                .show(getCoreUI().getFrame(), btnOrganizeGames
                .getLocationOnScreen().x + ((btnOrganizeGames.getBounds().width)
                                            / 3
                                            - (btnOrganizeGames.getBounds().width)
                                              / 5)
                                              - 3,
                btnOrganizeGames.getLocationOnScreen().y - btnOrganizeGames
                .getBounds().height
                - btnMiddle
                .getRealImageHeight());


    }

    public void showAddGameUI() {

        if (isEditGameUI_Visible()) {
            hideEditGameUI();
            showAddGameUI();
        } else if (isAddGameUI_Visible()) {

            hideAddGameUI();

        } else {
            pnlGlass.setVisible(true);


            addGameAnimator = new AAnimate(pnlAddGamePane);

            int num = 1 + (int) (Math.random() * ((3 - 1) + 1));
            ASound showSound = new ASound("swoop_" + num + ".wav", false);
            showSound.Play();

            AThreadWorker addGameWorker = new AThreadWorker(
                    new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    buildAddGameUI();
                    addGameAnimator.setInitialLocation(
                            (coreUI.getFrame().getWidth() / 2)
                            - (pnlAddGamePane.getImgIcon()
                            .getIconWidth() / 2), -390);
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //* Animate Down Add Game UI *//

                    addGameAnimator.moveVertical(0, 33);
                    pnlAddGamePane.revalidate();

                    txtSearchField_addUI.setFocusable(true);

                    addGameAnimator.addPostAnimationListener(
                            new APostHandler() {
                        @Override
                        public void postAction() {
                            addGameUI_Visible = true;
                            txtSearchField_addUI.requestFocus();
                        }
                    });
                }
            });

            addGameWorker.startOnce();

        }
    }

    public void hideAddGameUI() {
        if (addGameUI_Visible == true) {
            addGameUI_Visible = false;
            try {
                btnGameToLib_addUI.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            int num = 1 + (int) (Math.random() * ((3 - 1) + 1));
            ASound showSound = new ASound("reverse_swoop_" + num + ".wav", false);
            showSound.Play();

            //* Animate Up Add Game UI *//
            addGameAnimator.moveVertical(-492, 35);
            addGameAnimator.addPostAnimationListener(new APostHandler() {
                @Override
                public void postAction() {
                    pnlGlass.setVisible(false);
                }
            });

            statusBadge2.setImgURl("addUI_badge_invalid.png");
            addGameAnimator.removeAllListeners();
            btnGameToLib_addUI.setVisible(false);
            btnGameToLib_addUI.repaint();

            //TODO Fix Search Bar Focus
            searchTextField.requestFocus();
            coreUI.getFrame().requestFocus();
            searchTextField.requestFocus();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                logger.error(ex);
            }

            coreUI.getFrame().requestFocus();

            addGameAnimator = null;
        }
    }

    public void showEditGameUI(final Game game) {

        if (isAddGameUI_Visible()) {
            hideAddGameUI();
            showEditGameUI(game);
        } else if (isEditGameUI_Visible()) {
            hideEditGameUI();

            AThreadWorker showAfter = new AThreadWorker(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Thread.sleep(350);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(LibraryUI.class
                                .getName()).
                                log(Level.SEVERE, null, ex);
                    }
                    showEditGameUI(game);

                }
            });
            showAfter.startOnce();

        } else {

            pnlGlass.setVisible(true);
            editGameUI_Visible = true;

            editGameAnimator = new AAnimate(pnlEditGamePane);

            int num = 1 + (int) (Math.random() * ((3 - 1) + 1));
            ASound showSound = new ASound("swoop_" + num + ".wav", false);
            showSound.Play();

            AThreadWorker editGameWorker = new AThreadWorker(
                    new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    buildEditGameUI(game);

                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //* Animate Down Add Game UI *//
                    editGameAnimator.setInitialLocation(
                            (coreUI.getFrame().getWidth() / 2)
                            - (pnlEditGamePane.getImgIcon()
                            .getIconWidth() / 2), -390);
                    editGameAnimator.moveVertical(0, 33);
                    pnlEditGamePane.revalidate();

                }
            });

            editGameWorker.startOnce();
        }

    }

    /**
     * Show the Game Location change UI
     */
    public void showGameLocationUI() {

        if (isEditGameUI_Visible()) {

            pnlLeftPane_editUI.removeAll();
            pnlLeftPane_editUI.revalidate();

            final File location = new File(currentGame_editUI.getGamePath());

            txtCurrentLocation_editUI.setText(location.getAbsolutePath());


            pnlLeftPane_editUI.add(pnlGameLocation_editUI);
            pnlLeftPane_editUI.revalidate();
            pnlLeftPane_editUI.repaint();


        }

    }

    /**
     * Show the Game Box Art change UI
     */
    public void showGameCoverUI() {
        if (isEditGameUI_Visible()) {

            pnlLeftPane_editUI.removeAll();
            pnlLeftPane_editUI.revalidate();
            pnlLeftPane_editUI.repaint();


            pnlLeftPane_editUI.add(pnlGameCover_editUI);
            pnlLeftPane_editUI.revalidate();
            pnlLeftPane_editUI.repaint();
        }
    }

    /**
     * Show the Other settings UI
     */
    public void showOtherUI() {
        if (isEditGameUI_Visible()) {

            pnlLeftPane_editUI.removeAll();
            pnlLeftPane_editUI.revalidate();
            pnlLeftPane_editUI.repaint();
        }
    }

    public void hideEditGameUI() {

        if (editGameUI_Visible == true) {

            editGameUI_Visible = false;

            int num = 1 + (int) (Math.random() * ((3 - 1) + 1));
            ASound showSound = new ASound("reverse_swoop_" + num + ".wav", false);
            showSound.Play();

            //* Animate Up Add Game UI *//
            editGameAnimator.moveVertical(-492, 35);
            editGameAnimator.addPostAnimationListener(new APostHandler() {
                @Override
                public void postAction() {
                    pnlGlass.setVisible(false);
                }
            });

            editGameAnimator.removeAllListeners();

            btnGameLocation_editUI.setUnSelected();
            btnGameCover_editUI.setUnSelected();
            btnOther_editUI.setUnSelected();

            searchTextField.requestFocus();
            coreUI.getFrame().requestFocus();
            searchTextField.requestFocus();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                logger.error(ex);
            }

            coreUI.getFrame().requestFocus();
        }

    }

    public void moveGridLeft() {

        if (logger.isDebugEnabled()) {
            logger.debug("Left key pressed");
        }

        if (!GridAnimate.getAnimator1().isAnimating() && !GridAnimate
                .getAnimator2().isAnimating()) {

            //*
            // Get The Index of The Current Panel Being Displayed
            // Refer too GridManager array of All panels to find it
            //*
            currentIndex = GridSplit.getArray()
                    .indexOf(paneLibraryContainer.getComponent(1));

            //* Stop from going to far left *//
            if (currentIndex - 1 == -1) {
                currentIndex = 1;
                btnGameLeft.mouseExit();
            }


            if (currentIndex < GridSplit.getArray().size()) {

                GridSplit.decrementVisibleGridIndex();
                //Clear Panel
                if (currentIndex - 1 <= 0) {
                    //Far Left Image
                    paneLibraryContainer.remove(0);
                    paneLibraryContainer.add(imgOrganizeTypeSideBar,
                            BorderLayout.WEST, 0);

                } else {
                    //Left Button
                    paneLibraryContainer.remove(0);
                    paneLibraryContainer.add(btnGameLeft, BorderLayout.WEST, 0);
                }
                //Add GameCover Covers

                GridAnimate.moveLeft(currentIndex);

                paneLibraryContainer.add(BorderLayout.EAST, btnGameRight);

                try {
                    logic.loadGames(currentIndex - 1);
                } catch (MalformedURLException ex) {
                    logger.error(ex);
                }
            }

            coreUI.getCenterPanel().removeAll();
            coreUI.getCenterPanel().add(BorderLayout.CENTER,
                    paneLibraryContainer);

            paneLibraryContainer.repaint();
            paneLibraryContainer.revalidate();

            coreUI.getFrame().requestFocus();

        }
        btnGameLeft.mouseExit();
    }

    public void moveGridRight() {

        if (logger.isDebugEnabled()) {
            logger.debug("Right key pressed");
        }

        if (!GridAnimate.getAnimator1().isAnimating() && !GridAnimate
                .getAnimator2().isAnimating()) {

            currentIndex = GridSplit.getArray()
                    .indexOf(paneLibraryContainer.getComponent(1));


            if (currentIndex < GridSplit.getArray().size() - 1) {

                GridSplit.incrementVisibleGridIndex();

                paneLibraryContainer.remove(0);
                paneLibraryContainer.add(btnGameLeft, BorderLayout.WEST, 0);


                paneLibraryContainer.add(btnGameRight, BorderLayout.EAST, 2);

                GridAnimate.moveRight(currentIndex);


                try {
                    logic.loadGames(currentIndex + 1);
                } catch (MalformedURLException ex) {
                    logger.error(ex);
                }



                //of on last Grid then dont show right arrow button
                if (!(currentIndex + 1 < GridSplit.getArray().size() - 1)) {

                    paneLibraryContainer.remove(btnGameRight);
                    paneLibraryContainer.add(Box.createHorizontalStrut(140),
                            BorderLayout.EAST, 2);
                    btnGameRight.mouseExit();
                }
            }

            coreUI.getCenterPanel().removeAll();
            coreUI.getCenterPanel().add(BorderLayout.CENTER,
                    paneLibraryContainer);

            paneLibraryContainer.repaint();
            paneLibraryContainer.revalidate();

            coreUI.getFrame().requestFocus();

            currentIndex = GridSplit.getArray()
                    .indexOf(paneLibraryContainer.getComponent(1));

        }
        btnGameRight.mouseExit();
    }

    @Override
    public final void closeApp() {
        if (isAddGameUI_Visible()) {
            hideAddGameUI();
        }
        if (isEditGameUI_Visible()) {
            hideEditGameUI();
        }
    }

    public boolean isEditGameUI_Visible() {
        return editGameUI_Visible;
    }

    public void setSize() {

        double Ratio = ((double) coreUI.getFrame().getWidth()
                        / (double) coreUI.getFrame().getHeight());

        int Ratio2 = (coreUI.getFrame().getWidth() - coreUI.getFrame()
                .getHeight()) / 2;


        if (logger.isDebugEnabled()) {
            logger.debug("Ratio " + Ratio);
            logger.debug("Height " + coreUI.getFrame().getHeight());
            logger.debug("Width " + coreUI.getFrame().getWidth());

        }

        if (coreUI.isLargeScreen()) {
            gameCoverHeight = coreUI.getFrame().getHeight() / 3 - (Ratio2 / 10)
                              + 5;
            gameCoverWidth = coreUI.getFrame().getWidth() / 5 - (Ratio2 / 10)
                             - 5;
            selectedGameBarHeight = coreUI.getBottomPane().getHeight() / 3;
            selectedGameBarWidth = coreUI.getFrame().getWidth() / 3;
            addGameWidth = coreUI.getFrame().getWidth() / 3;
            addGameHeight = coreUI.getBottomPane().getHeight() / 3;
            gameNameFontSize = 32;
            SearchBarWidth = 880;
            listFontSize = 19;
            gridSearchFontSize = 35;
            addGameFontSize = 28;
            bottomTopPadding = 10;

        } else {
            gameCoverHeight = (int) ((coreUI.getFrame().getHeight()
                                      + (coreUI.getTopPanelHeight() * 2))
                                     / (Ratio * 2.5));
            gameCoverWidth = (int) ((coreUI.getFrame().getWidth()
                                     + coreUI.getTopPanelHeight()) / (Ratio
                                                                      * 3.5));
            addGameWidth = coreUI.getFrame().getWidth() / 3 - 20;
            addGameWidth = gameCoverWidth;
            addGameHeight = 40;
            selectedGameBarHeight = coreUI.getBottomPane().getHeight() / 3;
            selectedGameBarWidth = coreUI.getFrame().getWidth() / 3 - 20;
            gameNameFontSize = 30;
            SearchBarWidth = coreUI.getFrame().getWidth() / 2 + coreUI
                    .getControlWidth() / 2;
            listFontSize = 19;
            gridSearchFontSize = 35;
            addGameFontSize = 28;
            bottomTopPadding = -5;
        }


    }

    /**
     * Listener for the btnGoToProgram button to make gameFileChooser point to
     * the Programs folder based on the OS
     */
    private class GoToProgramsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Set File Choosers  location to the Programs/App folder //

            String goToPath = System.getProperty("user.dir");

            if (coreUI.getOS().contains("Windows")) {

                // Check which Programs folder to use, Use x86 one if possible//

                if (System.getenv("ProgramFiles(x86)") != null) {

                    goToPath = System.getenv("ProgramFiles(x86)");

                } else if (System.getenv("ProgramFiles") != null) {

                    goToPath = System.getenv("ProgramFiles");

                }


            } else if (coreUI.getOS().contains("Mac")) {
                goToPath = "/Applications/";
            } else {
                goToPath = "";
            }

            // Set appropriate path, will fall back to user.dir //
            gameFileChooser_addUI.setCurrentDirectory(new File(goToPath));
        }
    }

    /**
     * Listener for the btnGoToProgram button to make gameFileChooser point to
     * the Steam games folder.
     */
    private class GoToSteamListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Set File Choosers location to folder containing Steam Games //

            if (coreUI.getOS().contains("Windows")) {
                gameFileChooser_addUI.setCurrentDirectory(logic
                        .fetchSteamDirOnWindows());
            } else if (coreUI.getOS().contains("Mac")) {
                if (AFileManager
                        .checkFile("/Applications/Steam/steamapp/common")) {
                    gameFileChooser_addUI.setCurrentDirectory(new File(
                            "/Applications/Steam/steamapp/common"));
                }
            } else {
                gameFileChooser_addUI.setCurrentDirectory(null);
            }

            coreUI.getFrame().repaint();


        }
    }

    // Getters and Setters
    // ----------------------------------------------------------------.
    public Game getCurrentGame_editUI() {
        return currentGame_editUI;
    }

    public boolean isGameLocation() {
        return isGameLocation;
    }

    public void setIsGameLocation(boolean isGameLocation) {
        this.isGameLocation = isGameLocation;
    }

    public void setIsGameCover(boolean isGameCover) {
        this.isGameCover = isGameCover;
    }

    public void setIsOther(boolean isOther) {
        this.isOther = isOther;
    }

    public boolean isGameCover() {
        return isGameCover;
    }

    public boolean isOther() {
        return isOther;
    }

    public JFileChooser getGameFileChooser_editUI() {
        return gameFileChooser_editUI;
    }

    public ATextField getTxtCurrentLocation_editUI() {
        return txtCurrentLocation_editUI;
    }

    public ATextField getTxtNewLocation_editUI() {
        return txtNewLocation_editUI;
    }

    public AImage getImgGameLocationStatus() {
        return imgGameLocationStatus;
    }

    public AAnimate getAddGameAnimator() {
        return addGameAnimator;
    }

    public AFadeLabel getLblLibraryStatus() {
        return lblLibraryStatus;
    }

    public boolean IsGameLibraryKeyListenerAdded() {
        return isGameLibraryKeyListenerAdded;
    }

    public final void setIsGameLibraryKeyListenerAdded(
            boolean isGameLibraryKeyListenerAdded) {
        this.isGameLibraryKeyListenerAdded = isGameLibraryKeyListenerAdded;
    }

    public HoverButtonLeft getMoveLibraryLeftHandler() {
        return moveLibraryLeftHandler;
    }

    public HoverButtonRight getMoveLibraryRightHandler() {
        return moveLibraryRightHandler;
    }

    public AButton getAddGameToLibButton() {
        return btnGameToLib_addUI;
    }

    public AAnimate getAddGameToLibButtonAnimator() {
        return addGameToLibButtonAnimator;
    }

    public JPanel getAddSearchContainer() {
        return pnlAddGameSearchContainer;
    }

    public JPanel getBottomCenterPane() {
        return pnlBottomOfCenter;
    }

    public JFileChooser getGameLocator() {
        return gameFileChooser_addUI;
    }

    public LibraryHandler getHandler() {
        return handler;
    }

    public AImagePane getPnlBlankCoverGame() {
        return pnlBlankCoverGame_addUI;
    }

    public AButton getRemoveSearchButton() {
        return removeSearchButton;
    }

    public JTextField getGameSearchBar() {
        return txtSearchField_addUI;
    }

    public ASimpleDB getCoverDB() {
        return CoverDB;
    }

    public DefaultListModel getListModel() {
        return listModel_addUI;
    }

    public JList getGamesList() {
        return gamesList_addUI;
    }

    public JScrollPane getListScrollPane() {
        return gameScrollPane;
    }

    public AImage getStatusBadge1() {
        return statusBadge1;
    }

    public AImage getStatusBadge2() {
        return statusBadge2;
    }

    public AImagePane getAddGamePane() {
        return pnlAddGamePane;
    }

    public AImagePane getAddUICoverImg() {
        return pnlCoverPane_addUI;
    }

    public void setCoverGame(AImagePane coverGame) {
        this.pnlBlankCoverGame_addUI = coverGame;
    }

    public AImagePane getBlankCover() {
        return pnlBlankCoverGame_addUI;
    }

    public JPanel getBottomPane() {
        return pnlBottomPane;
    }

    public AImagePane getPnlSearchBG() {
        return pnlSearchBG;
    }

    public JTextField getSearchText() {
        return txtSearchField_addUI;
    }

    public JPanel getButtonPane() {
        return pnlSearchButton;
    }

    public JPanel getGamesContainer() {
        return paneLibraryContainer;
    }

    public GridAnimation getGridAnimate() {
        return GridAnimate;
    }

    public GridManager getGridSplit() {
        return GridSplit;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getAddGameHeight() {
        return addGameHeight;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getAddGameWidth() {
        return addGameWidth;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getGameCoverHeight() {
        return gameCoverHeight;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getGameNameFont() {
        return gameNameFontSize;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getSelectedGameBarHeight() {
        return selectedGameBarHeight;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getSelectedGameBarWidth() {
        return selectedGameBarWidth;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getGameNameFontSize() {
        return gameNameFontSize;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getZoomButtonHeight() {
        return zoomButtonHeight;
    }

    /**
     * Size Value.
     * <p/>
     * @return int
     */
    public final int getGameCoverWidth() {
        return gameCoverWidth;
    }

    public JTextField getSearchBar() {
        return searchTextField;
    }

    public AImagePane getSearchBarBG() {
        return pnlSearchBarBG;
    }

    public AButton getSearchButton() {
        return btnSearch;
    }

    public AImagePane getSearchButtonBG() {
        return pnlSearchButtonBG;
    }

    public JPanel getSearchContainer() {
        return pnlSearchContainer;
    }

    public JPanel getSearchPane() {
        return pnlSearchBar;
    }

    public JPanel getSelectedGameContainer() {
        return pnlBottomCenterContainer;
    }

    public AuroraStorage getStorage() {
        return storage;
    }

    public JPanel getTextPane() {
        return pnlSearchText;
    }

    public AButton getZoomM() {
        return btnZoomLess;
    }

    public AButton getZoomP() {
        return btnZoomPlus;
    }

    public AButton getBtnAddGame() {
        return btnShowAddGameUI;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String path) {
        currentPath = path;
    }

    public ArrayList<AImagePane> getGameCover() {
        return gameCover;
    }

    public AImage getImgOrganizeType() {
        return imgOrganizeTypeSideBar;
    }

    public AHoverButton getImgGameLeft() {
        return btnGameLeft;
    }

    public AHoverButton getImgGameRight() {
        return btnGameRight;
    }

    public AImage getImgKeyIco() {
        return imgKeyIco;
    }

    public AImagePane getImgSelectedGamePane() {
        return imgLibraryStatusPane;
    }

    public JLabel getLblKeyAction() {
        return lblKeyAction;
    }

    public ArrayList<Boolean> getLoadedPanels() {
        return loadedPanels;
    }

    public boolean isAddGameUI_Visible() {
        return addGameUI_Visible;
    }

    public int getZoom() {
        return zoom;
    }

    public AuroraCoreUI getCoreUI() {
        return coreUI;
    }

    public DashboardUI getDashboardUI() {
        return dashboardUI;
    }

    public LibraryLogic getLogic() {
        return logic;
    }
}
