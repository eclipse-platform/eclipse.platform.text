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
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IInputSelectionProvider;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * Specify how clients can add menu items to the context menu of the search
 * result view. A class that contributes context menu items must implement this
 * interface and pass an instance of itself to the search result view.
 *
 * @see ISearchResultView#searchStarted(IActionGroupFactory, String, String,
 *      ImageDescriptor, String, ILabelProvider, IAction, IGroupByKeyComputer,
 *      IRunnableWithProgress)
 * @deprecated Part of the old ('classic') search result view. Since 3.0 clients
 *             can create their own search result view pages (see
 *             {@link ISearchResultPage}), leaving it up to the page how to
 *             create actions in context menus. This class will be removed after
 *             2023-09 release. See
 *             https://bugs.eclipse.org/bugs/show_bug.cgi?id=487303 for more
 *             information.
 *
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noreference
 * @noextend
 * @noimplement
 */
@Deprecated(forRemoval = true)
public interface IContextMenuContributor {

	/**
	 * Contributes menu items to the given context menu appropriate for the
	 * given selection.
	 *
	 * @param menu		the menu to which the items are added
	 * @param inputProvider	the selection and input provider
	 */
	public void fill(IMenuManager menu, IInputSelectionProvider inputProvider);
}
