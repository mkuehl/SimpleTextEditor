package simpletexteditor.m_base.de.friesen.example.simpletexteditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
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
/*** added by dMultiple
 */
public class TextFieldMultiple extends TextField {
	private SimpleTextEditor ste;
	private CTabFolder tabs;
	private static final String SAVE_CHANGES = "save changes for? \n\n";
	public TextFieldMultiple(final SimpleTextEditor ste) {
		this.ste = ste;
		this.tabs = new CTabFolder(ste.getShell(), SWT.CLOSE);
		this.tabs.setUnselectedCloseVisible(false);
		this.tabs.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.tabs.setBorderVisible(true);
		this.tabs.addCTabFolder2Listener(new CTabFolder2Adapter() {
				@Override
				public void close(CTabFolderEvent event) {
					CTabItem tab = ( CTabItem ) event.item;
					StyledTextExtended text = ( StyledTextExtended ) tab.getControl();
					String message = SAVE_CHANGES + text.getTitel(false);
					if(! ste.saveChangesDialog(message)) {
						event.doit = false;
					}
					TextFieldMultiple.this.removeAllListener(text);
				}
			});
		ste.getShell().setText(SimpleTextEditor.TITEL);
		ste.getShell().addShellListener(new ShellAdapter() {
				@Override
				public void shellClosed(ShellEvent event) {
					for(CTabItem tab : TextFieldMultiple.this.tabs.getItems()) {
						StyledTextExtended text = ( StyledTextExtended ) tab.getControl();
						String message = SAVE_CHANGES + text.getTitel(false);
						if(text.isUnsaved() && ! ste.saveChangesDialog(message)) {
							event.doit = false;
						}
					}
				}
			});
		this.addListener(this.newFile());
		this.tabs.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					StyledTextExtended ste = ( StyledTextExtended )(( CTabItem )
						e.item).getControl();
					les.fireEvent(ste);
				}
			});
	}
	private void addListener(final StyledTextExtended ste) {
		ste.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					ste.setUnsaved(true);
					String titel = getCurrentStyledTextExtended().getTitel();
					TextFieldMultiple.this.getTab().setText(titel);
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
	@Override
	public void createMenuBar(Menu menu) {
		MenuItem open = new MenuItem(menu, SWT.CASCADE);
		open.setText("File");
		Menu dropMenu = new Menu(this.ste.getShell(), SWT.DROP_DOWN);
		open.setMenu(dropMenu);
		MenuItem newFile = new MenuItem(dropMenu, SWT.NULL);
		newFile.setText("New\tCtrl+N");
		open.setAccelerator(SWT.CTRL + 'T');
		newFile.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					StyledTextExtended ste = TextFieldMultiple.this.newFile();
					TextFieldMultiple.this.addListener(ste);
					les.fireEvent(getCurrentStyledTextExtended());
				}
			});
		new MenuItem(dropMenu, SWT.SEPARATOR);
		open = new MenuItem(dropMenu, SWT.NULL);
		open.setText("Open...\tCtrl+O");
		open.setAccelerator(SWT.CTRL + 'O');
		open.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					final FileDialog dialog = new
					FileDialog(TextFieldMultiple.this.ste.getShell(), SWT.OPEN);
					if(TextFieldMultiple.this.ste.getLastDirectory() != null) {
						dialog.setFilterPath(TextFieldMultiple.this.ste.getLastDirectory());
					}
					final String selectedFile = dialog.open();
					if(selectedFile == null) {
						return;
					}
					TextFieldMultiple.this.loadText(selectedFile);
				}
			});
		MenuItem save = new MenuItem(dropMenu, SWT.NULL);
		save.setText("Save\tCtrl+S");
		save.setAccelerator(SWT.CTRL + 'S');
		save.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldMultiple.this.saveText();
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
					boolean isDispose = true;
					for(CTabItem tab : TextFieldMultiple.this.tabs.getItems()) {
						StyledTextExtended ste = ( StyledTextExtended ) tab.getControl();
						if(! ste.isUnsaved()) {
							continue;
						}
						String message = SAVE_CHANGES + tab.getText();
						if(! TextFieldMultiple.this.ste.saveChangesDialog(message)) {
							isDispose = false;
						}
					}
					if(isDispose) {
						TextFieldMultiple.this.ste.getShell().dispose();
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
					TextFieldMultiple.this.getCurrentStyledTextExtended().cut();
				}
			});
		MenuItem copy = new MenuItem(dropMenu, SWT.NULL);
		copy.setText("Copy\tCtrl+C");
		copy.setAccelerator(SWT.CTRL + 'C');
		copy.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldMultiple.this.getCurrentStyledTextExtended().copy();
				}
			});
		MenuItem paste = new MenuItem(dropMenu, SWT.NULL);
		paste.setText("Paste\tCtrl+V");
		paste.setAccelerator(SWT.CTRL + 'V');
		paste.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldMultiple.this.getCurrentStyledTextExtended().paste();
				}
			});
		MenuItem selectAll = new MenuItem(dropMenu, SWT.SEPARATOR);
		selectAll = new MenuItem(dropMenu, SWT.NULL);
		selectAll.setText("Select All\tCtrl+A");
		selectAll.setAccelerator(SWT.CTRL + 'A');
		selectAll.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldMultiple.this.getCurrentStyledTextExtended().selectAll();
				}
			});
		MenuItem undo = new MenuItem(dropMenu, SWT.SEPARATOR);
		undo = new MenuItem(dropMenu, SWT.NULL);
		undo.setText("Undo\tCtrl+Z");
		undo.setAccelerator(SWT.CTRL + 'Z');
		undo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					TextFieldMultiple.this.ste.undo();
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
					TextFieldMultiple.this.ste.showAboutDialog();
				}
			});
	}
	@Override
	public StyledTextExtended getCurrentStyledTextExtended() {
		if(this.tabs.getSelection() == null) {
			return null;
		}
		return ( StyledTextExtended ) this.tabs.getSelection().getControl();
	}
	@Override
	public List<StyledTextExtended> getStyledTextExtended() {
		List<StyledTextExtended> list = new LinkedList<StyledTextExtended>();
		for(CTabItem tab : this.tabs.getItems()) {
			list.add(( StyledTextExtended ) tab.getControl());
		}
		return list;
	}
	@Override
	public boolean saveText() {
		StyledTextExtended ste = this.getCurrentStyledTextExtended();
		if(ste.getFile() == null) {
			final FileDialog fileDialog = new FileDialog(this.ste.getShell(),
				SWT.SAVE);
			if(this.ste.getLastDirectory() != null) {
				fileDialog.setFilterPath(this.ste.getLastDirectory());
			}
			final String selectedFile = fileDialog.open();
			if(selectedFile == null) {
				return false;
			}
			ste.setFile(new File(selectedFile));
			this.ste.setLastDirectory(ste.getFile().getParent());
		}
		try {
			FileWriter writer = new FileWriter(ste.getFile());
			writer.write(ste.getText());
			writer.close();
			ste.setUnsaved(false);
			this.getTab().setText(ste.getTitel());
			return true;
		}
		catch(final IOException e) {
		}
		return false;
	}
	private StyledTextExtended createStyledTextExtended() {
		final StyledTextExtended ste = new StyledTextExtended(this.tabs, SWT.MULTI |
			SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		ste.setLayoutData(new GridData(GridData.FILL_BOTH));
		final Font font = new Font(this.ste.getShell().getDisplay(), "Monospace",
			10, SWT.NORMAL);
		ste.setFont(font);
		return ste;
	}
	private CTabItem getTab() {
		if(this.tabs.getSelection() == null) {
			return null;
		}
		return this.tabs.getSelection();
	}
	boolean loadText(String selectedFile) {
		BufferedReader reader = null;
		try {
			File file = new File(selectedFile);
			reader = new BufferedReader(new FileReader(file));
			final StringBuilder buffer = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\r\n");
			}
			this.ste.setLastDirectory(file.getParent());
			StyledTextExtended ste = this.newFile();
			ste.setFile(file);
			ste.setText(buffer.toString());
			this.addListener(ste);
			String titel = this.getCurrentStyledTextExtended().getTitel();
			this.getTab().setText(titel);
			this.les.fireEvent(this.getCurrentStyledTextExtended());
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
	private StyledTextExtended newFile() {
		CTabItem item = new CTabItem(this.tabs, SWT.NONE);
		StyledTextExtended ste = this.createStyledTextExtended();
		item.setControl(ste);
		item.setText(ste.getTitel());
		this.tabs.setSelection(item);
		return ste;
	}
	private LoadEventSource les = new LoadEventSource();
	@Override
	public synchronized void addEventListener(LoadEventListener listener) {
		this.les.addEventListener(listener);
	}
	@Override
	public synchronized void removeEventListener(LoadEventListener listener) {
		this.les.removeEventListener(listener);
	}
}