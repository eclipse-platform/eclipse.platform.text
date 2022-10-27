/*******************************************************************************
 * Copyright (c) 2022 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * - Angelo ZERR (Red Hat Inc.)  - initial implementation
 *******************************************************************************/
package org.eclipse.jface.text;

import org.eclipse.jface.text.DefaultInformationControl.IInformationPresenter;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

/**
 * {@link ITextViewer} lifecycle API to track install / uninstall of a given {@link ITextViewer} for
 * the given contribution which extends {@link ITextViewerLifecycle}:
 *
 * <ul>
 * <li>{@link IReconciler}</li>
 * <li>{@link IPresentationReconciler}</li>
 * <li>{@link IHyperlinkPresenter}</li>
 * <li>{@link IInformationPresenter}</li>
 * <li>{@link IContentAssistant}</li>
 * </ul>
 *
 * It is possible too to implement {@link ITextViewerLifecycle} to track install / uninstall of a
 * given {@link ITextViewer} for implementation of:
 *
 * <ul>
 * <li>{@link IReconcilingStrategy}</li>
 * <li>{@link IAutoEditStrategy}</li>
 * </ul>
 *
 * @since 3.22
 *
 */
public interface ITextViewerLifecycle {

	/**
	 * Installs a text viewer.
	 *
	 * @param textViewer the text viewer
	 */
	void install(ITextViewer textViewer);

	/**
	 * Uninstalls the registered text viewer.
	 */
	void uninstall();
}
