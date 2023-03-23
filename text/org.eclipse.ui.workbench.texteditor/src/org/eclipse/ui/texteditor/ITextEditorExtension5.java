/*******************************************************************************
 * Copyright (c) 2009, 2015 Avaloq Evolution AG and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Tom Eicher (Avaloq Evolution AG) - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.texteditor;


/**
 * Extension interface for {@link org.eclipse.ui.texteditor.ITextEditor}. Adds the following
 * functions:
 * <ul>
 * <li>block selection mode</li>
 * </ul>
 * <p>
 * This interface may be implemented by clients.
 * </p>
 *
 * @since 3.5
 */
public interface ITextEditorExtension5 {
	/**
	 * Returns <code>true</code> if the receiver is in block (aka column) selection mode,
	 * <code>false</code> otherwise.
	 *
	 * @return the receiver's block selection state
	 */
	boolean isBlockSelectionModeEnabled();

	/**
	 * Sets the block selection mode state of the receiver to <code>state</code>. Nothing happens if
	 * the receiver already is in the requested state.
	 * <p>
	 * Note: enabling block selection mode disables word wrap {@link ITextEditorExtension6}),
	 * enabling word wrap will disable block selection mode.
	 *
	 * @param state the new block selection state
	 * @see ITextEditorExtension6#setWordWrap(boolean)
	 */
	void setBlockSelectionMode(boolean state);
}
