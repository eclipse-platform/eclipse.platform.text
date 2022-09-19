/**
 *  Copyright (c) 2022 Angelo Zerr and others.
 *  
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.text.editorconfig.internal;

import java.io.IOException;

import org.ec4j.core.ResourceProperties;
import org.ec4j.core.model.PropertyType;
import org.ec4j.core.model.PropertyType.EndOfLineValue;
import org.ec4j.core.model.PropertyType.IndentStyleValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferenceNodeVisitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.text.editorconfig.EditorConfigPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.IEncodingSupport;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ITextEditor;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class EditorConfigPreferenceStore implements IPreferenceStore, IEclipsePreferences {

	public static final String EDITOR_SPACES_FOR_TABS = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SPACES_FOR_TABS;
	public static final String EDITOR_TAB_WIDTH = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH;
	public static final String EDITOR_TRIM_TRAILING_WHITESPACE = "trim_trailing_whitespace";
	public static final String EDITOR_INSERT_FINAL_NEWLINE = "insert_final_newline";

	private final ITextEditor textEditor;
	private IPreferenceStore editorPreferenceStore;

	private Boolean spacesForTabs;
	private Integer tabWidth;
	private boolean trimTrailingWhitespace;
	private boolean insertFinalNewline;

	public EditorConfigPreferenceStore(ITextEditor textEditor) {
		this.textEditor = textEditor;
		EditorTracker.getInstance().track(textEditor, this);
	}

	public void applyConfig() {
		IFile file = getFile(textEditor);
		if (file != null) {
			IPreferenceStore preferenceStore = getEditorPreferenceStore();
			try {
				// Compute properties from .editorconfig files for the given file
				ResourceProperties result = IDEEditorConfigManager.getInstance().queryOptions(file);
				// Apply properties
				applyIndentStyleConfig(result, preferenceStore);
				applyIndentSizeConfig(result, preferenceStore);
				applyEndOfLineConfig(result);
				applyCharsetConfig(result);
				applyTrimTrailingWhitespaceConfig(result, preferenceStore);
				applyInsertFinalNewlineConfig(result, preferenceStore);
			} catch (IOException e) {
				EditorConfigPlugin.logError("Error while apply properties from .editorconfig", e);
			}
		}
	}

	private static IFile getFile(ITextEditor textEditor) {
		IEditorInput input = textEditor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile();
		}
		return null;
	}

	private IPreferenceStore getEditorPreferenceStore() {
		if (editorPreferenceStore == null) {
			editorPreferenceStore = textEditor.getAdapter(IPreferenceStore.class);
		}
		return editorPreferenceStore;
	}

	private void applyIndentStyleConfig(ResourceProperties result, IPreferenceStore preferenceStore) {
		Boolean oldSpacesForTabs = spacesForTabs;
		spacesForTabs = null;
		final IndentStyleValue indentStyle = result.getValue(PropertyType.indent_style.getName(), null, false);
		if (indentStyle != null) {
			spacesForTabs = indentStyle == IndentStyleValue.space;
			if (oldSpacesForTabs != spacesForTabs) {
				preferenceStore.firePropertyChangeEvent(EDITOR_SPACES_FOR_TABS, oldSpacesForTabs, spacesForTabs);
			}
		} else if (oldSpacesForTabs != null) {
			if (oldSpacesForTabs != spacesForTabs) {
				preferenceStore.firePropertyChangeEvent(EDITOR_SPACES_FOR_TABS, oldSpacesForTabs, false);
			}
		}
	}

	private void applyIndentSizeConfig(ResourceProperties result, IPreferenceStore preferenceStore) {
		Integer oldTabWidth = tabWidth;
		tabWidth = null;
		final Integer indentSize = result.getValue(PropertyType.indent_size.getName(), null, false);
		if (indentSize != null) {
			tabWidth = indentSize.intValue();
			if (oldTabWidth != tabWidth) {
				preferenceStore.firePropertyChangeEvent(EDITOR_TAB_WIDTH, oldTabWidth, tabWidth);
			}
		} else if (oldTabWidth != null) {
			if (oldTabWidth != tabWidth) {
				preferenceStore.firePropertyChangeEvent(EDITOR_TAB_WIDTH, oldTabWidth, tabWidth);
			}
		}
	}

	private void applyEndOfLineConfig(ResourceProperties result) {
		final EndOfLineValue eol = result.getValue(PropertyType.end_of_line.getName(), null, false);
		if (eol != null) {
			IEditorInput editorInput = textEditor.getEditorInput();
			IDocument document = textEditor.getDocumentProvider().getDocument(editorInput);
			if (document instanceof IDocumentExtension4) {
				((IDocumentExtension4) document).setInitialLineDelimiter(eol.getEndOfLineString());
			}
		}
	}

	private void applyCharsetConfig(ResourceProperties result) {
		final String charset = result.getValue(PropertyType.charset.getName(), null, false);
		if (charset != null) {
			IEncodingSupport encodingSupport = textEditor.getAdapter(IEncodingSupport.class);
			if (encodingSupport != null) {
				encodingSupport.setEncoding(charset.trim().toUpperCase());
			}
		}
	}

	private void applyTrimTrailingWhitespaceConfig(ResourceProperties result, IPreferenceStore preferenceStore) {
		final Boolean trimTrailigWs = result.getValue(PropertyType.trim_trailing_whitespace.getName(), null, false);
		if (trimTrailigWs != null) {
			boolean oldTrimTrailingWhitespace = trimTrailingWhitespace;
			trimTrailingWhitespace = trimTrailigWs.booleanValue();
			if (oldTrimTrailingWhitespace != trimTrailingWhitespace) {
				preferenceStore.firePropertyChangeEvent(EDITOR_TRIM_TRAILING_WHITESPACE, oldTrimTrailingWhitespace,
						trimTrailingWhitespace);
			}
		}
	}

	private void applyInsertFinalNewlineConfig(ResourceProperties result, IPreferenceStore preferenceStore) {
		final Boolean insertFinalNl = result.getValue(PropertyType.insert_final_newline.getName(), null, false);
		if (insertFinalNl != null) {
			boolean oldInsertFinalNewline = insertFinalNewline;
			insertFinalNewline = insertFinalNl.booleanValue();
			if (oldInsertFinalNewline != insertFinalNewline) {
				preferenceStore.firePropertyChangeEvent(EDITOR_INSERT_FINAL_NEWLINE, oldInsertFinalNewline,
						insertFinalNewline);
			}
		}
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {

	}

	@Override
	public boolean contains(String name) {
		if (EDITOR_SPACES_FOR_TABS.equals(name)) {
			return this.spacesForTabs != null;
		} else if (EDITOR_TAB_WIDTH.equals(name)) {
			return tabWidth != null;
		} else if (EDITOR_TRIM_TRAILING_WHITESPACE.equals(name)) {
			return true;
		} else if (EDITOR_INSERT_FINAL_NEWLINE.equals(name)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean getBoolean(String name) {
		if (EDITOR_SPACES_FOR_TABS.equals(name)) {
			return spacesForTabs;
		} else if (EDITOR_TRIM_TRAILING_WHITESPACE.equals(name)) {
			return trimTrailingWhitespace;
		} else if (EDITOR_INSERT_FINAL_NEWLINE.equals(name)) {
			return insertFinalNewline;
		}
		return false;
	}

	@Override
	public int getInt(String name) {
		if (EDITOR_TAB_WIDTH.equals(name)) {
			return tabWidth;
		}
		return 0;
	}

	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {

	}

	@Override
	public boolean getDefaultBoolean(String name) {
		return false;
	}

	@Override
	public double getDefaultDouble(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDefaultFloat(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefaultInt(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getDefaultLong(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDefaultString(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefault(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsSaving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String name, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, float value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, String defaultObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(String name, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToDefault(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String name, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String name, float value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String name, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String name, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(String name, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String get(String key, String def) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void putInt(String key, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInt(String key, int def) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void putLong(String key, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getLong(String key, long def) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void putBoolean(String key, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getBoolean(String key, boolean def) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putFloat(String key, float value) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getFloat(String key, float def) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void putDouble(String key, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getDouble(String key, double def) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void putByteArray(String key, byte[] value) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getByteArray(String key, byte[] def) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] keys() throws BackingStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] childrenNames() throws BackingStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Preferences parent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean nodeExists(String pathName) throws BackingStoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String absolutePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sync() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void accept(IPreferenceNodeVisitor arg0) throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNodeChangeListener(INodeChangeListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPreferenceChangeListener(IPreferenceChangeListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Preferences node(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeNode() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeNodeChangeListener(INodeChangeListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePreferenceChangeListener(IPreferenceChangeListener arg0) {
		// TODO Auto-generated method stub

	}

}
