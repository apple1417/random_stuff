import re
from html import unescape
from os import path
from os import system

# Used another script to combine all dialouge files and remove (most) comments
# Because of this I don't need to detect comments or 'inclue' commands
FILE_PATH = "Full.dlg"
# LANG = "Content/Talos/Locales/enu/translation_DLC_01_Road_To_Gehenna.txt"
LANG = "Content/Talos/Locales/enu/translation_All.txt"
TERMINAL_NAME = "Cloud_1_01"
# These can probably be improved/replaced with code, but they work
R_STR = re.compile(r"string (?P<id>\w*?) ?(\[)?(?(2)\[|\")(?:TTRS:(?P<translation>.*?)=)?(?P<default>.*?)(?(2)\]\]|\")\]?", re.I|re.S)
R_STR_VAR = re.compile(r"%t'(?P<id>.+?)'", re.I|re.S)
R_COMMAND = re.compile(r"(?P<type>terminal|player) when ?\((?P<req>.*?)\) ?{(?P<content>.*?})", re.I|re.S)
R_REQ = re.compile(r"(?!(?:and|or|not)(?:\s|\())(\b\w+\b|\".*?\")", re.I)
R_VARS = re.compile(r"(?P<type>set|clear)(?P<local>local)?: ?(?P<var>.+?)\b", re.I)
R_TEXT = re.compile(r"(?P<type>prompt|text|show_text): ?(\[)?(?(2)\[|\")\[?(?:TTRS:(?P<translation>.*?)=)?(?P<default>.*?)(?<!\\)(?(2)\]\]|\")(?:\s*?short: ?(\[)?(?(5)\[|\")\[?(?:TTRS:(?P<s_translation>.*?)=)?(?P<s_default>.*?)(?<!\\)(?(5)\]\]|\"))?", re.I|re.S)
R_IMAGE = re.compile(r"show_image: ?\"(?P<path>.*?)\"", re.I)
R_CODE = re.compile(r"enter_code: ?(?P<var>.+?) (?P<if_true>.+?) (?P<if_false>.+)\b", re.I)
R_OPTIONS = re.compile(r"options: ?{(?P<content>.*?)}", re.I|re.S)
R_T_OPTIONS = re.compile(r"(\[)?(?(1)\[|\")\[?(?:TTRS:(?P<translation>.*?)=)?(?P<default>.*?)(?<!\\)(?(1)\]\]|\") ?(?P<commands>.+?)[\n\r]", re.I|re.S) # Doesn't get short, hopefully that's only under "player when"
R_J_OPTIONS = re.compile(r"file: ?(\[)?(?(1)\[|\")\[?(?:TTRS:(?P<f_translation>.*?)=)?(?P<f_default>.*?)(?<!\\)(?(1)\]\]|\") ?user: ?(\[)?(?(4)\[|\")\[?(?:TTRS:(?P<u_translation>.*?)=)?(?P<u_default>.*?)(?<!\\)(?(4)\]\]|\") ?date: ?(\[)?(?(7)\[|\")\[?(?P<date>.+?)(?<!\\)(?(7)\]\]|\")", re.I|re.S)
R_GOTO = re.compile(r"(?:goto|next): ?(?P<state>.+?)\b", re.I)
R_EXIT = re.compile(r"(?<=\s)(?P<slow>slow)?exit", re.I)
# We can just completly ignore 'notext' and get intended behaviour

TRANSLATION = {}
with open(LANG, encoding="UTF-8") as translation_file:
    for line in translation_file:
        s_line = line.split("=")
        if len(s_line) >= 2:
            TRANSLATION[s_line[0]] = "=".join(s_line[1:]).replace("\\n", "\n")

def get_translation(match, short=False):
    if short:
        message, default = match.group("s_translation"), match.group("s_default")
    else:
        message, default = match.group("translation"), match.group("default")
    if message == "notrans":
        return default
    if message in TRANSLATION:
        return TRANSLATION[message]
    else:
        return default

# Will replace with tkinter eventually
def print_s_vars(text):
    text = re.sub(R_STR_VAR, _replace_s_vars, text)
    text = unescape(text)
    # Other stuff to implement:
    # <span> thing
    # %wn - wait n*100ms - leave as option
    # %p  - terminal file list
    # %o"var" - contents of var
    # %vc - player version number
    print(text)

def _replace_s_vars(match):
    id = match.group("id")
    if id in strings:
        return strings[id]
    else:
        return match.group(0)

def print_buttons(dict):
    # Need to convert to list so that we can get a number as input
    buttons = list(dict.keys())
    for i in range(len(buttons)):
        if not i == "":
            print("[" + str(i + 1) + "] " + buttons[i])
    while True:
        try:
            i = int(input("> "))
            if i - 1 not in range(len(buttons)):
                raise ValueError
        except ValueError:
            print("Invalid Input")
        else:
            return dict[buttons[i - 1]]

