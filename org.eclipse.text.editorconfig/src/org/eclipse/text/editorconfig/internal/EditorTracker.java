/**
 *  Copyright (c) 2022 Angelo Zerr and others.
 *  
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.text.editorconfig.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.text.editorconfig.EditorConfigPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Editor tracker used to
 * 
 * <ul>
 * <li>update {@link IPreferenceStore} of the opened text editor by adding the
 * {@link EditorConfigPreferenceStore}.</li>
 * <li>call {@link EditorConfigPreferenceStore#applyConfig()} when editor has
 * focus to apply properties coming from .editorconfig files.</li>
 * </ul>
 *
 */
public class EditorTracker implements IWindowListener, IPageListener, IPartListener {

	private static EditorTracker INSTANCE;

	private Map<ITextEditor, EditorConfigPreferenceStore> applies = new HashMap<>();

	private EditorTracker() {
		init();
	}

	public static EditorTracker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EditorTracker();
		}
		return INSTANCE;
	}

	private void init() {
		if (PlatformUI.isWorkbenchRunning()) {
			IWorkbench workbench = EditorConfigPlugin.getDefault().getWorkbench();
			if (workbench != null) {
				IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
				for (IWorkbenchWindow window : windows) {
					windowOpened(window);
				}
				EditorConfigPlugin.getDefault().getWorkbench().addWindowListener(this);
			}
		}
	}

	@Override
	public void windowActivated(IWorkbenchWindow window) {
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
		IWorkbenchPage[] pages = window.getPages();
		for (IWorkbenchPage page : pages) {
			pageClosed(page);
		}
		window.removePageListener(this);
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		if (window.getShell() != null) {
			IWorkbenchPage[] pages = window.getPages();
			for (IWorkbenchPage page : pages) {
				pageOpened(page);
			}
			window.addPageListener(this);
		}
	}

	@Override
	public void pageActivated(IWorkbenchPage page) {
	}

	@Override
	public void pageClosed(IWorkbenchPage page) {
		IEditorReference[] rs = page.getEditorReferences();
		for (IEditorReference r : rs) {
			IEditorPart part = r.getEditor(false);
			if (part != null) {
				editorClosed(part);
			}
		}
		page.removePartListener(this);
	}

	@Override
	public void pageOpened(IWorkbenchPage page) {
		IEditorReference[] rs = page.getEditorReferences();
		for (IEditorReference r : rs) {
			IEditorPart part = r.getEditor(false);
			if (part != null) {
				editorOpened(part);
			}
		}
		page.addPartListener(this);
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if (part instanceof AbstractTextEditor) {
			AbstractTextEditor editor = (AbstractTextEditor) part;
			EditorConfigPreferenceStore store = applies.get(editor);
			if (store != null) {
				store.applyConfig();
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof IEditorPart) {
			editorClosed((IEditorPart) part);
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if (part instanceof IEditorPart) {
			editorOpened((IEditorPart) part);
		}
	}

	public void track(ITextEditor editor, EditorConfigPreferenceStore store) {
		applies.put(editor, store);
	}

	private void editorOpened(IEditorPart part) {
		if (part instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) part;
			EditorConfigPreferenceStore apply = applies.get(editor);
			if (apply != null) {
				apply.applyConfig();
			}
		}
	}

	private void editorClosed(IEditorPart part) {
		if (part instanceof AbstractTextEditor) {
			EditorConfigPreferenceStore apply = applies.remove(part);
			if (apply != null) {
				Assert.isTrue(null == applies.get(part),
						"An old EditorConfigPreferenceStore is not un-installed on Text Editor instance");
			}
		}
	}

}
