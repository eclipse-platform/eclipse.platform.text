package org.eclipse.core.resources.proposed;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.IPreferenceStore;

public interface IPreferenceStoreProvider {

	IPreferenceStore getPreferenceStore(IResource resource);
}
