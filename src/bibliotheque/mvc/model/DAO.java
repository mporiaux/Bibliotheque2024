package bibliotheque.mvc.model;


import java.util.List;
import bibliotheque.mvc.observer.Subject;
public abstract class DAO<T> extends Subject {

    public abstract T add( T elt) ;

    public abstract boolean remove( T elt) ;

    public abstract T update (T elt) ;

    public abstract T read(T rech) ;

    public abstract List<T> getAll() ;

    public List<T> getNotification(){
        return getAll();
    }
}