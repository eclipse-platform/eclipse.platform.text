package org.eclipse.ui.internal.texteditor.preferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerLifecycle;
import org.eclipse.jface.text.preferences.IDisposablePreferenceStore;
import org.eclipse.jface.text.preferences.IPreferenceStoreProvider;

import org.eclipse.ui.internal.texteditor.TextEditorPlugin;

import org.eclipse.ui.texteditor.ITextEditor;

public class PreferenceStoreWrapper implements IPreferenceStore, ITextViewerLifecycle {

	private IPreferenceStore delegate;
	private boolean computed;

	private final ITextEditor editor;
	private final IPreferenceStore initialPreferenceStore;

	private Set<IPropertyChangeListener> listeners = new HashSet<>();
	private ITextViewer viewer;
	private List<IPreferenceStore> customPreferenceStore;

	public PreferenceStoreWrapper(ITextEditor editor, IPreferenceStore initialPreferenceStore) {
		this.editor = editor;
		this.initialPreferenceStore = initialPreferenceStore;
		this.delegate = initialPreferenceStore;
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener arg0) {
		listeners.add(arg0);
		if (delegate != null) {
			getDelegate().addPropertyChangeListener(arg0);
		}
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

		this.customPreferenceStore = createCustomPreferenceStore();
		if (customPreferenceStore != null) {
			if (customPreferenceStore.size() > 1) {
				IPreferenceStore store = new ChainedPreferenceStore(
						customPreferenceStore.toArray(new IPreferenceStore[customPreferenceStore.size()]));
				for (IPropertyChangeListener listener : listeners) {
					store.addPropertyChangeListener(listener);
				}
				for (IPropertyChangeListener listener : listeners) {
					delegate.removePropertyChangeListener(listener);
				}
				delegate = store;
			}
			computed = true;
		}
		return delegate;
	}

	List<IPreferenceStore> createCustomPreferenceStore() {
		if (editor.getEditorInput() == null) {
			return null;
		}
		if (editor.getAdapter(ITextViewer.class) == null) {
			// return null;
		}

		IPreferenceStoreProvider[] providers = TextEditorPlugin.getDefault().getPreferenceStoreProviderRegistry()
				.getProviders(editor.getAdapter(ITextViewer.class), editor);
		if (providers != null) {
			List<IPreferenceStore> all = Stream.of(providers) //
					.map(provider -> provider.providePreferenceStores()) //
					.flatMap(x -> Arrays.stream(x)) //
					.collect(Collectors.toList());
			if (initialPreferenceStore != null) {
				all.add(initialPreferenceStore);
			}
			return all;
		}
		return Arrays.asList(initialPreferenceStore);
	}

	private void installIfNeeded() {
		if (viewer != null && customPreferenceStore != null) {
			for (IPreferenceStore store : customPreferenceStore) {
				if (store instanceof ITextViewerLifecycle) {
					((ITextViewerLifecycle) store).install(viewer);
				}
			}
		}
	}

	@Override
	public void install(ITextViewer viewer) {
		this.viewer = viewer;
		installIfNeeded();
	}

	@Override
	public void uninstall() {
		if (viewer != null && customPreferenceStore != null) {
			for (IPreferenceStore store : customPreferenceStore) {
				if (store instanceof ITextViewerLifecycle) {
					((ITextViewerLifecycle) store).uninstall();
				}
			}
			this.customPreferenceStore = null;
		}
		this.viewer = null;
	}

	public void dispose() {
		if (viewer != null && customPreferenceStore != null) {
			for (IPreferenceStore store : customPreferenceStore) {
				if (store instanceof IDisposablePreferenceStore) {
					((IDisposablePreferenceStore) store).dispose();
				}
			}
			this.customPreferenceStore = null;
		}
	}
}