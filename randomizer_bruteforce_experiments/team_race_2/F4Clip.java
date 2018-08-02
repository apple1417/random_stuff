package randomizer_bruteforce_experiments.team_race_2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import randomizer_bruteforce.Enums.World;
import randomizer_bruteforce.GeneratorGeneric;
import randomizer_bruteforce.SeedScheduler;
import randomizer_bruteforce.TalosProgress;

class F4Clip {
    private static HashMap<String, Integer> options;
    private static HashMap<World, String[]> MARKERS;
    public static void main(String[] args) {
        int min = 0;
        int max = 0x7FFFFFFF;
        try {
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        MARKERS = new HashMap<World, String[]>();
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

        options = new HashMap<String, Integer>();
        options.put("Randomizer_Portals", 1);
        SeedScheduler s = new SeedScheduler(() -> new GeneratorGeneric(options),
                                            (TalosProgress p) -> evaluate(p));
        s.start(min, max);
    }

    private static void evaluate(TalosProgress progress) {
        // We want to start in B or C
        if (progress.getVar("A1") <= 9) return;
        /*
          Work out which markers are in which hub
          Turns out this doesn't work quite right
        */
        ArrayList<String> B_C_MARKERS = new ArrayList<String>();
        for (World w : World.values()) {
            int worldIndex = progress.getVar(w.toString());
            // Don't want to include the star worlds
            if (worldIndex >= 10 && worldIndex != 17 && worldIndex != 25) {
                B_C_MARKERS.addAll(Arrays.asList(MARKERS.get(w)));
            }
        }

        int MI = 0;
        int MJ = 0;
        int ML = 0;
        int MO = 0;
        int MS = 0;
        int MT = 0;
        int MZ = 0;
        int NI = 0;
        int NJ = 0;
        int NL = 0;
        int NO = 0;
        int NS = 0;
        int NT = 0;
        int NZ = 0;
        for (String marker: B_C_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
            if (sigil.charAt(0) == 'M') {
                switch (sigil.charAt(1)) {
                    case 'I': MI++; break;
                    case 'J': MJ++; break;
                    case 'L': ML++; break;
                    case 'O': MO++; break;
                    case 'S': MS++; break;
                    case 'T': MT++; break;
                    case 'Z': MZ++; break;
                }
            } else if (sigil.charAt(0) == 'N') {
                switch (sigil.charAt(1)) {
                    case 'I': NI++; break;
                    case 'J': NJ++; break;
                    case 'L': NL++; break;
                    case 'O': NO++; break;
                    case 'S': NS++; break;
                    case 'T': NT++; break;
                    case 'Z': NZ++; break;
                }
            }
        }

        if (NI >= 4 && NJ >= 2 && NL >= 4 && NO >= 2 && NS >= 3 && NT >= 4 && NZ >= 2
            && MI >= 1 && MJ >= 1 && ML >= 1 && MO >= 1 && MS >= 1 && MT >= 4 && MZ >= 2
            && NI + NJ + NL + NO + NS + NT + NZ >= 32
            && MI + MJ + ML + MO + MS + MT + MZ >= 19) {
            String output = String.format("%d (%d: %d %d %d %d %d %d %d, %d: %d %d %d %d %d %d %d)",
                                          progress.getVar("Randomizer_Seed"),
                                          NI + NJ + NL + NO + NS + NT + NZ,
                                          NI, NJ, NL, NO, NS, NT, NZ,
                                          MI + MJ + ML + MO + MS + MT + MZ,
                                          MI, MJ, ML, MO, MS, MT, MZ);
            System.out.println(output);
            try {
                Files.write(Paths.get("randomizer_bruteforce_experiments/team_race_2/F4Clip.txt"),
                            (output + "\n").getBytes(),
                            StandardOpenOption.APPEND);
            } catch (IOException e) {}
        }
    }
}
