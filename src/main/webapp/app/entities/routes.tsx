import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Product from './product';
import ProductDetails from './product-details';
import Color from './color';
import Processor from './processor';
import Memory from './memory';
import Storage from './storage';
import Description from './description';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="product/*" element={<Product />} />
        <Route path="product-details/*" element={<ProductDetails />} />
        <Route path="color/*" element={<Color />} />
        <Route path="processor/*" element={<Processor />} />
        <Route path="memory/*" element={<Memory />} />
        <Route path="storage/*" element={<Storage />} />
        <Route path="description/*" element={<Description />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
