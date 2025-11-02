import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class GameOfLife {
    public static void main(String[] args) {
        System.out.println("=== JOCUL VIETII - Cu Resurse Limitate ===\n");  // This will only show in terminal (before redirection)

        // Create ResourcePool with 15 units of food
        ResourcePool resourcePool = new ResourcePool(15);

        CellManager manager = new CellManager(resourcePool);

        // Create and show the GUI
        SimulationGUI gui = new SimulationGUI(manager, resourcePool);
        gui.setVisible(true);

        // Redirect System.out to duplicate output to GUI log (terminal still gets messages)
        PrintStream guiOut = new GuiPrintStream(System.out, gui.getLogArea());
        System.setOut(guiOut);

        // Create initial cells
        Cell cell1 = new AsexualCell(1, manager, resourcePool);
        Cell cell2 = new AsexualCell(2, manager, resourcePool);
        Cell cell3 = new SexualCell(3, manager, resourcePool);
        Cell cell4 = new SexualCell(4, manager, resourcePool);

        manager.addCell(cell1);
        manager.addCell(cell2);
        manager.addCell(cell3);
        manager.addCell(cell4);

        System.out.println("\n>>> Cells starting...\n");

        // Simulation
        try {
            Thread.sleep(8000);
            manager.printStats();

            Thread.sleep(8000);
            manager.printStats();

            Thread.sleep(8000);
            manager.printStats();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n>>> Simulation ending...");
        manager.stopAll();
        manager.printStats();

        System.out.println("\n>>> Simulation COMPLETED!");
    }
}