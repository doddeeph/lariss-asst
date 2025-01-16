package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProductDetails.
 */
@Entity
@Table(name = "product_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Description description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    private Processor processor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Memory memory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Storage storage;

    @ManyToOne(fetch = FetchType.LAZY)
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    private Connectivity connectivity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    private CaseSize caseSize;

    @ManyToOne(fetch = FetchType.LAZY)
    private StrapColor strapColor;

    @ManyToOne(fetch = FetchType.LAZY)
    private StrapSize strapSize;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetails")
    @JsonIgnoreProperties(value = { "order", "productDetails" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProductDetails name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProductDetails price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public ProductDetails thumbnail(String thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductDetails product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public ProductDetails description(Description description) {
        this.setDescription(description);
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ProductDetails color(Color color) {
        this.setColor(color);
        return this;
    }

    public Processor getProcessor() {
        return this.processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public ProductDetails processor(Processor processor) {
        this.setProcessor(processor);
        return this;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public ProductDetails memory(Memory memory) {
        this.setMemory(memory);
        return this;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public ProductDetails storage(Storage storage) {
        this.setStorage(storage);
        return this;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public ProductDetails screen(Screen screen) {
        this.setScreen(screen);
        return this;
    }

    public Connectivity getConnectivity() {
        return this.connectivity;
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public ProductDetails connectivity(Connectivity connectivity) {
        this.setConnectivity(connectivity);
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ProductDetails material(Material material) {
        this.setMaterial(material);
        return this;
    }

    public CaseSize getCaseSize() {
        return this.caseSize;
    }

    public void setCaseSize(CaseSize caseSize) {
        this.caseSize = caseSize;
    }

    public ProductDetails caseSize(CaseSize caseSize) {
        this.setCaseSize(caseSize);
        return this;
    }

    public StrapColor getStrapColor() {
        return this.strapColor;
    }

    public void setStrapColor(StrapColor strapColor) {
        this.strapColor = strapColor;
    }

    public ProductDetails strapColor(StrapColor strapColor) {
        this.setStrapColor(strapColor);
        return this;
    }

    public StrapSize getStrapSize() {
        return this.strapSize;
    }

    public void setStrapSize(StrapSize strapSize) {
        this.strapSize = strapSize;
    }

    public ProductDetails strapSize(StrapSize strapSize) {
        this.setStrapSize(strapSize);
        return this;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setProductDetails(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setProductDetails(this));
        }
        this.orderItems = orderItems;
    }

    public ProductDetails orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public ProductDetails addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setProductDetails(this);
        return this;
    }

    public ProductDetails removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setProductDetails(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", thumbnail='" + getThumbnail() + "'" +
            "}";
    }
}
