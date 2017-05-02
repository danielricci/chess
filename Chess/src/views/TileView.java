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

import controllers.BoardController;
import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;
import engine.core.mvc.view.PanelView;

public class TileView extends PanelView {
	
	private static int _counter = 0; 
	private int _coordinate = ++_counter;;
	
	private Image _image;
	
	private Color _defaultBackgroundColor;
	private Color _currentBackgroundColor;
	private Color _previousBackgroundColor;
		
	public enum TileBackgroundColor {
		FirstColor(new Color(209, 139, 71)),
		SecondColor(new Color(255, 205, 158)),
		NeighborColor(Color.BLUE);
		
		public final Color color;
		
		TileBackgroundColor(Color color) {
			this.color = color;
		}
	}
	
	public TileView(TileBackgroundColor defaultBackgroundColor) {
		
		// Set the controller associated to this view
		getViewProperties().setController(AbstractFactory
			.getFactory(ControllerFactory.class)
			.get(BoardController.class, true)
		);	
				
		// Set the default background color, and set the currently active color which should
		// be both the same when the initial render happens
		_defaultBackgroundColor = _currentBackgroundColor = defaultBackgroundColor.color;
	}
	
	@Override public void initializeComponents() {
		// TODO Auto-generated method stub
	}

	@Override public void initializeComponentBindings() {
		// TODO Auto-generated method stub		
	}
	
	@Override public void setBackground(Color backgroundColor) {
		super.setBackground(backgroundColor);
		_currentBackgroundColor = backgroundColor;
		repaint();
	}
		
 	@Override public void render() {
		super.render();
		setBackground(_currentBackgroundColor);
		repaint();
	}
	
	@Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(_image, 10, 8, 48, 48, null, null);       
	}	
}