package gh2;

import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    // uncomment the following line once you're ready to start this portion
    private Deque<Double> buffer;
//    private int capacity;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        //       Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this division operation into an int. For
        //       better accuracy, use the Math.round() function before casting.
        //       Your should initially fill your buffer array with zeros.
        int capacity = (int) Math.round(SR / frequency);
        buffer = new LinkedListDeque<>();
//        buffer = new ArrayDeque<>();
        for (int i = 0; i < capacity; ++i) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
//        buffer = new LinkedListDeque<>();
//        buffer = new ArrayDeque<>();
        int size = buffer.size();
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < size; ++i) {
            buffer.addLast(Math.random() - 0.5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        //       Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        double a = buffer.removeFirst();
        double b = buffer.get(0);
        double newDouble = DECAY * 0.5 * (a + b);
        buffer.addLast(newDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        //  Return the correct thing.
        return buffer.get(0);
    }
}
    //  Remove all comments that say TODO when you're done.
