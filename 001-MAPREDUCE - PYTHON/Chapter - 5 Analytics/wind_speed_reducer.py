#!/usr/bin/env python3

import sys

max_wind_speed = 0.000
min_wind_speed = 0.000

# Input format: wind_speed\t<value>
for line in sys.stdin:
    _, value = line.strip().split('\t')
    wind_speed = float(value)
    max_wind_speed = max(max_wind_speed, wind_speed)
    min_wind_speed = min(min_wind_speed, wind_speed)

print(f"Max Wind Speed: {max_wind_speed}")
print(f"Min Wind Speed: {min_wind_speed}")
