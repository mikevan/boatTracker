#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;
use File::Spec;
use Time::Piece;

# Version
# 0.0.1 - MVP


# Check if input files or a directory is provided
my @input_files;
foreach my $arg (@ARGV) {
    if (-d $arg) {  # If the argument is a directory
        opendir(my $dh, $arg) or die "Could not open directory '$arg' $!";
        # Get all .txt files in the directory
        my @files = grep { /\.txt$/ && -f File::Spec->catfile($arg, $_) } readdir($dh);
        closedir $dh;
        
        # Add full path for each file
        push @input_files, map { File::Spec->catfile($arg, $_) } @files;
    } elsif (-f $arg) {  # If the argument is a file
        push @input_files, $arg;
    } else {
        warn "Skipping invalid argument: $arg\n";
    }
}

# Ensure we have files to process
die "No valid files found to process.\n" unless @input_files;

# Define the output KML file path
my $output_file = 'combined_military_exercise_areas.kml';

# Start building the KML structure
my $kml_content = <<'END_HEADER';
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
  <name>Ship Tracker</name>
  <description> A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
  <Style id="thinBlackLine">
    <LineStyle>
     <color>87000000</color>
     <width>1</width>
    </LineStyle>
   </Style>
       <Style id="transparentPolyStyle">
      <PolyStyle>
        <color>80ADD8E6</color> <!-- Semi-transparent blue -->
        <outline>1</outline>
      </PolyStyle>
    </Style>
END_HEADER

# Function to convert date strings to ISO 8601
sub convert_to_iso8601 {
    my ($date_str) = @_;
    # Parse the date in the format "DDHHMM UTC MON"
    if ($date_str =~ /(\d{2})(\d{2})(\d{2}) UTC (\w{3})/) {
        my ($day, $hour, $min, $month_str) = ($1, $2, $3, $4);
        my %months = (
            'JAN' => 1, 'FEB' => 2, 'MAR' => 3, 'APR' => 4,
            'MAY' => 5, 'JUN' => 6, 'JUL' => 7, 'AUG' => 8,
            'SEP' => 9, 'OCT' => 10, 'NOV' => 11, 'DEC' => 12,
        );
        my $month = $months{$month_str};
        my $current_year = localtime->year;
        
        # Create a Time::Piece object
        my $time = Time::Piece->strptime("$current_year-$month-$day $hour:$min", "%Y-%m-%d %H:%M");
        return $time->datetime . "Z";  # Return in ISO 8601 format
    }
    return undef;
}

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

    # Extract the TO and FROM times
    my ($from_time) = $content =~ /FROM\s+(\d{2}\d{4} UTC \w{3})/;
    my ($to_time)   = $content =~ /TO\s+(\d{2}\d{4} UTC \w{3})/;
    
    # Convert times to ISO 8601 format
    my $from_iso = convert_to_iso8601($from_time) if $from_time;
    my $to_iso = convert_to_iso8601($to_time) if $to_time;

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
	$kml_content .= "      <styleUrl>#thinBlackLine</styleUrl>\n";
	$kml_content .= "      <styleUrl>#transparentPolyStyle</styleUrl>\n";
	
    if ($from_iso && $to_iso) {
        $kml_content .= "      <TimeSpan>\n";
        $kml_content .= "        <begin>$from_iso</begin>\n";
        $kml_content .= "        <end>$to_iso</end>\n";
        $kml_content .= "      </TimeSpan>\n";
    }
	
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
