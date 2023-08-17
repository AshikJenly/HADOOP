#!/usr/bin/env python3

import sys

for line in sys.stdin:
    # Remove leading and trailing whitespace
    line = line.strip()

    # Split the line into words
    words = line.split()

    # Emit each word with count 1
    for word in words:
        print(f"{word}\t1")
