package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.internal.debug.ui.interpreters.LibraryContentProvider;
import org.eclipse.dltk.internal.debug.ui.interpreters.LibraryStandin;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.LibraryLocation;
import org.eclipse.dltk.tcl.internal.launching.PackagesHelper;
import org.eclipse.dltk.tcl.internal.launching.PackagesHelper.Location;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

public class TclLibraryContentProvider extends LibraryContentProvider {
	Location[] packageLocations;

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Location) {
			return ((Location) parentElement).getPackages();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof Location) {
			return ((Location) element).getPackages().length > 0;
		}
		return false;
	}

	public void setLibraries(LibraryLocation[] libs) {
		super.setLibraries(libs);
		if (this.packageLocations != null) {
			updateColors();
		}
	}

	public void initialize(File file, EnvironmentVariable[] environmentVariables) {
		if (file.exists()) {
			packageLocations = PackagesHelper.getLocations(new Path(file
					.getAbsolutePath()), environmentVariables, super
					.getLibraries());
			// Try to use without specific libraries.
			if (packageLocations.length == 0) {
				packageLocations = PackagesHelper.getLocations(new Path(file
						.getAbsolutePath()), environmentVariables, null);
			}

			updateLibrariesFromPackages();

			this.fViewer.refresh();
			updateColors();
		}
	}

	private void updateLibrariesFromPackages() {
		// We need to remove libraries not pressent in packages.
		if (packageLocations == null) {
			return;
		}
		LibraryLocation[] libraries = super.getLibraries();
		Set set = new HashSet();
		set.addAll(Arrays.asList(this.packageLocations));
		Set result = new HashSet();
		for (int i = 0; i < libraries.length; i++) {
			IPath lPath = libraries[i].getLibraryPath();
			Location l = new Location(lPath.toOSString());
			if (set.contains(l)) {
				result.add(libraries[i]);
			} // We need to add all libs from packages with this

			// prefix.
			for (int j = 0; j < packageLocations.length; j++) {
				Path path = new Path(packageLocations[j].getPath());
				if (lPath.isPrefixOf(path)) {
					result.add(new LibraryLocation(path));
				}
			}
		}

		LibraryLocation[] newLocations = (LibraryLocation[]) result
				.toArray(new LibraryLocation[result.size()]);
		this.setLibraries(newLocations);
	}

	private void updateColors() {
		// We need to set some elements as greyed
		TreeItem[] items = this.fViewer.getTree().getItems();
		for (int i = 0; i < items.length; i++) {
			Color color = PlatformUI.getWorkbench().getDisplay()
					.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
			items[i].setBackground(color);
		}
		for (int j = 0; j < packageLocations.length; j++) {
			Location loc = packageLocations[j];
			boolean exist = find(loc.getPath(), this.fLibraries);

			for (int i = 0; i < items.length; i++) {
				Object data = items[i].getData();
				Color color = PlatformUI.getWorkbench().getDisplay()
						.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
				if (loc.equals(data) && exist) {
					items[i].setBackground(color);
				}
			}
		}
	}

	private boolean find(String path, LibraryStandin[] libraries) {
		Path lpath = new Path(path);
		for (int i = 0; i < libraries.length; i++) {
			if (libraries[i].getSystemLibraryPath().equals(lpath)) {
				return true;
			}
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		if (packageLocations == null) {
			return new Object[0];
		}
		return packageLocations;
	}

	public boolean canRemove(IStructuredSelection selection) {
		boolean all = true;
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (!(element instanceof LibraryStandin)) {
				all = false;
				break;
			}
		}
		return all;
	}

	public boolean canEnable(IStructuredSelection selection) {
		return selection.size() == 1
				&& selection.getFirstElement() instanceof Location;
	}

	public boolean isEnabled(Object lib) {
		if (lib instanceof Location) {
			Location loc = (Location) lib;
			return find(loc.getPath(), this.fLibraries);
		}
		return false;
	}

	public void changeEnabled() {
		ISelection selection = this.fViewer.getSelection();
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			for (Iterator iterator = sel.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if (object instanceof Location) {
					boolean oldState = isEnabled(object);
					boolean newState = !oldState;
					Set set = new HashSet();
					set.addAll(Arrays.asList(getLibraries()));
					IPath path = new Path(((Location) object).getPath());
					if (newState) {
						set.add(new LibraryLocation(path));
					} else {
						set.remove(new LibraryLocation(path));
					}

					LibraryLocation[] newLocations = (LibraryLocation[]) set
							.toArray(new LibraryLocation[set.size()]);
					this.setLibraries(newLocations);
				}
			}
		}
	}
}
