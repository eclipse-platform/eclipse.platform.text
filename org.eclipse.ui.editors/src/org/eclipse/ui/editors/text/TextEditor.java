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

package org.eclipse.ui.editors.text;


import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.AddMarkerAction;
import org.eclipse.ui.texteditor.AddTaskAction;
import org.eclipse.ui.texteditor.ConvertLineDelimitersAction;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.ExtendedTextEditor;
import org.eclipse.ui.texteditor.IAbstractTextEditorHelpContextIds;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.ResourceAction;

import org.eclipse.ui.internal.editors.text.EditorsPlugin;



/**
 * The standard text editor for file resources (<code>IFile</code>).
 * <p>
 * This editor has id <code>"org.eclipse.ui.DefaultTextEditor"</code>.
 * The editor's context menu has id <code>#TextEditorContext</code>.
 * The editor's ruler context menu has id <code>#TextRulerContext</code>.
 * </p>
 * <p>
 * The workbench will automatically instantiate this class when the default 
 * editor is needed for a workbench window.
 * </p>
 */
public class TextEditor extends ExtendedTextEditor {
	
	private class GotoMarkerAdapter implements IGotoMarker {
		public void gotoMarker(IMarker marker) {
			TextEditor.this.gotoMarker(marker);
		}
	}
	
	/** 
	 * The encoding support for the editor.
	 * @since 2.0
	 */
	protected DefaultEncodingSupport fEncodingSupport;
	/**
	 * The annotation preferences.
	 * @since 2.1
	 */
	private MarkerAnnotationPreferences fAnnotationPreferences;
	/** 
	 * The editor's implicit document provider.
	 * @since 3.0
	 */
	private IDocumentProvider fImplicitDocumentProvider;
	/** 
	 * The editor's goto marker adapter.
	 * @since 3.0 
	 */
	private Object fGotoMarkerAdapter= new GotoMarkerAdapter();
	
	
	/**
	 * Creates a new text editor.
	 */
	public TextEditor() {
		super();
		initializeKeyBindingScopes();
		setSourceViewerConfiguration(new TextSourceViewerConfiguration());
		initializeEditor();
	}
	
