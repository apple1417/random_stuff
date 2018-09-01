package apple1417.randomizer_experiments;

import apple1417.randomizer.Enums.World;
import apple1417.randomizer.Rand;

class RandomPortalsStarWorlds {
    public static void main(String[] args) {
        int[] amounts = new int[4];
        for (long seed = 0; seed <= 0x7FFFFFFF; seed++) {
            amounts[check(seed)]++;
            if (seed % 10000000 == 0) {
                System.out.println(seed);
            }
        }
        System.out.println(String.format(
            "0: %d, 1: %d, 2: %d, 3: %d",
            amounts[0],
            amounts[1],
            amounts[2],
            amounts[3]
        ));
    }

    public static int check(long seed) {
        Rand r = new Rand(seed);
        for (int i = 0; i < 6; i++) {
            r.next(0, 0);
        }

        World[] portalOrder = World.values();
        for (int index = portalOrder.length - 1; index > 0; index--) {
           int otherIndex = r.next(1, index);
           World tmp = portalOrder[index];
           portalOrder[index] = portalOrder[otherIndex];
           portalOrder[otherIndex] = tmp;
        }
        int index = r.next(0, 21);
        if (index == 7) {
           index = 8;
        } else if (8 <= index && index < 15) {
           index++;
        } else if (15 <= index) {
           index += 2;
        }
        World tmp = portalOrder[index];
        portalOrder[index] = portalOrder[0];
        portalOrder[0] = tmp;

        int amount = 3;
        for (int i = 0; i < portalOrder.length; i++) {
            if (portalOrder[i] == World.A8 || portalOrder[i] == World.B8 || portalOrder[i] == World.C8) {
                if (i == 7 || i == 16 || i == 24) {
                    amount--;
                }
            }
        }

       return amount;
    }
}
