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
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.ec4j.core.Resource;
import org.ec4j.core.ResourcePath;
import org.ec4j.core.model.Ec4jPath;
import org.ec4j.core.model.Ec4jPath.Ec4jPaths;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * A {@link Resource} implementation that uses an underlying {@link IFile}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class FileResource implements Resource {
	final IFile file;

	public FileResource(IFile file) {
		super();
		this.file = file;
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public ResourcePath getParent() {
		IContainer parent = file.getParent();
		return parent == null ? null : new ContainerResourcePath(parent);
	}

	@Override
	public Ec4jPath getPath() {
		return Ec4jPaths.of(file.getLocation().toString().replaceAll("[\\\\]", "/"));
	}

	@Override
	public RandomReader openRandomReader() throws IOException {
		try (Reader reader = openReader()) {
			return org.ec4j.core.Resource.Resources.StringRandomReader.ofReader(reader);
		}
	}

	@Override
	public Reader openReader() throws IOException {
		try {
			return new InputStreamReader(file.getContents(), Charset.forName(file.getCharset()));
		} catch (CoreException e) {
			throw new IOException(e);
		}
	}

}