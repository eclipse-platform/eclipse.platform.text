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

package org.eclipse.ui.texteditor;


import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Extension interface for {@link org.eclipse.ui.texteditor.IDocumentProvider}. It adds the following
 * functions:
 * <ul>
 * <li> global temporary progress monitor
 * </ul>
 * @since 2.1
 */
public interface IDocumentProviderExtension2 {

	/**
	 * Sets this providers progress monitor.
	 *
	 * @param progressMonitor the progress monitor
	 */
	void setProgressMonitor(IProgressMonitor progressMonitor);

	/**
	 * Returns this providers progress monitor.
	 * @return IProgressMonitor
	 */
	IProgressMonitor getProgressMonitor();
}
