/*
 *  Made By Sardonix Creative.
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
package aurora.V1.core.screen_logic;

import aurora.V1.core.AuroraCoreUI;
import aurora.V1.core.Game;
import aurora.V1.core.main;
import aurora.V1.core.screen_handler.LibraryHandler;
import aurora.V1.core.screen_handler.LibraryHandler.GameLibraryKeyListener;
import aurora.V1.core.screen_ui.DashboardUI;
import aurora.V1.core.screen_ui.LibraryUI;
import aurora.engine.V1.Logic.AAnimate;
import aurora.engine.V1.Logic.APostHandler;
import aurora.engine.V1.Logic.ASort;
import aurora.engine.V1.Logic.AThreadWorker;
import aurora.engine.V1.Logic.AuroraScreenHandler;
import aurora.engine.V1.Logic.AuroraScreenLogic;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.log4j.Logger;

/**
 * .------------------------------------------------------------------------.
 * | LibraryLogic
 * .------------------------------------------------------------------------.
 * |
 * |
 * | This Class is the Logic component of the Library App. Its instanced
 * | In LibraryUI.
 * |
 * | This class is supposed to handle all of the Long Processing of UI or
 * | Actions generated by the Handler. Reusable processing and long logic
 * | methods should go here. It implements the AuroraScreenLogic interface.
 * |
 * .........................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 * @author Carlos Machado <camachado@gmail.com>
 * <p/>
 */
public class LibraryLogic implements AuroraScreenLogic {

    /**
     * Library UI instance.
     */
    private final LibraryUI libraryUI;

    /**
     * Library Handler instance.
     */
    private LibraryHandler libraryHandler;

    /**
     * Core UI instance.
     */
    private final AuroraCoreUI coreUI;

    /**
     * Dashboard UI instance.
     */
    private final DashboardUI dashboardUI;

    /**
     * Boolean on whether the library even has a single favorited game in DB.
     */
    private boolean libHasFavourites;

    static final Logger logger = Logger.getLogger(LibraryLogic.class);

    private AAnimate addGameToLibButtonAnimator;

    private boolean isLoaded;

    /**
     * .-----------------------------------------------------------------------.
     * | LibraryLogic(LibraryUI)
     * .-----------------------------------------------------------------------.
     * |
     * | This is the Constructor of the Game Library Logic class.
     * |
     * | The LibraryUI is required to make adjustments to the UI from the
     * | logic.
     * |
     * | The games are loaded, added and overall managed through here.
     * |
     * | NOTE: for Logic to work you must use the set(HandlerDashboardHandler)
     * | method for the logic to be able to attach some handlers to UI
     * | elements
     * |
     * .........................................................................
     *
     * @param gamelibraryUi LibraryUI
     *
     */
    public LibraryLogic(final LibraryUI gamelibraryUi) {
        this.libraryUI = gamelibraryUi;
        this.coreUI = gamelibraryUi.getCoreUI();
        this.dashboardUI = gamelibraryUi.getDashboardUI();
    }

    @Override
    public final void setHandler(final AuroraScreenHandler handler) {

        this.libraryHandler = (LibraryHandler) handler;

    }

