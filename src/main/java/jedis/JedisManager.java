package jedis;

import company.Product;
import org.apache.commons.beanutils.BeanUtilsBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This class takes care of the interactions between Redis and JedisObjects
 */
public final class JedisManager {

    private final Logger logger = Logger.getLogger(JedisManager.class.getName());
    private JedisPool pool;
    private static JedisManager jedisManager = new JedisManager();

    private JedisManager() {
        //Connecting to Redis server on localhost
        final String host = "localhost";
        final int port = 6379;
        pool = new JedisPool(new JedisPoolConfig(),host,port);
        logger.info("Redis server running at "+host+":"+port);
    }

    // Returns a new resource from the pool
    public Jedis getResource() {
        return pool.getResource();
    }

    // Close resource
    public void closeResource(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }

    // Should be called on application termination
    public void destroyPool() {
        if (pool != null) {
            pool.destroy();
        }
    }

    // Save object in memory
    public void save(JedisObject object) {
        if (object.validate()) {
            try (Jedis jedis = getResource()) {
                Map<String, String> properties = BeanUtilsBean.getInstance().describe(object);
                jedis.hmset(getEntryId(object),properties);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Couldn't validate object of type \""+object.toString()+"\"");
        }
    }

    // Retrieve object from memory and populate bean. Bean must be an "empty" object from the calling class.
    public <T extends JedisObject> T retrieve(String id, T bean) {
        try(Jedis jedis = getResource()) {
            Map<String, String> properties = jedis.hgetAll(bean.getClass().getName().toLowerCase()+":"+id);
            populateObject(bean,properties);
        }
        return bean;
    }


    // Retrieve all objects from specific class
    public <T extends JedisObject> List<T> retrieveAllFromClass(T bean) {
        List<T> objects = new ArrayList<>();
        try (Jedis jedis = getResource()){
            // Get all keys from given class
            Set<String> keys = jedis.keys(bean.getClass().getName().toLowerCase()+":*");

            // Iterate over found keys and add newly populated object to list
            for (final String key : keys) {
                T newBean = (T) bean.clone();
                populateObject(newBean,jedis.hgetAll(key));
                objects.add(newBean);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public void delete(JedisObject object) {
        try(Jedis jedis = getResource()) {
            jedis.del(object.getId());
        }
    }

    // Populate an object with properties retrieve from Redis
    private void populateObject(Object object, Map<String,String> properties) {
        try {
            BeanUtilsBean.getInstance().populate(object,properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static JedisManager getInstance() {
        return jedisManager;
    }

    public JedisPool getPool() {
        return pool;
    }

    private <T extends JedisObject> String getEntryId(T object) {
        return object.getClass().getName().toLowerCase()+":"+object.getId().replace(' ','_');
    }
}
