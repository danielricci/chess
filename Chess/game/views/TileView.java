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

package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import communication.internal.dispatcher.DispatchOperation;
import controllers.TileController;
import models.GameModel;
import models.TileModel;

public class TileView extends BaseView {

	private static final Color SelectedColor = Color.DARK_GRAY;
	private static final Color GuideColor = Color.BLUE;
	private static final Color CaptureColor = Color.GREEN;
	private static final Color HoverColor = Color.LIGHT_GRAY;
	
	private static int TileViewCounter = 0;
	
	private Image _image;
	
	private final Color _defaultBackgroundColor;
	private Color _currentBackgroundColor;
	
	private Map<communication.internal.dispatcher.DispatchOperation, MouseListener> _operationHandlers = new HashMap<>();
	
	public TileView(final TileController controller) {
		super(controller);
		
		TileView.cycleBackgroundColor();
		_defaultBackgroundColor = _currentBackgroundColor = TileView.TileViewCounter % 2 == 0 
			? new Color(209, 139, 71) 
			: new Color(255, 205, 158);
	}
	
	@Override public void setBackground(Color backgroundColor) {
		super.setBackground(backgroundColor);
		_currentBackgroundColor = backgroundColor;
	}
		
    @Override public void register() {
    	addMouseListener(new MouseAdapter() {  		    		
    		@Override public void mouseReleased(MouseEvent e) {
    			TileController controller = getController(TileController.class);
    			controller.processTileSelected();
    		}
    		@Override public void mouseEntered(MouseEvent e) {
    			TileController controller = getController(TileController.class);
    			controller.setNeighborsSelected(true);    					    			
    		}
    		@Override public void mouseExited(MouseEvent e) {
    			TileController controller = getController(TileController.class);
    			controller.setNeighborsSelected(false);
    		}
		});
    }
    
	@Override public void update(Observable obs, Object arg) {
		
		super.update(obs, arg);
		
		TileModel tileModel = (TileModel)obs;
		TileController tileController = getController(TileController.class);
		
		for(DispatchOperation operation : tileModel.getOperations()) {
			switch(operation) {
			case Refresh:
				break;
			default:
				break;
			}
			refresh(tileModel); 
		}
		
	}
	
	@Override public void render() {
		
		super.render();
		setBackground(_currentBackgroundColor);
		repaint();
		
		/* 
		 * TODO Enable this when it is time to run with real pieces
		_image = controller.getTileImage();
		;
		 */
	}
	
	@Override public void refresh(GameModel gameModel) {
		
		super.refresh(gameModel);
		
		/* TODO Enable this when it is time to run with real pieces
		TileModel model = (TileModel)gameModel;
		PlayerModel player = model.getPlayer();

		if(player == null) {
			_image = null;
		}
		else {
			_image = player.getPieceData(model);
		}
		*/
		repaint();
		
	}

	@Override public void dispose() {
		removeAll();
	}
	
	@Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(_image, 10, 8, 48, 48, null, null);       
	}
	
	public static void cycleBackgroundColor() {
		++TileView.TileViewCounter;
	}
	
	private void highlightNeighbors(TileModel tile, DispatchOperation operation) {
		
		boolean result = (boolean)tile.getCachedData(operation);
		if(!result)
		{
			removeMouseListener(_operationHandlers.get(operation));
			_operationHandlers.remove(operation);
		}
		else
		{
			MouseListener ml = new MouseAdapter() {  		    		
	    		@Override public void mouseEntered(MouseEvent e) {
	    			TileController controller = getController(TileController.class);
	    			controller.setNeighborsSelected(true);    					    			
	    		}
	    		@Override public void mouseExited(MouseEvent e) {
	    			TileController controller = getController(TileController.class);
	    			controller.setNeighborsSelected(false);
	    		}
			};
    		
			addMouseListener(ml);
			_operationHandlers.put(operation, ml);
		}
	}
	
	private void updateSelectedCommand(Color color) {
    	setBackground(color);
    }
	
	private void debugger_playerColorVisibility(TileModel tile, DispatchOperation operation) {
		TileController controller = getController(TileController.class);
		Color color = controller.getTileColor();

		boolean isSelected = (boolean)tile.getCachedData(operation);
		if(!isSelected || color == null) {
			color = _currentBackgroundColor;
		}
		
		updateSelectedCommand(color);
		repaint();
	}
}