#!/usr/bin/perl
use strict;
use warnings;

# Check if input files are provided
my @input_files = @ARGV;
die "Usage: $0 file1.txt file2.txt ...\n" unless @input_files;

# Define the output KML file path
my $output_file = 'combined_military_exercise_areas.kml';

# Start building the KML structure
my $kml_content = <<'END_HEADER';
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
END_HEADER

# Process each input file
foreach my $input_file (@input_files) {
    # Open and read the content of the file
    open(my $in, '<', $input_file) or die "Could not open file '$input_file' $!";
    my $content = join '', <$in>;
    close $in;

    # Extract the placemark name
    my ($placemark_name) = $content =~ /^(.*?)(?=\s+IN\s+AREA\s+BOUNDED)/;
    $placemark_name ||= "Military Exercise Area";  # Default if not found

    # Extract the description
    my ($description) = $content =~ /(?:UTC NOV\.\s+)(.*?)(?=\s*$)/s;
    $description ||= "No description provided.";  # Default if not found

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
    die "Expected 4 coordinates in file '$input_file', but found " . scalar(@coordinates) unless scalar(@coordinates) == 4;

    # Add the Placemark entry for the current file to the KML content
    $kml_content .= "    <Placemark>\n";
    $kml_content .= "      <name>$placemark_name</name>\n";
    $kml_content .= "      <description>$description</description>\n";
    $kml_content .= "      <Polygon>\n";
    $kml_content .= "        <outerBoundaryIs>\n";
    $kml_content .= "          <LinearRing>\n";
    $kml_content .= "            <coordinates>\n";
    
    # Add the coordinates for the current Placemark
    foreach my $coord (@coordinates) {
        my ($lat, $lon) = @$coord;
        $kml_content .= sprintf("              %.6f,%.6f,0\n", $lon, $lat);
    }
    
    $kml_content .= "            </coordinates>\n";
    $kml_content .= "          </LinearRing>\n";
    $kml_content .= "        </outerBoundaryIs>\n";
    $kml_content .= "      </Polygon>\n";
    $kml_content .= "    </Placemark>\n";
}

# Finish the KML structure
$kml_content .= <<'END_FOOTER';
  </Document>
</kml>
END_FOOTER

# Write the full KML content to the output file
open(my $out, '>', $output_file) or die "Could not open file '$output_file' $!";
print $out $kml_content;
close $out;

print "Combined KML file '$output_file' created successfully.\n";
