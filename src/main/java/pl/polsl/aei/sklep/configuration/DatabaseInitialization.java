package pl.polsl.aei.sklep.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import pl.polsl.aei.sklep.repository.*;
import pl.polsl.aei.sklep.repository.entity.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
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
        rate.setComment("Polecam!");
        rate.setValue(5d);
        rate.setUser(user);

        Product product = productRepository.findProductByName("Czapka 2");
        rate.setProduct(product);

        rateRepository.save(rate);
    }

    private void insertRecordsToWarehouseRelation() throws Exception {
        Series series = new Series();
        series.setBuyCost(BigDecimal.valueOf(1));
        series.setName("Seria pierwsza");
        series.setBuyDate(new Date());

        Warehouse warehouseCzapka2XS = new Warehouse();
        warehouseCzapka2XS.setQuantity(1L);
        warehouseCzapka2XS.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka2XS.setSeries(series);
        warehouseCzapka2XS.setSize(
                sizeRepository.findSizeByName("XS")
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

        Warehouse warehouseCzapka2S = new Warehouse();
        warehouseCzapka2S.setQuantity(1L);
        warehouseCzapka2S.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka2S.setSeries(series);
        warehouseCzapka2S.setSize(
                sizeRepository.findSizeByName("S")
        );
        Warehouse warehouseCzapka2M = new Warehouse();
        warehouseCzapka2M.setQuantity(1L);
        warehouseCzapka2M.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka2M.setSeries(series);
        warehouseCzapka2M.setSize(
                sizeRepository.findSizeByName("M")
        );
        Warehouse warehouseCzapka2L = new Warehouse();
        warehouseCzapka2L.setQuantity(0L);
        warehouseCzapka2L.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka2L.setSeries(series);
        warehouseCzapka2L.setSize(
                sizeRepository.findSizeByName("L")
        );
        Warehouse warehouseCzapka2XL = new Warehouse();
        warehouseCzapka2XL.setQuantity(0L);
        warehouseCzapka2XL.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka2XL.setSeries(series);
        warehouseCzapka2XL.setSize(
                sizeRepository.findSizeByName("XL")
        );

        Product czapka2 = new Product();
        czapka2.setName("Czapka 2");
        czapka2.setSex("M");
        czapka2.setImage(hat2);
        czapka2.setCategory(category);
        czapka2.setWarehouse(new HashSet<>(Arrays.asList(warehouseCzapka2XS, warehouseCzapka2S, warehouseCzapka2M, warehouseCzapka2L, warehouseCzapka2XL)));
        czapka2.setSpecification("Testowa specyfikacja 1");
        warehouseCzapka2S.setProduct(czapka2);
        warehouseCzapka2M.setProduct(czapka2);
        warehouseCzapka2L.setProduct(czapka2);
        warehouseCzapka2XL.setProduct(czapka2);
        warehouseCzapka2XS.setProduct(czapka2);


        Series series1 = new Series();
        series1.setBuyCost(BigDecimal.valueOf(1));
        series1.setName("Seria jakaś tam");
        series1.setBuyDate(new Date());

        Warehouse warehouseCzapka1L = new Warehouse();
        warehouseCzapka1L.setQuantity(3L);
        warehouseCzapka1L.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka1L.setSeries(series1);
        warehouseCzapka1L.setSize(
                sizeRepository.findSizeByName("L")
        );

        Warehouse warehouseCzapka1XS = new Warehouse();
        warehouseCzapka1XS.setQuantity(3L);
        warehouseCzapka1XS.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka1XS.setSeries(series1);
        warehouseCzapka1XS.setSize(
                sizeRepository.findSizeByName("XS")
        );

        Warehouse warehouseCzapka1S = new Warehouse();
        warehouseCzapka1S.setQuantity(3L);
        warehouseCzapka1S.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka1S.setSeries(series1);
        warehouseCzapka1S.setSize(
                sizeRepository.findSizeByName("S")
        );

        Warehouse warehouseCzapka1M = new Warehouse();
        warehouseCzapka1M.setQuantity(3L);
        warehouseCzapka1M.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka1M.setSeries(series1);
        warehouseCzapka1M.setSize(
                sizeRepository.findSizeByName("M")
        );

        Warehouse warehouseCzapka1XL = new Warehouse();
        warehouseCzapka1XL.setQuantity(3L);
        warehouseCzapka1XL.setSaleCost(BigDecimal.valueOf(2));
        warehouseCzapka1XL.setSeries(series1);
        warehouseCzapka1XL.setSize(
                sizeRepository.findSizeByName("XL")
        );

        Product czapka1 = new Product();
        czapka1.setName("Czapka 1");
        czapka1.setSex("K");
        czapka1.setImage(hat1);
        czapka1.setCategory(category);
        czapka1.setWarehouse(new HashSet<>(Arrays.asList(warehouseCzapka1XS, warehouseCzapka1S, warehouseCzapka1M, warehouseCzapka1L, warehouseCzapka1XL)));
        czapka1.setSpecification("Testowa specyfikacja do kapelusza");
        warehouseCzapka1XS.setProduct(czapka1);
        warehouseCzapka1S.setProduct(czapka1);
        warehouseCzapka1L.setProduct(czapka1);
        warehouseCzapka1M.setProduct(czapka1);
        warehouseCzapka1XL.setProduct(czapka1);

        Category category1 = categoryRepository.findCategoryByName("Nogi");

        Series series2 = new Series();
        series2.setName("Seria z chin");
        series2.setBuyCost(BigDecimal.valueOf(10.11));
        series2.setBuyDate(new Date());

        Warehouse warehouseSpodnie1XS = new Warehouse();
        warehouseSpodnie1XS.setQuantity(1L);
        warehouseSpodnie1XS.setSaleCost(BigDecimal.valueOf(10));
        warehouseSpodnie1XS.setSeries(series2);
        warehouseSpodnie1XS.setSize(sizeRepository.findSizeByName("XS"));

        Warehouse warehouseSpodnie1S = new Warehouse();
        warehouseSpodnie1S.setQuantity(1L);
        warehouseSpodnie1S.setSaleCost(BigDecimal.valueOf(10));
        warehouseSpodnie1S.setSeries(series2);
        warehouseSpodnie1S.setSize(sizeRepository.findSizeByName("S"));

        Warehouse warehouseSpodnie1M = new Warehouse();
        warehouseSpodnie1M.setQuantity(1L);
        warehouseSpodnie1M.setSaleCost(BigDecimal.valueOf(10));
        warehouseSpodnie1M.setSeries(series2);
        warehouseSpodnie1M.setSize(sizeRepository.findSizeByName("M"));

        Warehouse warehouseSpodnie1L = new Warehouse();
        warehouseSpodnie1L.setQuantity(1L);
        warehouseSpodnie1L.setSaleCost(BigDecimal.valueOf(10));
        warehouseSpodnie1L.setSeries(series2);
        warehouseSpodnie1L.setSize(sizeRepository.findSizeByName("L"));

        Warehouse warehouseSpodnie1XL = new Warehouse();
        warehouseSpodnie1XL.setQuantity(1L);
        warehouseSpodnie1XL.setSaleCost(BigDecimal.valueOf(10));
        warehouseSpodnie1XL.setSeries(series2);
        warehouseSpodnie1XL.setSize(sizeRepository.findSizeByName("XL"));

        Product spodnie1 = new Product();
        spodnie1.setCategory(category1);
        spodnie1.setWarehouse(new HashSet<>(Arrays.asList(warehouseSpodnie1XS, warehouseSpodnie1S, warehouseSpodnie1M, warehouseSpodnie1L, warehouseSpodnie1XL)));
        spodnie1.setImage(trousers1);
        spodnie1.setSex("M");
        spodnie1.setName("Spodnie fajne 1");
        spodnie1.setSpecification("Testowa specyfikacja spodni");
        warehouseSpodnie1XS.setProduct(spodnie1);
        warehouseSpodnie1S.setProduct(spodnie1);
        warehouseSpodnie1M.setProduct(spodnie1);
        warehouseSpodnie1L.setProduct(spodnie1);
        warehouseSpodnie1XL.setProduct(spodnie1);

        Series series3 = new Series();
        series3.setName("Seria z turcji");
        series3.setBuyCost(BigDecimal.valueOf(0.99));
        series3.setBuyDate(new Date());

        Warehouse warehouse3 = new Warehouse();
        warehouse3.setQuantity(31L);
        warehouse3.setSaleCost(BigDecimal.valueOf(9.99));
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
        series4.setBuyCost(BigDecimal.valueOf(5));
        series4.setBuyDate(new Date());

        Warehouse warehouse4 = new Warehouse();
        warehouse4.setQuantity(22L);
        warehouse4.setSaleCost(BigDecimal.valueOf(15));
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
        series5.setBuyCost(BigDecimal.valueOf(10));
        series5.setBuyDate(new Date());

        Warehouse warehouse5 = new Warehouse();
        warehouse5.setQuantity(11L);
        warehouse5.setSaleCost(BigDecimal.valueOf(20));
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
        series6.setBuyCost(BigDecimal.valueOf(1));
        series6.setBuyDate(new Date());

        Warehouse warehouse6 = new Warehouse();
        warehouse6.setQuantity(111L);
        warehouse6.setSaleCost(BigDecimal.valueOf(1.5));
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
        series7.setBuyCost(BigDecimal.valueOf(0.5));
        series7.setBuyDate(new Date());

        Warehouse warehouse7 = new Warehouse();
        warehouse7.setQuantity(101L);
        warehouse7.setSaleCost(BigDecimal.valueOf(2));
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

        productRepository.save(czapka2);
        productRepository.save(czapka1);
        productRepository.save(spodnie1);
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
