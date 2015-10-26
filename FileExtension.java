package simpletexteditor.m_complete.de.friesen.example.simpletexteditor;

import org.eclipse.swt.custom.StyleRange;
/*** added by dHighlightMode
 */
public class FileExtension {
	private String [] keywords;
	private StyleRange styleRange;
	public FileExtension(String [] keywords, StyleRange styleRange) {
		super();
		this.keywords = keywords;
		this.styleRange = styleRange;
	}
	public String [] getKeywords() {
		return keywords;
	}
	public void setKeywords(String [] keywords) {
		this.keywords = keywords;
	}
	public StyleRange getStyleRange() {
		return styleRange;
	}
	public void setStyleRange(StyleRange styleRange) {
		this.styleRange = styleRange;
	}
}