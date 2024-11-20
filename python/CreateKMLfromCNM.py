import os
import re
import datetime

# Version
# 0.0.1 - MVP

# Check if input files or a directory is provided
input_files = []
for arg in os.sys.argv[1:]:
    if os.path.isdir(arg):  # If the argument is a directory
        # Get all .txt files in the directory
        input_files.extend(
            [os.path.join(arg, file) for file in os.listdir(arg) if file.endswith(".txt")]
        )
    elif os.path.isfile(arg):  # If the argument is a file
        input_files.append(arg)
    else:
        print(f"Skipping invalid argument: {arg}")

# Ensure we have files to process
if not input_files:
    raise FileNotFoundError("No valid files found to process.")

# Define the output KML file path
output_file = "combined_military_exercise_areas.kml"

# Start building the KML structure
kml_content = """<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>
  <name>Ship Tracker</name>
  <description>A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>
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
"""

# Function to convert date strings to ISO 8601
def convert_to_iso8601(date_str):
    # Parse the date in the format "DDHHMM UTC MON"
    match = re.match(r"(\d{2})(\d{2})(\d{2}) UTC (\w{3})", date_str)
    if match:
        day, hour, minute, month_str = match.groups()
        months = {
            "JAN": 1,
            "FEB": 2,
            "MAR": 3,
            "APR": 4,
            "MAY": 5,
            "JUN": 6,
            "JUL": 7,
            "AUG": 8,
            "SEP": 9,
            "OCT": 10,
            "NOV": 11,
            "DEC": 12,
        }
        month = months[month_str]
        current_year = datetime.datetime.now().year
        time = datetime.datetime(current_year, month, int(day), int(hour), int(minute))
        return time.isoformat() + "Z"  # Return in ISO 8601 format
    return None

# Process each input file
for input_file in input_files:
    # Open and read the content of the file
    with open(input_file, "r") as f:
        content = f.read()

    # Extract the placemark name
    placemark_name_match = re.search(r"^(.*?)(?=\s+IN\s+AREA\s+BOUNDED)", content, re.MULTILINE)
    placemark_name = placemark_name_match.group(1) if placemark_name_match else "Military Exercise Area"

    # Extract the description
    description_match = re.search(r"(?:UTC NOV\.\s+)(.*?)(?=\s*$)", content, re.DOTALL)
    description = description_match.group(1) if description_match else "No description provided."

    # Extract the TO and FROM times
    from_time_match = re.search(r"FROM\s+(\d{2}\d{4} UTC \w{3})", content)
    to_time_match = re.search(r"TO\s+(\d{2}\d{4} UTC \w{3})", content)
    from_time = convert_to_iso8601(from_time_match.group(1)) if from_time_match else None
    to_time = convert_to_iso8601(to_time_match.group(1)) if to_time_match else None

    # Extract coordinates using regex
    coordinates = []
    for match in re.finditer(r"(\d+)-(\d+\.\d+)([NS])\s+(\d+)-(\d+\.\d+)([EW])", content):
        lat_deg, lat_min, lat_dir, lon_deg, lon_min, lon_dir = match.groups()
        lat = int(lat_deg) + float(lat_min) / 60
        lon = int(lon_deg) + float(lon_min) / 60
        if lat_dir == "S":
            lat = -lat
        if lon_dir == "W":
            lon = -lon
        coordinates.append((lat, lon))

    # Check if four coordinates were found
    if len(coordinates) != 4:
        raise ValueError(f"Expected 4 coordinates in file '{input_file}', but found {len(coordinates)}")

    # Add the Placemark entry for the current file to the KML content
    kml_content += f"    <Placemark>\n"
    kml_content += f"      <name>{placemark_name}</name>\n"
    kml_content += f"      <description>{description}</description>\n"
    kml_content += f"      <styleUrl>#thinBlackLine</styleUrl>\n"
    kml_content += f"      <styleUrl>#transparentPolyStyle</styleUrl>\n"

    if from_time and to_time:
        kml_content += f"      <TimeSpan>\n"
        kml_content += f"        <begin>{from_time}</begin>\n"
        kml_content += f"        <end>{to_time}</end>\n"
        kml_content += f"      </TimeSpan>\n"

    kml_content += f"      <Polygon>\n"
    kml_content += f"        <outerBoundaryIs>\n"
    kml_content += f"          <LinearRing>\n"
    kml_content += f"            <coordinates>\n"
    for lat, lon in coordinates:
        kml_content += f"              {lon:.6f},{lat:.6f},0\n"
    kml_content += f"            </coordinates>\n"
    kml_content += f"          </LinearRing>\n"
    kml_content += f"        </outerBoundaryIs>\n"
    kml_content += f"      </Polygon>\n"
    kml_content += f"    </Placemark>\n"

# Finish the KML structure
kml_content += """  </Document>
</kml>
"""

# Write the full KML content to the output file
with open(output_file, "w") as f:
    f.write(kml_content)

print(f"Combined KML file '{output_file}' created successfully.")
