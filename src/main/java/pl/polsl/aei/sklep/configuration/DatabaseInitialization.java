package pl.polsl.aei.sklep.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import pl.polsl.aei.sklep.repository.*;
import pl.polsl.aei.sklep.repository.entity.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

@Component
public class DatabaseInitialization {

    @Value("${testDatabase}")
    private boolean isInitialize;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RateRepository rateRepository;

    @PostConstruct
    public void init() throws Exception {
        if (isInitialize) {
            insertRecordsToSizeTable();
            insertRecordsToUserRelation();
            insertRecordsToBoardRelation();
            insertRecordsToCategoryTable();
            insertRecordsToWarehouseRelation();
            insertRecordsToRateRelation();
        }
    }


    private void insertRecordsToRateRelation() {
        User user = userRepository.findUserByUsername("krzysztofNowak");

        Rate rate = new Rate();
        rate.setComment("Zajebiscie w chuj! Polecam w chuj");
        rate.setValue(5d);
        rate.setUser(user);

        Product product = productRepository.findProductByName("Czapka 2");
        rate.setProduct(product);

        rateRepository.save(rate);
    }

    private void insertRecordsToWarehouseRelation() throws Exception{
        Series series = new Series();
        series.setBuyCost(BigDecimal.valueOf(31.11));
        series.setName("Seria pierwsza");

        Warehouse warehouse = new Warehouse();
        warehouse.setQuantity(44L);
        warehouse.setSaleCost(BigDecimal.valueOf(12.11));
        warehouse.setSeries(series);
        warehouse.setSize(
                sizeRepository.findSizeByName("M")
        );

        byte[] hat1 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/hat1.jpg").getInputStream());
        byte[] hat2 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/hat2.jpg").getInputStream());
        byte[] shoes1 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/shoes1.jpg").getInputStream());
        byte[] shoes2 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/shoes2.jpg").getInputStream());
        byte[] trousers1 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/trousers1.jpg").getInputStream());
        byte[] trousers2 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/trousers2.jpg").getInputStream());
        byte[] shirt1 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/shirt1.jpg").getInputStream());
        byte[] shirt2 = FileCopyUtils.copyToByteArray(new ClassPathResource("/template-data/shirt2.jpg").getInputStream());

        Category category = categoryRepository.findCategoryByName("Głowa");

        Product product = new Product();
        product.setName("Czapka 2");
        product.setSex("M");
        product.setImage(hat2);
        product.setCategory(category);
        product.setWarehouse(Collections.singleton(warehouse));
        product.setSpecification("Testowa specyfikacja 1");
        warehouse.setProduct(product);

        Series series1 = new Series();
        series1.setBuyCost(BigDecimal.valueOf(8.00));
        series1.setName("Seria jakaś tam");

        Warehouse warehouse1 = new Warehouse();
        warehouse1.setQuantity(31L);
        warehouse1.setSaleCost(BigDecimal.valueOf(9.11));
        warehouse1.setSeries(series1);
        warehouse1.setSize(
                sizeRepository.findSizeByName("L")
        );

        Product product1 = new Product();
        product1.setName("Czapka 1");
        product1.setSex("K");
        product1.setImage(hat1);
        product1.setCategory(category);
        product1.setWarehouse(Collections.singleton(warehouse1));
        product1.setSpecification("Testtowa specyfikacja do kapelusza");
        warehouse1.setProduct(product1);

        Category category1 = categoryRepository.findCategoryByName("Nogi");

        Series series2 = new Series();
        series2.setName("Seria z chin");
        series2.setBuyCost(BigDecimal.valueOf(10.11));

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setQuantity(5L);
        warehouse2.setSaleCost(BigDecimal.valueOf(50.11));
        warehouse2.setSeries(series2);
        warehouse2.setSize(
                sizeRepository.findSizeByName("S")
        );

        Product trousersp1 = new Product();
        trousersp1.setCategory(category1);
        trousersp1.setWarehouse(Collections.singleton(warehouse2));
        trousersp1.setImage(trousers1);
        trousersp1.setSex("M");
        trousersp1.setName("Spodnie fajne 1");
        trousersp1.setSpecification("Testowa specyfikacja spodni");
        warehouse2.setProduct(trousersp1);

        Series series3 = new Series();
        series3.setName("Seria z turcji");
        series3.setBuyCost(BigDecimal.valueOf(13.12));

        Warehouse warehouse3 = new Warehouse();
        warehouse3.setQuantity(31L);
        warehouse3.setSaleCost(BigDecimal.valueOf(51.22));
        warehouse3.setSeries(series3);
        warehouse3.setSize(
                sizeRepository.findSizeByName("L")
        );

        Product trousersp2 = new Product();
        trousersp2.setImage(trousers2);
        trousersp2.setWarehouse(Collections.singleton(warehouse3));
        trousersp2.setSex("M");
        trousersp2.setName("Spodnie fajne 2");
        trousersp2.setCategory(category1);
        trousersp2.setSpecification("Testowa specyfikacja do spodni 2");
        warehouse3.setProduct(trousersp2);

        Category category2 = categoryRepository.findCategoryByName("Tółw");

        Series series4 = new Series();
        series4.setName("Seria z wietnamu");
        series4.setBuyCost(BigDecimal.valueOf(16.12));

        Warehouse warehouse4 = new Warehouse();
        warehouse4.setQuantity(22L);
        warehouse4.setSaleCost(BigDecimal.valueOf(41.22));
        warehouse4.setSeries(series4);
        warehouse4.setSize(
                sizeRepository.findSizeByName("L")
        );

        Product shirtp1 = new Product();
        shirtp1.setImage(shirt1);
        shirtp1.setWarehouse(Collections.singleton(warehouse4));
        shirtp1.setSex("M");
        shirtp1.setName("Fajna koszulka 1");
        shirtp1.setSpecification("Testowa specyfikacja do koszulki 2");
        shirtp1.setCategory(category2);
        warehouse4.setProduct(shirtp1);

        Series series5 = new Series();
        series5.setName("Seria z chin");
        series5.setBuyCost(BigDecimal.valueOf(14.12));

        Warehouse warehouse5 = new Warehouse();
        warehouse5.setQuantity(11L);
        warehouse5.setSaleCost(BigDecimal.valueOf(59.22));
        warehouse5.setSeries(series5);
        warehouse5.setSize(
               sizeRepository.findSizeByName("M")
        );

        Product shirtp2 = new Product();
        shirtp2.setImage(shirt2);
        shirtp2.setWarehouse(Collections.singleton(warehouse5));
        shirtp2.setSex("M");
        shirtp2.setName("Fajna koszulka 2");
        shirtp2.setCategory(category2);
        shirtp2.setSpecification("Testowa specyfikacja do koszulki 2");
        warehouse5.setProduct(shirtp2);

        Category category3 = categoryRepository.findCategoryByName("Stopy");

        Series series6 = new Series();
        series6.setName("Seria z polski");
        series6.setBuyCost(BigDecimal.valueOf(15.12));

        Warehouse warehouse6 = new Warehouse();
        warehouse6.setQuantity(111L);
        warehouse6.setSaleCost(BigDecimal.valueOf(31.22));
        warehouse6.setSeries(series6);
        warehouse6.setSize(
                sizeRepository.findSizeByName("L")
        );

        Product shoep1 = new Product();
        shoep1.setImage(shoes1);
        shoep1.setWarehouse(Collections.singleton(warehouse6));
        shoep1.setSex("M");
        shoep1.setName("Fajna buty 1");
        shoep1.setCategory(category3);
        shoep1.setSpecification("Testowa specyfikacja do butow 12");
        warehouse6.setProduct(shoep1);

        Series series7 = new Series();
        series7.setName("Seria z uzbekistanu");
        series7.setBuyCost(BigDecimal.valueOf(11.12));

        Warehouse warehouse7 = new Warehouse();
        warehouse7.setQuantity(101L);
        warehouse7.setSaleCost(BigDecimal.valueOf(45.22));
        warehouse7.setSeries(series7);
        warehouse7.setSize(
                sizeRepository.findSizeByName("XS")
        );

        Product shoep2 = new Product();
        shoep2.setImage(shoes2);
        shoep2.setWarehouse(Collections.singleton(warehouse7));
        shoep2.setSex("M");
        shoep2.setName("Fajne buty 2");
        shoep2.setCategory(category3);
        shoep2.setSpecification("Testowa specyfikacja do  butow 2134");
        warehouse7.setProduct(shoep2);

        productRepository.save(product);
        productRepository.save(product1);
        productRepository.save(trousersp1);
        productRepository.save(trousersp2);
        productRepository.save(shirtp1);
        productRepository.save(shirtp2);
        productRepository.save(shoep1);
        productRepository.save(shoep2);
    }

