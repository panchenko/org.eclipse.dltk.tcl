/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CombinedSet<E> extends AbstractSet<E> {

	private final Set<E>[] children;

	public CombinedSet(Set<E>... children) {
		this.children = children;
	}

	@Override
	public boolean contains(Object o) {
		for (Set<E> child : children) {
			if (child.contains(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		if (children.length == 0) {
			return Collections.<E> emptySet().iterator();
		} else if (children.length == 1) {
			return children[0].iterator();
		} else {
			Set<E> temp = new HashSet<E>();
			for (Set<E> child : children) {
				temp.addAll(child);
			}
			return temp.iterator();
		}
	}

	@Override
	public int size() {
		if (children.length == 0) {
			return 0;
		} else if (children.length == 1) {
			return children[0].size();
		} else if (children.length == 2) {
			int size = children[0].size();
			for (E e : children[1]) {
				if (!children[0].contains(e)) {
					++size;
				}
			}
			return size;
		} else {
			Set<E> temp = new HashSet<E>();
			for (Set<E> child : children) {
				temp.addAll(child);
			}
			return temp.size();
		}
	}
}
