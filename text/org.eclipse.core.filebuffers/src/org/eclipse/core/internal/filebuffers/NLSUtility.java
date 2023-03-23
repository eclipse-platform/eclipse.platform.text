/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
package org.eclipse.core.internal.filebuffers;

import java.text.MessageFormat;

/**
 * A number of routines used for string externalization.
 *
 * @since 3.1
 */
public class NLSUtility {

	/**
	 * Formats the given string with the given argument.
	 *
	 * @param message the message to format, must not be <code>null</code>
	 * @param argument the argument used to format the string
	 * @return the formatted string
	 */
	public static String format(String message, Object argument) {
		return MessageFormat.format(message, argument);
	}

	/**
	 * Formats the given string with the given argument.
	 *
	 * @param message the message to format, must not be <code>null</code>
	 * @param arguments the arguments used to format the string
	 * @return the formatted string
	 */
	public static String format(String message, Object[] arguments) {
		return MessageFormat.format(message, arguments);
	}

	private NLSUtility() {
		// Do not instantiate
	}
}
