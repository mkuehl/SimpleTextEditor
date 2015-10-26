package simpletexteditor.s_complete.de.friesen.example.simpletexteditor;

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
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.ArmEvent;
import java.util.EventObject;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.layout.GridData;
import java.util.EventObject;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.util.EventObject;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.LineStyleEvent;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Color;
/*** added by dBase* modified by dSingle* modified by dHighlightCurrentLine*
modified by dStatistics* modified by dLinewrap* modified by dHighlightMode*
modified by dSQL* modified by dJava* modified by dCSharp
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
	/*** modified by dHighlightCurrentLine* modified by dStatistics* modified by
	dLinewrap* modified by dHighlightMode
	 */
	private void addFeatures() {
		addFeatures_original4();
		this.addLanguageHighlight();
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
	/*** modified by dHighlightCurrentLine
	 */
	private void addFeatures_original0() {
	}
	/*** added by dHighlightCurrentLine
	 */
	private void addHighlightCurrentLine() {
		for(MenuItem menuItem : this.menu.getItems()) {
			if(menuItem.getText().toLowerCase().contains("edit")) {
				Menu edit = menuItem.getMenu();
				new MenuItem(edit, SWT.SEPARATOR);
				final MenuItem hcl = new MenuItem(edit, SWT.CHECK);
				hcl.setText("Highlight current line");
				hcl.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent event) {
							toggleHighlightCurrentLine();
						}
					});
				hcl.addArmListener(new ArmListener() {
						@Override
						public void widgetArmed(ArmEvent event) {
							hcl.setSelection(isHighlightCurrentLine());
						}
					});
				break;
			}
		}
		this.text.addEventListener(new LoadEventListener() {
				@Override
				public void load(EventObject e) {
					StyledTextExtended ste = ( StyledTextExtended ) e.getSource();
					if(isHighlightCurrentLine()) {
						addHighlightListener(ste);
						performHighlightCurrentLine();
					}
				}
			});
	}
	/*** added by dHighlightCurrentLine
	 */
	private void toggleHighlightCurrentLine() {
		if(isHighlightCurrentLine()) {
			for(StyledTextExtended ste : this.text.getStyledTextExtended()) {
				Listener [] listeners = ste.getListeners(3011);
				for(Listener listener : listeners) {
					if(listener instanceof HightlightListener) {
						ste.removeCaretListener(( HightlightListener ) listener);
					}
				}
			}
			this.setWhiteBackground();
			this.setHighlightCurrentLine(false);
		}
		else {
			for(StyledTextExtended ste : this.text.getStyledTextExtended()) {
				this.addHighlightListener(ste);
			}
			this.highlightCurrentLine();
			this.setHighlightCurrentLine(true);
		}
	}
	/*** added by dHighlightCurrentLine
	 */
	private void addHighlightListener(StyledTextExtended ste) {
		ste.addCaretListener(new HightlightListener() {
				@Override
				public void caretMoved(CaretEvent event) {
					performHighlightCurrentLine();
				}
			});
	}
	/*** added by dHighlightCurrentLine
	 */
	private void performHighlightCurrentLine() {
		setWhiteBackground();
		highlightCurrentLine();
	}
	/*** added by dHighlightCurrentLine
	 */
	private void highlightCurrentLine() {
		for(StyledTextExtended ste : this.text.getStyledTextExtended()) {
			int currentLine = ste.getLineAtOffset(ste.getCaretOffset());
			ste.setLineBackground(currentLine, 1,
				display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		}
	}
	/*** added by dHighlightCurrentLine
	 */
	private void setWhiteBackground() {
		for(StyledTextExtended ste : this.text.getStyledTextExtended()) {
			ste.setLineBackground(0, ste.getLineCount(),
				display.getSystemColor(SWT.COLOR_WHITE));
		}
	}
	/*** added by dHighlightCurrentLine
	 */
	private boolean highlightCurrentLine = false;
	/*** added by dHighlightCurrentLine
	 */
	public synchronized boolean isHighlightCurrentLine() {
		return this.highlightCurrentLine;
	}
	/*** added by dHighlightCurrentLine
	 */
	public synchronized void setHighlightCurrentLine(boolean
		highlightCurrentLine) {
		this.highlightCurrentLine = highlightCurrentLine;
	}
	/*** added by dStatistics
	 */
	private Label status;
	/*** modified by dHighlightCurrentLine* modified by dStatistics
	 */
	private void addFeatures_original2() {
		addFeatures_original0();
		this.addHighlightCurrentLine();
	}
	/*** added by dStatistics
	 */
	public void addStatistics() {
		this.status = new Label(this.shell, SWT.BORDER);
		this.status.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING,
				true, false, 2, 1));
		this.shell.layout();
		this.updateStatus();
		for(StyledTextExtended ste : this.text.getStyledTextExtended()) {
			addListener(ste);
		}
		this.text.addEventListener(new LoadEventListener() {
				@Override
				public void load(EventObject e) {
					StyledTextExtended ste = ( StyledTextExtended ) e.getSource();
					addListener(ste);
					updateStatus();
				}
			});
	}
	/*** added by dStatistics
	 */
	private void addListener(StyledTextExtended ste) {
		ste.addCaretListener(new CaretListener() {
				@Override
				public void caretMoved(CaretEvent event) {
					updateStatus();
				}
			});
	}
	/*** added by dStatistics
	 */
	private void updateStatus() {
		StringBuffer buf = new StringBuffer();
		buf.append("Chars: ");
		buf.append(this.text.getCurrentStyledTextExtended().getCharCount());
		buf.append("\tLine: ");
		buf.append(this.text.getCurrentStyledTextExtended().getLineAtOffset(this.text.getCurrentStyledTextExtended().getCaretOffset())
			+ 1);
		buf.append(" of ");
		buf.append(this.text.getCurrentStyledTextExtended().getLineCount());
		this.status.setText(buf.toString());
	}
	/*** modified by dHighlightCurrentLine* modified by dStatistics* modified by
	dLinewrap
	 */
	private void addFeatures_original3() {
		addFeatures_original2();
		this.addStatistics();
	}
	/*** added by dLinewrap
	 */
	private void addLineWrap() {
		for(MenuItem menuItem : this.menu.getItems()) {
			if(menuItem.getText().toLowerCase().contains("edit")) {
				Menu edit = menuItem.getMenu();
				new MenuItem(edit, SWT.SEPARATOR);
				final MenuItem lineWrap = new MenuItem(edit, SWT.CHECK);
				lineWrap.setText("Word Wrap\tCtrl+W");
				lineWrap.setAccelerator(SWT.CTRL + 'W');
				lineWrap.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent event) {
							toggleWordWrap();
						}
					});
				lineWrap.addArmListener(new ArmListener() {
						@Override
						public void widgetArmed(ArmEvent event) {
							lineWrap.setSelection(isWordWrap());
						}
					});
				break;
			}
		}
		this.text.addEventListener(new LoadEventListener() {
				@Override
				public void load(EventObject e) {
					StyledTextExtended ste = ( StyledTextExtended ) e.getSource();
					ste.setWordWrap(isWordWrap());
				}
			});
	}
	/*** added by dLinewrap
	 */
	private boolean wordWrap = false;
	/*** added by dLinewrap
	 */
	public synchronized boolean isWordWrap() {
		return wordWrap;
	}
	/*** added by dLinewrap
	 */
	public synchronized void setWordWrap(boolean wordWrap) {
		this.wordWrap = wordWrap;
	}
	/*** added by dLinewrap
	 */
	public synchronized void toggleWordWrap() {
		boolean wordWrap = ! this.isWordWrap();
		for(StyledTextExtended ste : this.text.getStyledTextExtended()) {
			ste.setWordWrap(wordWrap);
		}
		this.setWordWrap(wordWrap);
	}
	/*** modified by dHighlightCurrentLine* modified by dStatistics* modified by
	dLinewrap* modified by dHighlightMode
	 */
	private void addFeatures_original4() {
		addFeatures_original3();
		this.addLineWrap();
	}
	/*** added by dHighlightMode
	 */
	private Map<String, FileExtension> extensions = new HashMap<String,
		FileExtension>();
	/*** added by dHighlightMode* modified by dSQL* modified by dJava* modified
	by dCSharp
	 */
	private void addLanguageHighlight() {
		this.extensions.put("cs", this.getCSharpFileExtension());
		addLanguageHighlight_original8();
	}
	/*** added by dHighlightMode
	 */
	private void addSourceHighlighting(StyledTextExtended ste) {
		if(ste.getFile() == null) {
			return;
		}
		final String extension = this.getFileExtension(ste.getFile().getName());
		if(extension.isEmpty()) {
			return;
		}
		if(! this.extensions.containsKey(extension)) {
			return;
		}
		ste.addLineStyleListener(new LineStyleListener() {
				public void lineGetStyle(LineStyleEvent event) {
					StyleRange [] styleranges = getStyleRanges(extensions.get(extension),
						event.lineText, event.lineOffset);
					event.styles = styleranges;
				}
			});
	}
	/*** added by dHighlightMode
	 */
	private StyleRange [] getStyleRanges(FileExtension fileExtension, String
		line, int lineOffset) {
		final String [] keywords = fileExtension.getKeywords();
		final StyleRange styleRange = fileExtension.getStyleRange();
		List<StyleRange> list = new LinkedList<StyleRange>();
		int i = - 1;
		for(String keyword : keywords) {
			while((i = line.indexOf(keyword, i + 1)) > - 1) {
				if(0 < i && previousChar(line.charAt(i - 1))) {
					continue;
				}
				boolean isLineEnd =(i + keyword.length() == line.length());
				boolean perform = isLineEnd || Character.isWhitespace(line.charAt(i +
						keyword.length()));
				if(perform) {
					StyleRange cloned = ( StyleRange ) styleRange.clone();
					cloned.start = lineOffset + i;
					cloned.length = keyword.length();
					list.add(cloned);
				}
			}
		}
		StyleRange [] styleranges = ( StyleRange [] ) list.toArray(new
			StyleRange[list.size()]);
		return styleranges;
	}
	/*** added by dHighlightMode
	 */
	private boolean previousChar(char charAt) {
		if(charAt == '(' || Character.isWhitespace(charAt)) {
			return false;
		}
		return true;
	}
	/*** added by dHighlightMode
	 */
	private String getFileExtension(String fileWithPath) {
		int lastIndexOf = fileWithPath.lastIndexOf(".");
		if(lastIndexOf == - 1) {
			return "";
		}
		if(lastIndexOf + 1 < fileWithPath.length()) {
			return fileWithPath.substring(lastIndexOf + 1);
		}
		return "";
	}
	/*** added by dHighlightMode* modified by dSQL
	 */
	private void addLanguageHighlight_original5() {
		this.extensions.put("java", this.getJavaFileExtension());
		this.extensions.put("cs", this.getCSharpFileExtension());
		this.extensions.put("sql", this.getSqlFileExtension());
		this.text.addEventListener(new LoadEventListener() {
				@Override
				public void load(EventObject e) {
					StyledTextExtended text = ( StyledTextExtended ) e.getSource();
					addSourceHighlighting(text);
					text.redraw();
				}
			});
	}
	/*** added by dSQL
	 */
	private FileExtension getSqlFileExtension() {
		String [] keywords_sql03 = {
			"ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS",
			"ASENSITIVE", "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "BEGIN",
			"BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY", "CALL",
			"CALLED", "CASCADED", "CASE", "CAST", "CHAR", "CHARACTER", "CHECK", "CLOB",
			"CLOSE", "COLLATE", "COLUMN", "COMMIT", "CONDITION", "CONNECT",
			"CONSTRAINT", "CONTINUE", "CORRESPONDING", "CREATE", "CROSS", "CUBE",
			"CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP",
			"CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP",
			"CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE",
			"DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT",
			"DELETE", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT", "DISTINCT",
			"DO", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "ELSEIF",
			"END", "ESCAPE", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXTERNAL",
			"FALSE", "FETCH", "FILTER", "FLOAT", "FOR", "FOREIGN", "FREE", "FROM",
			"FULL", "FUNCTION", "GET", "GLOBAL", "GRANT", "GROUP", "GROUPING",
			"HANDLER", "HAVING", "HOLD", "HOUR", "IDENTITY", "IF", "IMMEDIATE", "IN",
			"INDICATOR", "INNER", "INOUT", "INPUT", "INSENSITIVE", "INSERT", "INT",
			"INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN",
			"LANGUAGE", "LARGE", "LATERAL", "LEADING", "LEAVE", "LEFT", "LIKE",
			"LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOOP", "MATCH", "MEMBER", "MERGE",
			"METHOD", "MINUTE", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL",
			"NATURAL", "NCHAR", "NCLOB", "NEW", "NO", "NONE", "NOT", "NULL", "NUMERIC",
			"OF", "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT", "OUTER", "OUTPUT",
			"OVER", "OVERLAPS", "PARAMETER", "PARTITION", "PRECISION", "PREPARE",
			"PRIMARY", "PROCEDURE", "RANGE", "READS", "REAL", "RECURSIVE", "REF",
			"REFERENCES", "REFERENCING", "RELEASE", "REPEAT", "RESIGNAL", "RESULT",
			"RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW",
			"ROWS", "SAVEPOINT", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SELECT",
			"SENSITIVE", "SESSION_USER", "SET", "SIGNAL", "SIMILAR", "SMALLINT",
			"SOME", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE",
			"SQLWARNING", "START", "STATIC", "SUBMULTISET", "SYMMETRIC", "SYSTEM",
			"SYSTEM_USER", "TABLE", "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP",
			"TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATION",
			"TREAT", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNNEST",
			"UNTIL", "UPDATE", "USER", "USING", "VALUE", "VALUES", "VARCHAR",
			"VARYING", "WHEN", "WHENEVER", "WHERE", "WHILE", "WINDOW", "WITH",
			"WITHIN", "WITHOUT", "YEAR"
		};
		StyleRange styleRange = new StyleRange();
		styleRange.foreground = new Color(display, 165, 42, 42);
		styleRange.font = new Font(display, "Monospace", 10, SWT.BOLD);
		return new FileExtension(keywords_sql03, styleRange);
	}
	/*** added by dHighlightMode* modified by dSQL* modified by dJava
	 */
	private void addLanguageHighlight_original7() {
		this.extensions.put("sql", this.getSqlFileExtension());
		addLanguageHighlight_original5();
	}
	/*** added by dJava
	 */
	private FileExtension getJavaFileExtension() {
		StyleRange styleRange = new StyleRange();
		styleRange.foreground = new Color(display, 127, 0, 100);
		styleRange.font = new Font(display, "Monospace", 10, SWT.BOLD);
		String [] keywords_java = {
			"abstract", "continue", "for", "new", "switch", "assert", "default",
			"goto", "package", "synchronized", "boolean", "do", "if", "private",
			"this", "break", "double", "implements", "protected", "throw", "byte",
			"else", "import", "public", "throws", "case", "enum", "instanceof",
			"return", "transient", "catch", "extends", "int", "short", "try", "char",
			"final", "interface", "static", "void", "class", "finally", "long",
			"strictfp", "volatile", "const", "float", "native", "super", "while"
		};
		return new FileExtension(keywords_java, styleRange);
	}
	/*** added by dHighlightMode* modified by dSQL* modified by dJava* modified
	by dCSharp
	 */
	private void addLanguageHighlight_original8() {
		this.extensions.put("java", this.getJavaFileExtension());
		addLanguageHighlight_original7();
	}
	/*** added by dCSharp
	 */
	private FileExtension getCSharpFileExtension() {
		String [] keywords_csharp = {
			"abstract", "event", "new", "struct", "as", "explicit", "null", "switch",
			"base", "extern", "object", "this", "bool", "false", "operator", "throw",
			"break", "finally", "out", "true", "byte", "fixed", "override", "try",
			"case", "float", "params", "typeof", "catch", "for", "private", "uint",
			"char", "foreach", "protected", "ulong", "checked", "goto", "public",
			"unchecked", "class", "if", "readonly", "unsafe", "const", "implicit",
			"ref", "ushort", "continue", "in", "return", "using", "decimal", "int",
			"sbyte", "virtual", "default", "interface", "sealed", "volatile",
			"delegate", "internal", "short", "void", "do", "is", "sizeof", "while",
			"double", "lock", "stackalloc", "else", "long", "static", "enum",
			"namespace", "string"
		};
		StyleRange styleRange = new StyleRange();
		styleRange.foreground = new Color(display, 48, 30, 255);
		styleRange.font = new Font(display, "Monospace", 10, SWT.BOLD);
		return new FileExtension(keywords_csharp, styleRange);
	}
}