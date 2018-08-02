import re

seeds = []
# maxCount = -1
for match in re.finditer(r"(\d+) \(.+?(\d+):.+?(\d+):.+?(\d+):.+", open("MoodyPrinciple.txt").read()):
    seed = match.group(1)
    """
    if seed == seed[::-1]:
        print(seed + " palindrome")
    """

    total = int(match.group(2)) + int(match.group(3)) + int(match.group(4))
    """
    if total > maxCount:
        maxCount = total
    """
    if match.group(2) == "19":
        seeds.append(match.group(0))

open("tmp", "w").write("\n".join(seeds))
# print(maxCount)
