/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.jface.text.projection;


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;


/**
 * Internal interface for defining the exact subset of
 * {@link org.eclipse.jface.text.projection.ProjectionMapping} that the
 * {@link org.eclipse.jface.text.projection.ProjectionTextStore} is allowed to
 * access.
 *
 * @since 3.0
 */
interface IMinimalMapping {

	/*
	 * @see org.eclipse.jface.text.IDocumentInformationMapping#getCoverage()
	 */
	IRegion getCoverage();

	/*
	 * @see org.eclipse.jface.text.IDocumentInformationMapping#toOriginRegion(IRegion)
	 */
	IRegion toOriginRegion(IRegion region) throws BadLocationException;

	/*
	 * @see org.eclipse.jface.text.IDocumentInformationMapping#toOriginOffset(int)
	 */
	int toOriginOffset(int offset) throws BadLocationException;

	/*
	 * @see org.eclipse.jface.text.IDocumentInformationMappingExtension#toExactOriginRegions(IRegion)
	 */
	IRegion[] toExactOriginRegions(IRegion region) throws BadLocationException;

	/*
	 * @see org.eclipse.jface.text.IDocumentInformationMappingExtension#getImageLength()
	 */
	int getImageLength();
}
