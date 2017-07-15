/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package application;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import engine.core.menu.MenuBuilder;
import engine.core.system.AbstractApplication;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;
import menu.AboutItem;
import menu.ExitItem;
import menu.NeighboursItem;
import menu.NewGameItem;
import resources.Resources;
import resources.Resources.ResourceKeys;

public final class Application extends AbstractApplication {

	/**
	 * Constructs a new instance of this class
	 */
	public Application() {

		// Set the application dimensions
		Dimension applicationDimensions = new Dimension(800, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Set the size of application
        setSize(applicationDimensions);
        
		// Set the location of the window to be in middle of the screen
		setLocation(
			screenSize.width / 2 - applicationDimensions.width / 2,
			screenSize.height / 2 - applicationDimensions.height / 2
		);
        
        // The user cannot resize the game
        setResizable(false);
                      
        // Add the window listener to listen in on when there is a signal to exit the game
        addWindowListener(new WindowAdapter() {
			/**
			 * Catches a closing of this JFrame so we can handle it properly
			 * 
			 * @param windowEvent The event that this window triggered
			 */
			@Override public void windowClosing(WindowEvent windowEvent) {
				// Exit the game
				System.exit(0);
			};		
		});
	}
	
	/**
	 * Main entry point of the application
	 * 
	 * @param args The arguments passed in to affect the game
	 */
	public static void main(String[] args) {
		try {
			// Call the event queue and invoke a new runnable
			// object to execute our game.
        	EventQueue.invokeLater(new Runnable() {
        		@Override public void run() {
        			try {
        				Application.initialize(Application.class);
        				Application.instance().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
            });
    	} 
		catch (Exception exception) {
        	exception.printStackTrace();
    	}
    }
	
	@Override protected void initializeEngineResources() {
		
		// Set the default locale for our engine to recognize our localization
		Resources.instance().addLocale(ResourceBundle.getBundle("resources/Resources", Locale.CANADA), true);
		
		// Set the title
		setTitle(Resources.instance().getLocalizedString(ResourceKeys.Title));
		
		// Set the icon that will at the upper-left of the window
        setIconImage(new ImageIcon(Resources.instance().getLocalizedString(ResourceKeys.GameIcon)).getImage());
	}
	
	@Override protected void setWindowedInstanceMenu() {
		
		// Populate the file menu and its entries
		PopulateFileMenu();
		
		// Populate the debug menu and its entries
		PopulateDebugMenu();
		
		// Populate the help menu and its entries
		PopulateHelpMenu();
	}

	/**
	 * Populate the file menu and its entries
	 */
	private void PopulateFileMenu() {
		MenuBuilder.start(getJMenuBar())
			.AddMenu("File")
				.AddMenuItem(NewGameItem.class)
			.AddSeparator()
				.AddMenuItem(ExitItem.class);
	}

	/**
	 * Populate the debug menu and its entries
	 */
	private void PopulateDebugMenu() {
		MenuBuilder.start(getJMenuBar())
			.AddMenu(Resources.instance().getLocalizedString(ResourceKeys.Debug))
				.AddMenuItem(NeighboursItem.class);
	}
	
	/**
	 * Populate the help menu and its entries
	 */
	private void PopulateHelpMenu() {
		MenuBuilder.start(getJMenuBar())
			.AddMenu("Help")
				.AddMenuItem(AboutItem.class);
	}

	@Override protected void initializeEngineProperties() {
		EngineProperties.instance().setProperty(Property.DATA_PATH_XML, "/generated/tilemap.xml");
		EngineProperties.instance().setProperty(Property.DATA_PATH_SHEET, "/generated/tilemap.png");
	}
}