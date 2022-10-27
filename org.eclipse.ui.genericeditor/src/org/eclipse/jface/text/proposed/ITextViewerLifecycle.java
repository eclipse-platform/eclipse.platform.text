package org.eclipse.jface.text.proposed;

import org.eclipse.jface.text.ITextViewer;

public interface ITextViewerLifecycle {

	void install(ITextViewer viewer);

	void uninstall();
}
