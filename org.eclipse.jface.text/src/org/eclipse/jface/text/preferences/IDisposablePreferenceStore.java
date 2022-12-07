package org.eclipse.jface.text.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

public interface IDisposablePreferenceStore extends IPreferenceStore {

	void dispose();
}
