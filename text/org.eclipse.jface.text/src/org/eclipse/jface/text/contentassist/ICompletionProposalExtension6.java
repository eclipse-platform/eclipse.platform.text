/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
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
package org.eclipse.jface.text.contentassist;

import org.eclipse.jface.viewers.StyledString;


/**
 * Extends {@link org.eclipse.jface.text.contentassist.ICompletionProposal} with the following
 * function:
 * <ul>
 * <li>Allow styled ranges in the display string.</li>
 * </ul>
 *
 * @since 3.4
 */
public interface ICompletionProposalExtension6 {

	/**
	 * Returns the styled string used to display this proposal in the list of completion proposals.
	 * This can for example be used to draw mixed colored labels.
	 * <p>
	 * <strong>Note:</strong> {@link ICompletionProposal#getDisplayString()} still needs to be
	 * correctly implemented as this method might be ignored in case of uninstalled owner draw
	 * support. If {@link ICompletionProposalExtension7} is implemented by this proposal then
	 * instead of using this method, the result from
	 * {@link ICompletionProposalExtension7#getStyledDisplayString(org.eclipse.jface.text.IDocument, int, BoldStylerProvider)}
	 * is used to display the proposal.
	 * </p>
	 *
	 * @return the string builder used to display this proposal
	 */
	StyledString getStyledDisplayString();
}
