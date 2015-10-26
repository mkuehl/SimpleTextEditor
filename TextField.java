package simpletexteditor.m_complete.de.friesen.example.simpletexteditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.custom.StyledText;
import java.util.List;
/*** added by dBase
 */
public abstract class TextField {
	abstract public void createMenuBar(Menu menu);
	abstract public StyledTextExtended getCurrentStyledTextExtended();
	abstract public boolean saveText();
	abstract public List<StyledTextExtended> getStyledTextExtended();
	abstract public void addEventListener(LoadEventListener listener);
	abstract public void removeEventListener(LoadEventListener listener);
	public void removeAllListener(StyledText ste) {
		int [] eventTypes = {
			3007, 3011, SWT.Resize, SWT.Move, SWT.Dispose, SWT.DragDetect, 3000,
			SWT.FocusIn, SWT.FocusOut, SWT.Gesture, SWT.Help, SWT.KeyUp, SWT.KeyDown,
			3001, 3002, SWT.MenuDetect, SWT.Modify, SWT.MouseDown, SWT.MouseUp,
			SWT.MouseDoubleClick, SWT.MouseMove, SWT.MouseEnter, SWT.MouseExit,
			SWT.MouseHover, SWT.MouseWheel, SWT.Paint, 3008, SWT.Selection, SWT.Touch,
			SWT.Traverse, 3005, SWT.Verify, 3009, 3010
		};
		for(int eventType : eventTypes) {
			Listener [] listeners = ste.getListeners(eventType);
			for(Listener listener : listeners) {
				ste.removeListener(eventType, listener);
			}
		}
	}
}