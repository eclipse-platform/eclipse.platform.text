/**
 *  Copyright (c) 2017 Angelo ZERR.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - [CodeMining] Provide extension point for CodeMining - Bug 528419
 */
package org.eclipse.ui.internal.texteditor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.preferences.IPreferenceStoreProvider;

import org.eclipse.ui.internal.texteditor.TextEditorPlugin;

import org.eclipse.ui.texteditor.ITextEditor;

/**
 * A codemining providers registry used to access the
 * {@link PreferenceStoreProviderDescriptor}s that describe the codemining provider
 * extensions.
 *
 * @see PreferenceStoreProviderDescriptor
 * @since 3.10
 */
public class PreferenceStoreProviderRegistry {

	/**
	 * Extension id of spelling engine extension point. (value
	 * <code>"codeMiningProviders"</code>).
	 */
	public static final String PREFERENCE_STORE_PROVIDERS_EXTENSION_POINT = "preferenceStoreProviders"; //$NON-NLS-1$

	/** All descriptors */
	private PreferenceStoreProviderDescriptor[] fDescriptors;

	/** <code>true</code> iff the extensions have been loaded at least once */
	private boolean fLoaded = false;

	/**
	 * Returns all descriptors.
	 *
	 * @return all descriptors
	 */
	private PreferenceStoreProviderDescriptor[] getDescriptors() {
		ensureExtensionsLoaded();
		return fDescriptors;
	}

	/**
	 * Reads all extensions.
	 * <p>
	 * This method can be called more than once in order to reload from a changed
	 * extension registry.
	 * </p>
	 */
	public synchronized void reloadExtensions() {
		List<PreferenceStoreProviderDescriptor> descriptors = new ArrayList<>();
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(TextEditorPlugin.PLUGIN_ID, PREFERENCE_STORE_PROVIDERS_EXTENSION_POINT);
		for (IConfigurationElement element : elements) {
			try {
				PreferenceStoreProviderDescriptor descriptor = new PreferenceStoreProviderDescriptor(element);
				descriptors.add(descriptor);
			} catch (CoreException e) {
				TextEditorPlugin.getDefault().getLog()
						.log(new Status(IStatus.ERROR, element.getNamespaceIdentifier(), e.getMessage()));
			}
		}
		fDescriptors = descriptors.toArray(new PreferenceStoreProviderDescriptor[descriptors.size()]);
		fLoaded = true;
	}

	/**
	 * Ensures the extensions have been loaded at least once.
	 */
	private void ensureExtensionsLoaded() {
		if (!fLoaded)
			reloadExtensions();
	}

	/**
	 * Returns the codemining providers for the given viewer and editor and null
	 * otherwise.
	 *
	 * @param viewer
	 *            the viewer
	 * @param editor
	 *            the editor
	 * @return the codemining providers for the given viewer and editor and null
	 *         otherwise.
	 */
	public IPreferenceStoreProvider[] getProviders(ITextViewer viewer, ITextEditor editor) {
		List<IPreferenceStoreProvider> providers = new ArrayList<>();
		for (PreferenceStoreProviderDescriptor descriptor : getDescriptors()) {
			if (descriptor.matches(viewer, editor)) {
				IPreferenceStoreProvider provider = descriptor.createPreferenceStoreProvider(editor);
				if (provider != null) {
					providers.add(provider);
				}
			}
		}
		return !providers.isEmpty() ? providers.toArray(new IPreferenceStoreProvider[providers.size()]) : null;
	}
}
