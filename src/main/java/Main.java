import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        // populate some data for the memory storage
        populateData();

        // Always start with more specific routes
        get("/hello", (req, res) -> "Hello World");

        // Always add generic routes to the end
        get("/", ProductController::renderProducts, new ThymeleafTemplateEngine());
        // Equivalent with above
        get("/index", (Request req, Response res) -> {
           return new ThymeleafTemplateEngine().render( ProductController.renderProducts(req, res) );
        });
        get("/Category/:name", (Request req, Response res) -> {
           return new ThymeleafTemplateEngine().render(ProductController.renderFilteredProductsByCategory(req, res));
        });
        get("/Supplier/:name", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.renderFilteredProductsBySupplier(req, res));
        });

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void populateData() {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);

        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);
        ProductCategory laptop = new ProductCategory("Laptop", "Hardware", "Bla bla bla.");
        productCategoryDataStore.add(laptop);
        ProductCategory camera = new ProductCategory("Camera", "Photography", "Bla bla bla");
        productCategoryDataStore.add(camera);

        //setting up products and printing it
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));

        productDataStore.add(new Product("Lenovo Yoga Tab 3 - HD 8\"", 170, "USD", "Android Tablet Computer. (Qualcomm Snapdragon APQ8009, 2GB RAM, 16GB SSD)", tablet, lenovo));
        productDataStore.add(new Product("Lenovo Ideapad 700 - 15.6\"", 300, "USD", "Bla", laptop, lenovo));

        productDataStore.add(new Product("Fire Tablet with Alexa, 7\"", 666, "USD", "FHD Laptop (Intel Core i7, 16 GB RAM)", tablet, amazon));
        productDataStore.add(new Product("2017 Lenovo Premium HD Laptop", 240, "USD", "FHD Laptop (Intel Core i7, 16 GB RAM)", laptop, lenovo));

        productDataStore.add(new Product("2019 Lenovo Ultra Super Noscope Camera", 4242, "USD", "FHD Laptop (Intel Core i7, 16 GB RAM)", camera, lenovo));
    }


}
