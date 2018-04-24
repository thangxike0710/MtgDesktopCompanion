package org.magic.services;

import java.awt.Color;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import org.magic.gui.components.dialog.AboutDialog;
import org.magic.gui.renderer.ManaCellRenderer;

public class MTGConstants {

	private MTGConstants() {
	}

	public static final String CONF_FILENAME = "mtgcompanion-conf.xml";
	public static final File CONF_DIR = new File(System.getProperty("user.home") + "/.magicDeskCompanion/");
	public static final File MTG_DECK_DIRECTORY = new File(MTGConstants.CONF_DIR, "decks");
	public static final File MTG_WALLPAPER_DIRECTORY = new File(MTGConstants.CONF_DIR, "downloadWallpaper");
	public static final String MTG_APP_NAME = "MTG Desktop Companion";

	public static final String MTG_DESKTOP_ISSUES_URL = "https://github.com/nicho92/MtgDesktopCompanion/issues";
	public static final String MTG_DESKTOP_WIKI_URL = "https://github.com/nicho92/MtgDesktopCompanion/wiki";
	public static final String MTG_DESKTOP_POM_URL = "https://raw.githubusercontent.com/nicho92/MtgDesktopCompanion/master/pom.xml";
	public static final String MTG_DESKTOP_APP_ZIP = "https://github.com/nicho92/MtgDesktopCompanion/tree/master/dist";
	public static final String MTG_BOOSTERS_URI = "https://raw.githubusercontent.com/nicho92/MtgDesktopCompanion/master/src/main/resources/data/boosters.xml";
	public static final String WIZARD_EVENTS_URL = "https://magic.wizards.com/en/calendar-node-field-event-date-ajax/month/";

	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";


	
	public static final int MTG_DESKTOP_TABBED_POSITION = JTabbedPane.LEFT;

	public static final String TABLE_ALTERNATE_ROW_COLOR = "#E1E4F2";
	public static final Color THUMBNAIL_BACKGROUND_COLOR = SystemColor.windowBorder;

	public static final Color COLLECTION_100PC = Color.GREEN;
	public static final Color COLLECTION_90PC = new Color(188, 245, 169);
	public static final Color COLLECTION_50PC = Color.ORANGE;
	public static final Color COLLECTION_5PC = Color.YELLOW;

	public static final String KEYSTORE_NAME = "jssecacerts";
	public static final String KEYSTORE_PASS = "changeit";

	public static final URL WEBUI_LOCATION = MTGConstants.class.getResource("/web-ui");
	public static final String MTG_TEMPLATES_DIR = "./templates";
	public static final String MTG_DESKTOP_VERSION_FILE = "/version";

	
	private static String iconPack="flat";

	public static final ImageIcon ICON_GAME_HAND = new ImageIcon(MTGConstants.class.getResource("/icons/game/hand.png"));
	public static final ImageIcon ICON_GAME_LIBRARY = new ImageIcon(MTGConstants.class.getResource("/icons/game/librarysize.png"));
	public static final ImageIcon ICON_GAME_PLANESWALKER = new ImageIcon(MTGConstants.class.getResource("/icons/game/planeswalker.png"));
	public static final ImageIcon ICON_GAME_LIFE = new ImageIcon(MTGConstants.class.getResource("/icons/game/heart.png"));
	public static final ImageIcon ICON_GAME_POISON = new ImageIcon(MTGConstants.class.getResource("/icons/game/poison.png"));
	public static final ImageIcon ICON_RSS = new ImageIcon(MTGConstants.class.getResource("/icons/plugins/rss.png"));
	public static final ImageIcon ICON_TWITTER = new ImageIcon(MTGConstants.class.getResource("/icons/plugins/twitter.png"));
	public static final ImageIcon ICON_FORUM = new ImageIcon(MTGConstants.class.getResource("/icons/plugins/forum.png"));

	
	public static final ImageIcon ICON_EXPORT = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/export.png"));
	public static final ImageIcon ICON_SEARCH = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/search.png"));
	public static final ImageIcon ICON_FILTER = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/filter.png"));
	public static final ImageIcon ICON_CLEAR = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/clear.png"));
	public static final ImageIcon ICON_LOADING = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/load.gif"));
	public static final ImageIcon ICON_IMPORT = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/import.png"));
	public static final ImageIcon ICON_EURO = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/euro.png"));
	public static final ImageIcon ICON_NEW = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/new.png"));
	public static final ImageIcon ICON_REFRESH = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/refresh.png"));
	public static final ImageIcon ICON_DELETE = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/delete.png"));
	public static final ImageIcon ICON_CHECK = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/check.png"));
	public static final ImageIcon ICON_WEBSITE = new ImageIcon(MTGConstants.class.getResource("/icons/plugins/website.png"));
	public static final ImageIcon ICON_MANUAL = new ImageIcon(MTGConstants.class.getResource("/icons/plugins/manual.png"));
	public static final ImageIcon ICON_SAVE = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/save.png"));
	public static final ImageIcon ICON_COLLECTION = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/collection.png"));
	public static final ImageIcon ICON_GAME = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/game.png"));
	public static final ImageIcon ICON_COLORS = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/colors.gif"));
	public static final ImageIcon ICON_ALERT = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/bell.png"));
	public static final ImageIcon ICON_UP = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/up.png"));
	public static final ImageIcon ICON_NEWS = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/news.png"));
	public static final ImageIcon ICON_DOWN = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/down.png"));
	public static final ImageIcon ICON_DOLLARS = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/dollars.png"));
	public static final ImageIcon ICON_DASHBOARD = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/dashboard.png"));
	public static final ImageIcon ICON_OPEN = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/open.png"));
	public static final ImageIcon ICON_DECK = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/deck.png"));
	public static final ImageIcon ICON_STOCK = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/stock.png"));
	public static final ImageIcon ICON_SHOP = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/shop.png"));
	public static final ImageIcon ICON_BUILDER = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/create.png"));
	public static final ImageIcon ICON_CONFIG = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/admin.png"));
	public static final ImageIcon ICON_STORY = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/stories.png"));
	public static final ImageIcon ICON_WALLPAPER = new ImageIcon(MTGConstants.class.getResource("/icons/"+iconPack+"/wallpaper.png"));
	public static final ImageIcon ICON_ABOUT = new ImageIcon(MTGConstants.class.getResource("/icons/about.jpg"));
	public static final ImageIcon ICON_SPLASHSCREEN = new ImageIcon(MTGConstants.class.getResource("/icons/masters-logo.png"));
	
	public static final Image IMAGE_LOGO = Toolkit.getDefaultToolkit().getImage(MTGConstants.class.getResource("/icons/logo.png"));

	public static final URL URL_MANA_SYMBOLS = MTGConstants.class.getResource("/icons/mana/Mana.png");
	public static final ImageIcon ICON_MANA_GOLD = new ImageIcon(MTGConstants.class.getResource("/icons/mana/gold.png"));
	public static final ImageIcon ICON_MANA_INCOLOR = new ImageIcon(MTGConstants.class.getResource("/icons/mana/uncolor.png"));

	public static final ImageIcon ICON_BACK = new ImageIcon(MTGConstants.class.getResource("/icons/bottom.png"));


	public static final int TABLE_ROW_HEIGHT = 18;
	public static final int TABLE_ROW_WIDTH = 18;

	public static final String GAME_FONT = "Tahoma";

	public static final String HTML_TAG_TABLE = "table";
	public static final String HTML_TAG_TBODY = "tbody";
	public static final String HTML_TAG_TR = "tr";
	public static final String HTML_TAG_TD = "td";
	
	
}
