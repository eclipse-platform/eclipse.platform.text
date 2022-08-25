/*******************************************************************************
 * Copyright (c) 2022 Joerg Kubitz and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.search.ui;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Represents a particular search query that can be rerun in background with an
 * updated search text
 * <p>
 * Clients may implement this interface.
 * </p>
 *
 * @since 3.15
 */
public interface IResearchQuery extends ISearchQuery {
	/**
	 * @return the text the user was searching for
	 */
	String getSearchString();

	/**
	 * Sets the search text for the next run
	 * 
	 * @param s
	 *            the text the user is searching for
	 * @see org.eclipse.search.ui.ISearchQuery#run(IProgressMonitor)
	 */
	void setSearchString(String s);

	@Override
	public default boolean canRerun() {
		return true;
	}

	@Override
	public default boolean canRunInBackground() {
		return true;
	}

}
