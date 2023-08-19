#!/usr/bin/env python3

import sys

# Input format: Formatted Date\tSummary\tPrecip Type\tTemperature (C)\t...
# Split input line by tab character and extract Wind Speed (km/h)
for line in sys.stdin:
    data = line.strip().split('\t')
    if len(data) >= 8:
        wind_speed = float(data[6])  # Wind Speed (km/h)
        print(f"wind_speed\t{wind_speed}")
