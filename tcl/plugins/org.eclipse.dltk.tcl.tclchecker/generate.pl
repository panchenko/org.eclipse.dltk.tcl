#<xmi:XMI xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:org.eclipse.dltk.tcl.tclchecker="http://www.eclipse.org/dltk/tcl/tclchecker.ecore">
#  <org.eclipse.dltk.tcl.tclchecker:CheckerMessage messageId="argAfterArgs" explanation="An argument has been specified after the args keyword in a procedure argument list. The args argument is treated like a normal parameter and does not collect the remaining parameters into a single list."/>
#  <org.eclipse.dltk.tcl.tclchecker:CheckerMessage messageId="argsNotDefault" explanation="The args keyword cannot be initialized to contain a default value. Although the Tcl interpreter does not complain about this usage, the default value is ignored."/>
#</xmi:XMI>

use IO::File;

my %CATEGORIES = (
  'error' => 'ERROR',
  'upgrade error' => 'UPGRADE_ERROR',
  'warning' => 'WARNING',
  'non-portable warning' => 'NON_PORTABLE_WARNING',
  'upgrade warning' => 'UPGRADE_WARNING',
  'performance warning' => 'PERFORMANCE_WARNING',
  'usage warning' => 'USAGE_WARNING'
);

sub convertCategory($) {
  my $type = lc shift;
  if (exists $CATEGORIES{$type}) {
    return $CATEGORIES{$type};
  }
  else {
    print "ERROR '$type'\n";
    return "ERROR";
  }
}

sub encode($) {
  my $value = shift;
  $value =~ s/&/&amp;/g;
  $value =~ s/"/&quot;/g;
  $value =~ s/</&lt;/g;
  $value =~ s/>/&gt;/g;
  $value =~ s/\\n/\n/g;
  return $value;
}

sub main() {
  my $f = new IO::File("tclchecker.properties", "r");
  die $! unless $f;

  my %messages;
  while (<$f>) {
    chomp;
    s/^\s+//;
    s/\s+$//;
    next if /^#/;
    next if /^$/;
    if (/^(.+)_(.+?)=(.*)$/) {
      my $messageId = $1;
      my $field = $2;
      my $value = $3;
      if ($field eq 'type' || $field eq 'explanation') {
        if (!exists $messages{$messageId}) {
          $messages{$messageId} = {};
        }
        if ($field eq 'type') {
          $messages{$messageId}->{CATEGORY} = convertCategory($value);
        }
        elsif ($field eq 'explanation') {
          $messages{$messageId}->{EXPLANATION} = $value;
        }
      }
      else {
        print "ERROR '$_'\n";
      }
    }
    else {
      print "WARN '$_'\n";
    }
  }
  my $out = new IO::File("resources/tclchecker-messages.xml", "w");
  print $out '<?xml version="1.0" encoding="ISO-8859-1"?>'."\n";
  print $out '<xmi:XMI xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:org.eclipse.dltk.tcl.tclchecker="http://www.eclipse.org/dltk/tcl/tclchecker.ecore">'."\n";
  my %groups;
  foreach (keys %messages) {
    if (/^(.+)::/) {
      $groups{$1} = 1;
    }
    else {
      $groups{'Generic'} = 1;
    }
  }
  foreach (sort keys %groups) {
    my $group = $_;
    print $out '<org.eclipse.dltk.tcl.tclchecker:MessageGroup id="'.$group.'" name="'.$group.'">'."\n";
    foreach (sort keys %messages) {
      my $msgGroup;
      if (/^(.+)::/) {
        $msgGroup = $1;
      }
      else {
        $msgGroup = 'Generic';
      }
      if ($group eq $msgGroup) {
        my $msg = $messages{$_};
        print $out '<messages messageId="'.$_.'" explanation="'.encode($msg->{EXPLANATION}).'" category="'.$msg->{CATEGORY}.'"/>'."\n";
      }
    }
    print $out '</org.eclipse.dltk.tcl.tclchecker:MessageGroup>'."\n";
  }
  print $out '</xmi:XMI>'."\n";
  $out->close();
}

main();
