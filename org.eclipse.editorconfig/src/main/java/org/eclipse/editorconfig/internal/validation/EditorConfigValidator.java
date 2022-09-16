package org.eclipse.editorconfig.internal.validation;

import org.eclipse.editorconfig.internal.CompositeReconcilingStrategy;
import org.eclipse.editorconfig.internal.folding.EditorConfigFoldingStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;

public class EditorConfigValidator extends MonoReconciler{

	public EditorConfigValidator() {
		super(create(), false);
	}

	private static IReconcilingStrategy create() {
		CompositeReconcilingStrategy strategy = new CompositeReconcilingStrategy();
		strategy.setReconcilingStrategies(new IReconcilingStrategy[] { new ValidateEditorConfigStrategy(),
				/*new ValidateAppliedOptionsStrategy(preferenceStore, resource), */new EditorConfigFoldingStrategy() });
		return strategy;
	}
	
	public void install(ITextViewer textViewer) {
		super.install(textViewer);
		((IReconciler) getReconcilingStrategy(IDocument.DEFAULT_CONTENT_TYPE)).install(textViewer);
	}

	@Override
	public void uninstall() {
		super.uninstall();
		((IReconciler) getReconcilingStrategy(IDocument.DEFAULT_CONTENT_TYPE)).uninstall();
	}
}
