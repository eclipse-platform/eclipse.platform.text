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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.IDocumentProviderOperation;
import org.eclipse.ui.texteditor.IDocumentProviderOperationRunner;

/**
 * 
 */
public class WorkspaceOperationRunner implements IDocumentProviderOperationRunner {
	
	/*
	 * @see org.eclipse.ui.texteditor.IDocumentProviderOperationRunner#run(org.eclipse.ui.texteditor.IDocumentProviderOperation)
	 */
	public void run(final IDocumentProviderOperation operation, IProgressMonitor progressMonitor) throws CoreException {
		WorkspaceModifyOperation wsop= new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
				operation.execute();
			}
		};
		
		try {
			wsop.run(progressMonitor);
		} catch (InvocationTargetException x) {
			Throwable e = x.getTargetException();
			if (e instanceof CoreException)
				throw (CoreException) e;
			throw new CoreException(new Status(IStatus.ERROR, EditorsPlugin.getPluginId(), IStatus.ERROR, e.getMessage(), e));
		} catch (InterruptedException e) {
			throw new CoreException(new Status(IStatus.CANCEL, EditorsPlugin.getPluginId(), IStatus.OK, e.getMessage(), e));
		}
	}
}
