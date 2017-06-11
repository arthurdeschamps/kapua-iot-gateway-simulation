package jedis;

import gherkin.deps.com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.*;
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
            try (Jedis jedis = pool.getResource()) {
                Gson gson = new Gson();
                String properties = gson.toJson(object);
                jedis.set(getEntryId(object), properties);
            }
        } else {
            logger.info("Couldn't validate object of type \""+object.toString()+"\"");
        }
    }

    // Retrieve object from memory and populate bean. Bean must be an "empty" object from the calling class.
    public <T extends JedisObject> T retrieve(String id, Class<T> clazz) {
        T returnObject = null;
        try(Jedis jedis = pool.getResource()) {
            Gson gson = new Gson();
            String properties = jedis.get(clazz.getName().toLowerCase()+":"+id);
            if (properties != null) {
                returnObject = gson.fromJson(properties,clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnObject;
    }


    // Retrieve all objects from specific class
    public <T extends JedisObject> List<T> retrieveAllFromClass(Class<T> clazz) {
        List<T> objects = new ArrayList<>();
        try (Jedis jedis = pool.getResource()){
            // Get all keys from given class
            Set<String> keys = jedis.keys(clazz.getName().toLowerCase()+":*");

            Gson gson = new Gson();
            // Iterate over found keys and add newly populated object to list
            for (final String key : keys)
                objects.add(gson.fromJson(jedis.get(key),clazz));
        }
        return objects;
    }

    public void delete(JedisObject object) {
        try(Jedis jedis = pool.getResource()) {
            jedis.del(object.getId());
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

    // Generate an id for a JedisObject
    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public void flushDB() {
        try(Jedis jedis = pool.getResource()) {
            jedis.flushDB();
        }
    }

    public void delete(String key) {
        try(Jedis jedis = pool.getResource()) {
            jedis.del(key);
        }
    }
}
