package apple1417.randomizer_experiments.team_race_2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import apple1417.randomizer.Enums.World;
import apple1417.randomizer.GeneratorGeneric;
import apple1417.randomizer.SeedScheduler;
import apple1417.randomizer.TalosProgress;

class F0_to_B {
    private static HashMap<String, Integer> options;
    private static HashMap<World, String[]> MARKERS;
    public static void main(String[] args) {
        int min = 0;
        boolean skip = false;
        try {
            min = Integer.parseInt(args[0]);
            skip = args.length > 1;
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

        // We need seperate sets of options, but the eval function is made to work with both
        options = new HashMap<String, Integer>();
        options.put("Randomizer_Mode", 3);
        SeedScheduler s = new SeedScheduler(() -> new GeneratorGeneric(options),
                                            (TalosProgress p) -> evaluate(p));
        if (!skip) {
            s.start(min, 0x7FFFFFFF);
            min = 0;
        }
        // We can use the same scheduler, just need to update the options used to create generators
        options.put("Randomizer_Portals", 1);
        s.start(min, 0x7FFFFFFF);
    }

    private static void evaluate(TalosProgress progress) {
        /*
        A1, A2, A3, A4, A5, A6, A7, A8, ADevIsland,
        B1, B2, B3, B4, B5, B6, B7, B8,
        C1, C2, C3, C4, C5, C6, C7, C8, CMessenger,

        A1 = 23
        A1 is in spot 23 -> C6
        */

        /*
          Some simple things we can quickly use to invalidate a seed:
           We want to start in A
           We want F0 star to be a green
           We want F3 star to be a grey
        */
        if (progress.getVar("A1") > 9
            || TalosProgress.TETROS[progress.getVar("F0-Star")].charAt(0) != 'D'
            || TalosProgress.TETROS[progress.getVar("F3-Star")].charAt(0) != 'E') {
            return;
        }

        // Work out which markers are in which hub
        ArrayList<String> A_MARKERS = new ArrayList<String>();
        ArrayList<String> C_MARKERS = new ArrayList<String>();

        for (World w : World.values()) {
            int worldIndex = progress.getVar(w.toString());
            // Don't want to include the star worlds
            if (worldIndex <= 9 && worldIndex != 8) {
                A_MARKERS.addAll(Arrays.asList(MARKERS.get(w)));
            } else if (worldIndex >= 18 && worldIndex != 25) {
                C_MARKERS.addAll(Arrays.asList(MARKERS.get(w)));
            }
        }

        // Count the greens in A
        int DI = 0;
        int DJ = 0;
        int DL = 0;
        int DT = 0;
        int DZ = 0;
        for (String marker : A_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
            if (sigil.charAt(0) == 'D') {
                switch (sigil.charAt(1)) {
                    case 'I': DI++; break;
                    case 'J': DJ++; break;
                    case 'L': DL++; break;
                    case 'T': DT++; break;
                    case 'Z': DZ++; break;
                }
            }
        }

        // Can't get out of A
        if (DI < 1 || DJ < 1 || DL < 1 || DZ < 1) return;
        DI--;
        DJ--;
        DL--;
        DZ--;
        // Can get into B
        if (DI >= 1 && DL >= 1 && DT >= 2 && DZ >= 1) return;
        // Can't get into C
        if (DL < 1 || DJ < 2 || DT < 2 || DZ < 1) return;
        DL--;
        DJ -= 2;
        DT -= 2;
        DZ--;

        // Add the greens in C
        for (String marker : C_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
            if (sigil.charAt(0) == 'D') {
                switch (sigil.charAt(1)) {
                    case 'I': DI++; break;
                    case 'J': DJ++; break;
                    case 'L': DL++; break;
                    case 'T': DT++; break;
                    case 'Z': DZ++; break;
                }
            }
        }
        // Still don't want to be able to get into B
        if (DI >= 1 && DL >= 1 && DT >= 2 && DZ >= 1) return;

        // Add F0 Star
        String sigil = TalosProgress.TETROS[progress.getVar("F0-Star")];
        if (sigil.charAt(0) == 'D') {
            switch (sigil.charAt(1)) {
                case 'I': DI++; break;
                case 'J': DJ++; break;
                case 'L': DL++; break;
                case 'T': DT++; break;
                case 'Z': DZ++; break;
            }
        }

        // Now we finally want to be able to get to B
        if (DI >= 1 && DL >= 1 && DT >= 2 && DZ >= 1) {
            String output = String.format("%d, %s, %s Portals",
                                          progress.getVar("Randomizer_Seed"),
                                          progress.getChecksum(),
                                          progress.getVar("Randomizer_Portals") == -1 ? "Standard"
                                                                                      : "Random");
            System.out.println(output);
            try {
                Files.write(Paths.get("randomizer_bruteforce_experiments/team_race_2/F0_to_B.txt"),
                            (output + "\n").getBytes(),
                            StandardOpenOption.APPEND);
            } catch (IOException e) {}
        }
    }
}
