package aion.dashboard.worker;

import aion.dashboard.email.EmailService;
import aion.dashboard.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This thread ensures that the ETL gracefully shuts down so that no interrupts occur in the middle of a database write
 */
public class ShutdownHook extends Thread{

    private static final Logger GENERAL = LoggerFactory.getLogger("logger_general");
    private final BlockchainReaderThread blockchainReaderThread;
    private final DBThread dbThread;
    private final IntegrityCheckThread integrityCheckThread;

    private ShutdownHook(BlockchainReaderThread readerThread, DBThread dbThread, IntegrityCheckThread integrityCheckThread) {
        super("Shutdown hook");
        blockchainReaderThread = readerThread;
        this.dbThread = dbThread;
        this.integrityCheckThread = integrityCheckThread;
    }


    @Override
    public void run() {

        GENERAL.info("Shutting down ETL");

        // Signalling threads that they should end their execution
        blockchainReaderThread.kill();
        dbThread.kill();
        integrityCheckThread.kill();

        //closing all singleton services
        EmailService.getInstance().close();
        SchedulerService.getInstance().close();


        try {
            // Pause the shutdown until all threads have ended their execution
            int i =0;
            boolean threadsAreAlive = blockchainReaderThread.isAlive() || dbThread.isAlive() || integrityCheckThread.isAlive();
            while (i < 10 && threadsAreAlive){
                Thread.sleep(1000);
                threadsAreAlive = blockchainReaderThread.isAlive() || dbThread.isAlive() || integrityCheckThread.isAlive();
                i++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            GENERAL.debug("Exception thrown in shutdown hook", e);
        }


        GENERAL.info("--------------------------------------------------");
        GENERAL.info("ETL shutdown gracefully");
        GENERAL.info("--------------------------------------------------");
    }


    public static class Builder{

        private BlockchainReaderThread readerThread;
        private DBThread dbThread;
        private IntegrityCheckThread integrityCheckThread;


        public Builder readerThread(BlockchainReaderThread readerThread) {
            this.readerThread = readerThread;
            return this;
        }

        public Builder dbThread(DBThread dbThread) {
            this.dbThread = dbThread;
            return this;
        }

        public Builder integrityCheckThread(IntegrityCheckThread integrityCheckThread) {
            this.integrityCheckThread = integrityCheckThread;
            return this;
        }

        public ShutdownHook build(){
            return new ShutdownHook(readerThread, dbThread, integrityCheckThread);
        }
    }
}
