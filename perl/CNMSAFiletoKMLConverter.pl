#!/usr/bin/perl
use strict;
use warnings;

# Check if an input file was provided
my $input_file = shift @ARGV or die "Usage: $0 input.txt\n";

# Define the output KML file path
my $output_file = 'military_exercise_area.kml';

# Open the input file and read the content
open(my $in, '<', $input_file) or die "Could not open file '$input_file' $!";
my $content = join '', <$in>;
close $in;

# Extract the placemark name (e.g., "HN136/24 SOUTH CHINA SEA MILITARY EXERCISES")
my ($placemark_name) = $content =~ /^(.*?)(?=\s+IN\s+AREA\s+BOUNDED)/;
$placemark_name ||= "Military Exercise Area";  # Default if not found

# Extract coordinates using regex
my @coordinates;
while ($content =~ /(\d+)-(\d+\.\d+)([NS])\s+(\d+)-(\d+\.\d+)([EW])/g) {
    my ($lat_deg, $lat_min, $lat_dir, $lon_deg, $lon_min, $lon_dir) = ($1, $2, $3, $4, $5, $6);
    my $lat = $lat_deg + $lat_min / 60;
    my $lon = $lon_deg + $lon_min / 60;
    $lat *= -1 if $lat_dir eq 'S';
    $lon *= -1 if $lon_dir eq 'W';
    push @coordinates, [$lat, $lon];
}

# Check if four coordinates were found
die "Expected 4 coordinates, but found " . scalar(@coordinates) unless scalar(@coordinates) == 4;

# Open the KML file for writing
open(my $fh, '>', $output_file) or die "Could not open file '$output_file' $!";

# Start writing the KML structure
print $fh <<END_HEADER;
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
    <name>$placemark_name</name>
    <Placemark>
      <name>$placemark_name</name>
      <description>Military exercises in area bounded by the coordinates from 052200 UTC Nov to 061200 UTC Nov. Entering prohibited. Hainan MSA China.</description>
      <Polygon>
        <outerBoundaryIs>
          <LinearRing>
            <coordinates>
END_HEADER

# Add the coordinates to the KML
foreach my $coord (@coordinates) {
    my ($lat, $lon) = @$coord;
    print $fh "              $lon,$lat,0\n";
}

# Finish the KML structure
print $fh <<'END_FOOTER';
            </coordinates>
          </LinearRing>
        </outerBoundaryIs>
      </Polygon>
    </Placemark>
  </Document>
</kml>
END_FOOTER

# Close the file
close $fh;

print "KML file '$output_file' created successfully.\n";
