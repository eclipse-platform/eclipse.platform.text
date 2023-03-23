/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 *
 * This program and the  accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
  *******************************************************************************/
package org.eclipse.jface.text.source;


/**
 * Extension interface for {@link org.eclipse.jface.text.source.IOverviewRuler}.
 * <p>
 * Allows to set whether to use saturated colors in the overview ruler.
 * </p>
 *
 * @see org.eclipse.jface.text.source.IOverviewRuler
 * @since 3.8
 */
public interface IOverviewRulerExtension {

	/**
	 * Sets whether to use saturated colors in the overview ruler.
	 * <p>
	 * The initial value is defined by the ruler implementation.
	 * </p>
	 *
	 * @param useSaturatedColors <code>true</code> if saturated colors should be used,
	 *            <code>false</code> otherwise
	 */
	void setUseSaturatedColors(boolean useSaturatedColors);
}
