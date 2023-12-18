package frontend;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Utils {
	public static void addComp(
			JPanel panel,
			JComponent comp,
			int xPos,
			int yPos,
			int topInset,
			int leftInset,
			int bottomInset,
			int rightInset
			) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = xPos;
		gc.gridy = yPos;
		gc.fill = GridBagConstraints.CENTER;
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets = new Insets(topInset, leftInset, bottomInset, rightInset);
		
		panel.add(comp, gc);
	}
	
	public static String formatString(String str, int maxLen) {
	
		if (str.length() < maxLen) {
			while (str.length() < maxLen) {
				str += " ";
			}
		}
		
		return str;
	}
}
