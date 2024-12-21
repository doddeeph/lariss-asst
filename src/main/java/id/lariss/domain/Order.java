package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.lariss.domain.enumeration.OrderStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Order.
 */
@Entity
@Table(name = "order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Customer customer;

    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Shipping shipping;

    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Billing billing;

    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Payment payment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonIgnoreProperties(value = { "order", "productDetails" }, allowSetters = true)
    private Set<OrderProduct> orderProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public Order totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTrackId() {
        return this.trackId;
    }

    public Order trackId(String trackId) {
        this.setTrackId(trackId);
        return this;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public Order expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Shipping getShipping() {
        return this.shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Order shipping(Shipping shipping) {
        this.setShipping(shipping);
        return this;
    }

    public Billing getBilling() {
        return this.billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Order billing(Billing billing) {
        this.setBilling(billing);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Order payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Set<OrderProduct> getOrderProducts() {
        return this.orderProducts;
    }

    public void setOrderProducts(Set<OrderProduct> orderProducts) {
        if (this.orderProducts != null) {
            this.orderProducts.forEach(i -> i.setOrder(null));
        }
        if (orderProducts != null) {
            orderProducts.forEach(i -> i.setOrder(this));
        }
        this.orderProducts = orderProducts;
    }

    public Order orderProducts(Set<OrderProduct> orderProducts) {
        this.setOrderProducts(orderProducts);
        return this;
    }

    public Order addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
        return this;
    }

    public Order removeOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.remove(orderProduct);
        orderProduct.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", trackId='" + getTrackId() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
