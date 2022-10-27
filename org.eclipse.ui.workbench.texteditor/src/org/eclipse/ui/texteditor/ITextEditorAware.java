/*******************************************************************************
 * Copyright (c) 2022 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * - Angelo ZERR (Red Hat Inc.)  - initial implementation
 *******************************************************************************/
package org.eclipse.ui.texteditor;

/**
 * API to inject {@link ITextEditor} instance.
 *
 * @since 3.21
 *
 */
public interface ITextEditorAware {

	/**
	 * Sets the editor
	 *
	 * @param editor the editor
	 */
	void setEditor(ITextEditor editor);

}
