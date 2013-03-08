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
import aurora.V1.core.main;
import aurora.V1.core.screen_handler.WelcomeHandler;
import aurora.V1.core.screen_ui.DashboardUI;
import aurora.V1.core.screen_ui.WelcomeUI;
import aurora.engine.V1.Logic.AMixpanelAnalytics;
import aurora.engine.V1.Logic.ASound;
import aurora.engine.V1.Logic.AThreadWorker;
import aurora.engine.V1.Logic.AuroraScreenHandler;
import aurora.engine.V1.Logic.AuroraScreenLogic;
import aurora.engine.V1.UI.AImage;
import aurora.engine.V1.UI.AImagePane;
import aurora.engine.V1.UI.AProgressWheel;
import aurora.engine.V1.UI.AScrollingImage;
import aurora.engine.V1.UI.ATimeLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class WelcomeLogic implements AuroraScreenLogic {

    private final WelcomeUI startScreenUI;

    private WelcomeHandler startHandler;

    private final AuroraCoreUI coreUI;

    private AScrollingImage imgHexPane;

    private AImage imgTopLogo;

    private int topHeight;

    private AImage imgTopLogoSmall;

    private int topSmallImageHeight;

    private int topSmallImageWidth;

    private AThreadWorker animateTransision;

    private int centerHeight;

    private DashboardUI dashboardUI;

    static final Logger logger = Logger.getLogger(WelcomeLogic.class);

    private int frameControlHeight;

    private int logoHeight;

    private int logoWidth;

    public WelcomeLogic(WelcomeUI aStartScreenUI) {

        this.startScreenUI = aStartScreenUI;
        this.coreUI = startScreenUI.getCoreUI();

    }

    @Override
    public void setHandler(AuroraScreenHandler handler) {
        startHandler = (WelcomeHandler) handler;
    }

    private void loadTransitionUI() {

        setSize();

        imgHexPane = startScreenUI.getImgHexPane();
        imgTopLogo = coreUI.getLogoImage();
        imgTopLogoSmall = new AImage("dash_header_logo.png");


        imgTopLogoSmall.setImageSize(topSmallImageWidth, topSmallImageHeight);

    }

    public void startBackgroundMusic() {

//        try {
//            coreUI.getBackgroundSound().Play();
//        } catch (UnsupportedAudioFileException ex) {
//            logger.error(ex);
//        } catch (IOException ex) {
//            logger.error(ex);
//        } catch (LineUnavailableException ex) {
//            logger.error(ex);
//        } catch (InterruptedException ex) {
//            logger.error(ex);
//        }

    }

    public void transisionToDashboard() {

        loadTransitionUI();

        animateTransision = new AThreadWorker(new ActionListener() {
            //* Times cycling through threadWorker loop *//
            private int c = 0;

            //* Scale of Hex Image growning *//
            private int scale = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                c++;
                if (c == 1) {
                    //* change header logo to smaller logo *//
                    imgTopLogo.setIcon(imgTopLogoSmall.getImgIcon());
                    imgTopLogo.repaint();

                    //* stop scrolling Animation *//
                    imgHexPane.stop();

                    //* Remove Panel Containing Frame Controls*//
                    coreUI.getTopPane().remove(coreUI.getSouthFromTopPanel());
                    coreUI.getSouthFromTopPanel().setVisible(false);
                    coreUI.getTopPane().revalidate();

                    imgHexPane.setCenterToFrame(coreUI.getFrame());
                    imgHexPane.repaint();

                } else {
                    //* Change Size Values *//
                    scale++;
                    topHeight--;
                    centerHeight += 5;

                    //* Change Component Sizes *//
                    imgHexPane.grow(scale);
                    coreUI.getTopPane().setImageHeight(topHeight);
                    imgHexPane.repaint();
                    imgHexPane.revalidate();

                    coreUI.getTopPane().setPreferredSize(new Dimension(coreUI
                            .getTopPane()
                            .getWidth(),
                            topHeight - 50));
                    coreUI.getCenterPanel()
                            .setPreferredSize(new Dimension(coreUI
                            .getCenterPanel()
                            .getWidth(),
                            centerHeight));




                    if (topHeight >= imgTopLogoSmall.getImgIcon()
                            .getIconHeight() + 300) {
                        centerHeight = centerHeight - 2;
                        topHeight = imgTopLogoSmall.getImgIcon().getIconHeight()
                                    + 50;
                        coreUI.getTopPane().setImageHeight(topHeight);

                    }

                    //* Check if Reached Proper Size to stop *//
                    if (scale == 37) {

                        showDashdoard();
                        animateTransision.stop();
                    }

                }


            }
        }, 18);

        animateTransision.start();

    }

    private void showDashdoard() {

        AThreadWorker loadDashboard = new AThreadWorker(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //* Re-add Frame Controls *//

                coreUI.getTopPane().add(BorderLayout.PAGE_END, coreUI
                        .getSouthFromTopPanel());
                coreUI.getTopPane().revalidate();
                coreUI.getSouthFromTopPanel().revalidate();

                //* Set bigger background image for Frame Control panel *//
                coreUI.getFrameControlImagePane().setImage(
                        "dash_frameControl_bg.png");
                coreUI.getFrameControlImagePane().repaint();
                coreUI.getFrameControlImagePane().setImageHeight(
                        frameControlHeight);
                coreUI.getFrameControlImagePane().revalidate();


//                coreUI.getSouthFromTopPanel().setVisible(true);


                //* Remove all components in Center Panel *//
                coreUI.getCenterPanel().removeAll();

                //* Get or Generate new DashboardUI *//
                dashboardUI = startScreenUI.getLoadedDashboardUI();

                if (dashboardUI == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Creating New Dashboard");
                    }
                    dashboardUI = new DashboardUI(coreUI, startScreenUI);
                    dashboardUI.loadUI();
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Using LOADED dashboard");
                    }
                }

                //* Build DashboardUI *//
                dashboardUI.buildUI();

                coreUI.getSouthFromTopPanel().setVisible(true);


            }
        });

        loadDashboard.startOnce();

    }

    public boolean checkOnline(String URL) {
        final URL url;
        try {
            url = new URL("http://" + URL);
            try {

                final URLConnection conn = url.openConnection();
                conn.connect();
            } catch (IOException ex) {
                logger.warn("Computer is not online");
                return false;
            }
        } catch (MalformedURLException ex) {
            logger.error(ex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Computer is online");
        }

        return true;
    }

    public void sendAnalytics() {
        AMixpanelAnalytics analytics = new AMixpanelAnalytics(
                "f5f777273e62089193a68f99f4885a55");
        analytics.addProperty("Version", main.VERSION + " b" + coreUI
                .getBuildNumber());
        analytics.addProperty("Resolution", coreUI.getScreenHeight() + "x"
                                            + coreUI.getScreenWidth());
        analytics
                .addProperty("Java Version", System.getProperty("java.version"));
        analytics.addProperty("OS", System.getProperty("os.name"));
        analytics.sendEventProperty("Launched Aurora");


    }

    private void setSize() {



        if (coreUI.isLargeScreen()) {
            frameControlHeight = 0;
            topHeight = coreUI.getTopPane().getHeight();
            centerHeight = coreUI.getCenterPanel().getHeight() + 60;
            topSmallImageHeight = coreUI.getCenterPanel().getHeight() / 16 + 20;
            topSmallImageWidth = coreUI.getFrame().getWidth() / 2 + 20;
        } else {
            frameControlHeight = coreUI.getFrameControlImagePane().getImgIcon()
                    .getIconHeight();
            topHeight = coreUI.getTopPane().getHeight();
            centerHeight = coreUI.getCenterPanel().getHeight() + 60;
            topSmallImageHeight = coreUI.getCenterPanel().getHeight() / 16 + 20;
            topSmallImageWidth = coreUI.getFrame().getWidth() / 2 + 20;
        }


    }
}