package simpletexteditor.m_base.de.friesen.example.simpletexteditor;

/*** added by dBase
 */
public class TextChange {
	private int start;
	private int length;
	String replacedText;
	public TextChange(int start, int length, String replacedText) {
		this.start = start;
		this.length = length;
		this.replacedText = replacedText;
	}
	public int getStart() {
		return start;
	}
	public int getLength() {
		return length;
	}
	public String getReplacedText() {
		return replacedText;
	}
}