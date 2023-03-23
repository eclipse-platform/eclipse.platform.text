/*******************************************************************************
 * Copyright (c) 2007, 2012 Dakshinamurthy Karra, IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Dakshinamurthy Karra (Jalian Systems) - Templates View - https://bugs.eclipse.org/bugs/show_bug.cgi?id=69581
 *******************************************************************************/
package org.eclipse.ui.texteditor.templates;

import org.eclipse.ui.part.IPageBookViewPage;


/**
 * Interface for a templates page. This interface defines the minimum
 * requirement for a page within the {@link TemplatesView}.
 * <p>
 * In order to provided backward compatibility for clients of
 * <code>ITemplatesPage</code>, extension interfaces are used to provide a means
 * of evolution. The following extension interfaces exist:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.ui.texteditor.templates.ITemplatesPageExtension} since
 * version 3.6 introducing access to the currently selected items and the
 * template store of the page.</li>
 * </ul>
 * <p>
 * Clients can either implement this interface directly or subclass the
 * {@link AbstractTemplatesPage}.
 * </p>
 *
 * @see org.eclipse.ui.texteditor.templates.ITemplatesPageExtension
 * @see TemplatesView
 * @since 3.4
 */
public interface ITemplatesPage extends IPageBookViewPage {
}
