/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.texteditor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;

/**
 * @since 3,0
 */
public class ResourceTextEditor extends StatusTextEditor {
	
	/** The editor's implicit document provider. */
	private IDocumentProvider fImplicitDocumentProvider;
	
	/**
	 * If there is no explicit document provider set, the implicit one is
	 * re-initialized based on the given editor input.
	 *
	 * @param input the editor input.
	 */
	protected void setDocumentProvider(IEditorInput input) {
		fImplicitDocumentProvider= DocumentProviderRegistry.getDefault().getDocumentProvider(input);		
	}
	
	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#getDocumentProvider()
	 */
	public IDocumentProvider getDocumentProvider() {
		IDocumentProvider provider= super.getDocumentProvider();
		if (provider == null)
			return fImplicitDocumentProvider;
		return provider;
	}
	
	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#disposeDocumentProvider()
	 */
	protected void disposeDocumentProvider() {
		super.disposeDocumentProvider();
		fImplicitDocumentProvider= null;
	}
	
	/**
	 * If the editor can be saved all marker ranges have been changed according to
	 * the text manipulations. However, those changes are not yet propagated to the
	 * marker manager. Thus, when opening a marker, the marker's position in the editor
	 * must be determined as it might differ from the position stated in the marker.
	 * 
	 * @param marker the marker to go to
	 * @see EditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker) {
		
		if (getSourceViewer() == null)
			return;
		
		int start= MarkerUtilities.getCharStart(marker);
		int end= MarkerUtilities.getCharEnd(marker);
		
		if (start < 0 || end < 0) {
			
			// there is only a line number
			int line= MarkerUtilities.getLineNumber(marker);
			if (line > -1) {
				
				// marker line numbers are 1-based
				-- line;
				
				try {
					
					IDocument document= getDocumentProvider().getDocument(getEditorInput());
					selectAndReveal(document.getLineOffset(line), document.getLineLength(line));
				
				} catch (BadLocationException x) {
					// marker refers to invalid text position -> do nothing
				}
			}
			
		} else {
		
			// look up the current range of the marker when the document has been edited
			IAnnotationModel model= getDocumentProvider().getAnnotationModel(getEditorInput());
			if (model instanceof AbstractMarkerAnnotationModel) {
				
				AbstractMarkerAnnotationModel markerModel= (AbstractMarkerAnnotationModel) model;
				Position pos= markerModel.getMarkerPosition(marker);
				if (pos != null && !pos.isDeleted()) {
					// use position instead of marker values
					start= pos.getOffset();
					end= pos.getOffset() + pos.getLength();
				}
					
				if (pos != null && pos.isDeleted()) {
					// do nothing if position has been deleted
					return;
				}
			}
			
			IDocument document= getDocumentProvider().getDocument(getEditorInput());
			int length= document.getLength();
			if (end - 1 < length && start < length)
				selectAndReveal(start, end - start);
		}
	}
}
