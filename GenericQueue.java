public class GenericQueue<T> {
    public int length;

    // constructor
    public GenericQueue() {
        length = 0;
    }

    public void enqueue(T value) {
        QueueRecord newRecord = new QueueRecord(value);

        if (head == null) {
            head = newRecord;
            tail = newRecord;

        } else {
            tail.next = newRecord;
            tail = newRecord;

        }
        length += 1;
    }

    public T dequeue() {
        if (head == null) {
            return (null);
        }
        QueueRecord record = head;

        if (tail == head) { // Resets queue back to nothing if there is one element left
            head = null;
            tail = null;
        } else {
            head = head.next;
        }
        length -= 1;
        return (record.value);
    }

    public boolean isEmpty() {
        return (length == 0);
    }

    private class QueueRecord {
        public T value;
        public QueueRecord next;

        public QueueRecord(T value) {
            this.value = value;
        }
    }

    private QueueRecord head, tail;

    public static void doUnitTests() {

        int failCount = 0;
        int successCount = 0;
        // Integer array test
        GenericQueue<Integer> queue = new GenericQueue<>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 10; i++) {
            if (queue.dequeue() == i) {
                successCount++;
            } else {
                failCount++;
            }
        }
        if (queue.dequeue() == null) {
            successCount++;
        } else {
            failCount++;
        }
        queue.enqueue(1);
        if (queue.dequeue() == 1) {
            successCount++;
        } else {
            failCount++;
        }
        // String Array Test
        GenericQueue<String> queue2 = new GenericQueue<>();
        String[] testArray = new String[] { "foo", "roo", "pop", "queue", "yoo" };
        for (int i = 0; i < testArray.length; i++) {
            queue2.enqueue(testArray[i]);
        }
        for (int i = 0; i < testArray.length; i++) {
            if (queue2.dequeue().equals(testArray[i])) {
                successCount++;
            } else {
                failCount++;
            }
        }
        // isEmpty Test
        GenericQueue<Integer> queue3 = new GenericQueue<>();
        if (queue3.isEmpty()) {
            successCount++;
        } else {
            failCount++;
        }
        queue3.enqueue(3);
        if (queue3.isEmpty()) {
            failCount++;
        } else {
            successCount++;
        }
        System.out.println("Fail count: " + failCount);
        System.out.println("Success count: " + successCount);
    }
}