    private void insertRecordsToUserRelation() {
        User janKowalski = new User();
        janKowalski.setUsername("janKowalski");
        janKowalski.setSurname("Kowalski");
        janKowalski.setName("Jan");
        janKowalski.setEmail("janKowalski@o2.pl");
        janKowalski.setAddress("ul.Zwycięstwa 4/1 Gliwice");
        janKowalski.setRole("ROLE_ADMIN");
        janKowalski.setPassword("$2a$10$/74vVKp0LiPP6y2aLxXSpeaD2PMElpp0BftkfxVius.plDNxlTBsq"); //1234
        Worker worker = new Worker("91111112345", BigDecimal.valueOf(2222.11), "12312412441241412",
                janKowalski);
        janKowalski.setWorkers(new HashSet<>(Arrays.asList(worker)));

        User krzysztofNowak = new User();
        krzysztofNowak.setUsername("krzysztofNowak");
        krzysztofNowak.setName("Krzysztof");
        krzysztofNowak.setSurname("Nowak");
        krzysztofNowak.setAddress("ul.Częstochowska 4/2 Gliwice");
        krzysztofNowak.setEmail("krzysztofNowak@o2.pl");
        krzysztofNowak.setRole("ROLE_USER");
        krzysztofNowak.setPassword("$2a$10$/74vVKp0LiPP6y2aLxXSpeaD2PMElpp0BftkfxVius.plDNxlTBsq"); // 1234

        userRepository.save(janKowalski);
        userRepository.save(krzysztofNowak);
    }



