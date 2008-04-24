###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################
proc a::b::alfa { } {}
proc a::d::alfa { } {}
proc b::d::alfa { } {}
proc alfa {} {}
proc a::alfa {} {}
proc a::beta {} {}

#References
a::b::alfa 
a::d::alfa
b::d::alfa
alfa
a::alfa
a::beta

