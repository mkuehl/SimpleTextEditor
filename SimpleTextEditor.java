package simpletexteditor.s_base.de.friesen.example.simpletexteditor;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
/*** added by dBase* modified by dSingle
 */
public class SimpleTextEditor {
	public static final String TITEL = "SimpleTextEditor";
	private Display display = new Display();
	private Shell shell = new Shell(this.display);
	private String lastDirectory;
	private Menu menu = new Menu(this.getShell(), SWT.BAR);
	private TextField text;
	public SimpleTextEditor() {
		this.getShell().setLayout(new GridLayout());
		this.text = getText();
		this.text.createMenuBar(this.menu);
		this.getShell().setMenuBar(menu);
		this.getShell().setSize(400, 300);
		this.addFeatures();
		this.getShell().open();
		this.getShell().pack();
		this.getShell().setSize(400, 300);
		while(! this.getShell().isDisposed()) {
			if(! this.display.readAndDispatch()) {
				this.display.sleep();
			}
		}
		this.display.dispose();
	}
	/*** modified by dSingle
	 */
	private TextField getText() {
		return new TextFieldSingle(this);
	}
	private void addFeatures() {
	}
	public void undo() {
		if(! text.getCurrentStyledTextExtended().getChanges().empty()) {
			Map<Integer, Listener []> map = this.removeAllModifyListener();
			TextChange change = text.getCurrentStyledTextExtended().getChanges().pop();
			text.getCurrentStyledTextExtended().replaceTextRange(change.getStart(),
				change.getLength(), change.getReplacedText());
			text.getCurrentStyledTextExtended().setCaretOffset(change.getStart());
			text.getCurrentStyledTextExtended().setTopIndex(text.getCurrentStyledTextExtended().getLineAtOffset(change.getStart()));
			this.addAllModifyListener(map);
		}
	}
	public void showAboutDialog() {
		final Shell dialog = new Shell(getShell(), SWT.APPLICATION_MODAL |
			SWT.DIALOG_TRIM);
		dialog.setText("About");
		dialog.setSize(250, 150);
		final Label label = new Label(dialog, SWT.NONE);
		label.setText("SimpleTextEditor v.0.0.1");
		label.setBounds(20, 15, 200, 20);
		dialog.open();
		while(! dialog.isDisposed()) {
			if(! display.readAndDispatch()) display.sleep();
		}
	}
	public boolean saveChangesDialog(String message) {
		if(! text.getCurrentStyledTextExtended().isUnsaved()) {
			return true;
		}
		final MessageBox box = new MessageBox(this.getShell(), SWT.ICON_WARNING |
			SWT.YES | SWT.NO | SWT.CANCEL);
		box.setMessage(message);
		box.setText("Editor");
		final int condition = box.open();
		if(condition == SWT.YES) {
			return text.saveText();
		}
		else if(condition == SWT.NO) {
			return true;
		}
		else {
			return false;
		}
	}
	public void addAllModifyListener(Map<Integer, Listener []> map) {
		for(Integer key : map.keySet()) {
			for(Listener listener : map.get(key)) {
				text.getCurrentStyledTextExtended().addListener(key, listener);
			}
		}
	}
	public Map<Integer, Listener []> removeAllModifyListener() {
		Map<Integer, Listener []> map = new HashMap<Integer, Listener []>();
		Listener [] listeners =
		text.getCurrentStyledTextExtended().getListeners(SWT.Modify);
		for(Listener listener : listeners) {
			text.getCurrentStyledTextExtended().removeListener(SWT.Modify, listener);
		}
		map.put(SWT.Modify, listeners);
		Listener [] lExtendetListener =
		text.getCurrentStyledTextExtended().getListeners(3000);
		for(Listener listener : lExtendetListener) {
			text.getCurrentStyledTextExtended().removeListener(3000, listener);
		}
		map.put(3000, lExtendetListener);
		return map;
	}
	public static void main(String [] args) {
		new SimpleTextEditor();
	}
	public String getLastDirectory() {
		return lastDirectory;
	}
	public void setLastDirectory(String lastDirectory) {
		this.lastDirectory = lastDirectory;
	}
	public Shell getShell() {
		return shell;
	}
}