    /**
     * .-----------------------------------------------------------------------.
     * | addGamesToLibrary()
     * .-----------------------------------------------------------------------.
     * |
     * | This method will add all games found in the Aurora Storage to the
     * | Library UI.
     * |
     * | It will add the games from favorite to non-favorite games.
     * | It will generate new Grids along the way when it fills previous ones.
     * |
     * .........................................................................
     *
     * <p/>
     */
    public final void addGamesToLibrary() {
        try {


            //* check that favorites are not null *//
            if (libraryUI.getStorage().getStoredLibrary().getFaveStates()
                != null) {
                libHasFavourites = true;
            }


            //clear grids to start


            libraryUI.getGridSplit().clearAllGrids();




            int librarySize = libraryUI.getStorage().getStoredLibrary()
                    .getGameNames()
                    .size() - 1;

            String organize = libraryUI.getStorage().getStoredSettings()
                    .getSettingValue(
                    "organize");

            ASort sorter = new ASort();


            if (organize == null){
                organize = "favorite";
                libraryUI.getStorage().getStoredSettings().saveSetting(organize,
                        "favorite");
            }

            // Check if Organization Type is "Favorite" //
            if (organize.equalsIgnoreCase("Favorite")) {

                //* Reverse Add Games Marked Fav first *//
                for (int i = librarySize; i >= 0;
                        i--) {

                    Game Game = new Game(libraryUI.getGridSplit(), coreUI,
                            dashboardUI, libraryUI.getStorage());
                    if (libHasFavourites && libraryUI.getStorage()
                            .getStoredLibrary()
                            .getFaveStates()
                            .get(i)) {
                        Game.setGameName(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getGameNames()
                                .get(i));
                        Game.setCoverUrl(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getBoxArtPath()
                                .get(i));

                        //* Handle appostrophese in game path *//
                        Game.setGamePath(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getGamePath()
                                .get(i).replace("'", "''"));
                        Game.setFavorite(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getFaveStates()
                                .get(i));
                        Game.setCoverSize(libraryUI.getGameCoverWidth(),
                                libraryUI
                                .getGameCoverHeight());

                        libraryUI.getGridSplit().addGame(Game);
                    }
                }


                //* Add Non-Fav games after *//

                for (int i = 0; i <= librarySize;
                        i++) {

                    Game Game = new Game(libraryUI.getGridSplit(), coreUI,
                            dashboardUI, libraryUI.getStorage());
                    if (!libHasFavourites || !libraryUI.getStorage()
                            .getStoredLibrary()
                            .getFaveStates()
                            .get(i)) {
                        Game.setGameName(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getGameNames()
                                .get(i));
                        Game.setCoverUrl(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getBoxArtPath()
                                .get(i));
                        //* Handle appostrophese in game path *//
                        Game.setGamePath(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getGamePath()
                                .get(i).replace("'", "''"));
                        if (libHasFavourites) {
                            Game.setFavorite(libraryUI.getStorage()
                                    .getStoredLibrary()
                                    .getFaveStates()
                                    .get(i));
                        }

                        Game.setCoverSize(libraryUI.getGameCoverWidth(),
                                libraryUI
                                .getGameCoverHeight());

                        libraryUI.getGridSplit().addGame(Game);
                    }

                }

                // Check if Organization Type is "Favorite" //
            } else if (organize.equalsIgnoreCase("Alphabetic")) {

                ArrayList<Game> gamesList = new ArrayList<Game>();

                // Create Array of Games //
                for (int i = librarySize; i >= 0;
                        i--) {

                    Game Game = new Game(libraryUI.getGridSplit(), coreUI,
                            dashboardUI, libraryUI.getStorage());

                    Game.setGameName(libraryUI.getStorage()
                            .getStoredLibrary()
                            .getGameNames()
                            .get(i));
                    Game.setCoverUrl(libraryUI.getStorage()
                            .getStoredLibrary()
                            .getBoxArtPath()
                            .get(i));

                    if (libHasFavourites) {
                        Game.setFavorite(libraryUI.getStorage()
                                .getStoredLibrary()
                                .getFaveStates()
                                .get(i));
                    }

                    //* Handle appostrophese in game path *//
                    Game.setGamePath(libraryUI.getStorage()
                            .getStoredLibrary()
                            .getGamePath()
                            .get(i).replace("'", "''"));

                    Game.setCoverSize(libraryUI.getGameCoverWidth(),
                            libraryUI
                            .getGameCoverHeight());

                    gamesList.add(Game);

                }

                String[] alphaArray = new String[gamesList.size()];

                for (int i = librarySize; i >= 0;
                        i--) {

                    alphaArray[i] = gamesList.get(i).getName();
                }


                alphaArray = sorter.firstToLast(alphaArray);

                for (int i = 0; i <= librarySize;
                        i++) {
                    int h = 0;
                    while (!gamesList.get(h).getName().equals(alphaArray[i])) {
                        h++;
                    }


                    libraryUI.getGridSplit().addGame(gamesList.get(h));

                }

                gamesList = null;

            } else if (organize.equalsIgnoreCase("Most Played")) {
            }


            // Add Metadata to games from database if it exists //
            if (libraryUI.getStorage().getStoredProfile()
                    .getGameNames() != null) {
                for (int i = 0; i < libraryUI.getStorage().getStoredProfile()
                        .getGameNames()
                        .size();
                        i++) {

                    String gameName = libraryUI.getStorage().getStoredProfile()
                            .getGameNames().get(i);
                    Game game = libraryUI.getGridSplit().getGameFromName(
                            gameName);

                    if (game != null) {

                        game.setGameType(libraryUI.getStorage()
                                .getStoredProfile()
                                .getGameTypes().get(i));
                        game.setTotalTimePlayed(libraryUI.getStorage()
                                .getStoredProfile()
                                .getTotalTimes().get(i));
                        game.setOcurrencesPlayed(libraryUI.getStorage()
                                .getStoredProfile()
                                .getOccurrenceTimes().get(i));
                        game.setLastPlayed(libraryUI.getStorage()
                                .getStoredProfile()
                                .getLastTimes().get(i));

                        logger.info("ProfileDB Game Name:" + game.getGameName());
                        logger.info("ProfileDB Game Type:" + game.getGameType());
                        logger.info("ProfileDB Last Played:" + game
                                .getLastPlayed());
                        logger.info("ProfileDB Occurences:" + game
                                .getOccurencesPlayed());
                        logger.info("ProfileDB Total Time:" + game
                                .getTotalTimePlayed());



                    }
                }


            }


            libraryUI.getGridSplit()
                    .finalizeGrid(libraryHandler.new ShowAddGameUiHandler(),
                    libraryUI
                    .getGameCoverWidth(), libraryUI.getGameCoverHeight());


            //Load First Grid by default
            loadGames(0);


        } catch (MalformedURLException ex) {
            logger.error(ex);
        }
    }

