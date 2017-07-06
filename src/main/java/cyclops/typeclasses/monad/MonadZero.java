package cyclops.typeclasses.monad;

import com.aol.cyclops2.hkt.Higher;
import cyclops.typeclasses.Filterable;


import java.util.function.Predicate;



/**
 * A filterable monad
 * 
 * The zero() operator is used toNested replaceWith supplied HKT with it's zero / empty equivalent when filtered out
 * 
 * @author johnmcclean
 *
 * @param <CRE> CORE Type
 */
public interface MonadZero<CRE> extends Monad<CRE>, Filterable<CRE> {
    
    
    /**
     * e.g. for Optional we can use Optional.empty()
     * 
     * @return Identity value or zero value for the HKT type, the generic type is unknown
     */
    public Higher<CRE, ?> zero();
    
    /* (non-Javadoc)
     * @see com.aol.com.aol.cyclops2.hkt.typeclasses.Filterable#filter(java.util.function.Predicate, com.aol.com.aol.cyclops2.hkt.alias.Higher)
     */
    @Override
    default <T> Higher<CRE,T> filter(Predicate<? super T> predicate, Higher<CRE, T> ds){
        
        return flatMap((T in)->predicate.test(in) ? ds : narrowZero(),ds);
    }
    default <T> Higher<CRE,T> filter_(Higher<CRE, T> ds,Predicate<? super T> predicate){

        return filter(predicate,ds);
    }
    
    default <T> Higher<CRE,T> narrowZero(){
        return  (Higher)zero();
    }
    
}
