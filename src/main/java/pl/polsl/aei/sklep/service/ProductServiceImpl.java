package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.ProductDetailsDTO;
import pl.polsl.aei.sklep.dto.ProductOnListDTO;
import pl.polsl.aei.sklep.dto.SizeAvailableDTO;
import pl.polsl.aei.sklep.dto.WarehouseDTO;
import pl.polsl.aei.sklep.repository.CategoryRepository;
import pl.polsl.aei.sklep.repository.ProductRepository;
import pl.polsl.aei.sklep.repository.SizeRepository;
import pl.polsl.aei.sklep.repository.WarehouseRepository;
import pl.polsl.aei.sklep.repository.entity.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private SizeRepository sizeRepository;
    private WarehouseRepository warehouseRepository;
    private Base64.Encoder encoder = Base64.getEncoder();

    @Autowired
    public void setSizeRepository(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setWarehouseRepository(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public List<ProductOnListDTO> searchByName(String query) {
        return productRepository
                .findProductsByNameContaining(query)
                .stream()
                .filter(product -> !product.isDeleted())
                .map(product -> product.productOnListMapper(product, null))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDetailsDTO> getDetailsById(Long id) {
        return productRepository.findById(id).map(product -> {

            ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
            productDetailsDTO.setId(id.toString());
            productDetailsDTO.setImage(encoder.encodeToString(product.getImage()));
            productDetailsDTO.setSpecification(product.getSpecification());

            Long quantity = product.getWarehouse().stream().mapToLong(Warehouse::getQuantity).sum();
            Warehouse warehouse = product.getWarehouse().iterator().next();
            productDetailsDTO.setAvailableCount(quantity.toString());
            productDetailsDTO.setSize(warehouse.getSize().getName());
            productDetailsDTO.setPrice(NumberFormat.getCurrencyInstance().format(warehouse.getSaleCost()));

            return productDetailsDTO;
        });
    }

    @Override
    public Optional<SizeAvailableDTO> getSizeAvailableById(Long id) {
        return productRepository.findById(id).map(product -> {
            SizeAvailableDTO dto = new SizeAvailableDTO();
            dto.setXs(getSizeAvailability(product, "XS"));
            dto.setS(getSizeAvailability(product, "S"));
            dto.setM(getSizeAvailability(product, "M"));
            dto.setL(getSizeAvailability(product, "L"));
            dto.setXl(getSizeAvailability(product, "XL"));
            return dto;
        });
    }

    private String getSizeAvailability(Product product, String sizeName) {
        return String.valueOf(product.getWarehouse().stream().filter(warehouse -> warehouse.getSize().getName().equals(sizeName)).mapToLong(Warehouse::getQuantity).sum());
    }

    @Override
    public void insertProductToGroup(String group, ProductDetailsDTO productDetailsDTO, byte[] image) throws IOException {
        Size size = sizeRepository.findSizeByName(productDetailsDTO.getSize());
        Category category = categoryRepository.findCategoryByName(ProductService.CategoryName.findByDenormalizeName(group).getName());
        byte[] img = resizeImageFromBytes(image, 500, 500);

        Product product = new Product();
        product.setSpecification(productDetailsDTO.getSpecification());
        product.setCategory(category);
        product.setName(productDetailsDTO.getName());
        product.setImage(img);

        Warehouse warehouse = new Warehouse();
        warehouse.setQuantity(Long.parseLong(productDetailsDTO.getAvailableCount()));
        warehouse.setSize(size);
        warehouse.setSaleCost(new BigDecimal(productDetailsDTO.getPrice()));
        product.setWarehouse(Collections.singleton(warehouse));
        warehouse.setProduct(product);

        Series series = new Series();
        series.setBuyCost(new BigDecimal(productDetailsDTO.getBuyCost()));
        series.setName("Domyślna seria");
        warehouse.setSeries(series);
        series.setWarehouseSet(Collections.singleton(warehouse));
        series.setBuyDate(new Date());

        productRepository.save(product);
    }

    public void insertWarehouseToProduct(Long productId, WarehouseDTO dto) {
        Size size = sizeRepository.findSizeByName(dto.getSize());
        Optional<Product> product = productRepository.findById(productId);

        product.ifPresent(product1 -> {
            Optional<Warehouse> first = product1.getWarehouse().stream().findAny();

            Warehouse warehouse = new Warehouse();
            warehouse.setQuantity(dto.getQuantity());
            warehouse.setProduct(product1);
            first.ifPresent(e1 -> warehouse.setSaleCost(e1.getSaleCost()));
            warehouse.setSize(size);

            Series series = new Series();
            series.setBuyCost(dto.getBuyCost());
            series.setName("Domyślna seria");
            series.setWarehouseSet(Collections.singleton(warehouse));
            warehouse.setSeries(series);
            series.setBuyDate(new Date());

            product1.getWarehouse().add(warehouse);
            productRepository.save(product1);
        });
    }

    private byte[] resizeImageFromBytes(byte[] image, int width, int height) throws IOException{

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(ImageIO.read(new ByteArrayInputStream(image)), 0, 0, width, height, null);
        g2.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return b;
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository
                .findById(productId)
                .ifPresent(product -> {
                    product.delete();
                    productRepository.save(product);
                });
    }

    @Override
    public List<ProductOnListDTO> getAllProductsFromCategory(CategoryName categoryName) {
        return categoryRepository.findCategoryByName(categoryName.getName())
                .getProducts()
                .stream()
                .filter(product -> !product.isDeleted())
                .map(product -> product.productOnListMapper(product, null))
                .collect(Collectors.toList());
    }
}