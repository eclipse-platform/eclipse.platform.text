/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
package org.eclipse.ui.examples.javaeditor;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;

import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * A content outline page which always represents the content of the
 * connected editor in 10 segments.
 */
public class JavaContentOutlinePage extends ContentOutlinePage {

	/**
	 * A segment element.
	 */
	protected static class Segment {
		public String name;
		public Position position;

		public Segment(String name, Position position) {
			this.name= name;
			this.position= position;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Divides the editor's document into ten segments and provides elements for them.
	 */
	protected class ContentProvider implements ITreeContentProvider {

		protected final static String SEGMENTS= "__java_segments"; //$NON-NLS-1$
		protected IPositionUpdater fPositionUpdater= new DefaultPositionUpdater(SEGMENTS);
		protected List<Segment> fContent= new ArrayList<>(10);

		protected void parse(IDocument document) {

			int lines= document.getNumberOfLines();
			int increment= Math.max(Math.round(lines / 10), 10);

			for (int line= 0; line < lines; line += increment) {

				int length= increment;
				if (line + increment > lines)
					length= lines - line;

				try {

					int offset= document.getLineOffset(line);
					int end= document.getLineOffset(line + length);
					length= end - offset;
					Position p= new Position(offset, length);
					document.addPosition(SEGMENTS, p);
					fContent.add(new Segment(MessageFormat.format(JavaEditorMessages.getString("OutlinePage.segment.title_pattern"), new Object[] { Integer.valueOf(offset) }), p)); //$NON-NLS-1$

				} catch (BadPositionCategoryException x) {
				} catch (BadLocationException x) {
				}
			}
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				IDocument document= fDocumentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(SEGMENTS);
					} catch (BadPositionCategoryException x) {
					}
					document.removePositionUpdater(fPositionUpdater);
				}
			}

			fContent.clear();

			if (newInput != null) {
				IDocument document= fDocumentProvider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(SEGMENTS);
					document.addPositionUpdater(fPositionUpdater);

					parse(document);
				}
			}
		}

		/*
		 * @see IContentProvider#dispose
		 */
		@Override
		public void dispose() {
			if (fContent != null) {
				fContent.clear();
				fContent= null;
			}
		}

		@Override
		public Object[] getElements(Object element) {
			return fContent.toArray();
		}

		@Override
		public boolean hasChildren(Object element) {
			return element == fInput;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof Segment)
				return fInput;
			return null;
		}

		@Override
		public Object[] getChildren(Object element) {
			if (element == fInput)
				return fContent.toArray();
			return new Object[0];
		}
	}

	protected Object fInput;
	protected IDocumentProvider fDocumentProvider;
	protected ITextEditor fTextEditor;

	/**
	 * Creates a content outline page using the given provider and the given editor.
	 *
	 * @param provider the document provider
	 * @param editor the editor
	 */
	public JavaContentOutlinePage(IDocumentProvider provider, ITextEditor editor) {
		super();
		fDocumentProvider= provider;
		fTextEditor= editor;
	}

	@Override
	public void createControl(Composite parent) {

		super.createControl(parent);

		TreeViewer viewer= getTreeViewer();
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.addSelectionChangedListener(this);

		if (fInput != null)
			viewer.setInput(fInput);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		super.selectionChanged(event);

		IStructuredSelection selection= event.getStructuredSelection();
		if (selection.isEmpty())
			fTextEditor.resetHighlightRange();
		else {
			Segment segment= (Segment) selection.getFirstElement();
			int start= segment.position.getOffset();
			int length= segment.position.getLength();
			try {
				fTextEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				fTextEditor.resetHighlightRange();
			}
		}
	}

	/**
	 * Sets the input of the outline page
	 *
	 * @param input the input of this outline page
	 */
	public void setInput(Object input) {
		fInput= input;
		update();
	}

	/**
	 * Updates the outline page.
	 */
	public void update() {
		TreeViewer viewer= getTreeViewer();

		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fInput);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}
}
