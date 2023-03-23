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
package org.eclipse.search.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;

import org.eclipse.ui.actions.ActionGroup;

/**
 * Allows to specify an <code>ActionGroup</code> factory which will be used by
 * the Search view to create an <code>ActionGroup</code> which is used to build
 * the actions bars and the context menu.
 * <p>
 * Note: Local tool bar contributions are not supported in 2.0.
 * </p>
 *
 * Clients can implement this interface and pass an instance to the search
 * result view.
 *
 * @see org.eclipse.ui.actions.ActionGroup
 * @see ISearchResultView#searchStarted(IActionGroupFactory, String, String,
 *      ImageDescriptor, String, ILabelProvider, IAction, IGroupByKeyComputer,
 *      IRunnableWithProgress)
 * @since 2.0
 * @deprecated Part of the old ('classic') search result view. Since 3.0 clients
 *             can create their own search result view pages (see
 *             {@link ISearchResultPage}), leaving it up to the page how to
 *             create actions. This class will be removed after 2023-09 release.
 *             See https://bugs.eclipse.org/bugs/show_bug.cgi?id=487303 for more
 *             information.
 *
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noreference
 * @noextend
 * @noimplement
 */
@Deprecated(forRemoval = true)
public interface IActionGroupFactory {

	/**
	 * Creates an <code>ActionGroup</code> for a Search view.
	 *
	 * @param 	searchView the search result view for which the group is made
	 * @see	org.eclipse.ui.actions.ActionGroup
	 * @return an <code>ActionGroup</code> for a Search view
	 */
	ActionGroup createActionGroup(ISearchResultView searchView);
}
