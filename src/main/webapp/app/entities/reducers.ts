import category from 'app/entities/category/category.reducer';
import product from 'app/entities/product/product.reducer';
import productDetails from 'app/entities/product-details/product-details.reducer';
import color from 'app/entities/color/color.reducer';
import processor from 'app/entities/processor/processor.reducer';
import memory from 'app/entities/memory/memory.reducer';
import storage from 'app/entities/storage/storage.reducer';
import description from 'app/entities/description/description.reducer';
import screen from 'app/entities/screen/screen.reducer';
import connectivity from 'app/entities/connectivity/connectivity.reducer';
import material from 'app/entities/material/material.reducer';
import caseSize from 'app/entities/case-size/case-size.reducer';
import strapColor from 'app/entities/strap-color/strap-color.reducer';
import strapSize from 'app/entities/strap-size/strap-size.reducer';
import order from 'app/entities/order/order.reducer';
import orderItem from 'app/entities/order-item/order-item.reducer';
import customer from 'app/entities/customer/customer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  category,
  product,
  productDetails,
  color,
  processor,
  memory,
  storage,
  description,
  screen,
  connectivity,
  material,
  caseSize,
  strapColor,
  strapSize,
  order,
  orderItem,
  customer,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
