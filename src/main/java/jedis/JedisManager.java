package jedis;

import company.Product;
import org.apache.commons.beanutils.BeanUtilsBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 31.05.17.
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
                jedis.hmset(object.getId(),properties);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Logger.getLogger(Product.class.getName()).info("Couldn't validate object of type \""+object.toString()+"\"");
        }
    }

    // Retrieve object from memory
    public JedisObject retrieve(JedisObject object) {
        try(Jedis jedis = getResource()) {
            Map<String, String> properties = jedis.hgetAll(object.getId());
            BeanUtilsBean.getInstance().populate(object,properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void delete(JedisObject object) {
        try(Jedis jedis = getResource()) {
            jedis.del(object.getId());
        }
    }

    public static JedisManager getInstance() {
        return jedisManager;
    }

    public JedisPool getPool() {
        return pool;
    }
}
