package pl.polsl.aei.sklep.service;

import pl.polsl.aei.sklep.dto.ProductDetailsDTO;
import pl.polsl.aei.sklep.dto.ProductOnListDTO;
import pl.polsl.aei.sklep.dto.SizeAvailableDTO;
import pl.polsl.aei.sklep.dto.WarehouseDTO;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface ProductService {
    List<ProductOnListDTO> getAllProductsFromCategory(CategoryName categoryName);
    List<ProductOnListDTO> searchByName(String query);
    Optional<ProductDetailsDTO> getDetailsById(Long id);

    Optional<SizeAvailableDTO> getSizeAvailableById(Long id);
    void insertProductToGroup(String group, ProductDetailsDTO productDetailsDTO, byte[] image) throws IOException;
    void deleteProduct(Long productId);
    void insertWarehouseToProduct(Long productId, WarehouseDTO dto);

    enum CategoryName {
        HEAD("Głowa","glowa"), TORSO("Tółw","tolw"), LEGS("Nogi","nogi"), FEETS("Stopy", "stopy");

        private static Set<CategoryName> categoryNames = EnumSet.allOf(CategoryName.class);

        private String name;

        private String normalizeName;

        CategoryName(String name, String normalizeName) {
            this.name = name;
            this.normalizeName = normalizeName;
        }

        public String getName() {
            return name;
        }

        public static CategoryName findByDenormalizeName(String name) {
            return categoryNames
                    .stream()
                    .filter(e -> e.normalizeName.equals(name))
                    .findFirst()
                    .orElse(HEAD);
        }
    }
}
