/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
package org.eclipse.text.edits;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * A <code>CopyingRangeMarker</code> can be used to track positions when executing
 * text edits. Additionally a copying range marker stores a local copy of the
 * text it captures when it gets executed.
 *
 * @since 3.0
 */
public final class CopyingRangeMarker extends TextEdit {

	private String fText;

	/**
	 * Creates a new <code>CopyRangeMarker</code> for the given
	 * offset and length.
	 *
	 * @param offset the marker's offset
	 * @param length the marker's length
	 */
	public CopyingRangeMarker(int offset, int length) {
		super(offset, length);
	}

	/*
	 * Copy constructor
	 */
	private CopyingRangeMarker(CopyingRangeMarker other) {
		super(other);
		fText= other.fText;
	}

	@Override
	protected TextEdit doCopy() {
		return new CopyingRangeMarker(this);
	}

	@Override
	protected void accept0(TextEditVisitor visitor) {
		boolean visitChildren= visitor.visit(this);
		if (visitChildren) {
			acceptChildren(visitor);
		}
	}

	@Override
	int performDocumentUpdating(IDocument document) throws BadLocationException {
		fText= document.get(getOffset(), getLength());
		fDelta= 0;
		return fDelta;
	}

	@Override
	boolean deleteChildren() {
		return false;
	}
}
