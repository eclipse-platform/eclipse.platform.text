/*******************************************************************************
 * Copyright (c) 2022 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.ui.editors.tests;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import org.eclipse.core.filebuffers.tests.ResourceHelper;

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.ui.tests.harness.util.DisplayHelper;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

public class RulerTest {

	private IPreferenceStore preferenceStore = EditorsPlugin.getDefault().getPreferenceStore();
	private boolean initialPreference = false;
	private IProject fProject;

	@Before
	public void setup() throws CoreException {
		IIntroPart intro = PlatformUI.getWorkbench().getIntroManager().getIntro();
		if (intro != null) {
			PlatformUI.getWorkbench().getIntroManager().closeIntro(intro);
		}
		fProject = ResourceHelper.createProject("RulerTest");
		initialPreference = preferenceStore
				.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER);
	}

	@After
	public void tearDown() throws Exception {
		fProject.delete(true, null);
		fProject = null;
		TestUtil.cleanUp();
		preferenceStore.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER,
				initialPreference);
	}

	@Test
	public void testScrollThenToggleWordWrap() throws Exception {
		IFile file = fProject.getFile("testWordWrap.txt");
		file.create(new ByteArrayInputStream(CONTENT.getBytes(Charset.defaultCharset())), true, null);
		preferenceStore.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER, true);
		AbstractDecoratedTextEditor editor = (AbstractDecoratedTextEditor) IDE
				.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file, true);
		StyledText control = (StyledText) editor.getAdapter(Control.class);
		Display display = control.getDisplay();
		// draw at least once
		DisplayHelper.sleep(display, 200);
		control.setTopPixel(20);
		control.setTopPixel(0);
		editor.selectAndReveal(0, 0);
		AtomicReference<Throwable> caught = new AtomicReference<>();
		ILogListener listener = (status, plugin) -> {
			if (status.getSeverity() == IStatus.ERROR) {
				caught.set(status.getException());
			}
		};
		Platform.addLogListener(listener);
		try {
			control.setWordWrap(true);
			// process events and allow redraw
			assertFalse("Exception was caught", new DisplayHelper() {
				@Override
				protected boolean condition() {
					return caught.get() != null;
				}
			}.waitForCondition(display, 500));
		} finally {
			Platform.removeLogListener(listener);
		}
	}

	public static final String CONTENT = "org.eclipse.core.runtime.AssertionFailedException: assertion failed: \n"
			+ "	at org.eclipse.core.runtime.Assert.isTrue(Assert.java:113)\n"
			+ "	at org.eclipse.core.runtime.Assert.isTrue(Assert.java:99)\n"
			+ "	at org.eclipse.jface.text.Position.<init>(Position.java:65)\n"
			+ "	at org.eclipse.jface.text.AbstractDocument.getPositions(AbstractDocument.java:1608)\n"
			+ "	at org.eclipse.core.internal.filebuffers.SynchronizableDocument.getPositions(SynchronizableDocument.java:246)\n"
			+ "	at org.eclipse.jface.text.source.AnnotationModel.getRegionAnnotationIterator(AnnotationModel.java:716)\n"
			+ "	at org.eclipse.jface.text.source.AnnotationModel.getAnnotationIterator(AnnotationModel.java:678)\n"
			+ "	at org.eclipse.jface.text.source.AnnotationRulerColumn.doPaint(AnnotationRulerColumn.java:642)\n"
			+ "	at org.eclipse.jface.text.source.AnnotationRulerColumn.doubleBufferPaint(AnnotationRulerColumn.java:548)\n"
			+ "	at org.eclipse.jface.text.source.AnnotationRulerColumn.lambda$1(AnnotationRulerColumn.java:274)\n"
			+ "	at org.eclipse.swt.widgets.TypedListener.handleEvent(TypedListener.java:234)\n"
			+ "	at org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:89)\n"
			+ "	at org.eclipse.swt.widgets.Display.sendEvent(Display.java:5794)\n"
			+ "	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1529)\n"
			+ "	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1555)\n"
			+ "	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1538)\n"
			+ "	at org.eclipse.swt.widgets.Control.gtk_draw(Control.java:3882)\n"
			+ "	at org.eclipse.swt.widgets.Scrollable.gtk_draw(Scrollable.java:365)\n"
			+ "	at org.eclipse.swt.widgets.Composite.gtk_draw(Composite.java:496)\n"
			+ "	at org.eclipse.swt.widgets.Canvas.gtk_draw(Canvas.java:174)\n"
			+ "	at org.eclipse.swt.widgets.Widget.windowProc(Widget.java:2409)\n"
			+ "	at org.eclipse.swt.widgets.Control.windowProc(Control.java:6832)\n"
			+ "	at org.eclipse.swt.widgets.Display.windowProc(Display.java:6114)\n"
			+ "	at org.eclipse.swt.internal.gtk3.GTK3.gtk_main_do_event(Native Method)\n"
			+ "	at org.eclipse.swt.widgets.Display.eventProc(Display.java:1552)\n"
			+ "	at org.eclipse.swt.internal.gtk3.GTK3.gtk_main_iteration_do(Native Method)\n"
			+ "	at org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:4474)\n"
			+ "	at org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$5.run(PartRenderingEngine.java:1155)\n"
			+ "	at org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:338)\n"
			+ "	at org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1046)\n"
			+ "	at org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:155)\n"
			+ "	at org.eclipse.ui.internal.Workbench.lambda$3(Workbench.java:644)\n"
			+ "	at org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:338)\n"
			+ "	at org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:551)\n"
			+ "	at org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:156)\n"
			+ "	at org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:152)\n"
			+ "	at org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:203)\n"
			+ "	at org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:136)\n"
			+ "	at org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)\n"
			+ "	at org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:402)\n"
			+ "	at org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:255)\n"
			+ "	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n"
			+ "	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n"
			+ "	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n"
			+ "	at java.base/java.lang.reflect.Method.invoke(Method.java:566)\n"
			+ "	at org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:659)\n"
			+ "	at org.eclipse.equinox.launcher.Main.basicRun(Main.java:596)\n"
			+ "	at org.eclipse.equinox.launcher.Main.run(Main.java:1467)\n"
			+ "	at org.eclipse.equinox.launcher.Main.main(Main.java:1440)\n" + "\n" + "";
}