package jvm.multithread;

/**
 * 要使 volatile 变量提供理想的线程安全，必须同时满足下面两个条件：
 * <p>
 * 对变量的写操作不依赖于当前值。
 * 该变量没有包含在具有其他变量的不变式中。
 * 实际上，这些条件表明，可以被写入 volatile 变量的这些有效值独立于任何程序的状态，包括变量的当前状态。
 * <p>
 * 第一个条件的限制使 volatile 变量不能用作线程安全计数器。虽然增量操作（x++）看上去类似一个单独操作，实际上它是一个由（读取－修改－写入）操作序列组成的组合操作，必须以原子方式执行，而 volatile 不能提供必须的原子特性。实现正确的操作需要使x 的值在操作期间保持不变，而 volatile 变量无法实现这点。
 *
 * 代码相关资料https://www.ibm.com/developerworks/cn/java/j-jtp06197.html
 */
public class VolatileDemo {
    /*  模式 #1：状态标志
    也许实现 volatile 变量的规范使用仅仅是使用一个布尔状态标志，用于指示发生了一个重要的一次性事件，例如完成初始化或请求停机。
     */
    volatile boolean shutdownRequested;
    public void shutdown() {
        shutdownRequested = true;
    }
    public void doWork() {
        while (!shutdownRequested) {
            // do stuff
        }
    }

    /*
    模式 #2：一次性安全发布（one-time safe publication）
    缺乏同步会导致无法实现可见性，这使得确定何时写入对象引用而不是原语值变得更加困难。
    在缺乏同步的情况下，可能会遇到某个对象引用的更新值（由另一个线程写入）和该对象状态的旧值同时存在。
    （这就是造成著名的双重检查锁定（double-checked-locking）问题的根源，其中对象引用在没有同步的情况下进行读操作，
    产生的问题是您可能会看到一个更新的引用，但是仍然会通过该引用看到不完全构造的对象）。

    public class BackgroundFloobleLoader {
    public volatile Flooble theFlooble;
    public void initInBackground() {
        // do lots of stuff
        theFlooble = new Flooble();  // this is the only write to theFlooble
    }
    }
    public class SomeOtherClass {
        public void doWork() {
            while (true) {
                // do some stuff...
                // use the Flooble, but only if it is ready
                if (floobleLoader.theFlooble != null)
                    doSomething(floobleLoader.theFlooble);
            }
        }
    }
     */

    /*  模式 #3：独立观察（independent observation）
    定期 “发布” 观察结果供程序内部使用。【例如】假设有一种环境传感器能够感觉环境温度。一个后台线程可能会每隔几秒读取一次该传感器，并更新包含当前文档的 volatile 变量。然后，其他线程可以读取这个变量，从而随时能够看到最新的温度值。

使用该模式的另一种应用程序就是收集程序的统计信息。【例】如下代码展示了身份验证机制如何记忆最近一次登录的用户的名字。将反复使用lastUser 引用来发布值，以供程序的其他部分使用。

     */
    /*public volatile String lastUser;
    public boolean authenticate(String user, String password) {
        boolean valid = passwordIsValid(user, password);
        if (valid) {
            User u = new User();
            activeUsers.add(u);
            lastUser = user;
        }
        return valid;
    }*/

    /*  模式 #4：开销较低的“读－写锁”策略
     */
    private volatile int value;
    public int getValue() { return value; }
    public synchronized int increment() {
        return value++;
    }

}