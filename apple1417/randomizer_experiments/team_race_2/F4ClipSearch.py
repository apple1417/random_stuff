import enchant
import re

eng1 = enchant.Dict("en_US")
eng2 = enchant.Dict("en_GB")
eng3 = enchant.Dict("en_AU")

seeds = []
maxCount = -1
for match in re.finditer(r"(\d+) \((\d+):.+?(\d+):.+", open("F4ClipRound2.txt").read()):
    seed = match.group(1)
    if seed == seed[::-1]:
        print(seed + " palindrome")
    seed = int(seed)

    if int(match.group(2)) + int(match.group(3)) > maxCount:
        maxCount = int(match.group(2)) + int(match.group(3))

    if int(match.group(2)) + int(match.group(3)) >= 54:
        print(seed)

    seedStr = chr((seed & 0xFF000000) >> 24) + chr((seed & 0x00FF0000) >> 16) + chr((seed & 0x0000FF00) >> 8) + chr(seed & 0x000000FF)
    if (eng1.check(seedStr) or eng2.check(seedStr) or eng3.check(seedStr)):
        print("word:", seedStr, seed)

print(maxCount)
