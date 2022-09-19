/**
 *  Copyright (c) 2022 Angelo Zerr, Peter Palaga and others.
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
package org.eclipse.text.editorconfig.internal.resource;

import java.io.IOException;

import org.ec4j.core.Resource.RandomReader;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * A {@link RandomReader} implementation that uses an underlying {@link IDocument}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DocumentRandomReader implements RandomReader {
	private final IDocument document;

	public DocumentRandomReader(IDocument document) {
		super();
		this.document = document;
	}

	@Override
	public void close() throws IOException {
		/* nothing to do */
	}

	@Override
	public long getLength() {
		return document.getLength();
	}

	@Override
	public char read(long offset) throws IndexOutOfBoundsException {
		try {
			return document.getChar((int)offset);
		} catch (BadLocationException e) {
			throw new IndexOutOfBoundsException(e.getMessage());
		}
	}

}