package apple1417.randomizer_experiments.team_race_2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import apple1417.randomizer.GeneratorGeneric;
import apple1417.randomizer.SeedScheduler;
import apple1417.randomizer.TalosProgress;

class MoodyPrinciple {
    private static HashMap<String, Integer> options;
    public static void main(String[] args) {
        int min = 0;
        int max = 0x7FFFFFFF;
        try {
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        options = new HashMap<String, Integer>();
        options.put("Randomizer_Mode", 2);

        SeedScheduler s = new SeedScheduler(() -> new GeneratorGeneric(options),
                                            (TalosProgress p) -> evaluate(p));
        s.start(min, max);
    }

    private static String[] A_MARKERS = {
        "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
        "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
        "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
        "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
        "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole"
    };
    private static void evaluate(TalosProgress progress) {
        int DI = 0;
        int DJ = 0;
        int DL = 0;
        int DZ = 0;
        int E = 0;
        int ML = 0;
        int MT = 0;
        int MZ = 0;
        int NI = 0;
        int NJ = 0;
        int NL = 0;
        int NO = 0;
        int NS = 0;
        int NT = 0;
        int NZ = 0;

        for (String marker : A_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
            if (sigil.charAt(0) == 'D') {
                switch(sigil.charAt(1)) {
                    case 'I': DI++; break;
                    case 'J': DJ++; break;
                    case 'L': DL++; break;
                    case 'Z': DZ++; break;
                }
            } else if (sigil.charAt(0) == 'E') {
                E++;
            } else if (sigil.charAt(0) == 'M') {
                switch(sigil.charAt(1)) {
                    case 'L': ML++; break;
                    case 'T': MT++; break;
                    case 'Z': MZ++; break;
                }
            } else if (sigil.charAt(0) == 'N') {
                switch(sigil.charAt(1)) {
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

        // Don't want to be able to do F3 or F6
        if ((NI >= 4 && NL >= 3 && NS >= 1 && NZ >= 3)
            || (E >= 9 && NL >= 2 && NZ >=2)) {
            return;
        }

        // If F2 is possible and >16 useful reds (too much output otherwise)
        if (NL >= 6 && NO >= 1 && NT >= 4 && NZ >= 2
            && ML >= 1 && MT >= 2 && MZ >= 1
            && NL + NO + NT + NZ >= 16) {
            String output = String.format("%d (%d/%d: %d %d %d %d %d %d %d, %d: %d %d %d, %d: %d %d %d %d)",
                                          progress.getVar("Randomizer_Seed"),
                                          NI + NJ + NL + NO + NS + NT + NZ,
                                          NL + NO + NT + NZ,
                                          NI, NJ, NL, NO, NS, NT, NZ,
                                          MT + ML + MZ,
                                          ML, MT, MZ,
                                          DI + DJ + DL + DZ,
                                          DI, DJ, DL, DZ);
            System.out.println(output);
            try {
                Files.write(Paths.get("randomizer_bruteforce_experiments/team_race_2/MoodyPrinciple.txt"),
                            (output + "\n").getBytes(),
                            StandardOpenOption.APPEND);
            } catch (IOException e) {}
        }
    }
}
