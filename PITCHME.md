# multi-threading

---
## Today
1. How to create and run threads.
1. `Thread.sleep`.
1. Sending and supporting interrupts. 
1. `join`.
1. Synchronized blocks.

---
## Resources
1. Oracle Java Tutorials - [Concurrency](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
1. Jenkov.com - [Java Concurrency and Multithreading Tutorial](http://tutorials.jenkov.com/java-concurrency/index.html)
1. The [java memory model](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#volatile)



---
### Introduction
A computer system normally has many active processes and threads. This is true even in systems that only have a single execution core, and thus only have one thread actually executing at any given moment. 

The operating system switches between the programs running, executing each of them for a little while before switching.



---
### Concurrency
There are two levels of concurrency:
1. Processes - each gets its own memory.
1. Threads within a process - share the memory space.
  + This makes for efficient, but potentially problematic, communication.
  + Much lighter and faster to create and manage.


---
### Why Write Multi-Threaded Code
+ CPU's today have multiple cores, and multi-threading lets us take advantage of it.
+ Some operations (especially I/O from storage, or network access) take a long time.
  + One thread can take care of the slow operation while the other continues to work.
+ In programs that have a user interface, if a calculation takes a long time, we can use multi-threading to keep the UI responsive.


---
### Java Support
Java is built with concurrency in mind:
+ It has primitive operations to support multi-threading and synchronization between them.
+ The Java libraries have higher level classes to make safe multi-threading easier.


---
### Java Threads
1. A program starts with one thread, the **main** thread.
1. A thread can create other threads.

In fact, a Java program has more threads running, like the garbage collector. 



---
### Creating Threads
To create a thread, you first need a class that implements:
```java code-noblend
interface Runnable {
	void run();
}
```
Then:
+ create a new `Thread` object with an instance of this `Runnable` class, 
+ `start()` it.


---
@code[java code-max code-noblend](src/HelloThread.java)

Don't do `t.run()`! It will not start a new thread, but just execute the method `run`.


---
@code[java code-max code-noblend](src/TwoThreads.java)
@[1-14](`main` creates one `Runnnable` object `r`. It then creates two threads based on it. Lastly, it starts these threads which execute the method `run`.)
@[15-20](This is one possible output.)



---
@code[java code-max code-noblend](src/SharedMemory.java)
@[1-8](`i` is a field variable, and not local to the method `run` like in the previous example.)
@[10-16](The two threads share the `Runnable` `r`, and so the variable `i`.)
@[17-22](Why is `0` printed after `2`?.)


---
### Threads Share Memory
+ Threads can run the same code,
+ and share access to the same variables.
+ This can be very useful.
+ but can cause a lot of unexpected trouble!

@css[fragment](*We will discuss how to deal with these things later.*)



---
### `Thread.sleep()`
A static method of  `Thread` that makes the thread that runs it wait for the specified number of milliseconds ($1/1000$ of a second).
+ It is not precise.
+ Sleep can be interrupted by another thread. In this case, `sleep` throws an `InterruptedException`.



---
@code[java code-max code-noblend](src/SleepExample.java)
We don't bother to catch the interrupted exception, and just let `main` throw it.



---
### Interrupts
An interrupt is an indication to a thread that it should stop what it is doing and do something else.
+ It's up to the programmer, but normally a thread getting an interrupt stops.
+ One thread can send another thread an interrupt by using its `Thread` object.



---
@code[java code-max code-noblend](src/InterruptExample.java)
@[1-13](Prints 1,2,...,100, with one second between prints. Unless interrupted.)
@[15-21](Starts the other thread, waits 7 seconds, and then interrupts it. Notice that the `throws` here has nothing to do with the `sleep` inside `run`.)



---
A thread that may receive an interrupt should support this possibility.
+ Either by using methods like `sleep` that throw an exception if an interrupt happens.
+ Or by using the method `Thread.interrupted()`, which returns true if an interrupt has been received.
+ Otherwise, the interrupt will not have any effect.

@box[](Interrupts do not automatically stop a thread!)




---
@code[java code-max code-noblend](src/InterruptExample2.java)
@[1-10](`Thread.interrupted` resets the internal flag.)
@[12-18](To send an interrupt to a thread we use it's thread object.)



---
### The Interrupt Status Flag

The interrupt mechanism is implemented using an internal flag known as the **interrupt status**. 
+ Invoking `Thread.interrupt()` sets this flag. 
+ When a thread checks for an interrupt with `Thread.interrupted()`, interrupt status is cleared.



---
### Join
+ `t.join()` will wait until thread `t` terminates.
+ `t.join(int millis)` will wait, but up to a maximum of `millis` milliseconds.
+ Like `sleep`, the `join` command can be interrupted by another thread (possibly a third one). In this case it throws `InterruptedException`.


@css[fragment](*Here's an example with all these together:*)



---
@code[java code-max code-noblend](src/SimpleThreads.java)
@[1-6](A useful helper method.)
@[8-20]
@[22-28]
@[30-42](Using `t.isAlive` after a join can tell us why it exited.)



---
### Synchronization

Threads communicate primarily by sharing access to variables.
+ Extremely efficient.
+ Quite problematic: 

It is very difficult to maintain correct behavior when many threads access and change the same objects.
To solve these problems Java has tools for synchronization.




---
@code[java code-max code-noblend](src/NonSyncExample.java)
@[1-7](We create an instance of this class, with a field `c`.)
@[9-14](Will increase `c` by 1000 if `run` is executed.)
@[15-25](On my computer this prints something between 1000 to 2000, but never 2000.)


---

What went wrong?

`c++` is actually: get `c`, do `c+1` and write `c`. So it may happen that:
+ The first thread gets the value of `c`.
+ The second increments `c` many times.
+ Finally, the first thread writes the value it first read, plus one.

So we miss many of the increments!

@css[fragment](Using `synchronized` blocks we can solve this problem:)



---
@code[java code-max code-noblend](src/SyncExample.java)
@[1-8](The only difference is `lock`. It can actually be any instance of any class.)
@[10-17](We wrap the problematic part with a `synchronized` block, which practically  makes it into an atomic operation.)



---
### `synchronized(obj)` 
+ To enter a synchronized block, a thread must first acquire the lock of `obj`.
+ If any other thread has this lock already. This thread will wait.
+ Only when leaving the block, the lock is released.

@css[fragment](*What would happen in the following example?*)



---
@code[java code-max code-noblend](src/SyncProblem.java)
@[1-7]
@[9-17](The lock is declared inside our `Runnable` class.)
@[19-29](What is the problem and how would you fix it?)

@css[fragment](The `lock` is not the same `lock` for the two threads. Solution: create only one `Counter` object.)



---
### Multiple locks
+ When using `synchronized`, we have to check we are synchronizing on the same object.
+ We can have many locks whenever some operations do not interfere with each other.
+ This can increase concurrency, and so improve our running time.



---
@code[java code-max code-noblend](src/TwoLocks.java)
@[1-5]
@[7-19](`get1` and `get2` don't need synchronization.)



---
### Question

If `a` is an array of integers shared between two threads, and one thread does `a[0]++`, while another does `a[1]++`. Will we see a problem? 

@css[fragment](No, because the shared reference `a` itself is not changed, and `a[0]` and `a[1]` are different.)



---
### Deadlocks

With all this locking, problems are bound to appear...



---
@code[java code-max code-noblend](src/DeadLock.java)
@[1-5](Two locks.)
@[6-14](First thread first needs `lock1` and then `lock2`.)
@[15-25](Second thread needs `lock2` and then `lock1`.)
@css[fragment](Sometimes this will get stuck. If you make one thread sleep between the two locks, it will happen more.)




---
### Reentrant Locking
Locking with `synchronized` is **reentrant**. If a thread tries to aquire a lock it already has, it gets it.

For example, the following will not deadlock:
```java code-noblend
synchronized(someLock) {
	synchronized(someLock) {
		System.out.println("Hi");
	}
}
```
    
    
---
### Synchronized Methods
It is common to see a method with the `synchronized` keyword in its signature. For example:
```java code-noblend
public synchronized void method() {
	...
}
```
This is equivalent to:
```java code-noblend
public void method() {
	synchronized(this) {
		...
	}
}
```


---
This is useful for example when you want to make sure no two threads view or change some object simultaneously. Just make all its methods synchronized. 
+ However, excess locking slows down your application, and misses the benefits of concurrency. 
+ Of course, there is not always a need to lock all the methods.

---
### A larger example
A program to count the number of primes between 2 and some given upper bound.
1. Divide the range of numbers equally between a number of threads.
1. Let each check every number in the range.
1. When a thread completes, it increases a shared counter.


---
@code[java code-max code-noblend](src/Speedup.java)
@[1-6](Access to `counter` is synchronized on the `Speedup` instance, which will be shared between threads.)
@[8-14](The inner `Runnable` class, counting primes in a given range.)
@[16-22](A simple prime checking algorithm.)
@[24-31](Why don't we increase the counter after every prime found?)
@[33-42](Creating the threads and running them.)
@[43-47](Waiting until all of them are done.)
@[50-59](Let's see how much time do we save when running many threads.)
@[61-74](Improvement basically stops after number of threads passes number of cores. But not entirely because many threads may allow a better work load balancing.) 
@css[fragment](Actually, there are better algorithms to count the number of primes.)



---
### Exercise

Write a class `SafeArray` that holds a counter array that is thread-safe. It will have methods:
+ `SafeArray(int size)` - initializes an array of length `size`.
+ `void increase(int index, int amount)`.
+ `int get(int index)`.


---
@code[java code-max code-noblend](src/SafeArray.java)

This works, but blocks access to `a[i]` when someone tries to access `a[j]`. 
@css[fragment](Improve it by locking each index separately.)



---
@code[java code-max code-noblend](src/BetterSafeArray.java)
@[1-12]
@[14-24](It is not clear we need the synchronization on the `get`, but it is safer.)
@css[fragment](For those interested, there is also a `main` in the source code that checks it, and can compare its running time to `SafeArray`.)


---
### Modern Processors

+ In CPUs with multiple cores, each core has its own cache and registers. 
+ Different threads run with their own copy of the memory in the cache of their core.
+ Only when needed to (or forced to), the local cache memory is copied back to main memory 


---
@img[span-90](resources/memory.png)


---

This model makes programming multi-threaded code very difficult.
+ Java's multi-threading support makes it much easier.
+ For us, using `synchronized` solves most of the problems transparently.
+ Make sure you use it when accessing common variables and you will be fine :) 


 
---
### Waiting

One thread wants to wait until some other thread sets a flag to true.

```java code-noblend
public void waitUntil() {
	while(!flag) {}
	System.out.println("Done!");
}

```
This is called a **busy-wait**, 
and is terrible on cpu time.

@css[fragment](**Never do it!**)




---
A better option is to sleep and wait to be woken up by an interrupt.
```java code-noblend
while (!flag)
	try {
		Thread.sleep(Long.MAX_VALUE);
	} catch (InterruptedException e) {}
```	
The thread setting the flag should do:
```java code-noblend
flag = true;
waitingThread.interrupt();
```	

---
This seems to work, however, 
1. If the flag is set to true, then the waiting thread checks it and skips the sleep, and only then the interrupt is sent, we may get an interrupt at some unexpected time in the future. 
1. Java recommends using interrupts for stopping threads only.
1. Interrupts are quite limited.


---

Java offers us a much more interesting option for synchronization between threads, which we will learn about next week.






