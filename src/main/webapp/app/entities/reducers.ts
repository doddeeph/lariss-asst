import category from 'app/entities/category/category.reducer';
import product from 'app/entities/product/product.reducer';
import productDetails from 'app/entities/product-details/product-details.reducer';
import color from 'app/entities/color/color.reducer';
import processor from 'app/entities/processor/processor.reducer';
import memory from 'app/entities/memory/memory.reducer';
import storage from 'app/entities/storage/storage.reducer';
import description from 'app/entities/description/description.reducer';
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
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
