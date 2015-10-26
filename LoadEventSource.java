package simpletexteditor.m_complete.de.friesen.example.simpletexteditor;

import java.util.ArrayList;
import java.util.List;
/*** added by dBase
 */
public class LoadEventSource {
	private List<LoadEventListener> listeners = new
	ArrayList<LoadEventListener>();
	public synchronized void addEventListener(LoadEventListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeEventListener(LoadEventListener listener) {
		listeners.remove(listener);
	}
	public synchronized void fireEvent(StyledTextExtended ste) {
		LoadEvent event = new LoadEvent(ste);
		for(LoadEventListener i : listeners) {
			i.load(event);
		}
	}
}