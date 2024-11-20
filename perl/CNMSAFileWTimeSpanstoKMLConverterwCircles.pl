#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;
use File::Spec;
use Time::Piece;
use Math::Trig qw(deg2rad rad2deg pi);

# Version
# 0.0.2 - Added circle generation for 6-mile radius

# Earth's radius in miles
my $earth_radius = 3958.8;

# Function to generate a circle as KML coordinates
sub generate_circle_coordinates {
    my ($lat, $lon, $radius_miles, $num_points) = @_;
    my @coordinates;

    for (my $i = 0; $i <= $num_points; $i++) {
        my $angle = 2 * pi * $i / $num_points;
        my $delta = $radius_miles / $earth_radius;

        # Convert to radians
        my $lat_rad = deg2rad($lat);
        my $lon_rad = deg2rad($lon);

        # Calculate new point
        my $new_lat = rad2deg(asin(sin($lat_rad) * cos($delta) + cos($lat_rad) * sin($delta) * cos($angle)));
        my $new_lon = rad2deg($lon_rad + atan2(sin($angle) * sin($delta) * cos($lat_rad), cos($delta) - sin($lat_rad) * sin(deg2rad($new_lat))));

        push @coordinates, sprintf("%.6f,%.6f,0", $new_lon, $new_lat);
    }

    return join "\n", @coordinates;
}

# Function to convert date strings to ISO 8601
sub convert_to_iso8601 {
    my ($date_str) = @_;
    if ($date_str =~ /(\d{2})(\d{2})(\d{2}) UTC (\w{3})/) {
        my ($day, $hour, $min, $month_str) = ($1, $2, $3, $4);
        my %months = (
            'JAN' => 1, 'FEB' => 2, 'MAR' => 3, 'APR' => 4,
            'MAY' => 5, 'JUN' => 6, 'JUL' => 7, 'AUG' => 8,
            'SEP' => 9, 'OCT' => 10, 'NOV' => 11, 'DEC' => 12,
        );
        my $month = $months{$month_str};
        my $current_year = localtime->year;

        my $time = Time::Piece->strptime("$current_year-$month-$day $hour:$min", "%Y-%m-%d %H:%M");
        return $time->datetime . "Z";
    }
    return undef;
}

# Check if input files or a directory is provided
my @input_files;
foreach my $arg (@ARGV) {
    if (-d $arg) {
        opendir(my $dh, $arg) or die "Could not open directory '$arg' $!";
        my @files = grep { /\.txt$/ && -f File::Spec->catfile($arg, $_) } readdir($dh);
        closedir $dh;
        push @input_files, map { File::Spec->catfile($arg, $_) } @files;
    } elsif (-f $arg) {
        push @input_files, $arg;
    } else {
        warn "Skipping invalid argument: $arg\n";
    }
}

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
        <color>80ADD8E6</color>
        <outline>1</outline>
      </PolyStyle>
    </Style>
END_HEADER

foreach my $input_file (@input_files) {
    open(my $in, '<', $input_file) or die "Could not open file '$input_file' $!";
    my $content = join '', <$in>;
    close $in;

    # Look for "6 miles radius of" pattern
    if ($content =~ /6 miles radius of (\d+)-(\d+\.\d+)([NS]) (\d+)-(\d+\.\d+)([EW])/) {
        my ($lat_deg, $lat_min, $lat_dir, $lon_deg, $lon_min, $lon_dir) = ($1, $2, $3, $4, $5, $6);

        # Convert to decimal degrees
        my $lat = $lat_deg + $lat_min / 60;
        my $lon = $lon_deg + $lon_min / 60;
        $lat *= -1 if $lat_dir eq 'S';
        $lon *= -1 if $lon_dir eq 'W';

        # Generate circle coordinates
        my $circle_coordinates = generate_circle_coordinates($lat, $lon, 6, 100);

        # Add the circle to the KML
        $kml_content .= "    <Placemark>\n";
        $kml_content .= "      <name>6 Mile Radius</name>\n";
        $kml_content .= "      <styleUrl>#transparentPolyStyle</styleUrl>\n";
        $kml_content .= "      <Polygon>\n";
        $kml_content .= "        <outerBoundaryIs>\n";
        $kml_content .= "          <LinearRing>\n";
        $kml_content .= "            <coordinates>\n";
        $kml_content .= $circle_coordinates . "\n";
        $kml_content .= "            </coordinates>\n";
        $kml_content .= "          </LinearRing>\n";
        $kml_content .= "        </outerBoundaryIs>\n";
        $kml_content .= "      </Polygon>\n";
        $kml_content .= "    </Placemark>\n";
    }
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
