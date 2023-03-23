/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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
package org.eclipse.core.filebuffers.tests;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.core.resources.IProject;

import org.eclipse.core.filebuffers.FileBuffers;

import org.eclipse.jface.text.source.IAnnotationModel;

/**
 * FileBuffersForExternalFiles
 */
public class FileStoreFileBuffersForExternalFiles extends FileStoreFileBufferFunctions {

	@Override
	@After
	public void tearDown() {
		FileTool.delete(getPath());
		FileTool.delete(FileBuffers.getSystemFileAtLocation(getPath()).getParentFile());
		super.tearDown();
	}

	@Override
	protected IPath createPath(IProject project) throws Exception {
		File sourceFile= FileTool.getFileInPlugin(FileBuffersTestPlugin.getDefault(), new Path("testResources/ExternalFile"));
		File externalFile= FileTool.createTempFileInPlugin(FileBuffersTestPlugin.getDefault(), new Path("externalResources/ExternalFile"));
		FileTool.copy(sourceFile, externalFile);
		return new Path(externalFile.getAbsolutePath());
	}

	@Override
	protected void setReadOnly(boolean state) throws Exception {
		IFileStore fileStore= FileBuffers.getFileStoreAtLocation(getPath());
		assertNotNull(fileStore);
		fileStore.fetchInfo().setAttribute(EFS.ATTRIBUTE_READ_ONLY, state);
	}

	@Override
	protected boolean isStateValidationSupported() {
		return false;
	}

	@Override
	protected boolean deleteUnderlyingFile() throws Exception {
		return false;
	}

	@Override
	protected IPath moveUnderlyingFile() throws Exception {
		return null;
	}

	@Override
	protected boolean modifyUnderlyingFile() throws Exception {
		return false;
	}

	@Override
	protected Class<IAnnotationModel> getAnnotationModelClass() throws Exception {
		return null;
	}
}
