package communication;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MenuComponent extends BaseComponent{

	protected MenuComponent(JComponent component) {
		super(component, null);
	}

	@Override protected void onInitialize() {
		super.get(JMenu.class).addMenuListener(new MenuListener() {
			@Override public void menuSelected(MenuEvent e) {
				System.out.println("menuSelected");
			}
			@Override public void menuCanceled(MenuEvent e) {
				System.out.println("menuCanceled");
			}
			@Override public void menuDeselected(MenuEvent e) {
				System.out.println("menuDeselected");
			}			
		});
	}

	@Override public void onExecute() {
	}	
}
