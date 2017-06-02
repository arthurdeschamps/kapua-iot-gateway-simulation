package jedis;

import company.Product;

import java.util.List;
import java.util.Optional;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Defines JedisObject factories (stores)
 */
public interface JedisObjectStoreInterface<T extends JedisObject> {

    // Retrieve an object by id
    Optional<T> retrieve(String id);

    // Retrieve all objects from class
    List<T>retrieveAll();

    // Creates new "empty" object to act as a bean for JedisManager
    T getNewBean();

}
