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
package org.eclipse.jface.text.information;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerLifecycle;


/**
 * An information presenter shows information available at the text viewer's current document
 * position. An <code>IInformationPresenter</code> is a {@link org.eclipse.jface.text.ITextViewer}
 * add-on.
 * <p>
 * An information presenters has a list of
 * {@link org.eclipse.jface.text.information.IInformationProvider} objects each of which is
 * registered for a particular document content type. The presenter uses the strategy objects to
 * retrieve the information to present.
 * </p>
 * <p>
 * In order to provide backward compatibility for clients of <code>IInformationPresenter</code>,
 * extension interfaces are used to provide a means of evolution. The following extension interfaces
 * exist:
 * </p>
 * <ul>
 * <li>{@link IInformationPresenterExtension} since version 3.0 introducing the ability to handle
 * documents with multiple partitions</li>
 * </ul>
 * <p>
 * The interface can be implemented by clients. By default, clients use
 * {@link org.eclipse.jface.text.information.InformationPresenter} as the standard implementer of
 * this interface.
 * </p>
 *
 * @see org.eclipse.jface.text.information.IInformationPresenterExtension
 * @see org.eclipse.jface.text.ITextViewer
 * @see org.eclipse.jface.text.information.IInformationProvider
 * @since 2.0
 */
public interface IInformationPresenter extends ITextViewerLifecycle {

	/**
	 * Installs the information presenter on the given text viewer. After this method has been
	 * finished, the presenter is operational, i.e. the method {@link #showInformation()}
	 * can be called until {@link #uninstall()} is called.
	 *
	 * @param textViewer the viewer on which the presenter is installed
	 */
	@Override
	void install(ITextViewer textViewer);

	/**
	 * Removes the information presenter from the text viewer it has previously been
	 * installed on.
	 */
	@Override
	void uninstall();

	/**
	 * Shows information related to the cursor position of the text viewer
	 * this information presenter is installed on.
	 */
	void showInformation();

	/**
	 * Returns the information provider to be used for the given content type.
	 *
	 * @param contentType the type of the content for which information will be requested
	 * @return an information provider or <code>null</code> if none exists for the specified content type
	 */
	IInformationProvider getInformationProvider(String contentType);
}
