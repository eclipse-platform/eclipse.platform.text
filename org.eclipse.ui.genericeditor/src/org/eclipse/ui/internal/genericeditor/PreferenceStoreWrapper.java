package org.eclipse.ui.internal.genericeditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.genericeditor.IPreferenceStoreProvider;
import org.eclipse.ui.internal.genericeditor.preferences.GenericEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

public class PreferenceStoreWrapper implements IPreferenceStore {

	private IPreferenceStore delegate;
	private boolean computed;

	private ExtensionBasedTextEditor editor;
	private Set<IPropertyChangeListener> listeners = new HashSet<>();

	public PreferenceStoreWrapper(ExtensionBasedTextEditor editor) {
		this.editor = editor;
		this.delegate = createPreferenceStore(null, null);
	}

	public static IPreferenceStore createPreferenceStore(Set<IContentType> contentTypes,
			ExtensionBasedTextEditor editor) {
		List<IPreferenceStore> stores = new ArrayList<>();
		if (contentTypes != null && editor != null) {
			PreferenceStoreProviderRegistry registry = GenericEditorPlugin.getDefault().getPreferenceStoreRegistry();
			List<IPreferenceStoreProvider> providers = registry.getPreferenceStoreProviders(editor.getViewer(), editor,
					contentTypes);
			for (IPreferenceStoreProvider provider : providers) {
				IPreferenceStore editorStore = provider.getPreferenceStore(editor.getViewer(), editor);
				if (editorStore != null) {
					stores.add(editorStore);
				}
			}
		}
		stores.add(GenericEditorPreferenceConstants.getPreferenceStore());
		stores.add(EditorsUI.getPreferenceStore());
		return new ChainedPreferenceStore(stores.toArray(new IPreferenceStore[stores.size()]));
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener arg0) {
		listeners.add(arg0);
		getDelegate().addPropertyChangeListener(arg0);
	}

	@Override
	public boolean contains(String arg0) {
		return getDelegate().contains(arg0);
	}

	@Override
	public void firePropertyChangeEvent(String arg0, Object arg1, Object arg2) {
		getDelegate().firePropertyChangeEvent(arg0, arg1, arg2);
	}

	@Override
	public boolean getBoolean(String arg0) {
		return getDelegate().getBoolean(arg0);
	}

	@Override
	public boolean getDefaultBoolean(String arg0) {
		return getDelegate().getDefaultBoolean(arg0);
	}

	@Override
	public double getDefaultDouble(String arg0) {
		return getDelegate().getDefaultDouble(arg0);
	}

	@Override
	public float getDefaultFloat(String arg0) {
		return getDelegate().getDefaultFloat(arg0);
	}

	@Override
	public int getDefaultInt(String arg0) {
		return getDelegate().getDefaultInt(arg0);
	}

	@Override
	public long getDefaultLong(String arg0) {
		return getDelegate().getDefaultLong(arg0);
	}

	@Override
	public String getDefaultString(String arg0) {
		return getDelegate().getDefaultString(arg0);
	}

	@Override
	public double getDouble(String arg0) {
		return getDelegate().getDouble(arg0);
	}

	@Override
	public float getFloat(String arg0) {
		return getDelegate().getFloat(arg0);
	}

	@Override
	public int getInt(String arg0) {
		return getDelegate().getInt(arg0);
	}

	@Override
	public long getLong(String arg0) {
		return getDelegate().getLong(arg0);
	}

	@Override
	public String getString(String arg0) {
		return getDelegate().getString(arg0);
	}

	@Override
	public boolean isDefault(String arg0) {
		return getDelegate().isDefault(arg0);
	}

	@Override
	public boolean needsSaving() {
		return getDelegate().needsSaving();
	}

	@Override
	public void putValue(String arg0, String arg1) {
		getDelegate().putValue(arg0, arg1);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener arg0) {
		listeners.remove(arg0);
		getDelegate().removePropertyChangeListener(arg0);
	}

	@Override
	public void setDefault(String arg0, boolean arg1) {
		getDelegate().setDefault(arg0, arg1);
	}

	@Override
	public void setDefault(String arg0, double arg1) {
		getDelegate().setDefault(arg0, arg1);
	}

	@Override
	public void setDefault(String arg0, float arg1) {
		getDelegate().setDefault(arg0, arg1);
	}

	@Override
	public void setDefault(String arg0, int arg1) {
		getDelegate().setDefault(arg0, arg1);
	}

	@Override
	public void setDefault(String arg0, long arg1) {
		getDelegate().setDefault(arg0, arg1);
	}

	@Override
	public void setDefault(String arg0, String arg1) {
		getDelegate().setDefault(arg0, arg1);
	}

	@Override
	public void setToDefault(String arg0) {
		getDelegate().setToDefault(arg0);
	}

	@Override
	public void setValue(String arg0, boolean arg1) {
		getDelegate().setValue(arg0, arg1);
	}

	@Override
	public void setValue(String arg0, double arg1) {
		getDelegate().setValue(arg0, arg1);
	}

	@Override
	public void setValue(String arg0, float arg1) {
		getDelegate().setValue(arg0, arg1);
	}

	@Override
	public void setValue(String arg0, int arg1) {
		getDelegate().setValue(arg0, arg1);
	}

	@Override
	public void setValue(String arg0, long arg1) {
		getDelegate().setValue(arg0, arg1);
	}

	@Override
	public void setValue(String arg0, String arg1) {
		getDelegate().setValue(arg0, arg1);
	}

	private IPreferenceStore getDelegate() {
		if (!computed) {
			delegate = getPreferenceStore();
		}
		return delegate;
	}

	private synchronized IPreferenceStore getPreferenceStore() {
		if (computed) {
			return delegate;
		}
		IPreferenceStore store = createCustomPreferenceStore();
		if (store != null) {
			for (IPropertyChangeListener listener : listeners) {
				store.addPropertyChangeListener(listener);
			}
			for (IPropertyChangeListener listener : listeners) {
				delegate.removePropertyChangeListener(listener);
			}
			delegate = store;
			computed = true;
		}
		return delegate;
	}

	IPreferenceStore createCustomPreferenceStore() {
		Set<IContentType> contentTypes = editor.getContentTypes();
		if (contentTypes == null) {
			return null;
		}
		return createPreferenceStore(contentTypes, editor);
	}
}
