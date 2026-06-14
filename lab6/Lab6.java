import java.util.*;

/**
 * Лабораторна робота №6
 * HashSet з об'єктами "Точка" (x, y) — перетин та різниця множин
 */
public class Lab6 {

    // =====================================================================
    // Клас Point — обов'язково перевизначає equals() та hashCode()
    // для коректної роботи з HashSet
    // =====================================================================
    static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Point)) return false;
            Point other = (Point) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // =====================================================================
    // Допоміжний метод: генерація множини з n точок
    // Координати в діапазоні [minVal, maxVal]
    // =====================================================================
    static HashSet<Point> generateSet(int n, int minVal, int maxVal, Random rnd) {
        HashSet<Point> set = new HashSet<>();
        while (set.size() < n) {
            int x = rnd.nextInt(maxVal - minVal + 1) + minVal;
            int y = rnd.nextInt(maxVal - minVal + 1) + minVal;
            set.add(new Point(x, y));
        }
        return set;
    }

    // Виведення множини у консоль, відсортованої для зручності
    static void printSet(String label, HashSet<Point> set) {
        List<Point> sorted = new ArrayList<>(set);
        sorted.sort(Comparator.comparingInt(Point::getX).thenComparingInt(Point::getY));
        System.out.println(label + " [" + set.size() + " точок]:");
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < sorted.size(); i++) {
            sb.append(sorted.get(i));
            if (i < sorted.size() - 1) sb.append(", ");
            if ((i + 1) % 8 == 0 && i < sorted.size() - 1) {
                System.out.println(sb);
                sb = new StringBuilder("  ");
            }
        }
        if (sb.length() > 2) System.out.println(sb);
    }

    // =====================================================================
    // MAIN
    // =====================================================================
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Лабораторна робота №6: HashSet з точками ===\n");

        // Введення параметрів
        int n1 = readInt(scanner, "Введіть кількість точок у першій множині (наприклад, 20): ", 1, 100);
        int n2 = readInt(scanner, "Введіть кількість точок у другій множині (наприклад, 20): ", 1, 100);
        int range = readInt(scanner, "Введіть діапазон координат [0..N] (наприклад, 10): ", 2, 50);
        System.out.println();

        // Генерація множин
        Random rnd = new Random(42); // фіксоване зерно для відтворюваності
        System.out.println("[INFO] Генерація множин...");
        HashSet<Point> setA = generateSet(n1, 0, range, rnd);
        HashSet<Point> setB = generateSet(n2, 0, range, rnd);

        printSet("Множина A", setA);
        System.out.println();
        printSet("Множина B", setB);
        System.out.println();

        // ---- ПЕРЕТИН: точки, що є в обох множинах ----
        // Використовуємо копію, щоб не змінювати оригінал
        HashSet<Point> intersection = new HashSet<>(setA);
        intersection.retainAll(setB); // залишає лише спільні елементи

        System.out.println("--- Операція: ПЕРЕТИН (A ∩ B) ---");
        System.out.println("[INFO] Метод: intersection.retainAll(setB)");
        if (intersection.isEmpty()) {
            System.out.println("  Перетин порожній — спільних точок немає.");
        } else {
            printSet("A ∩ B", intersection);
        }
        System.out.println();

        // ---- РІЗНИЦЯ: точки з A, яких немає в B ----
        HashSet<Point> difference = new HashSet<>(setA);
        difference.removeAll(setB); // видаляє з A всі елементи, що є в B

        System.out.println("--- Операція: РІЗНИЦЯ (A \\ B) ---");
        System.out.println("[INFO] Метод: difference.removeAll(setB)");
        if (difference.isEmpty()) {
            System.out.println("  Різниця порожня — усі точки A є також у B.");
        } else {
            printSet("A \\ B", difference);
        }
        System.out.println();

        // ---- СИМЕТРИЧНА РІЗНИЦЯ: точки, що є лише в одній з множин ----
        HashSet<Point> symDiff = new HashSet<>(setA);
        symDiff.addAll(setB);        // об'єднання A ∪ B
        symDiff.removeAll(intersection); // мінус перетин

        System.out.println("--- Бонус: СИМЕТРИЧНА РІЗНИЦЯ (A △ B) ---");
        System.out.println("[INFO] Точки, що є лише в одній з множин:");
        if (symDiff.isEmpty()) {
            System.out.println("  Симетрична різниця порожня — множини рівні.");
        } else {
            printSet("A △ B", symDiff);
        }
        System.out.println();

        // ---- Підсумок ----
        System.out.println("=== Підсумок ===");
        System.out.println("  |A|         = " + setA.size());
        System.out.println("  |B|         = " + setB.size());
        System.out.println("  |A ∩ B|     = " + intersection.size());
        System.out.println("  |A \\ B|     = " + difference.size());
        System.out.println("  |A △ B|     = " + symDiff.size());

        // Перевірка: |A| = |A∩B| + |A\B|
        System.out.println("\n[Перевірка] |A ∩ B| + |A \\ B| = "
                + intersection.size() + " + " + difference.size()
                + " = " + (intersection.size() + difference.size())
                + " == |A| = " + setA.size()
                + (intersection.size() + difference.size() == setA.size() ? " ✓" : " ✗"));

        System.out.println("\n=== Роботу завершено ===");
    }

    // Допоміжний метод введення цілого числа з перевіркою
    static int readInt(Scanner sc, String prompt, int min, int max) {
        int val = 0;
        while (true) {
            System.out.print(prompt);
            try {
                val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("  [!] Введіть число від " + min + " до " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Невірний формат.");
            }
        }
    }
}
