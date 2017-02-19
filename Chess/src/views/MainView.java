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

import java.awt.BorderLayout;

import controllers.BoardController;
import controllers.MainWindowController;
import engine.core.mvc.controller.BaseController;
import engine.core.mvc.view.BaseView;
import engine.factories.ViewFactory;

public class MainView extends BaseView {

	public <T extends BaseController> MainView(Class<T> controller) {
		super(controller, true);
	}
	
	
	public MainView(MainWindowController controller) {
		super(controller);
	}
	
	@Override public void render() {
		setLayout(new BorderLayout());
		
		BaseView boardGameView = ViewFactory.instance().get(BoardView.class, true, BoardController.class);
		boardGameView.render();
		
		add(boardGameView);
	}

	@Override public void dispose() {
		//removeAll();
		System.out.println("TODO - dispose in mainwindowview");
	}
}