import java.util.function.Consumer;
import java.util.logging.ErrorManager;

import static spark.Spark.*;

public class HelloWorld {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        // stop();

    }

}
