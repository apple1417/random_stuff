class RunAllDefault {
    private static int THREAD_NUM = 8;
    private static long PER_LOOP = 10000;
    private static long PER_THREAD = (PER_LOOP / THREAD_NUM);
    private static long current_seed = 0;
    private static long max_seed = 0x7FFFFFFF;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(current_seed);
            }
        });

        try {
            current_seed = Math.max(0, Long.parseLong(args[0]));
            max_seed = Math.max(current_seed, Math.min(0x7FFFFFFF, Long.parseLong(args[1])));
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        while (current_seed + PER_LOOP < max_seed) {
            GeneratorAllDefault[] threads = new GeneratorAllDefault[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new GeneratorAllDefault(Integer.toString(i));
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            for (GeneratorAllDefault thread : threads) {
                thread.waitFinished();
            }
        }
        GeneratorAllDefault thread = new GeneratorAllDefault("0");
        thread.start(current_seed, 0x7FFFFFFF);
        System.out.println("Out of Seeds");
    }
}