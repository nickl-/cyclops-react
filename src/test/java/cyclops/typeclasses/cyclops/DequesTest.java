package cyclops.typeclasses.cyclops;

import static cyclops.function.Lambda.l1;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.aol.cyclops2.hkt.Higher;
import cyclops.collections.mutable.DequeX;
import cyclops.control.Maybe;
import cyclops.function.Fn1;
import cyclops.function.Lambda;
import cyclops.function.Monoid;
import cyclops.monads.Witness;
import cyclops.monads.Witness.deque;
import org.junit.Test;



public class DequesTest {

    @Test
    public void unit(){
        
        DequeX<String> list = DequeX.Instances.unit()
                                     .unit("hello")
                                     .convert(DequeX::narrowK);
        
        assertThat(list.toArray(),equalTo(DequeX.of("hello").toArray()));
    }
    @Test
    public void functor(){
        
        DequeX<Integer> list = DequeX.Instances.unit()
                                     .unit("hello")
                                     .applyHKT(h->DequeX.Instances.functor().map((String v) ->v.length(), h))
                                     .convert(DequeX::narrowK);
        
        assertThat(list.toArray(),equalTo(DequeX.of("hello".length()).toArray()));
    }
    @Test
    public void apSimple(){
        DequeX.Instances.zippingApplicative()
            .ap(DequeX.of(l1(this::multiplyByTwo)),DequeX.of(1,2,3));
    }
    private int multiplyByTwo(int x){
        return x*2;
    }
    @Test
    public void applicative(){
        
        DequeX<Fn1<Integer,Integer>> listFn =DequeX.Instances.unit().unit(Lambda.l1((Integer i) ->i*2)).convert(DequeX::narrowK);
        
        DequeX<Integer> list = DequeX.Instances.unit()
                                     .unit("hello")
                                     .applyHKT(h->DequeX.Instances.functor().map((String v) ->v.length(), h))
                                     .applyHKT(h->DequeX.Instances.zippingApplicative().ap(listFn, h))
                                     .convert(DequeX::narrowK);
        
        assertThat(list.toArray(),equalTo(DequeX.of("hello".length()*2).toArray()));
    }
    @Test
    public void monadSimple(){
       DequeX<Integer> list  = DequeX.Instances.monad()
                                      .flatMap(i->DequeX.range(0,i), DequeX.of(1,2,3))
                                      .convert(DequeX::narrowK);
    }
    @Test
    public void monad(){
        
        DequeX<Integer> list = DequeX.Instances.unit()
                                     .unit("hello")
                                     .applyHKT(h->DequeX.Instances.monad().flatMap((String v) ->DequeX.Instances.unit().unit(v.length()), h))
                                     .convert(DequeX::narrowK);
        
        assertThat(list.toArray(),equalTo(DequeX.of("hello".length()).toArray()));
    }
    @Test
    public void monadZeroFilter(){
        
        DequeX<String> list = DequeX.Instances.unit()
                                     .unit("hello")
                                     .applyHKT(h->DequeX.Instances.monadZero().filter((String t)->t.startsWith("he"), h))
                                     .convert(DequeX::narrowK);
        
        assertThat(list.toArray(),equalTo(DequeX.of("hello").toArray()));
    }
    @Test
    public void monadZeroFilterOut(){
        
        DequeX<String> list = DequeX.Instances.unit()
                                     .unit("hello")
                                     .applyHKT(h->DequeX.Instances.monadZero().filter((String t)->!t.startsWith("he"), h))
                                     .convert(DequeX::narrowK);
        
        assertThat(list.toArray(),equalTo(DequeX.of().toArray()));
    }
    
    @Test
    public void monadPlus(){
        DequeX<Integer> list = DequeX.Instances.<Integer>monadPlus()
                                      .plus(DequeX.of(), DequeX.of(10))
                                      .convert(DequeX::narrowK);
        assertThat(list.toArray(),equalTo(DequeX.of(10).toArray()));
    }
    @Test
    public void monadPlusNonEmpty(){
        
        Monoid<DequeX<Integer>> m = Monoid.of(DequeX.of(), (a, b)->a.isEmpty() ? b : a);
        DequeX<Integer> list = DequeX.Instances.<Integer>monadPlus(m)
                                      .plus(DequeX.of(5), DequeX.of(10))
                                      .convert(DequeX::narrowK);
        assertThat(list.toArray(),equalTo(DequeX.of(5).toArray()));
    }
    @Test
    public void  foldLeft(){
        int sum  = DequeX.Instances.foldable()
                        .foldLeft(0, (a,b)->a+b, DequeX.of(1,2,3,4));
        
        assertThat(sum,equalTo(10));
    }
    @Test
    public void  foldRight(){
        int sum  = DequeX.Instances.foldable()
                        .foldRight(0, (a,b)->a+b, DequeX.of(1,2,3,4));
        
        assertThat(sum,equalTo(10));
    }
    @Test
    public void traverse(){
       Maybe<Higher<deque, Integer>> res = DequeX.Instances.traverse()
                                                           .traverseA(Maybe.Instances.applicative(), (Integer a)->Maybe.just(a*2), DequeX.of(1,2,3))
                                                            .convert(Maybe::narrowK);
       
       
       assertThat(res.map(h->DequeX.fromIterable(h.convert(DequeX::narrowK)).toList()),
                  equalTo(Maybe.just(DequeX.of(2,4,6).toList())));
    }
    
}
