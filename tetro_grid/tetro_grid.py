from itertools import combinations

def recenter(grid):
    while grid[0] == [0, 0, 0, 0]:
        grid.append(grid.pop(0))
    while grid[0][0] == 0 and grid[1][0] == 0 and grid[2][0] == 0 and grid[3][0] == 0:
        grid[0].append(grid[0].pop(0))
        grid[1].append(grid[1].pop(0))
        grid[2].append(grid[2].pop(0))
        grid[3].append(grid[3].pop(0))
    return grid

allCombos = []
for combo in combinations(range(16), 4):
    grid = [[0, 0, 0, 0] for _ in range(4)]

    for index in combo:
        grid[int(index/4)][index % 4] = 1

    grid = recenter(grid)

    if grid in allCombos:
        continue

    baseRotated = grid
    for _ in range(3):
        baseRotated = list(zip(*baseRotated[::-1]))
        rotated = []
        for i in range(4):
            rotated.append(list(baseRotated[i]))
        rotated = recenter(rotated)

        if rotated in allCombos:
            break
    else:
        allCombos.append(grid)

print(len(allCombos))
print()
for combo in allCombos:
    for row in combo:
        out = ""
        for char in row:
            out += "###" if char == 1 else "..."
        print(out)
        print(out)
    print()
