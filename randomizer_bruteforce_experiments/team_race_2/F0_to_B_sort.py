import re

# Sort by the amount of stars

seeds = []
for match in re.finditer(r"\d+, [0-9A-F]+, (Random|Standard) Portals \((\d+),.+", open("F0_to_B_Analyzed.txt").read()):
    seeds.append((match.group(0), int(match.group(2))))

seeds = sorted(seeds, key=lambda x: x[1])[::-1]

open("F0_to_B_sorted.txt", "w").write("\n".join([x[0] for x in seeds]))