    private void insertRecordsToBoardRelation() {
        User janKowalski = userRepository.findUserByUsername("janKowalski");

        User krzysztofNowak = userRepository.findUserByUsername("krzysztofNowak");

        pl.polsl.aei.sklep.repository.entity.Thread thread1 = new pl.polsl.aei.sklep.repository.entity.Thread();
        Post post1 = new Post();

        thread1.setName("Testowy watek1");
        thread1.setCreateDate(new Date());
        thread1.setUser(janKowalski);

        post1.setContent("Testowa tresc");
        post1.setCreateDate(new Date());
        post1.setUser(krzysztofNowak);
        post1.setThread(thread1);

        thread1.setPosts(new HashSet<>(Arrays.asList(post1)));

        threadRepository.save(thread1);
    }

    private void insertRecordsToCategoryTable() {
        Category head = new Category();
        head.setName("Głowa");

        Category legs = new Category();
        legs.setName("Nogi");

        Category torso = new Category();
        torso.setName("Tółw");

        Category feet = new Category();
        feet.setName("Stopy");

        categoryRepository.save(head);
        categoryRepository.save(legs);
        categoryRepository.save(torso);
        categoryRepository.save(feet);
    }

    private void insertRecordsToSizeTable() {
        Size sizeM = new Size();
        sizeM.setName("M");

        Size sizeS = new Size();
        sizeS.setName("S");

        Size sizeXS = new Size();
        sizeXS.setName("XS");

        Size sizeL = new Size();
        sizeL.setName("L");

        Size sizeXL = new Size();
        sizeXL.setName("XL");

        sizeRepository.save(sizeS);
        sizeRepository.save(sizeM);
        sizeRepository.save(sizeXS);
        sizeRepository.save(sizeL);
        sizeRepository.save(sizeXL);

    }
}
