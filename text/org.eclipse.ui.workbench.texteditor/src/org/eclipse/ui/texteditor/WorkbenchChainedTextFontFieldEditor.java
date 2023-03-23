/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


package org.eclipse.ui.texteditor;


import org.eclipse.swt.widgets.Composite;

import org.eclipse.core.runtime.preferences.InstanceScope;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;

import org.eclipse.jface.text.PropagatingFontFieldEditor;

import org.eclipse.ui.preferences.ScopedPreferenceStore;


/**
 * This font field editor implements chaining between the workbench's preference
 * store and a given target preference store. Any time the workbench's preference
 * for the text font changes, the change is propagated to the target store.
 * Propagation means that the actual text font stored in the workbench store is set as
 * default text font in the target store. If the target store does not contain a value
 * rather than the default text font, the new default text font is immediately effective.
 *
 * @see org.eclipse.jface.preference.FontFieldEditor
 * @deprecated since 3.0 not longer in use, no longer supported, use a
 *             <code>ChainedPreferenceStore</code> to access preferences from the
 *             <code>org.eclipse.ui.editors</code> plug-in.
 * @since 2.0
 */
@Deprecated
public class WorkbenchChainedTextFontFieldEditor extends PropagatingFontFieldEditor {

	/**
	 * Creates a new font field editor with the given parameters.
	 *
	 * @param name the editor's name
	 * @param labelText the text shown as editor description
	 * @param parent the editor's parent widget
	 */
	public WorkbenchChainedTextFontFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent, EditorMessages.WorkbenchChainedTextFontFieldEditor_defaultWorkbenchTextFont);
	}

	/**
	 * Starts the propagation of the text font preference set in the workbench
	 * to given target preference store using the given preference key.
	 *
	 * @param target the target preference store
	 * @param targetKey the key to be used in the target preference store
	 */
	public static void startPropagate(IPreferenceStore target, String targetKey) {
		IPreferenceStore store= new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.workbench"); //$NON-NLS-1$
		PropagatingFontFieldEditor.startPropagate(store, JFaceResources.TEXT_FONT, target, targetKey);
	}
}

