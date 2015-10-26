package simpletexteditor.s_complete.de.friesen.example.simpletexteditor;

import org.eclipse.swt.custom.StyledText;
import java.io.File;
import java.util.Stack;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
/*** added by dBase
 */
public class StyledTextExtended extends StyledText {
	public static final int UNDO_LIMIT = 500;
	private Stack<TextChange> changes = new Stack<TextChange>();
	private boolean unsaved;
	private File file;
	private static final String UNTITLED_DOCUMENT = "Untitled Document";
	public StyledTextExtended(Composite parent, int style) {
		super(parent, style);
	}
	public boolean isUnsaved() {
		return unsaved;
	}
	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
	}
	public Stack<TextChange> getChanges() {
		return changes;
	}
	public void setChanges(Stack<TextChange> changes) {
		this.changes = changes;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getTitel() {
		return this.getTitel(true);
	}
	public String getTitel(boolean showState) {
		StringBuilder sb = new StringBuilder();
		sb.append(file == null ? UNTITLED_DOCUMENT : file.getName());
		if(showState) {
			sb.append(unsaved ? "*" : "");
		}
		return sb.toString();
	}
}