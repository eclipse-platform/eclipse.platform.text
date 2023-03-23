/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

import java.util.ResourceBundle;

import org.eclipse.jface.text.source.IVerticalRulerInfo;


/**
 * @deprecated use {@link org.eclipse.ui.texteditor.MarkerRulerAction} instead
 * @since 2.0
 */
@Deprecated
public class MarkerRulerInfoAction extends MarkerRulerAction {

	/**
	 * Creates a new action for the given ruler and editor. The action configures
	 * its visual representation from the given resource bundle.
	 *
	 * @param bundle the resource bundle
	 * @param prefix a prefix to be prepended to the various resource keys
	 * @param ruler the ruler
	 * @param editor the editor
	 * @param markerType the type of marker
	 * @param askForLabel <code>true</code> if the user should be asked for a label when a new marker is created
	 * @deprecated use super class constructor instead
	 */
	@Deprecated
	public MarkerRulerInfoAction(ResourceBundle bundle, String prefix, IVerticalRulerInfo ruler, ITextEditor editor, String markerType, boolean askForLabel) {
		super(bundle, prefix, editor, ruler, markerType, askForLabel);
	}
}
