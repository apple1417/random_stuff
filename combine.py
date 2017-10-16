import re

# Comment detection isn't 100%, but I can't risk removing a valid line, and in
#  practice it doesn't leave any lines that would be detected anyway
R_COMMENT = re.compile(r"\s*[^\"]#.*[^\"]$", re.I|re.M)
R_INCLUDE = re.compile(r"include \"(?P<path>.*?)\"", re.I)

def include_file(match):
    # Think it's better behaviour to let this fail if you haven't extracted all
    #  the files you need
    new_file = open(match.group("path"), encoding="utf-8").read()
    new_file = re.sub(R_COMMENT, "", new_file)
    return new_file

main_file = open("Content/Talos/Databases/ComputerTerminalDialogs/AllDialogs.dlg", encoding="UTF-8").read()
main_file = re.sub(R_COMMENT, "", main_file)
# Technically this will get stuck if two files include each other, but I think
#  the game would too
while re.search(R_INCLUDE, main_file):
    main_file = re.sub(R_INCLUDE, include_file, main_file)

open("Full.dlg", "w", encoding="utf-8").write(main_file)