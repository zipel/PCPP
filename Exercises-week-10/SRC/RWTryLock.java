public interface RWTryLock {
    boolean readerTryLock();
    void readerUnlock();
    boolean writerTryLock();
    void writerUnlock();
}

