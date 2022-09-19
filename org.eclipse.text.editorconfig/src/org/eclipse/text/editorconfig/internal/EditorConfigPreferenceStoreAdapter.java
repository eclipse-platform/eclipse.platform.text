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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Adapter used to create and add an instance of
 * {@link EditorConfigPreferenceStore} in the preference store of the
 * GenericEditor.
 * 
 * @author Angelo ZERR
 *
 */
public class EditorConfigPreferenceStoreAdapter implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		// The adapter is registered with
		// <adapter
		// type="org.eclipse.core.runtime.preferences.IEclipsePreferences">
		// </adapter>
		// although it returns an instance of IPreferenceStore, but to avoid collision
		// with
		// textEditor.getAdapter(IPreferenceStore.class) which returns the preference
		// store of the editor, IEclipsePreferences i sused here.
		// To improve that Eclipse should provide a new extension point like
		// documentPreferenceStore to add any IPreferenceStore in the genereic editor.
		if (adapterType == IEclipsePreferences.class) {
			ITextEditor textEditor = (ITextEditor) adaptableObject;
			return new EditorConfigPreferenceStore(textEditor);
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IEclipsePreferences.class };
	}

}
