package org.eclipse.ui.genericeditor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;

public interface IPreferenceStoreProvider {

	IPreferenceStore getPreferenceStore(ISourceViewer viewer, ITextEditor editor);
}
