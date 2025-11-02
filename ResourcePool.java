import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class ResourcePool {
    private int availableFood;
    private final Semaphore foodSemaphore;
    private final Random random;
    
    public ResourcePool(int initialFood) {
        this.availableFood = initialFood;
        this.foodSemaphore = new Semaphore(initialFood, true); // fair semaphore
        this.random = new Random();
    }
    
    // FIX #3: Synchronize the read of availableFood before printing
    public boolean tryToEat(int cellId, long timeoutMs) throws InterruptedException {
        int currentFood;
        synchronized (this) {
            currentFood = availableFood;
        }
        System.out.println("[Cell " + cellId + "] Tries to acquire food... (available: " + currentFood + ")");
        
        // Încearcă să obțină permisiunea în timpul dat
        boolean acquired = foodSemaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS);
        
        if (acquired) {
            synchronized (this) {
                availableFood--;
                System.out.println("[Cell " + cellId + "] GOT FOOD! (remaining: " + availableFood + ")");
            }
            return true;
        } else {
            System.out.println("[Cell " + cellId + "] NO FOOD AVAILABLE!");
            return false;
        }
    }
    
    // Când o celulă moare, produce hrană
    public void addFoodFromDeadCell(int cellId) {
        int foodProduced = random.nextInt(5) + 1; // 1-5 unități
        
        synchronized (this) {
            availableFood += foodProduced;
        }
        
        // Eliberează permisiunile în semaphore
        foodSemaphore.release(foodProduced);
        
        System.out.println("[ResourcePool] Cell " + cellId + " died and produced " + 
                         foodProduced + " food units (total: " + availableFood + ")");
    }
    
    public synchronized int getAvailableFood() {
        return availableFood;
    }
}