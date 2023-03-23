/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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

import org.eclipse.core.runtime.CoreException;

/**
 * Extension interface for {@link org.eclipse.ui.texteditor.IDocumentProvider}.
 * Extends a document provider with the ability to detect a non-synchronized exception.
 * <p>
 * This interface may be implemented by clients.
 * </p>
 *
 * @see org.eclipse.ui.texteditor.IDocumentProvider
 * @since 3.2
 */
public interface IDocumentProviderExtension5 {

	/**
	 * Tells whether the given core exception is exactly the
	 * exception which is thrown for a non-synchronized element.
	 *
	 * @param element the element
	 * @param ex the core exception
	 * @return <code>true</code> iff the given core exception is exactly the
	 *			exception which is thrown for a non-synchronized element
	 */
	boolean isNotSynchronizedException(Object element, CoreException ex);
}