    public final void addAllGamesToLibrary() {
    }

    /**
     * .-----------------------------------------------------------------------.
     * | loadGames(int currentGridIndex)
     * .-----------------------------------------------------------------------.
     * |
     * | This method is where the Library loads the first and sometimes the
     * | second grid of games.
     * |
     * .........................................................................
     *
     * @throws MalformedURLException Exception
     */
    public final void loadGames(final int currentGridIndex) throws
            MalformedURLException {

        if (logger.isDebugEnabled()) {
            logger.debug("LAUNCHING LOAD METHOD");
        }

        int currentGrid = currentGridIndex;
        if (currentGrid < 0) {
            currentGrid = 0;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("current panel: " + currentGrid);
        }



        //Load First Panels

        libraryUI.setIsGameLibraryKeyListenerAdded(false);
        for (int i = 0; i < libraryUI.getGridSplit().getGrid(currentGrid)
                .getArray().size();
                i++) {
            Game game = new Game(libraryUI.getGridSplit(), coreUI, dashboardUI);
            try {
                game = (Game) libraryUI.getGridSplit().getGrid(currentGrid)
                        .getArray().get(i);
                game.addKeyListener(libraryHandler.new searchRefocusListener());

                for (int j = 0; j < game.getKeyListeners().length; j++) {
                    if (game.getKeyListeners()[j] instanceof GameLibraryKeyListener) {
                        libraryUI.setIsGameLibraryKeyListenerAdded(true);
                        break;
                    }
                }

                if (!libraryUI.IsGameLibraryKeyListenerAdded()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("ADDING GAMELIBRARYLISTENER TO " + game
                                .getName());
                    }

                    game
                            .addKeyListener(
                            libraryHandler.new GameLibraryKeyListener());
                }


                if (!game.isLoaded()) {
                    game.update();

                    if (logger.isDebugEnabled()) {
                        logger.debug("loading: " + game.getGameName());
                    }

                }
            } catch (RuntimeException ex) {
                logger.error(ex);
            }
        }


