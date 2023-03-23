/*******************************************************************************
 * Copyright (c) 2012, 2015 IBM Corporation and others.
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
package org.eclipse.jface.text.tests;

import org.junit.Test;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

/**
 * Tests for the default pair matcher.
 *
 * @since 3.8
 */
public class DefaultPairMatcherTest2 extends AbstractPairMatcherTest {

	public DefaultPairMatcherTest2() {
		super(true);
	}

	/** Tests that the test case reader works */
	@Test
	public void testTestCaseReader1() {
		performReaderTest("#( )%", 3, 0, "( )");
		performReaderTest("( )%", 3, -1, "( )");
	}

	/**
	 * Very simple checks.
	 *
	 * @throws BadLocationException test failure
	 */
	@Test
	public void testSimpleMatchSameMatcher1() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, "#(   %)");
		performMatch(matcher, "#[   %]");
		performMatch(matcher, "#{   %}");
		performMatch(matcher, "%(   )#");
		performMatch(matcher, "%[   ]#");
		performMatch(matcher, "%{   }#");
		matcher.dispose();
	}
}