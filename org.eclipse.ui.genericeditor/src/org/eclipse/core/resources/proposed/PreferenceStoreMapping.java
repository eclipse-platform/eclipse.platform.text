package org.eclipse.core.resources.proposed;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class PreferenceStoreMapping implements IPreferenceStore {

	private final IPreferenceStore delegate;

	private final Map<String, String> mappings;
	private final Map<String, String> mappings2;

	public PreferenceStoreMapping(IPreferenceStore delegate) {
		this.delegate = delegate;
		this.mappings = new HashMap<>();
		this.mappings2 = new HashMap<>();
	}

	public PreferenceStoreMapping addMapping(String name1, String name2) {
		mappings.put(name1, name2);
		mappings2.put(name2, name1);
		return this;
	}

	private String getProperName(String name) {
		if (mappings.containsKey(name)) {
			return mappings.get(name);
		}
		return name;
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		delegate.addPropertyChangeListener(listener);
	}

	@Override
	public boolean contains(String name) {
		if (mappings.containsKey(name) || mappings2.containsKey(name)) {
			return true;
		}
		return delegate.contains(getProperName(name));
	}

	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		delegate.firePropertyChangeEvent(name, oldValue, newValue);
	}

	@Override
	public boolean getBoolean(String name) {
		if (mappings2.containsKey(name)) {
			name = mappings2.get(name);
		}
		return delegate.getBoolean(name);
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		return delegate.getDefaultBoolean(name);
	}

	@Override
	public double getDefaultDouble(String name) {
		return delegate.getDefaultDouble(getProperName(name));
	}

	@Override
	public float getDefaultFloat(String name) {
		return delegate.getDefaultFloat(getProperName(name));
	}

	@Override
	public int getDefaultInt(String name) {
		return delegate.getDefaultInt(getProperName(name));
	}

	@Override
	public long getDefaultLong(String name) {
		return delegate.getDefaultLong(getProperName(name));
	}

	@Override
	public String getDefaultString(String name) {
		return delegate.getDefaultString(getProperName(name));
	}

	@Override
	public double getDouble(String name) {
		return delegate.getDouble(getProperName(name));
	}

	@Override
	public float getFloat(String name) {
		return delegate.getFloat(getProperName(name));
	}

	@Override
	public int getInt(String name) {
		return delegate.getInt(getProperName(name));
	}

	@Override
	public long getLong(String name) {
		return delegate.getLong(getProperName(name));
	}

	@Override
	public String getString(String name) {
		return delegate.getString(getProperName(name));
	}

	@Override
	public boolean isDefault(String name) {
		return delegate.isDefault(getProperName(name));
	}

	@Override
	public boolean needsSaving() {
		return delegate.needsSaving();
	}

	@Override
	public void putValue(String name, String value) {
		delegate.putValue(name, value);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		delegate.removePropertyChangeListener(listener);
	}

	@Override
	public void setDefault(String name, double value) {
		delegate.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, float value) {
		delegate.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, int value) {
		delegate.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, long value) {
		delegate.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, String defaultObject) {
		delegate.setDefault(name, defaultObject);
	}

	@Override
	public void setDefault(String name, boolean value) {
		delegate.setDefault(name, value);
	}

	@Override
	public void setToDefault(String name) {
		delegate.setToDefault(getProperName(name));
	}

	@Override
	public void setValue(String name, double value) {
		delegate.setValue(name, value);
	}

	@Override
	public void setValue(String name, float value) {
		delegate.setValue(name, value);
	}

	@Override
	public void setValue(String name, int value) {
		delegate.setValue(name, value);
	}

	@Override
	public void setValue(String name, long value) {
		delegate.setValue(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		delegate.setValue(getProperName(name), value);
	}

	@Override
	public void setValue(String name, boolean value) {
		boolean oldValue = delegate.getBoolean(name);
		delegate.setValue(name, value);
		String mapedName = getProperName(name);
		if (oldValue != value && !mapedName.equals(name)) {
			firePropertyChangeEvent(mapedName, oldValue, value);
		}
	}

}
