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
package org.eclipse.core.filebuffers;

import org.eclipse.jface.text.IDocument;

/**
 * Factory for text file buffer documents. Used by the text file buffer manager
 * to create the document for a new text file buffer.
 * <p>
 * This interface is the expected interface of extensions provided for the
 * <code>"org.eclipse.core.filebuffers.documentCreation"</code> extension
 * point.</p>
 *
 * @since 3.0
 * @deprecated As of 3.2 the <code>"org.eclipse.core.filebuffers.documentCreation"</code>
 *				extension point has been deprecated. See the extension point documentation for more details.
 */
@Deprecated
public interface IDocumentFactory {

	/**
	 * Creates and returns a new, empty document.
	 *
	 * @return a new, empty document
	 */
	IDocument createDocument();
}