	/**
	 * Initializes this editor.
	 */
	protected void initializeEditor() {
		setEditorContextMenuId("#TextEditorContext"); //$NON-NLS-1$
		setRulerContextMenuId("#TextRulerContext"); //$NON-NLS-1$
		setHelpContextId(ITextEditorHelpContextIds.TEXT_EDITOR);
		setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());
		configureInsertMode(SMART_INSERT, false);
		setInsertMode(INSERT);
	}

	/**
	 * Initializes the key binding scopes of this editor.
	 * 
	 * @since 2.1
	 */
	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });  //$NON-NLS-1$
	}
	
	/*
	 * @see IWorkbenchPart#dispose()
	 * @since 2.0
	 */
	public void dispose() {
		if (fEncodingSupport != null) {
				fEncodingSupport.dispose();
				fEncodingSupport= null;
		}

		super.dispose();
	}

	/*
	 * @see AbstractTextEditor#doSaveAs()
	 * @since 2.1
	 */
	public void doSaveAs() {
		if (askIfNonWorkbenchEncodingIsOk())
			super.doSaveAs();
	}
	
	/*
	 * @see AbstractTextEditor#doSave(IProgressMonitor)
	 * @since 2.1
	 */
	public void doSave(IProgressMonitor monitor){
		if (askIfNonWorkbenchEncodingIsOk())
			super.doSave(monitor);
		else
			monitor.setCanceled(true);
	}

	/**
	 * Installs the encoding support on the given text editor.
	 * <p> 
 	 * Subclasses may override to install their own encoding
 	 * support or to disable the default encoding support.
 	 * </p>
	 * @since 2.1
	 */
	protected void installEncodingSupport() {
		fEncodingSupport= new DefaultEncodingSupport();
		fEncodingSupport.initialize(this);
	}

	/**
	 * Asks the user if it is ok to store in non-workbench encoding.
	 * 
	 * @return <true> if the user wants to continue or if no encoding support has been installed
	 * @since 2.1
	 */
	private boolean askIfNonWorkbenchEncodingIsOk() {
		
		if (fEncodingSupport == null)
			return true;
		
		IDocumentProvider provider= getDocumentProvider();
		if (provider instanceof IStorageDocumentProvider) {
			IEditorInput input= getEditorInput();
			IStorageDocumentProvider storageProvider= (IStorageDocumentProvider)provider;
			String encoding= storageProvider.getEncoding(input);
			String defaultEncoding= storageProvider.getDefaultEncoding();
			if (encoding != null && !encoding.equals(defaultEncoding)) {
				Shell shell= getSite().getShell();
				String title= TextEditorMessages.getString("Editor.warning.save.nonWorkbenchEncoding.title"); //$NON-NLS-1$
				String msg;
				if (input != null)
					msg= MessageFormat.format(TextEditorMessages.getString("Editor.warning.save.nonWorkbenchEncoding.message1"), new String[] {input.getName(), encoding});//$NON-NLS-1$
				else
					msg= MessageFormat.format(TextEditorMessages.getString("Editor.warning.save.nonWorkbenchEncoding.message2"), new String[] {encoding});//$NON-NLS-1$
				return MessageDialog.openQuestion(shell, title, msg);
			}
		}
		return true;
	}

	/**
	 * The <code>TextEditor</code> implementation of this  <code>AbstractTextEditor</code> 
	 * method asks the user for the workspace path of a file resource and saves the document there.
	 * 
	 * @param progressMonitor the progress monitor to be used
	 */
	protected void performSaveAs(IProgressMonitor progressMonitor) {
		Shell shell= getSite().getShell();
		IEditorInput input= getEditorInput();
		
		SaveAsDialog dialog= new SaveAsDialog(shell);
		
		IFile original= (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
		if (original != null)
			dialog.setOriginalFile(original);
		
		dialog.create();
			
		IDocumentProvider provider= getDocumentProvider();
		if (provider == null) {
			// editor has programatically been  closed while the dialog was open
			return;
		}
		
		if (provider.isDeleted(input) && original != null) {
			String message= MessageFormat.format(TextEditorMessages.getString("Editor.warning.save.delete"), new Object[] { original.getName() }); //$NON-NLS-1$
			dialog.setErrorMessage(null);
			dialog.setMessage(message, IMessageProvider.WARNING);
		}
		
		if (dialog.open() == Window.CANCEL) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}
			
		IPath filePath= dialog.getResult();
		if (filePath == null) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}
			
		IWorkspace workspace= ResourcesPlugin.getWorkspace();
		IFile file= workspace.getRoot().getFile(filePath);
		final IEditorInput newInput= new FileEditorInput(file);
				
		boolean success= false;
		try {
			
			provider.aboutToChange(newInput);
			provider.saveDocument(progressMonitor, newInput, provider.getDocument(input), true);			
			success= true;
			
		} catch (CoreException x) {
			
			String title= TextEditorMessages.getString("Editor.error.save.title"); //$NON-NLS-1$
			String msg= MessageFormat.format(TextEditorMessages.getString("Editor.error.save.message"), new Object[] { x.getMessage() }); //$NON-NLS-1$
			
			IStatus status= x.getStatus();
			if (status != null) {
				switch (status.getSeverity()) {
					case IStatus.INFO:
						MessageDialog.openInformation(shell, title, msg);
						break;
					case IStatus.WARNING:
						MessageDialog.openWarning(shell, title, msg);
						break;
					default:
						MessageDialog.openError(shell, title, msg);
				}
			} else {
			  	 MessageDialog.openError(shell, title, msg);
			}
						
		} finally {
			provider.changed(newInput);
			if (success)
				setInput(newInput);
		}
		
		if (progressMonitor != null)
			progressMonitor.setCanceled(!success);
	}
	
	/*
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	/*
	 * @see AbstractTextEditor#createActions()
	 * @since 2.0
	 */
	protected void createActions() {
		super.createActions();
		
		ResourceAction action= new AddTaskAction(TextEditorMessages.getResourceBundle(), "Editor.AddTask.", this); //$NON-NLS-1$
		action.setHelpContextId(ITextEditorHelpContextIds.ADD_TASK_ACTION);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.ADD_TASK);
		setAction(IDEActionFactory.ADD_TASK.getId(), action);
		
		action= new AddMarkerAction(TextEditorMessages.getResourceBundle(), "Editor.AddBookmark.", this, IMarker.BOOKMARK, true); //$NON-NLS-1$
		action.setHelpContextId(ITextEditorHelpContextIds.BOOKMARK_ACTION);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.ADD_BOOKMARK);
		setAction(IDEActionFactory.BOOKMARK.getId(), action);

		action= new ConvertLineDelimitersAction(TextEditorMessages.getResourceBundle(), "Editor.ConvertToWindows.", this, "\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
		action.setHelpContextId(IAbstractTextEditorHelpContextIds.CONVERT_LINE_DELIMITERS_TO_WINDOWS);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONVERT_LINE_DELIMITERS_TO_WINDOWS);
		setAction(ITextEditorActionConstants.CONVERT_LINE_DELIMITERS_TO_WINDOWS, action);

		action= new ConvertLineDelimitersAction(TextEditorMessages.getResourceBundle(), "Editor.ConvertToUNIX.", this, "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		action.setHelpContextId(IAbstractTextEditorHelpContextIds.CONVERT_LINE_DELIMITERS_TO_UNIX);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONVERT_LINE_DELIMITERS_TO_UNIX);
		setAction(ITextEditorActionConstants.CONVERT_LINE_DELIMITERS_TO_UNIX, action);
		
		action= new ConvertLineDelimitersAction(TextEditorMessages.getResourceBundle(), "Editor.ConvertToMac.", this, "\r"); //$NON-NLS-1$ //$NON-NLS-2$
		action.setHelpContextId(IAbstractTextEditorHelpContextIds.CONVERT_LINE_DELIMITERS_TO_MAC);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONVERT_LINE_DELIMITERS_TO_MAC);
		setAction(ITextEditorActionConstants.CONVERT_LINE_DELIMITERS_TO_MAC, action);
		
		// http://dev.eclipse.org/bugs/show_bug.cgi?id=17709
		markAsStateDependentAction(ITextEditorActionConstants.CONVERT_LINE_DELIMITERS_TO_WINDOWS, true);
		markAsStateDependentAction(ITextEditorActionConstants.CONVERT_LINE_DELIMITERS_TO_UNIX, true);
		markAsStateDependentAction(ITextEditorActionConstants.CONVERT_LINE_DELIMITERS_TO_MAC, true);

		installEncodingSupport();
	}
	
	/*
	 * @see StatusTextEditor#getStatusHeader(IStatus)
	 * @since 2.0
	 */
	protected String getStatusHeader(IStatus status) {
		if (fEncodingSupport != null) {
			String message= fEncodingSupport.getStatusHeader(status);
			if (message != null)
				return message;
		}
		return super.getStatusHeader(status);
	}
	
	/*
	 * @see StatusTextEditor#getStatusBanner(IStatus)
	 * @since 2.0
	 */
	protected String getStatusBanner(IStatus status) {
		if (fEncodingSupport != null) {
			String message= fEncodingSupport.getStatusBanner(status);
			if (message != null)
				return message;
		}
		return super.getStatusBanner(status);
	}
	
	/*
	 * @see StatusTextEditor#getStatusMessage(IStatus)
	 * @since 2.0
	 */
	protected String getStatusMessage(IStatus status) {
		if (fEncodingSupport != null) {
			String message= fEncodingSupport.getStatusMessage(status);
			if (message != null)
				return message;
		}
		return super.getStatusMessage(status);
	}
	
	/*
	 * @see AbstractTextEditor#doSetInput(IEditorInput)
	 * @since 2.0
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		if (fEncodingSupport != null)
			fEncodingSupport.reset();
	}
	
	/*
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 * @since 2.0
	 */
	public Object getAdapter(Class adapter) {
		if (IEncodingSupport.class.equals(adapter))
			return fEncodingSupport;
		if (IGotoMarker.class.equals(adapter))
			return fGotoMarkerAdapter;
		return super.getAdapter(adapter);
	}
	
	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#updatePropertyDependentActions()
	 * @since 2.0
	 */
	protected void updatePropertyDependentActions() {
		super.updatePropertyDependentActions();
		if (fEncodingSupport != null)
			fEncodingSupport.reset();
	}
	
	/**
	 * If the editor can be saved all marker ranges have been changed according to
	 * the text manipulations. However, those changes are not yet propagated to the
	 * marker manager. Thus, when opening a marker, the marker's position in the editor
	 * must be determined as it might differ from the position stated in the marker.
	 * 
	 * @param marker the marker to go to
	 * @since 3.0
	 */
	protected void gotoMarker(IMarker marker) {
		
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

	/**
	 * If there is no explicit document provider set, the implicit one is
	 * re-initialized based on the given editor input.
	 *
	 * @param input the editor input.
	 * @since 3.0
	 */
	protected void setDocumentProvider(IEditorInput input) {
		fImplicitDocumentProvider= DocumentProviderRegistry.getDefault().getDocumentProvider(input);		
	}

	/*
	 * @see org.eclipse.ui.texteditor.ITextEditor#getDocumentProvider()
	 * @since 3.0
	 */
	public IDocumentProvider getDocumentProvider() {
		IDocumentProvider provider= super.getDocumentProvider();
		if (provider == null)
			return fImplicitDocumentProvider;
		return provider;
	}

	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#disposeDocumentProvider()
	 * @since 3.0
	 */
	protected void disposeDocumentProvider() {
		super.disposeDocumentProvider();
		fImplicitDocumentProvider= null;
	}
}
