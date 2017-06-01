package jedis;

import java.util.List;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Defines JedisObject factories (stores)
 */
public interface JedisObjectFactoryInterface {

    // Retrieve an object by id
    <T extends JedisObject> T retrieve(String id);

    // Retrieve all objects from class
    <T extends JedisObject> List<T>retrieveAll();

    // Creates new "empty" object to act as a bean for JedisManager
    <T extends JedisObject> T getNewBean();

}
