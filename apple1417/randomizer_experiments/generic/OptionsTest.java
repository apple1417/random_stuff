package apple1417.randomizer_experiments.generic;

import java.util.HashMap;
import java.util.Random;
import apple1417.randomizer.Enums.RandomizerMode;
import apple1417.randomizer.Enums.ScavengerMode;
import apple1417.randomizer.GeneratorGeneric;
import apple1417.randomizer.TalosProgress;

/*
  Generates a seed + checksum with each combination of options that'd affect it
  Can be used both to double check stuff is generating correctly and to (slowly)
   get an idea of how often it actually fails in game (e.g. intended/short
   scavenger/portals basicly always fails, there's no good fix though)
*/
class OptionsTest {
    private static Random rand = new Random();
    public static void main(String[] args) {
        HashMap<String, Integer> options = new HashMap<String, Integer>();
        for (int mode = 0; mode <= 6; mode++) {
            options.put("Randomizer_Mode", mode);
            for (int scavenger = 0; scavenger <= 2; scavenger++) {
                options.put("Randomizer_Scavenger", scavenger);
                for (int portals = -1; portals <= 1; portals += 2) {
                    options.put("Randomizer_Portals", portals);
                    for (int loop = 0; loop <= 1; loop ++) {
                        options.put("Randomizer_Loop", loop);
                        GeneratorGeneric g = new GeneratorGeneric(options);
                        int seed = rand.nextInt(1000000);
                        System.out.println(String.format("Mode: %s, Scavenger: %s, Portals: %s, Mobius: %s, Seed: %d", RandomizerMode.fromInt(mode), ScavengerMode.fromInt(scavenger), (portals == 1) ? "On" : "Off", (loop == 1) ? "All Sigils" : "Off", seed));
                        try {
                            System.out.println(g.generate(seed).getChecksum());
                        } catch (Exception e) {
                            System.out.println("Fails to generate");
                        }
                    }
                }
            }
        }
    }
}