def check_actions(text):
    global state, g_vars, vars
    # These aren't mutually exclusive
    # 'vars' has highest priority, as it doesn't return anything
    # 'exit' has lowest, as we want to stay in the terminal if possible
    for i in re.finditer(R_VARS, text):
        if i.group("type").lower() == "set":
            vars.add(i.group("var"))
        elif i.group("type").lower() == "clear":
            vars.discard(i.group("var"))
        if not i.group("local"):
            if i.group("type").lower() == "set":
                g_vars.add(i.group("var"))
            elif i.group("type").lower() == "clear":
                g_vars.discard(i.group("var"))
    goto = re.search(R_GOTO, text)
    if goto:
        vars.discard(state)
        state = goto.group("state")
        vars.add(state)
        return "goto"
    if re.search(R_EXIT, text):
        return "exit"


with open(FILE_PATH, encoding="utf-8") as FILE:
    g_vars = set()
    strings = {}
    last_try = False
    for match in re.finditer(R_STR, FILE.read()):
        strings[match.group("id")] = get_translation(match)
    # Going to be at the end of the file now, so might as well grab size
    FILE_SIZE = FILE.tell()
    while True:
        buffer = ""
        state = "Booting"
        player_options = {}
        # This whole thing is messy but I need a way to let you enable vars that
        #  you only get from gameplay
        print("[1] Continue")
        print("[2] Change Global Vars")
        print("[3] Exit")
        while True:
            try:
                option = int(input("> "))
                if option not in range(1,4):
                    raise ValueError
            except ValueError:
                print("Invalid Input")
            else:
                break
        if option == 2:
            print("Current Global Vars:")
            print(", ".join(g_vars))
            print("\nVars to add (leave empty to continue):")
            new_var = " "
            while new_var != "":
                new_var = input("> ")
                g_vars.add(new_var)
            # Will add "" when you exit, could leave it as it would be removed
            #  by next bit anyway, but it looks messy
            g_vars.discard("")
            print("Current Global Vars:")
            print(", ".join(g_vars))
            print("\nVars to add (leave empty to continue):")
            new_var = " "
            while new_var != "":
                new_var = input("> ")
                g_vars.discard(new_var)
        elif option == 3:
            break
        vars = set((state, "InTerminal_" + TERMINAL_NAME)) | g_vars
        FILE.seek(0)
        system("cls")
        while True:
            buffer += FILE.readline()
            command = re.search(R_COMMAND, buffer)
            if not command:
                continue
            buffer = ""
            # TEST STUFF REMEMBER TO REMOVE
            r = command.group("req")
            if "CLI_Booting" in r or "MiltonAllowed" in r or "InTerminal_Cloud_1_01" in r:
                # print("r", r)
                pass
            # Devs use lots of brackets so just letting python handle it should be fine
            if eval("(" + re.sub(R_REQ, r"('\1' in vars)", command.group("req")) + ")"):
                content = command.group("content")
                result = None
                if command.group("type").lower() == "terminal":
                    text = re.search(R_TEXT, content)
                    if text:
                        print_s_vars(get_translation(text))
                        result = "text"
                    image = re.search(R_IMAGE, content)
                    if image:
                        # Replace with actual image eventually
                        print("IMAGE:", image.group("path"))
                        result = "image"
                    options = re.search(R_OPTIONS, content)
                    if options:
                        options = re.finditer(R_T_OPTIONS, options.group("content"))
                        buttons = {}
                        for i in options:
                            buttons[get_translation(i)] = i.group("commands")
                        result = check_actions(print_buttons(buttons))
                elif command.group("type").lower() == "player":
                    files = re.search(R_J_OPTIONS, content)
                    print(files)
                    if files:
                        # Journal stuff, don't really know how to handle it
                        # Hopefully it never comes up
                        print("")
                        buttons = {}
                        for i in re.finditer(R_J_OPTIONS, content):
                            name = get_translation(i)
                            buttons[name] = name
                            print(name)
                        chosen_file = print_buttons(buttons)
                        print(chosen_file)
                    else:
                        # This is the same as stuff in terminal option groups,
                        #  just one (?) value at a time
                        # Need to compile it over the whole file, so we don't
                        #  instantly ask w/o knowing all options
                        options = re.finditer(R_T_OPTIONS, content)
                        print("o", options)
                        for i in options:
                            print("adding player options")
                            player_options[get_translation(i)] = i.group("commands")
                print("r1", result)
                if not result:
                    result = check_actions(command.group("content"))
                print("r2", result)
                if result == "goto":
                    FILE.seek(0)
                elif result == "exit":
                    break
            # 'FILE.tell()' is weird, sometimes it returns values like '18446744073709568986'
            # This is at consistent locations, so it's possible to hardcode fixes
            # Hopefully won't need to do that though
            if not FILE.tell() == FILE_SIZE:
                continue
            print("At end of file")
            # Loop over the file again because we may have opened up dialoges
            #  that were skipped in the first pass
            if not last_try:
                print("last_try")
                FILE.seek(0)
                last_try = True
                continue
            # If we reach the end of the file and have no player options we have
            #  no choice but to exit
            if not player_options:
                print("No player_options")
                break
            print("player_options")
            # But if we do have player options we can check them and loop over
            #  the file again
            last_try = False
            result = check_actions(print_buttons(player_options))
            player_options = {}
            if result == "exit":
                break
            # We need to do this regardless of if we got a goto command, as 
            #  options might have changed opening new paths
            FILE.seek(0)
print(vars)