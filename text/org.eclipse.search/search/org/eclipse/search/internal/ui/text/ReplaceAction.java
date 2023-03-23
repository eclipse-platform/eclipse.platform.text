/*******************************************************************************
 * Copyright (c) 2007, 2011 IBM Corporation and others.
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
package org.eclipse.search.internal.ui.text;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.action.Action;

import org.eclipse.search.internal.ui.SearchMessages;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;

public class ReplaceAction extends Action {

	public static class ReplaceWizard extends RefactoringWizard {
		public ReplaceWizard(ReplaceRefactoring refactoring) {
			super(refactoring, RefactoringWizard.DIALOG_BASED_USER_INTERFACE);
		}

		@Override
		protected void addUserInputPages() {
			addPage(new ReplaceConfigurationPage((ReplaceRefactoring) getRefactoring()));
		}
	}

	private final FileSearchResult fResult;
	private final Object[] fSelection;
	private final Shell fShell;

	/**
	 * Creates the replace action to be
	 * @param shell the parent shell
	 * @param result the file search page to
	 * @param selection the selected entries or <code>null</code> to replace all
	 */
	public ReplaceAction(Shell shell, FileSearchResult result, Object[] selection) {
		fShell= shell;
		fResult= result;
		fSelection= selection;
	}

	@Override
	public void run() {
		try {
			ReplaceRefactoring refactoring= new ReplaceRefactoring(fResult, fSelection);
			ReplaceWizard refactoringWizard= new ReplaceWizard(refactoring);
			if (fSelection == null) {
				refactoringWizard.setDefaultPageTitle(SearchMessages.ReplaceAction_title_all);
			} else {
				refactoringWizard.setDefaultPageTitle(SearchMessages.ReplaceAction_title_selected);
			}
			RefactoringWizardOpenOperation op= new RefactoringWizardOpenOperation(refactoringWizard);
			op.run(fShell, SearchMessages.ReplaceAction_description_operation);
		} catch (InterruptedException e) {
			// refactoring got cancelled
		}
	}

}
