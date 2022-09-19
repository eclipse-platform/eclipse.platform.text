package org.eclipse.ui.internal.genericeditor.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.ITextEditor;

public class GenericEditorPreferenceStore extends ChainedPreferenceStore {

	private final DocumentPreferenceStore documentPreferenceStore;

	public GenericEditorPreferenceStore() {
		this(new IPreferenceStore[] { new DocumentPreferenceStore(),
				GenericEditorPreferenceConstants.getPreferenceStore(), EditorsUI.getPreferenceStore() });
	}

	private GenericEditorPreferenceStore(IPreferenceStore[] stores) {
		super(stores);
		this.documentPreferenceStore = (DocumentPreferenceStore) stores[0];
	}

	public void install(ITextEditor viewer) {
		documentPreferenceStore.install(viewer);
	}
}
