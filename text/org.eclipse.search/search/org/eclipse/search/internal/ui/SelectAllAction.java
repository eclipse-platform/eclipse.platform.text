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
package org.eclipse.search.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.ui.PlatformUI;

/**
 * This action selects all entries currently showing in view.
 */
public class SelectAllAction extends Action {

	private StructuredViewer fViewer;

	/**
	 * Creates the action.
	 */
	public SelectAllAction() {
		super("selectAll"); //$NON-NLS-1$
		setText(SearchMessages.SelectAllAction_label);
		setToolTipText(SearchMessages.SelectAllAction_tooltip);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, ISearchHelpContextIds.SELECT_ALL_ACTION);
	}

	public void setViewer(StructuredViewer viewer) {
		fViewer= viewer;
	}

	private void collectExpandedAndVisible(TreeItem[] items, List<TreeItem> result) {
		for (TreeItem item : items) {
			result.add(item);
			if (item.getExpanded()) {
				collectExpandedAndVisible(item.getItems(), result);
			}
		}
	}

	/**
	 * Selects all resources in the view.
	 */
	@Override
	public void run() {
		if (fViewer == null || fViewer.getControl().isDisposed()) {
			return;
		}
		if (fViewer instanceof TreeViewer) {
			ArrayList<TreeItem> allVisible= new ArrayList<>();
			Tree tree= ((TreeViewer) fViewer).getTree();
			collectExpandedAndVisible(tree.getItems(), allVisible);
			tree.setSelection(allVisible.toArray(new TreeItem[allVisible.size()]));
		} else if (fViewer instanceof TableViewer) {
			((TableViewer) fViewer).getTable().selectAll();
			// force viewer selection change
			fViewer.setSelection(fViewer.getSelection());
		}
	}
}
