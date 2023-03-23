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
package org.eclipse.text.edits;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

class TextEditMessages {

	private static final String BUNDLE_NAME= "org.eclipse.text.edits.Messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE= ResourceBundle.getBundle(BUNDLE_NAME);

	private TextEditMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getFormattedString(String key, Object arg) {
		return getFormattedString(key, new Object[] { arg });
	}

	public static String getFormattedString(String key, Object[] args) {
		return MessageFormat.format(getString(key), args);
	}
}
