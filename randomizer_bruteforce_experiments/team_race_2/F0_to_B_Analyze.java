package randomizer_bruteforce_experiments.team_race_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import randomizer_bruteforce.Enums.World;
import randomizer_bruteforce.GeneratorGeneric;
import randomizer_bruteforce.TalosProgress;

class F0_to_B_Analyze {
    public static void main(String[] args) {
        HashMap<World, String[]> MARKERS = new HashMap<World, String[]>();
        MARKERS.put(World.A1, new String[] {"A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star"});
        MARKERS.put(World.A2, new String[] {"A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star"});
        MARKERS.put(World.A3, new String[] {"A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star"});
        MARKERS.put(World.A4, new String[] {"A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS"});
        MARKERS.put(World.A5, new String[] {"A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star"});
        MARKERS.put(World.A6, new String[] {"A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star"});
        MARKERS.put(World.A7, new String[] {"A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole"});
        MARKERS.put(World.A8, new String[] {"A*-JfW", "A*-Nervewrecker", "A*-DDM"});
        MARKERS.put(World.ADEVISLAND, new String[0]);
        MARKERS.put(World.B1, new String[] {"B1-WtaD", "B1-Third Wheel", "B1-Over the Fence", "B1-RoD", "B1-SaaS", "B1-Star"});
        MARKERS.put(World.B2, new String[] {"B2-Tomb", "B2-Star", "B2-MotM", "B2-Moonshot", "B2-Higher Ground"});
        MARKERS.put(World.B3, new String[] {"B3-Blown Away", "B3-Star", "B3-Sunshot", "B3-Eagle's Nest", "B3-Woosh"});
        MARKERS.put(World.B4, new String[] {"B4-Self Help", "B4-Double-Plate", "B4-TRA", "B4-TRA Star", "B4-RPS", "B4-ABUH", "B4-WAtC", "B4-Sphinx Star"});
        MARKERS.put(World.B5, new String[] {"B5-SES", "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-Chambers", "B5-Obelisk Star"});
        MARKERS.put(World.B6, new String[] {"B6-Crisscross", "B6-JDaW", "B6-Egyptian Arcade"});
        MARKERS.put(World.B7, new String[] {"B7-AFaF", "B7-WLJ", "B7-BSbS", "B7-BSbS Star", "B7-BLoM", "B7-Star"});
        MARKERS.put(World.B8, new String[] {"B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"});
        MARKERS.put(World.C1, new String[] {"C1-Conservatory", "C1-MIA", "C1-Labyrinth", "C1-Blowback", "C1-Star",});
        MARKERS.put(World.C2, new String[] {"C2-ADaaF", "C2-Star", "C2-Rapunzel", "C2-Cemetery", "C2-Short Wall",});
        MARKERS.put(World.C3, new String[] {"C3-Three Connectors", "C3-Jammer Quarantine", "C3-BSLS", "C3-Weathertop", "C3-Star",});
        MARKERS.put(World.C4, new String[] {"C4-Armory", "C4-Oubliette", "C4-Oubliette Star", "C4-Stables", "C4-Throne Room", "C4-Throne Room Star",});
        MARKERS.put(World.C5, new String[] {"C5-Time Flies", "C5-Time Flies Star", "C5-Time Crawls", "C5-Dumbwaiter", "C5-Dumbwaiter Star", "C5-UCaJ", "C5-UCAJ Star",});
        MARKERS.put(World.C6, new String[] {"C6-Seven Doors", "C6-Star", "C6-Circumlocution", "C6-Two Way Street",});
        MARKERS.put(World.C7, new String[] {"C7-Carrier Pigeons", "C7-DMS", "C7-Star", "C7-Prison Break", "C7-Crisscross",});
        MARKERS.put(World.C8, new String[] {"C*-Unreachable Garden", "C*-Nexus", "C*-Cobweb"});
        MARKERS.put(World.CMESSENGER, new String[] {"CM-Star"});


        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("randomizer_bruteforce_experiments/team_race_2/F0_to_B.txt"));
        } catch (IOException e) {
            System.err.println("Could not access file");
            return;
        }

        HashMap<String, Integer> options = new HashMap<String, Integer>();
        options.put("Randomizer_Mode", 3);
        GeneratorGeneric standardGenerator = new GeneratorGeneric(options);
        options.put("Randomizer_Portals", 1);
        GeneratorGeneric portalsGenerator = new GeneratorGeneric(options);

        Pattern p = Pattern.compile("(\\d+), [0-9A-F]+, ([SR]).+");
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                Matcher m = p.matcher(line);
                if (!m.find()) continue;
                long seed = Long.parseLong(m.group(1));
                boolean portals = m.group(2).equals("R");

                TalosProgress progress = portals ? portalsGenerator.generate(seed)
                                                 : standardGenerator.generate(seed);

                // Work out which markers are in which hub
                ArrayList<String> A_MARKERS = new ArrayList<String>();
                ArrayList<String> B_MARKERS = new ArrayList<String>();
                ArrayList<String> C_MARKERS = new ArrayList<String>();
                for (World w : World.values()) {
                    int worldIndex = progress.getVar(w.toString());
                    // Don't want to include the star worlds
                    if (worldIndex <= 9 && worldIndex != 8) {
                        A_MARKERS.addAll(Arrays.asList(MARKERS.get(w)));
                    } else if (worldIndex > 9 && worldIndex < 17) {
                        B_MARKERS.addAll(Arrays.asList(MARKERS.get(w)));
                    } else if (worldIndex >= 18 && worldIndex != 25) {
                        C_MARKERS.addAll(Arrays.asList(MARKERS.get(w)));
                    }
                }

                // One of thge greys is guarenteed to be F3
                int E = 1;
                int DI = 0;
                int DJ = 0;
                int DL = 0;
                int DT = 0;
                int DZ = 0;
                int NL = 0;
                int NZ = 0;

                // Count up each type of sigil
                for (String marker : A_MARKERS) {
                    String sigil = TalosProgress.TETROS[progress.getVar(marker)];
                    if (sigil.charAt(0) == 'E') E++;
                    else if (sigil.startsWith("DI")) DI++;
                    else if (sigil.startsWith("DJ")) DJ++;
                    else if (sigil.startsWith("DL")) DL++;
                    else if (sigil.startsWith("DT")) DT++;
                    else if (sigil.startsWith("DZ")) DZ++;
                    else if (sigil.startsWith("NL")) NL++;
                    else if (sigil.startsWith("NZ")) NZ++;
                }
                for (String marker : B_MARKERS) {
                    String sigil = TalosProgress.TETROS[progress.getVar(marker)];
                    if (sigil.charAt(0) == 'E') E++;
                }
                for (String marker : C_MARKERS) {
                    String sigil = TalosProgress.TETROS[progress.getVar(marker)];
                    if (sigil.charAt(0) == 'E') E++;
                    else if (sigil.startsWith("NL")) NL++;
                    else if (sigil.startsWith("NZ")) NZ++;
                }

                if (E < 9 || NL < 2 || NZ < 2) continue;
                String output = String.format("%s (%d, %d %d %d %d %d, %d %d)",
                                              line,
                                              DI + DJ + DL + DT + DZ + NL + NZ,
                                              DI, DJ, DL, DT, DZ,
                                              NL, NZ);
                System.out.println(output);
                try {
                    Files.write(Paths.get("randomizer_bruteforce_experiments/team_race_2/F0_to_B_Analyzed.txt"),
                                (output + "\n").getBytes(),
                                StandardOpenOption.APPEND);
                } catch (IOException e) {System.err.println("Error");}
            }

        } catch (IOException e) {
            System.err.println("Error while reading file");
        }
    }
}
