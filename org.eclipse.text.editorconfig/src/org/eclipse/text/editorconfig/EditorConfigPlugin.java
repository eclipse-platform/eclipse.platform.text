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
package org.eclipse.text.editorconfig;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.text.editorconfig.internal.IDEEditorConfigManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle of the .editorconfig
 * plugin.
 */
public class EditorConfigPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.text.editorconfig"; //$NON-NLS-1$

	// The shared instance
	private static EditorConfigPlugin plugin;

	/**
	 * The constructor
	 */
	public EditorConfigPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		IDEEditorConfigManager.getInstance().init();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		IDEEditorConfigManager.getInstance().dispose();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EditorConfigPlugin getDefault() {
		return plugin;
	}

	/**
	 * Utility method to log errors.
	 *
	 * @param thr
	 *                The exception through which we noticed the error
	 */
	public static void logError(final Throwable thr) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0, thr.getMessage(), thr));
	}

	/**
	 * Utility method to log errors.
	 *
	 * @param message
	 *                    User comprehensible message
	 * @param thr
	 *                    The exception through which we noticed the error
	 */
	public static void logError(final String message, final Throwable thr) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0, message, thr));
	}

}
