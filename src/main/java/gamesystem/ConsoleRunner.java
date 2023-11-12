package gamesystem;

import gamesystem.controllers.GameController;
import gamesystem.controllers.ItemController;
import gamesystem.controllers.UserController;
import gamesystem.repositories.GameRepository;
import gamesystem.repositories.OrderRepository;
import gamesystem.repositories.UserRepository;
import gamesystem.services.api.GameService;
import gamesystem.services.api.OrderService;
import gamesystem.services.api.UserService;
import gamesystem.services.impl.GameServiceImpl;
import gamesystem.services.impl.OrderServiceImpl;
import gamesystem.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;


@SpringBootApplication
public class ConsoleRunner implements CommandLineRunner {
    private UserService userService;
    private BufferedReader br;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private HashMap<String,Object> cache;
    private HashMap<String,Class<?>>mappings;
    private OrderRepository orderRepository;

    @Autowired
    public ConsoleRunner(UserService userService, UserRepository userRepository,
                         GameRepository gameRepository, OrderRepository orderRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.orderRepository = orderRepository;
        br = new BufferedReader(new InputStreamReader(System.in));
        this.getMappings();
        this.cacheRepositories();
    }

    @Override
    public void run(String... args) throws Exception {
        while(true){
            String[] input = br.readLine().split("\\|");
            String methodName = input[0].split("(?=\\p{Upper})")[0].toLowerCase();
            String controllerName = input[0].split("(?=\\p{Upper})")[1];
            String pathName = "gamesystem.controllers."+controllerName+"Controller";
            Object controller = this.resolveParameters(pathName);
            Method[] methods = controller.getClass().getDeclaredMethods();
            Method mtd = Arrays.stream(methods)
                    .filter(method -> method.getName().equals(methodName))
                    .findFirst().get();
            Class<?>[] params = mtd.getParameterTypes();
            Object[] methodArguments = new Object[mtd.getParameterCount()];

            for(int i =0;i< params.length;i++){
                if(params[i] == String.class){
                    methodArguments[i] = input[i+1];
                }else if (params[i] == BigDecimal.class){
                    methodArguments[i] = new BigDecimal(input[i+1]);
                }else if(params[i]==Long.class){
                    methodArguments[i] = Long.valueOf(input[i+1]);
                }
            }

            System.out.println(mtd.invoke(controller, methodArguments));
        }


    }

    private Object resolveParameters(String pathName) throws InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException {
        if(this.cache.containsKey(pathName)){
            return this.cache.get(pathName);
        }
        Class<?>clazz = mappings.get(pathName);
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        Parameter[] params = constructor.getParameters();
        Object[] arguments = new Object[constructor.getParameterCount()];
        int i=0;
        for (Parameter param : params) {
            arguments[i]= resolveParameters(param.getParameterizedType().getTypeName());
            i++;
        }
        Object instance = constructor.newInstance(arguments);
        this.cache.put(instance.getClass().getName(),instance);
        return instance;
    }

    private void getMappings(){
        this.mappings = new HashMap<>();
        this.mappings.put(UserService.class.getName(), UserServiceImpl.class);
        this.mappings.put(GameService.class.getName(), GameServiceImpl.class);
        this.mappings.put(OrderService.class.getName(), OrderServiceImpl.class);
        this.mappings.put(UserController.class.getName(),UserController.class);
        this.mappings.put(GameController.class.getName(),GameController.class);
        this.mappings.put(ItemController.class.getName(), ItemController.class);
        this.mappings.put(AuthenticationContext.class.getName(),AuthenticationContext.class);

    }

    private void cacheRepositories(){
        this.cache = new HashMap<>();
        cache.put(UserRepository.class.getName(),this.userRepository);
        cache.put(GameRepository.class.getName(),this.gameRepository);
        cache.put(OrderRepository.class.getName(),this.orderRepository);
    }
}