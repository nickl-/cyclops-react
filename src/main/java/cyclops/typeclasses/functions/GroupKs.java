package cyclops.typeclasses.functions;

import cyclops.collections.immutable.LinkedListX;
import cyclops.collections.immutable.OrderedSetX;
import cyclops.collections.immutable.PersistentQueueX;
import cyclops.collections.immutable.VectorX;
import cyclops.collections.mutable.DequeX;
import cyclops.collections.mutable.ListX;
import cyclops.collections.mutable.QueueX;
import cyclops.collections.mutable.SortedSetX;
import cyclops.companion.Monoids;
import cyclops.companion.Streams;
import cyclops.function.Group;
import cyclops.monads.Witness;
import cyclops.monads.Witness.*;
import cyclops.stream.FutureStream;
import cyclops.stream.ReactiveSeq;
import cyclops.stream.Spouts;
import org.jooq.lambda.Seq;
import org.reactivestreams.Publisher;

import java.math.BigInteger;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.SortedSet;
import java.util.stream.Stream;


public interface GroupKs {





    /**
     * @return A combiner for ListX (concatenates two ListX into a singleUnsafe ListX)
     */
    static <T> GroupK<list,T> listXConcat() {
        return GroupK.of(l->l.convert(ListX::narrowK).reverse(),MonoidKs.listXConcat());
    }


    /**
     * @return A combiner for SortedSetX (concatenates two SortedSetX into a singleUnsafe SortedSetX)
     
    static <T> GroupK<sortedSet,T> sortedSetXConcat() {
        return GroupK.of(l->l.convert(SortedSetX::narrowK),MonoidKs.sortedSetXConcat());
    }*/

    /**
     * @return A combiner for QueueX (concatenates two QueueX into a singleUnsafe QueueX)
     */
    static <T> GroupK<Witness.queue,T> queueXConcat() {
        return GroupK.of(l->l.convert(QueueX::narrowK),MonoidKs.queueXConcat());
    }

    /**
     * @return A combiner for DequeX (concatenates two DequeX into a singleUnsafe DequeX)
     */
    static <T> GroupK<Witness.deque,T> dequeXConcat() {
        return GroupK.of(l->l.convert(DequeX::narrowK),MonoidKs.dequeXConcat());
    }

    /**
     * @return A combiner for LinkedListX (concatenates two LinkedListX into a singleUnsafe LinkedListX)
     */
    static <T> GroupK<linkedListX,T> linkedListXConcat() {
        return GroupK.of(l->l.convert(LinkedListX::narrowK),MonoidKs.linkedListXConcat());
    }

    /**
     * @return A combiner for VectorX (concatenates two VectorX into a singleUnsafe VectorX)
     */
    static <T> GroupK<vectorX,T> vectorXConcat() {
        return GroupK.of(l->l.convert(VectorX::narrowK),MonoidKs.vectorXConcat());
    }



    /**
     * @return A combiner for OrderedSetX (concatenates two OrderedSetX into a singleUnsafe OrderedSetX)

    static <T> GroupK<orderedSetX,T> orderedSetXConcat() {
        return GroupK.of(l->l.convert(OrderedSetX::narrowK),MonoidKs.orderedSetXConcat());
    }
     */
    /**
     * @return A combiner for PersistentQueueX (concatenates two PersistentQueueX into a singleUnsafe PersistentQueueX)
     */
    static <T> GroupK<persistentQueueX,T> persistentQueueXConcat() {
        return GroupK.of(l->l.convert(PersistentQueueX::narrowK),MonoidKs.persistentQueueXConcat());
    }



    /**
     * @return Combination of two ReactiveSeq Streams b is appended to a
     */
    static <T> GroupK<reactiveSeq,T> combineReactiveSeq() {
        return GroupK.of(l->l.convert(ReactiveSeq::narrowK),MonoidKs.combineReactiveSeq());
    }


    static <T> GroupK<reactiveSeq,T> mergeLatestReactiveSeq() {
        return GroupK.of(l->l.convert(ReactiveSeq::narrowK),MonoidKs.mergeLatestReactiveSeq());
    }
    


    
    /**
     * @return Combination of two Stream's : b is appended to a
     */
    static <T> GroupK<stream,T> combineStream() {
        return GroupK.of(l-> Streams.StreamKind.widen(Streams.reverse(l.convert(Streams.StreamKind::narrowK))), MonoidKs.combineStream());
    }





}
