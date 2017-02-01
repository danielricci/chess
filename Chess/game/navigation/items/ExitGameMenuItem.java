package navigation.items;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import communication.internal.command.ItemComponent;
import managers.LocalizationManager;
import managers.LocalizationManager.Resources;

public class ExitGameMenuItem extends ItemComponent {
	
	public ExitGameMenuItem(JComponent parent) {
		super(new JMenuItem(LocalizationManager.Get(Resources.ExitMenu)), parent);
	}
	
	@Override public void onExecute(ActionEvent actionEvent) {
		//Application.Instance().dispatchEvent(new WindowEvent(Application.Instance(), WindowEvent.WINDOW_CLOSING));
	}
}