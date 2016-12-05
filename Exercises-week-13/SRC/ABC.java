import java.io.Serializable;
import java.util.Random;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.Props;

/*
 * Messages
 * 
 * A normal string will be used for 'printBalance' message.
 */
class Deposit implements Serializable {
    public final int amount;

    public Deposit(int amount) {
        this.amount = amount;
    }
}

class Transfer implements Serializable {
    public final int amount;
    public final ActorRef from, to;

    public Transfer(int amount, ActorRef from, ActorRef to) {
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

}

class Start implements Serializable {
    final ActorRef bank;
    final ActorRef from, to;

    public Start(ActorRef bank, ActorRef from, ActorRef to) {
        this.bank = bank;
        this.from = from;
        this.to = to;
    }
}

/*
 * Actors
 */

class Account extends UntypedActor {
    private int balance;

    public void onReceive(Object o) {
        if (o instanceof Deposit) 
            balance += ((Deposit) o).amount;
        else if (o.toString().equals("printbalance")) 
            System.out.println(balance);
    }
}

class Bank extends UntypedActor {
    
    public void onReceive(Object o) {
        if (o instanceof Transfer) {
            Transfer transfer = (Transfer) o;
            int amount = transfer.amount;
            ActorRef from = transfer.from, to = transfer.to;
            from.tell(new Deposit(-amount), getSelf());
            to.tell(new Deposit(amount), getSelf());
        }
    }
}

class Clerk extends UntypedActor {

    public void onReceive(Object o) {
        if (o instanceof Start) {
            Start start = (Start) o;
            ActorRef bank = start.bank;
            ActorRef from = start.from, to = start.to;
            Random rnd = new Random();
            for (int i = 0; i < 200; i++) 
                bank.tell(new Transfer(rnd.nextInt(200), from, to), getSelf());
            
        }
    }
}

public class ABC {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("ABCSystem");

        final ActorRef a1 = getActorOf(system, Account.class, "a1"),
              a2 = getActorOf(system, Account.class, "a2"),
              b1 = getActorOf(system, Bank.class, "b1"),
              b2 = getActorOf(system, Bank.class, "b2"),
              c1 = getActorOf(system, Clerk.class, "c1"),
              c2 = getActorOf(system, Clerk.class, "c2");

        c1.tell(new Start(b1, a1, a2), ActorRef.noSender());
        c2.tell(new Start(b2, a2, a1), ActorRef.noSender());
        long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now < 1000) {}
        a1.tell("printbalance", ActorRef.noSender());
        a2.tell("printbalance", ActorRef.noSender());
        
        try {
            System.out.println("Press return to exit...");
            System.in.read(); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            system.shutdown();
        }
    }

    private static ActorRef getActorOf(ActorSystem system, Class cl, String st) {
        return system.actorOf(Props.create(cl), st);
    }
}
