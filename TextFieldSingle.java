package simpletexteditor.s_base.de.friesen.example.simpletexteditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
/*** added by dSingle
 */
public class TextFieldSingle extends TextField {
	private SimpleTextEditor ste;
	private static final String SAVE_CHANGES = "save changes? ";
	private StyledTextExtended text;
	public TextFieldSingle(SimpleTextEditor ste) {
		this.ste = ste;
		this.text = this.createStyledTextExtended(null);
		this.addListener(this.text);
		this.setTitle();
		this.ste.getShell().addShellListener(new ShellAdapter() {
				@Override
				public void shellClosed(ShellEvent event) {
					if(TextFieldSingle.this.text.isUnsaved() && !
						TextFieldSingle.this.ste.saveChangesDialog(SAVE_CHANGES)) {
						event.doit = false;
					}
				}
			});
	}
	@Override
	public void createMenuBar(Menu menu) {
		MenuItem open = new MenuItem(menu, SWT.CASCADE);
		open.setText("File");
		Menu dropMenu = new Menu(this.ste.getShell(), SWT.DROP_DOWN);
		open.setMenu(dropMenu);
		open = new MenuItem(dropMenu, SWT.NULL);
		open.setText("Open...\tCtrl+O");
		open.setAccelerator(SWT.CTRL + 'O');
		open.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					if(TextFieldSingle.this.ste.saveChangesDialog(SAVE_CHANGES)) {
						final FileDialog dialog = new
						FileDialog(TextFieldSingle.this.ste.getShell(), SWT.OPEN);
						if(TextFieldSingle.this.ste.getLastDirectory() != null) {
							dialog.setFilterPath(TextFieldSingle.this.ste.getLastDirectory());
						}
						final String selectedFile = dialog.open();
						if(selectedFile == null) {
							return;
						}
						TextFieldSingle.this.loadText(selectedFile);
					}
				}
			});
		MenuItem save = new MenuItem(dropMenu, SWT.NULL);
		save.setText("Save\tCtrl+S");
		save.setAccelerator(SWT.CTRL + 'S');
		save.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.saveText();
				}
			});
		MenuItem saveAs = new MenuItem(dropMenu, SWT.NULL);
		saveAs.setText("Save As...");
		saveAs.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					System.err.println("NEEDS TO BE IMPLEMENTED");
				}
			});
		MenuItem exit = new MenuItem(dropMenu, SWT.SEPARATOR);
		exit = new MenuItem(dropMenu, SWT.NULL);
		exit.setText("Exit\tAlt+F4");
		exit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					if(TextFieldSingle.this.ste.saveChangesDialog(SAVE_CHANGES)) {
						TextFieldSingle.this.ste.getShell().dispose();
					}
				}
			});
		MenuItem edit = new MenuItem(menu, SWT.CASCADE);
		edit.setText("Edit");
		dropMenu = new Menu(this.ste.getShell(), SWT.DROP_DOWN);
		edit.setMenu(dropMenu);
		edit = new MenuItem(dropMenu, SWT.NULL);
		edit.setText("Cut\tCtrl+X");
		edit.setAccelerator(SWT.CTRL + 'X');
		edit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.getCurrentStyledTextExtended().cut();
				}
			});
		MenuItem copy = new MenuItem(dropMenu, SWT.NULL);
		copy.setText("Copy\tCtrl+C");
		copy.setAccelerator(SWT.CTRL + 'C');
		copy.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.getCurrentStyledTextExtended().copy();
				}
			});
		MenuItem paste = new MenuItem(dropMenu, SWT.NULL);
		paste.setText("Paste\tCtrl+V");
		paste.setAccelerator(SWT.CTRL + 'V');
		paste.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.getCurrentStyledTextExtended().paste();
				}
			});
		MenuItem selectAll = new MenuItem(dropMenu, SWT.SEPARATOR);
		selectAll = new MenuItem(dropMenu, SWT.NULL);
		selectAll.setText("Select All\tCtrl+A");
		selectAll.setAccelerator(SWT.CTRL + 'A');
		selectAll.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.getCurrentStyledTextExtended().selectAll();
				}
			});
		MenuItem undo = new MenuItem(dropMenu, SWT.SEPARATOR);
		undo = new MenuItem(dropMenu, SWT.NULL);
		undo.setText("Undo\tCtrl+Z");
		undo.setAccelerator(SWT.CTRL + 'Z');
		undo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.ste.undo();
				}
			});
		MenuItem help = new MenuItem(menu, SWT.CASCADE);
		help.setText("Help");
		dropMenu = new Menu(this.ste.getShell(), SWT.DROP_DOWN);
		help.setMenu(dropMenu);
		help = new MenuItem(dropMenu, SWT.NULL);
		help.setText("About");
		help.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldSingle.this.ste.showAboutDialog();
				}
			});
	}
	@Override
	public List<StyledTextExtended> getStyledTextExtended() {
		LinkedList<StyledTextExtended> l = new LinkedList<StyledTextExtended>();
		l.add(text);
		return l;
	}
	@Override
	public StyledTextExtended getCurrentStyledTextExtended() {
		return this.text;
	}
	@Override
	public boolean saveText() {
		if(this.text.getFile() == null) {
			final FileDialog fileDialog = new FileDialog(this.ste.getShell(),
				SWT.SAVE);
			if(this.ste.getLastDirectory() != null) {
				fileDialog.setFilterPath(this.ste.getLastDirectory());
			}
			final String selectedFile = fileDialog.open();
			if(selectedFile == null) {
				return false;
			}
			this.text.setFile(new File(selectedFile));
			this.ste.setLastDirectory(this.text.getFile().getParent());
		}
		try {
			FileWriter writer = new FileWriter(this.text.getFile());
			writer.write(this.text.getText());
			writer.close();
			this.text.setUnsaved(false);
			this.setTitle();
			return true;
		}
		catch(final IOException e) {
		}
		return false;
	}
	private StyledTextExtended createStyledTextExtended(StyledTextExtended s) {
		final StyledTextExtended ste = new StyledTextExtended(this.ste.getShell(),
			SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		ste.setLayoutData(new GridData(GridData.FILL_BOTH));
		final Font font = new Font(this.ste.getShell().getDisplay(), "Monospace",
			10, SWT.NORMAL);
		ste.setFont(font);
		if(s != null) {
			ste.moveAbove(s);
			s.dispose();
			this.ste.getShell().layout();
		}
		return ste;
	}
	private void addListener(final StyledTextExtended ste) {
		ste.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent event) {
					ste.setUnsaved(true);
					TextFieldSingle.this.setTitle();
				}
			});
		ste.addExtendedModifyListener(new ExtendedModifyListener() {
				@Override
				public void modifyText(ExtendedModifyEvent event) {
					ste.getChanges().push(new TextChange(event.start, event.length,
							event.replacedText));
					if(ste.getChanges().size() > StyledTextExtended.UNDO_LIMIT) {
						ste.getChanges().remove(0);
					}
				}
			});
	}
	boolean loadText(String selectedFile) {
		this.ste.setLastDirectory(this.text.getFile() != null ?
			this.text.getFile().getParent() : null);
		BufferedReader reader = null;
		try {
			File file = new File(selectedFile);
			reader = new BufferedReader(new FileReader(file));
			final StringBuffer buffer = new StringBuffer();
			String line = null;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\r\n");
			}
			this.removeAllListener(this.text);
			StyledTextExtended text = createStyledTextExtended(this.text);
			text.setFile(file);
			text.setText(buffer.toString());
			this.addListener(text);
			this.text = text;
			this.setTitle();
			this.les.fireEvent(text);
			return true;
		}
		catch(final IOException e) {
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	private void setTitle() {
		String titel = this.text.getTitel() + " - " + SimpleTextEditor.TITEL;
		this.ste.getShell().setText(titel);
	}
	private LoadEventSource les = new LoadEventSource();
	@Override
	public synchronized void addEventListener(LoadEventListener listener) {
		les.addEventListener(listener);
	}
	@Override
	public synchronized void removeEventListener(LoadEventListener listener) {
		les.removeEventListener(listener);
	}
}