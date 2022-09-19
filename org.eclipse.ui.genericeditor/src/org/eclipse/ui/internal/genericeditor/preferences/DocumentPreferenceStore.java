package org.eclipse.ui.internal.genericeditor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.texteditor.ITextEditor;

public class DocumentPreferenceStore implements IPreferenceStore {

	private final List<IPropertyChangeListener> listeners;
	private IPreferenceStore delegate;

	public DocumentPreferenceStore() {
		listeners = new ArrayList<>();
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		this.listeners.add(listener);
		if (delegate != null) {
			delegate.addPropertyChangeListener(listener);
		}
	}

	@Override
	public boolean contains(String name) {
		if (delegate != null) {
			return delegate.contains(name);
		}
		return false;
	}

	@Override
	public void firePropertyChangeEvent(String arg0, Object arg1, Object arg2) {
		if (delegate != null) {
			delegate.firePropertyChangeEvent(arg0, arg1, arg2);
		}
	}

	@Override
	public boolean getBoolean(String name) {
		if (delegate != null) {
			return delegate.getBoolean(name);
		}
		return false;
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		if (delegate != null) {
			return delegate.getDefaultBoolean(name);
		}
		return false;
	}

	@Override
	public double getDefaultDouble(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDefaultFloat(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefaultInt(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getDefaultLong(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDefaultString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String name) {
		if (delegate != null) {
			return delegate.getInt(name);
		}
		return 0;
	}

	@Override
	public long getLong(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefault(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsSaving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		this.listeners.remove(listener);
		if (delegate != null) {
			delegate.removePropertyChangeListener(listener);
		}
	}

	@Override
	public void setDefault(String arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToDefault(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	public void install(ITextEditor editor) {
		delegate = (IPreferenceStore) Adapters.adapt(editor, IEclipsePreferences.class);
		if (delegate != null) {
			for (IPropertyChangeListener listener : listeners) {
				delegate.addPropertyChangeListener(listener);
			}
		}
	}

}
