package org.eclipse.editorconfig.internal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.genericeditor.IPreferenceStoreProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class EditorConfigPreferenceStoreProvider implements IPreferenceStoreProvider {

	@Override
	public IPreferenceStore getPreferenceStore(ISourceViewer viewer, ITextEditor editor) {
		return new EditorConfigPreferenceStore(editor);
	}

}
