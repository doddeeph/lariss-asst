package id.lariss.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.ProductDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String thumbnail;

    private ProductDTO product;

    private DescriptionDTO description;

    private ColorDTO color;

    private ProcessorDTO processor;

    private MemoryDTO memory;

    private StorageDTO storage;

    private ScreenDTO screen;

    private ConnectivityDTO connectivity;

    private MaterialDTO material;

    private CaseSizeDTO caseSize;

    private StrapColorDTO strapColor;

    private StrapSizeDTO strapSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public DescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(DescriptionDTO description) {
        this.description = description;
    }

    public ColorDTO getColor() {
        return color;
    }

    public void setColor(ColorDTO color) {
        this.color = color;
    }

    public ProcessorDTO getProcessor() {
        return processor;
    }

    public void setProcessor(ProcessorDTO processor) {
        this.processor = processor;
    }

    public MemoryDTO getMemory() {
        return memory;
    }

    public void setMemory(MemoryDTO memory) {
        this.memory = memory;
    }

    public StorageDTO getStorage() {
        return storage;
    }

    public void setStorage(StorageDTO storage) {
        this.storage = storage;
    }

    public ScreenDTO getScreen() {
        return screen;
    }

    public void setScreen(ScreenDTO screen) {
        this.screen = screen;
    }

    public ConnectivityDTO getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(ConnectivityDTO connectivity) {
        this.connectivity = connectivity;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    public CaseSizeDTO getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(CaseSizeDTO caseSize) {
        this.caseSize = caseSize;
    }

    public StrapColorDTO getStrapColor() {
        return strapColor;
    }

    public void setStrapColor(StrapColorDTO strapColor) {
        this.strapColor = strapColor;
    }

    public StrapSizeDTO getStrapSize() {
        return strapSize;
    }

    public void setStrapSize(StrapSizeDTO strapSize) {
        this.strapSize = strapSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDetailsDTO)) {
            return false;
        }

        ProductDetailsDTO productDetailsDTO = (ProductDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDetailsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", product=" + getProduct() +
            ", description=" + getDescription() +
            ", color=" + getColor() +
            ", processor=" + getProcessor() +
            ", memory=" + getMemory() +
            ", storage=" + getStorage() +
            ", screen=" + getScreen() +
            ", connectivity=" + getConnectivity() +
            ", material=" + getMaterial() +
            ", caseSize=" + getCaseSize() +
            ", strapColor=" + getStrapColor() +
            ", strapSize=" + getStrapSize() +
            "}";
    }
}
