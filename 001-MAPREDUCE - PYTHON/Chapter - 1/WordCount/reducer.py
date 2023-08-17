#!/usr/bin/env python3

import sys

current_word = None
current_count = 0

for line in sys.stdin:
    # Split the line into word and count
    word, count = line.strip().split('\t')

    # Convert count to an integer
    count = int(count)

    # If the current word is the same as the previous word, increment its count
    if current_word == word:
        current_count += count
    else:
        # If there's a previous word, print its word and count
        if current_word:
            print(f"{current_word}\t{current_count}")

        # Set the current word and reset the count
        current_word = word
        current_count = count

# Print the word and count for the last word
if current_word == word:
    print(f"{current_word}\t{current_count}")