        libraryUI.setIsGameLibraryKeyListenerAdded(false);
        //Load Second Panel if exists -- SMART LOAD
        if (currentGrid < libraryUI.getGridSplit().getArray().size() - 1) {
            for (int i = 0; i < libraryUI.getGridSplit().getGrid(currentGrid
                                                                 + 1).getArray()
                    .size(); i++) {
                Game game = new Game(libraryUI.getGridSplit(), coreUI,
                        dashboardUI);
                try {
                    game = (Game) libraryUI.getGridSplit().getGrid(currentGrid
                                                                   + 1)
                            .getArray()
                            .get(i);

                    for (int j = 0; j < game.getKeyListeners().length; j++) {
                        if (game.getKeyListeners()[j] instanceof GameLibraryKeyListener) {
                            libraryUI.setIsGameLibraryKeyListenerAdded(true);
                            break;
                        }
                    }

                    if (!libraryUI.IsGameLibraryKeyListenerAdded()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("ADDING GAMELIBRARYLISTENER TO" + game
                                    .getName());
                        }

                        game.addKeyListener(
                                libraryHandler.new GameLibraryKeyListener());
                    }

                    if (!game.isLoaded()) {
                        game.update();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Secondary loading: " + game.getName());
                        }

                    }
                } catch (RuntimeException ex) {
                    logger.error(ex);
                }


            }
        }
    }

    /**
     * .-----------------------------------------------------------------------.
     * | checkNotifiers()
     * .-----------------------------------------------------------------------.
     * |
     * | This method checks if both Add Game UI badges are Green meaning
     * | user is able to add game to the library.
     * |
     * .........................................................................
     *
     * @throws MalformedURLException Exception
     */
    public void checkNotifiers() {

        if (libraryUI.getStatusBadge1().getImgURl().equals(
                "addUI_badge_valid.png")
            && libraryUI.getStatusBadge2()
                .getImgURl().equals("addUI_badge_valid.png") && !libraryUI
                .getAddGameToLibButton().isVisible()) {

            //Animate the Button below Add Game UI//
            animateAddButtonDown();


        } else if ((libraryUI.getStatusBadge1().getImgURl().equals(
                "addUI_badge_invalid.png")
                    || libraryUI.getStatusBadge2()
                .getImgURl().equals("addUI_badge_invalid.png"))
                   && libraryUI.getAddGameToLibButton().isVisible()) {

            //Animate up and hide it//
            animateAddButtonUp();
        }

    }

    private void animateAddButtonDown() {
        addGameToLibButtonAnimator = new AAnimate(libraryUI
                .getAddGameToLibButton());

        libraryUI.getAddGameToLibButton().setVisible(true);
        addGameToLibButtonAnimator.setInitialLocation((coreUI
                .getFrame()
                .getWidth() / 2) - libraryUI.getAddGameToLibButton()
                .getWidth() / 2, libraryUI.getAddGamePane()
                .getImgIcon()
                .getIconHeight() - 180);
        addGameToLibButtonAnimator.moveVertical(libraryUI
                .getAddGamePane()
                .getImgIcon()
                .getIconHeight() - 55, 20);
        addGameToLibButtonAnimator.removeAllListeners();
    }

    private void animateAddButtonUp() {
        addGameToLibButtonAnimator = new AAnimate(libraryUI
                .getAddGameToLibButton());

        addGameToLibButtonAnimator.setInitialLocation(libraryUI
                .getAddGameToLibButton().getX(), libraryUI
                .getAddGameToLibButton().getY());
        addGameToLibButtonAnimator.moveVertical(-5, 20);

        addGameToLibButtonAnimator
                .addPostAnimationListener(new APostHandler() {
            @Override
            public void postAction() {
                libraryUI.getAddGameToLibButton().setVisible(false);
            }
        });

    }
    private File steamFile = null;

    public File fetchSteamDirOnWindows() {
        final int HKEY_CURRENT_USER = 0x80000001;
        final int KEY_QUERY_VALUE = 1;
        final int KEY_SET_VALUE = 2;
        final int KEY_READ = 0x20019;

        final Preferences userRoot = Preferences.userRoot();
        final Preferences systemRoot = Preferences.systemRoot();
        final Class clz = userRoot.getClass();


        try {
            final Method openKey = clz.getDeclaredMethod("openKey",
                    byte[].class, int.class, int.class);
            openKey.setAccessible(true);

            final Method closeKey = clz
                    .getDeclaredMethod("closeKey",
                    int.class);
            closeKey.setAccessible(true);

            final Method winRegQueryValue = clz.getDeclaredMethod(
                    "WindowsRegQueryValueEx", int.class,
                    byte[].class);
            winRegQueryValue.setAccessible(true);
            final Method winRegEnumValue = clz.getDeclaredMethod(
                    "WindowsRegEnumValue1", int.class, int.class,
                    int.class);
            winRegEnumValue.setAccessible(true);
            final Method winRegQueryInfo = clz.getDeclaredMethod(
                    "WindowsRegQueryInfoKey1", int.class);
            winRegQueryInfo.setAccessible(true);


            byte[] valb = null;
            String vals = null;
            String key = null;
            Integer handle = -1;

            // Query for steam path
            key = "Software\\Classes\\steam\\Shell\\Open\\Command";
            handle = (Integer) openKey.invoke(systemRoot,
                    toCstr(key),
                    KEY_READ, KEY_READ);
            valb = (byte[]) winRegQueryValue.invoke(systemRoot,
                    handle,
                    toCstr(""));
            vals = (valb != null ? new String(valb).trim() : null);
            closeKey.invoke(Preferences.systemRoot(), handle);

            int steamExeIndex = vals.indexOf("steam.exe");
            if (steamExeIndex > 0) {
                String steamPath = vals.substring(1, steamExeIndex);
                steamPath = steamPath + "\\steamapps\\common";
                steamFile = new File(steamPath);

            }
        } catch (Exception ex) {
            logger.error(ex);
        }


        return steamFile;

    }

    private byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
}
