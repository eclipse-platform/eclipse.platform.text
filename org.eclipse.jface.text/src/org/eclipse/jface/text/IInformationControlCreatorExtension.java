/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.text;

/**
 * Extension to <code>IInformationControlCreator</code> which
 * tests if an existing information control can be reused.
 * 
 * @see org.eclipse.jface.text.IInformationControlCreator
 * @see org.eclipse.jface.text.IInformationControl
 * @since 3.0
 */
public interface IInformationControlCreatorExtension {
	
	/**
	 * Tests if an existing information control can be reused.
	 * 
	 * @param control the information control to test
	 * @return <code>true</code> if the control can be reused
	 */
	boolean canBeReused(IInformationControl control);
}
